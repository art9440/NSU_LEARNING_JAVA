package org.chat.server;

import org.chat.common.messagesOBJ.*;

import java.io.*;
import java.lang.Error;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HandlerOBJ implements ProtocolHandler {
    private final Chat chat;
    private final Socket socket;
    private final String sessionId = UUID.randomUUID().toString();
    private String name = "Anonymous";

    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    private volatile boolean running = true;
    private volatile long lastActivity = System.currentTimeMillis();
    private static final long TIMEOUT_MS = 100_000L;

    // Один поток справится и с пингом, и с таймаутом
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public HandlerOBJ(Socket socket, Chat chat,
                      ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.chat = chat;
        this.ois = ois;
        this.oos = oos;
        startHeartbeat();
    }

    private void startHeartbeat() {
        // 1) Проверка таймаута
        scheduler.scheduleAtFixedRate(this::checkTimeout, 1, 1, TimeUnit.SECONDS);

        // 2) Отправка Ping
        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            try {
                sendObject(new Ping());
            } catch (IOException e) {
                System.err.println("Failed to send ping to " + name + ": " + e.getMessage());
                disconnect();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void checkTimeout() {
        if (!running) return;
        if (System.currentTimeMillis() - lastActivity > TIMEOUT_MS) {
            System.out.println("Client " + name + " timed out. Disconnecting...");
            disconnect();
        }
    }

    private void disconnect() {
        running = false;
        try { ois.close(); } catch (IOException ignored) {}
        try { socket.close(); } catch (IOException ignored) {}
        scheduler.shutdownNow();
    }

    @Override
    public void handle() {
        try {
            while (running) {
                Object obj = ois.readObject();
                lastActivity = System.currentTimeMillis();

                if (obj instanceof Pong) {
                    // просто обновили активность
                    continue;
                }

                if (obj instanceof LoginCommand cmd) {
                    this.name = cmd.login;
                    sendObject(new Success(sessionId));
                    for (String msg : chat.getHistory()) {
                        sendObject(new EventMessage(msg));
                    }
                    chat.broadcast("[SERVER] " + name + " has joined the chat");

                } else if (obj instanceof MessageCommand msg) {
                    if (!sessionId.equals(msg.session)) {
                        sendObject(new Error("Invalid session"));
                        continue;
                    }
                    String full = "[" + name + "] " + msg.message;
                    chat.broadcast(full);
                    sendObject(new Success());

                } else if (obj instanceof ListCommand list) {
                    if (!sessionId.equals(list.session)) {
                        sendObject(new Error("Invalid session"));
                        continue;
                    }
                    sendObject(new UserList(chat.getUserNames()));

                } else if (obj instanceof LogoutCommand logout) {
                    if (!sessionId.equals(logout.session)) {
                        sendObject(new Error("Invalid session"));
                        continue;
                    }
                    sendObject(new Success());
                    break;

                } else {
                    sendObject(new Error("Unknown command"));
                }
            }
        } catch (Exception e) {
            if (running) {
                System.err.println("HandlerOBJ error for " + name + ": " + e.getMessage());
            }
        } finally {
            running = false;
            chat.unregister(this);
            chat.broadcast("[SERVER] " + name + " disconnected");
            scheduler.shutdownNow();
        }
    }

    @Override
    public void sendRaw(String message) {
        try {
            sendObject(new EventMessage(message));
        } catch (IOException e) {
            System.err.println("Failed to send EventMessage to " + name + ": " + e.getMessage());
        }
    }

    /** Универсальный метод отправки объекта + flush */
    private void sendObject(Object obj) throws IOException {
        oos.writeObject(obj);
        oos.flush();
    }

    @Override
    public void sendPing() throws IOException {
        sendObject(new Ping());
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }
}
