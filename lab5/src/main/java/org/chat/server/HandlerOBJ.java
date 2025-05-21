package org.chat.server;

import org.chat.common.messagesOBJ.*;

import org.chat.common.messagesOBJ.Error;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HandlerOBJ implements ProtocolHandler {
    private final Chat chat;
    private final Socket socket;
    private final String sessionId = UUID.randomUUID().toString();
    private String name = "Anonymous";

    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    private volatile boolean running = true;
    private volatile long lastActivity = System.currentTimeMillis();
    private volatile long lastPongTime  = System.currentTimeMillis();

    private static final long IDLE_TIMEOUT_MS = 100_000L;  // 100 с без «реальных» сообщений
    private static final long PONG_TIMEOUT_MS = 10_000L;   // 10 с без ответа на ping

    private final AtomicBoolean disconnected = new AtomicBoolean(false);

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    public HandlerOBJ(Socket socket, Chat chat,
                      ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.chat   = chat;
        this.ois    = ois;
        this.oos    = oos;
        startHeartbeat();
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
                sendObject(new Ping());
            } catch (IOException e) {
                disconnect("ping send failed: " + e.getMessage());
            }
        }, 1, 1, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            if (System.currentTimeMillis() - lastPongTime > PONG_TIMEOUT_MS) {
                disconnect("pong timeout");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void disconnect(String reason) {
        if (!disconnected.compareAndSet(false, true)) return;

        System.out.println("Disconnecting " + name + ": " + reason);
        running = false;

        try { socket.shutdownInput(); } catch (IOException ignored) {}

        try { ois.close(); } catch (IOException ignored) {}
        try { oos.close(); } catch (IOException ignored) {}
        try { socket.close(); } catch (IOException ignored) {}

        scheduler.shutdownNow();
    }

    @Override
    public void handle() {
        try {
            while (running) {
                Object obj = ois.readObject();

                if (obj instanceof Pong p && sessionId.equals(p.session)) {
                    lastPongTime = System.currentTimeMillis();
                    continue;
                }

                lastActivity = System.currentTimeMillis();

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
                    chat.broadcastExcept(full, this);
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
        } catch (IOException | ClassNotFoundException e) {
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
    public synchronized void  sendRaw(String message) {
        try {
            sendObject(new EventMessage(message));
        } catch (IOException e) {
            System.err.println("Failed to send EventMessage to " + name + ": " + e.getMessage());
        }
    }

    private void sendObject(Object obj) throws IOException {
        oos.writeObject(obj);
        oos.flush();
    }

    @Override
    public String getName(){
        return name;
    }
}
