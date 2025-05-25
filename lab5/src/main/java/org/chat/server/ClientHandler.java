package org.chat.server;

import java.io.*;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Chat   chat;

    public ClientHandler(Socket socket, Chat chat) {
        this.socket = socket;
        this.chat   = chat;
    }

    @Override
    public void run() {
        ProtocolHandler handler = null;
        try {

            DataInputStream  dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String protocol = dis.readUTF();

            if ("obj".equalsIgnoreCase(protocol)) {

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                ObjectInputStream  ois = new ObjectInputStream(socket.getInputStream());

                handler = new HandlerOBJ(socket, chat, ois, oos);

            } else if ("xml".equalsIgnoreCase(protocol)) {

                handler = new HandlerXML(socket, chat);

            } else {
                dos.writeUTF("ERROR Unknown protocol: " + protocol);
                dos.flush();
                socket.close();
                return;
            }

            chat.register(handler);
            handler.handle();

        } catch (Exception e) {
            System.err.println("Error in ClientHandler: " + e.getMessage());
        } finally {
            if (handler != null) chat.unregister(handler);
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
