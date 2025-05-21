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

public class ClientXML implements ClientProtocol{
    private final Socket socket;
    private final String login;
    private  DataOutputStream dos;
    private DataInputStream dis;

    private volatile boolean running = true;
    private String sessionId;

    public ClientXML(Socket socket, String login) {
        this.socket = socket;
        this.login = login;
    }

    @Override
    public void start(){
        try {
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            sendLogin();

            int len = dis.readInt();
            byte[] byteMessage = new byte[len];
            dis.readFully(byteMessage);

            DocumentBuilder db = XMLUtil.dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(byteMessage));

            Element root = doc.getDocumentElement();
            String tag = root.getTagName();

            switch (tag){
                case "Success" -> {
                    sessionId = doc.getElementsByTagName("session").item(0).getTextContent();
                    System.out.println("Successfully log in, sessionId=" + sessionId);
                }
                case "Error" -> {
                    System.err.println("Error while entering: " + doc.getElementsByTagName("message").item(0).
                            getTextContent());
                    return;
                }
                default -> {
                    System.err.println("Unknown answer : " + tag);
                    return;
                }
            }

            Thread readerThread = new Thread(this::receiveLoop, "client-receiver");
            readerThread.start();

            Scanner scanner = new Scanner(System.in);

            while (running){
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("/exit")) {
                    sendLogout();
                    running = false;
                }
                else if(line.equalsIgnoreCase("/list")){
                    sendListCommand();
                }
                else{
                    System.out.print("\033[1A");
                    System.out.print("\033[2K");
                    System.out.flush();

                    System.out.println("[" + login + "] " + line);

                    sendMessage(line);
                }
            }

            readerThread.join();

        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void sendLogin() throws ParserConfigurationException, TransformerException, IOException {
        Document doc = XMLUtil.newDocument();

        Element root = doc.createElement("LoginCommand");
        doc.appendChild(root);

        XMLUtil.addTextElement(doc, root, "login", login);
        XMLUtil.addTextElement(doc, root, "type", "xml");

        byte[] bytesXml = XMLUtil.toBytes(doc);

        dos.writeInt(bytesXml.length);
        dos.write(bytesXml);
        dos.flush();
    }

    private void sendLogout() throws ParserConfigurationException, TransformerException, IOException {
        Document doc = XMLUtil.newDocument();

        Element root = doc.createElement("LogoutCommand");
        doc.appendChild(root);

        XMLUtil.addTextElement(doc, root, "session", sessionId);
        byte[] bytesXml = XMLUtil.toBytes(doc);

        dos.writeInt(bytesXml.length);
        dos.write(bytesXml);
        dos.flush();
    }

    private void sendListCommand() throws ParserConfigurationException, TransformerException, IOException {
        Document doc = XMLUtil.newDocument();

        Element root = doc.createElement("ListCommand");
        doc.appendChild(root);

        XMLUtil.addTextElement(doc, root, "session", sessionId);
        byte[] bytesXml = XMLUtil.toBytes(doc);

        dos.writeInt(bytesXml.length);
        dos.write(bytesXml);
        dos.flush();
    }

    private void sendMessage(String line) throws ParserConfigurationException, TransformerException, IOException {
        Document doc = XMLUtil.newDocument();

        Element root = doc.createElement("MessageCommand");
        doc.appendChild(root);

        XMLUtil.addTextElement(doc, root, "session", sessionId);
        XMLUtil.addTextElement(doc, root, "message", line);

        byte[] bytesXml = XMLUtil.toBytes(doc);

        dos.writeInt(bytesXml.length);
        dos.write(bytesXml);
        dos.flush();
    }


    private void receiveLoop() {
        try{
            while(running) {
                int len = dis.readInt();
                byte[] byteMessage = new byte[len];
                dis.readFully(byteMessage);

                DocumentBuilder db = XMLUtil.dbf.newDocumentBuilder();
                Document doc = db.parse(new ByteArrayInputStream(byteMessage));

                Element root = doc.getDocumentElement();
                String tag = root.getTagName();

                if (tag.equals("Ping")) {
                    Document docPong = XMLUtil.newDocument();

                    Element rootPong = docPong.createElement("Pong");
                    docPong.appendChild(rootPong);

                    byte[] bytesXml = XMLUtil.toBytes(docPong);

                    dos.writeInt(bytesXml.length);
                    dos.write(bytesXml);
                    dos.flush();
                }

                switch (tag) {
                    case "EventMessage", "Error" -> {
                        System.out.println(doc.getElementsByTagName("message").item(0).
                                getTextContent());
                    }
                    case "UserList" -> {

                    }
                    case "Success" ->{
                        //Nothing
                    }
                    default -> {
                        System.out.println("Unknown object: " + tag);
                    }

                }
            }
        } catch (Exception e) {
            System.err.println("Disconnection " + e.getMessage());
        }
    }

}
