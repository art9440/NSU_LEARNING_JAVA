package org.chat.server;

import org.chat.common.messagesOBJ.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class HandlerXML implements ProtocolHandler {
    private final Chat chat;
    private final Socket socket;
    private final String sessionId = UUID.randomUUID().toString();
    private String name = "Anonymous";

    private DataInputStream  dis;
    private DataOutputStream dos;

    private volatile boolean running      = true;
    private volatile long    lastActivity = System.currentTimeMillis();
    private volatile long    lastPongTime  = System.currentTimeMillis();

    private static final long IDLE_TIMEOUT_MS = 100_000L;
    private static final long PONG_TIMEOUT_MS =  10_000L;

    private final AtomicBoolean disconnected = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    public HandlerXML(Socket socket, Chat chat) {
        this.socket = socket;
        this.chat   = chat;
    }

    private final ExecutorService sendExecutor =
            Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "send-to-" + sessionId);
                t.setDaemon(true);
                return t;
            });

    @Override
    public void handle() {
        try {
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            startHeartbeat();

            while (running) {
                int len = dis.readInt();
                byte[] buf = new byte[len];
                dis.readFully(buf);

                DocumentBuilder db  = XMLUtil.dbf.newDocumentBuilder();
                Document        doc = db.parse(new ByteArrayInputStream(buf));
                Element         root= doc.getDocumentElement();
                String          tag = root.getTagName();

                if ("Pong".equals(tag)) {
                    String sess = doc.getElementsByTagName("session")
                            .item(0).getTextContent();
                    if (sessionId.equals(sess)) {
                        lastPongTime = System.currentTimeMillis();
                    }
                    continue;
                }

                lastActivity = System.currentTimeMillis();

                switch (tag) {
                    case "LoginCommand" -> handleLogin(doc);
                    case "MessageCommand"-> handleMessage(doc);
                    case "ListCommand"   -> handleList(doc);
                    case "LogoutCommand" -> { handleLogout(doc); return; }
                    default              -> sendError("Unknown command: " + tag);
                }
            }
        } catch (Exception e) {
            if (running) {
                System.err.println("HandlerXML error: " + e.getMessage());
            }
        } finally {
            cleanup();
        }
    }

    private void handleLogin(Document doc) throws Exception {

        name = doc.getElementsByTagName("login")
                .item(0).getTextContent();

        sendSuccess();

        for (String msg : chat.getHistory()) {
            sendRaw(msg);
        }

        chat.broadcastExcept("[SERVER] " + name + " has joined the chat", this);
    }

    private void handleMessage(Document doc) throws Exception {
        String sess = doc.getElementsByTagName("session")
                .item(0).getTextContent();
        if (!sessionId.equals(sess)) {
            sendError("Invalid session");
            return;
        }
        String text = doc.getElementsByTagName("message")
                .item(0).getTextContent();
        String full = "[" + name + "] " + text;
        chat.broadcastExcept(full, this);
        sendSuccess();
    }

    private void handleList(Document doc) throws Exception {
        String sess = doc.getElementsByTagName("session")
                .item(0).getTextContent();
        if (!sessionId.equals(sess)) {
            sendError("Invalid session");
            return;
        }
        Document ulDoc = XMLUtil.newDocument();
        Element root   = ulDoc.createElement("UserList");
        ulDoc.appendChild(root);
        for (String user : chat.getUserNames()) {
            XMLUtil.addTextElement(ulDoc, root, "user", user);
        }
        sendDocument(ulDoc);
    }

    private void handleLogout(Document doc) throws Exception {
        String sess = doc.getElementsByTagName("session")
                .item(0).getTextContent();
        if (!sessionId.equals(sess)) {
            sendError("Invalid session");
            return;
        }
        sendSuccess();

        running = false;
    }

    private void startHeartbeat() {

        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            if (System.currentTimeMillis() - lastActivity > IDLE_TIMEOUT_MS) {
                disconnect("idle timeout");
            }
        }, 1, 1, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            try {
                Document doc = XMLUtil.newDocument();
                doc.appendChild(doc.createElement("Ping"));
                sendDocument(doc);
            } catch (Exception e) {
                disconnect("ping failed: " + e.getMessage());
            }
        }, 1, 1, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            if (System.currentTimeMillis() - lastPongTime > PONG_TIMEOUT_MS) {
                disconnect("pong timeout");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void sendSuccess() throws Exception {
        Document doc = XMLUtil.newDocument();
        Element root = doc.createElement("Success");
        doc.appendChild(root);
        XMLUtil.addTextElement(doc, root, "session", sessionId);
        sendDocument(doc);
    }

    private void sendError(String msg) {
        try {
            Document doc = XMLUtil.newDocument();
            Element root = doc.createElement("Error");
            doc.appendChild(root);
            XMLUtil.addTextElement(doc, root, "message", msg);
            sendDocument(doc);
        } catch (Exception ignored) {}
    }

    @Override
    public synchronized void sendRaw(String message) {
        sendExecutor.submit(() -> {
        try {
            Document doc = XMLUtil.newDocument();
            Element root = doc.createElement("EventMessage");
            doc.appendChild(root);
            XMLUtil.addTextElement(doc, root, "message", message);
            sendDocument(doc);
        } catch (Exception e) {
            System.err.println("sendRaw error: " + e.getMessage());
        }});
    }

    private void sendDocument(Document doc) throws Exception {
        byte[] bytes = XMLUtil.toBytes(doc);
        dos.writeInt(bytes.length);
        dos.write(bytes);
        dos.flush();
    }

    private void disconnect(String reason) {
        if (!disconnected.compareAndSet(false, true)) return;
        System.out.println("Disconnecting " + name + ": " + reason);
        running = false;
        cleanup();
    }

    private void cleanup() {
        try { socket.close(); } catch (IOException ignored) {}
        scheduler.shutdownNow();
        sendExecutor.shutdownNow();
        chat.unregister(this);
        chat.broadcast("[SERVER] " + name + " disconnected");
    }


    @Override
    public String getName() {
        return name;
    }
}
