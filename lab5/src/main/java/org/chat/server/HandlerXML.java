package org.chat.server;

import org.chat.common.messagesOBJ.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;
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

    private final ExecutorService sendExecutor =
            Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "xml-sender-" + sessionId);
                t.setDaemon(true);
                return t;
            });

    public HandlerXML(Socket socket, Chat chat) {
        this.socket = socket;
        this.chat   = chat;
        log("[CONNECTED] session=%s from %s", sessionId, socket.getRemoteSocketAddress());
    }

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
                String tag = doc.getDocumentElement().getTagName();
                log("[RECV] %s ← %s", name, tag);

                if ("Pong".equals(tag)) {
                    String sess = doc.getElementsByTagName("session")
                            .item(0).getTextContent();
                    if (sessionId.equals(sess)) {
                        lastPongTime = System.currentTimeMillis();
                        log("[PONG] received from %s", name);
                    }
                    continue;
                }

                lastActivity = System.currentTimeMillis();

                switch (tag) {
                    case "LoginCommand"   -> handleLogin(doc);
                    case "MessageCommand" -> handleMessage(doc);
                    case "ListCommand"    -> handleList(doc);
                    case "LogoutCommand"  -> { handleLogout(doc); return; }
                    default               -> {
                        log("[ERROR] Unknown command from %s: %s", name, tag);
                        sendError("Unknown command: " + tag);
                    }
                }
            }
        } catch (Exception e) {
            if (running) log("[ERROR] HandlerXML for %s: %s", name, e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void handleLogin(Document doc) throws Exception {
        name = doc.getElementsByTagName("login")
                .item(0).getTextContent();
        log("[LOGIN] session=%s as user=%s", sessionId, name);

        sendSuccess();

        for (String msg : chat.getHistory()) {
            sendRaw(msg);
        }

        chat.broadcastExcept("[SERVER] " + name + " has joined the chat");
    }

    private void handleMessage(Document doc) throws Exception {
        String sess = doc.getElementsByTagName("session")
                .item(0).getTextContent();
        if (!sessionId.equals(sess)) {
            log("[ERROR] Invalid session in MessageCommand from %s", name);
            sendError("Invalid session");
            return;
        }
        String text = doc.getElementsByTagName("message")
                .item(0).getTextContent();
        log("[MESSAGE] from %s: %s", name, text);

        String full = "[" + name + "] " + text;
        chat.broadcastExcept(full);
        sendSuccess();
    }

    private void handleList(Document doc) throws Exception {
        String sess = doc.getElementsByTagName("session")
                .item(0).getTextContent();
        if (!sessionId.equals(sess)) {
            log("[ERROR] Invalid session in ListCommand from %s", name);
            sendError("Invalid session");
            return;
        }
        log("[LIST] requested by %s", name);

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
            log("[ERROR] Invalid session in LogoutCommand from %s", name);
            sendError("Invalid session");
            return;
        }
        log("[LOGOUT] from %s", name);
        sendSuccess();
        disconnect("client requested logout");
        running = false;
    }

    private void startHeartbeat() {
        // idle timeout
        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            if (System.currentTimeMillis() - lastActivity > IDLE_TIMEOUT_MS) {
                disconnect("idle timeout");
            }
        }, 1, 1, TimeUnit.SECONDS);

        // send ping
        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            try {
                Document doc = XMLUtil.newDocument();
                doc.appendChild(doc.createElement("Ping"));
                sendDocument(doc);
                log("[PING SENT] to %s", name);
            } catch (Exception e) {
                disconnect("ping send failed: " + e.getMessage());
            }
        }, 1, 1, TimeUnit.SECONDS);

        // pong timeout
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
        log("[SUCCESS] sent to %s", name);
    }

    private void sendError(String msg) {
        try {
            Document doc = XMLUtil.newDocument();
            Element root = doc.createElement("Error");
            doc.appendChild(root);
            XMLUtil.addTextElement(doc, root, "message", msg);
            sendDocument(doc);
            log("[ERROR] sent to %s: %s", name, msg);
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
                log("[SEND] to %s: %s", name, message);
            } catch (Exception e) {
                log("[ERROR] sendRaw to %s: %s", name, e.getMessage());
            }
        });
    }

    private void sendDocument(Document doc) throws Exception {
        byte[] bytes = XMLUtil.toBytes(doc);
        dos.writeInt(bytes.length);
        dos.write(bytes);
        dos.flush();
    }

    private void disconnect(String reason) {
        if (!disconnected.compareAndSet(false, true)) return;
        log("[DISCONNECTING] %s: %s", name, reason);
        running = false;
        log("[UNREGISTERED] %s", name);
        chat.broadcast("[SERVER] " + name + " disconnected");
        cleanup();
    }

    private void cleanup() {
        try { socket.close(); } catch (IOException ignored) {}
        scheduler.shutdownNow();
        sendExecutor.shutdownNow();
        chat.unregister(this);
    }

    @Override
    public String getName() {
        return name;
    }

    private void log(String fmt, Object... args) {
        System.out.printf((fmt.endsWith("%n") ? fmt : fmt + "%n"), args);
    }
}
