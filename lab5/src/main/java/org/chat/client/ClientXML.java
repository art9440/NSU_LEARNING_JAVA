package org.chat.client;

import org.chat.common.messagesOBJ.LogoutCommand;
import org.chat.common.messagesOBJ.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientXML implements ClientProtocol {
    private final Socket socket;
    private final String login;

    private DataOutputStream dos;
    private DataInputStream  dis;

    private volatile boolean running   = true;
    private String           sessionId;

    public ClientXML(Socket socket, String login) {
        this.socket = socket;
        this.login  = login;
    }

    @Override
    public void start() {
        try {

            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));


            sendLogin();
            DocumentBuilder db = XMLUtil.dbf.newDocumentBuilder();
            Document respDoc = db.parse(new ByteArrayInputStream(readMessage()));
            String respTag = respDoc.getDocumentElement().getTagName();
            if ("Success".equals(respTag)) {
                sessionId = respDoc.getElementsByTagName("session")
                        .item(0).getTextContent();
                System.out.println("Successfully log in, sessionId=" + sessionId);
            } else if ("Error".equals(respTag)) {
                String err = respDoc.getElementsByTagName("message")
                        .item(0).getTextContent();
                System.err.println("Error while entering: " + err);
                return;
            } else {
                System.err.println("Unknown answer: " + respTag);
                return;
            }


            Thread reader = new Thread(this::receiveLoop, "client-receiver");
            reader.setDaemon(true);
            reader.start();


            Scanner scanner = new Scanner(System.in);
            while (running) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("/exit")) {
                    sendLogout();
                    running = false;

                } else if (line.equalsIgnoreCase("/list")) {
                    sendList();

                } else {

                    System.out.print("\033[1A");
                    System.out.print("\033[2K");
                    System.out.flush();
                    System.out.println("[" + login + "] " + line);

                    sendMessage(line);
                }
            }
            reader.join();

        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void receiveLoop() {
        try {
            DocumentBuilder db = XMLUtil.dbf.newDocumentBuilder();
            while (running) {
                byte[] raw = readMessage();
                Document doc = db.parse(new ByteArrayInputStream(raw));
                String tag  = doc.getDocumentElement().getTagName();

                if ("Ping".equals(tag)) {
                    sendPong();
                    continue;
                }

                switch (tag) {
                    case "EventMessage" -> {
                        String msg = doc.getElementsByTagName("message")
                                .item(0).getTextContent();
                        System.out.println(msg);
                    }
                    case "UserList" -> {
                        System.out.println("Users:");
                        var nl = doc.getElementsByTagName("user");
                        for (int i = 0; i < nl.getLength(); i++) {
                            System.out.println("- " + nl.item(i).getTextContent());
                        }
                    }
                    case "Error" -> {
                        String err = doc.getElementsByTagName("message")
                                .item(0).getTextContent();
                        System.err.println("Error: " + err);
                    }
                    case "Success" -> {

                    }
                    default -> {
                        System.out.println("Unknown XML tag: " + tag);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Disconnection: " + e.getMessage());
            running = false;
        }
    }

    private byte[] readMessage() throws IOException {
        int len = dis.readInt();
        byte[] buf = new byte[len];
        dis.readFully(buf);
        return buf;
    }

    private void sendLogin() throws Exception {
        Document doc = XMLUtil.newDocument();
        Element root = doc.createElement("LoginCommand");
        doc.appendChild(root);
        XMLUtil.addTextElement(doc, root, "login", login);
        XMLUtil.addTextElement(doc, root, "type",  "xml");
        sendDocument(doc);
    }

    private void sendLogout() throws Exception {
        Document doc = XMLUtil.newDocument();
        Element root = doc.createElement("LogoutCommand");
        doc.appendChild(root);
        XMLUtil.addTextElement(doc, root, "session", sessionId);
        sendDocument(doc);
    }

    private void sendList() throws Exception {
        Document doc = XMLUtil.newDocument();
        Element root = doc.createElement("ListCommand");
        doc.appendChild(root);
        XMLUtil.addTextElement(doc, root, "session", sessionId);
        sendDocument(doc);
    }

    private void sendMessage(String text) throws Exception {
        Document doc = XMLUtil.newDocument();
        Element root = doc.createElement("MessageCommand");
        doc.appendChild(root);
        XMLUtil.addTextElement(doc, root, "session", sessionId);
        XMLUtil.addTextElement(doc, root, "message", text);
        sendDocument(doc);
    }


    private void sendPong() throws Exception {
        Document doc = XMLUtil.newDocument();
        Element root = doc.createElement("Pong");
        doc.appendChild(root);
        XMLUtil.addTextElement(doc, root, "session", sessionId);
        sendDocument(doc);
    }

    private void sendDocument(Document doc) throws IOException, TransformerException {
        byte[] bytes = XMLUtil.toBytes(doc);
        dos.writeInt(bytes.length);
        dos.write(bytes);
        dos.flush();
    }
}
