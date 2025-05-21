package org.chat.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream  ois = new ObjectInputStream(socket.getInputStream());

            String protocol = (String) ois.readObject();

            if ("obj".equalsIgnoreCase(protocol)) {
                handler = new HandlerOBJ(socket, chat, ois, oos);
            }
            else if ("xml".equalsIgnoreCase(protocol)) {
                handler = new HandlerXML(socket, chat);
            }
            else {

                oos.writeObject("Unknown protocol: " + protocol);
                oos.flush();
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
