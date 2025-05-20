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
            // 1) Читаем протокол текстом
            DataInputStream  dis  = new DataInputStream(socket.getInputStream());
            DataOutputStream dos  = new DataOutputStream(socket.getOutputStream());
            String protocol = dis.readUTF();

            // 2) В зависимости от protocol создаём нужный handler
            if ("obj".equalsIgnoreCase(protocol)) {
                // Оборачиваем в буферизированные Object-потоки
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                ObjectOutputStream  oos = new ObjectOutputStream(bos);
                oos.flush();

                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                ObjectInputStream  ois = new ObjectInputStream(bis);

                handler = new HandlerOBJ(socket, chat, ois, oos);
            }
            else if ("xml".equalsIgnoreCase(protocol)) {
                handler = new HandlerXML(socket, chat);
            }
            else {
                dos.writeUTF("Unknown protocol: " + protocol);
                dos.flush();
                socket.close();
                return;
            }

            // 3) Регистрируем и запускаем основной цикл
            chat.register(handler);
            handler.handle();

        } catch (Exception e) {
            System.err.println("Error with ClientHandler: " + e.getMessage());
        } finally {
            if (handler != null) chat.unregister(handler);
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
