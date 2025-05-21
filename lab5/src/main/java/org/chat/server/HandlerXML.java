package org.chat.server;

import org.chat.common.messagesOBJ.EventMessage;
import org.chat.common.messagesOBJ.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class HandlerXML implements ProtocolHandler{
    private final Chat chat;
    private final Socket socket;
    private final String sessionId = UUID.randomUUID().toString();
    private String name = "Anonymous";

    private DataOutputStream dos;
    private DataInputStream dis;

    private volatile boolean running = true;
    private volatile long lastActivity = System.currentTimeMillis();
    private volatile long lastPongTime  = System.currentTimeMillis();

    private static final long IDLE_TIMEOUT_MS = 100_000L;  // 100 с без «реальных» сообщений
    private static final long PONG_TIMEOUT_MS = 10_000L;   // 10 с без ответа на ping

    private final AtomicBoolean disconnected = new AtomicBoolean(false);

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);


    public HandlerXML(Socket socket, Chat chat) {
        this.chat = chat;
        this.socket = socket;
        startHeartbeat();
    }

    public void startHeartbeat(){

    }

    @Override
    public void handle(){
        try{
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            while (running){
                int len = dis.readInt();
                byte[] byteMessage = new byte[len];
                dis.readFully(byteMessage);

                DocumentBuilder db = XMLUtil.dbf.newDocumentBuilder();
                Document doc = db.parse(new ByteArrayInputStream(byteMessage));

                Element root = doc.getDocumentElement();
                String tag = root.getTagName();

                if(tag.equals("pong")){
                    lastPongTime = System.currentTimeMillis();
                    continue;
                }

                lastActivity = System.currentTimeMillis();

                switch (tag){
                    case "LoginCommand" -> {
                        this.name = doc.getElementsByTagName("login").item(0).getTextContent();
                        sendSuccess();
                        for (String msg : chat.getHistory()) {
                            sendMessage(msg);
                        }
                    }
                }


            }

        } catch (Exception e) {

        }
    }

    private void sendSuccess() {
        try {
            Document doc = XMLUtil.newDocument();

            Element root = doc.createElement("Success");
            doc.appendChild(root);

            XMLUtil.addTextElement(doc, root, "session", sessionId);

            byte[] bytesXml = XMLUtil.toBytes(doc);

            dos.writeInt(bytesXml.length);
            dos.write(bytesXml);
            dos.flush();
        } catch (Exception e) {
            System.err.println("Handler error: " + e.getMessage());
        }
    }


    @Override
    public void sendRaw(String message){

    }

    @Override
    public String getName(){
        return "";
    }
}
