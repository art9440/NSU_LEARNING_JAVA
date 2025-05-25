package org.chat.server;

import org.chat.common.messagesOBJ.*;

import org.chat.common.messagesOBJ.Error;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HandlerOBJ implements ProtocolHandler {
    private static final long IDLE_TIMEOUT_MS = 100_000L;
    private static final long PONG_TIMEOUT_MS = 10_000L;

    private final Chat                        chat;
    private final Socket                      socket;
    private final String                      sessionId     = UUID.randomUUID().toString();
    private       String                      name          = "Anonymous";
    private final ObjectInputStream           ois;
    private final ObjectOutputStream          oos;

    private volatile boolean running         = true;
    private volatile long    lastActivity    = System.currentTimeMillis();
    private volatile long    lastPongTime    = System.currentTimeMillis();

    private final AtomicBoolean               disconnected   = new AtomicBoolean(false);
    private final ScheduledExecutorService    scheduler      = Executors.newScheduledThreadPool(2);
    private final ExecutorService             sendExecutor   =
            Executors.newSingleThreadExecutor(r -> { Thread t = new Thread(r, "send-"+sessionId); t.setDaemon(true); return t; });

    public HandlerOBJ(Socket socket, Chat chat,
                      ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.chat   = chat;
        this.ois    = ois;
        this.oos    = oos;

        log("[CONNECTED] session=%s from %s", sessionId, socket.getRemoteSocketAddress());
        startHeartbeat();
    }

    private void startHeartbeat() {
        // Idle
        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            if (System.currentTimeMillis() - lastActivity > IDLE_TIMEOUT_MS) {
                disconnect("idle timeout");
            }
        }, 1, 1, TimeUnit.SECONDS);

        // Send Ping
        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            try {
                sendObject(new Ping());
                log("[PING SENT] to %s", name);
            } catch (IOException e) {
                disconnect("ping send failed: " + e.getMessage());
            }
        }, 1, 1, TimeUnit.SECONDS);

        // Pong timeout
        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            if (System.currentTimeMillis() - lastPongTime > PONG_TIMEOUT_MS) {
                disconnect("pong timeout");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void disconnect(String reason) {
        if (!disconnected.compareAndSet(false, true)) return;
        log("[DISCONNECTING] %s: %s", name, reason);
        running = false;
        try { socket.shutdownInput(); } catch (IOException ignored) {}
        try { ois.close(); } catch (IOException ignored) {}
        try { oos.close(); } catch (IOException ignored) {}
        try { socket.close(); } catch (IOException ignored) {}
        scheduler.shutdownNow();
        sendExecutor.shutdownNow();
    }

    @Override
    public void handle() {
        try {
            while (running) {
                Object obj = ois.readObject();
                log("[RECV] %s ← %s", name, obj.getClass().getSimpleName());

                if (obj instanceof Pong p && sessionId.equals(p.session)) {
                    lastPongTime = System.currentTimeMillis();
                    log("[PONG] received from %s", name);
                    continue;
                }

                lastActivity = System.currentTimeMillis();

                if (obj instanceof LoginCommand cmd) {
                    name = cmd.login;
                    log("[LOGIN] session=%s as user=%s", sessionId, name);

                    sendObject(new Success(sessionId));
                    for (String hist : chat.getHistory()) {
                        sendRaw(hist);
                    }
                    chat.broadcast("[SERVER] " + name + " has joined the chat");

                } else if (obj instanceof MessageCommand msg) {
                    if (!sessionId.equals(msg.session)) {
                        log("[ERROR] Invalid session in MessageCommand from %s", name);
                        sendObject(new Error("Invalid session"));
                        continue;
                    }
                    log("[MESSAGE] from %s: %s", name, msg.message);
                    String full = "[" + name + "] " + msg.message;
                    chat.broadcastExcept(full, this);
                    sendObject(new Success());

                } else if (obj instanceof ListCommand list) {
                    if (!sessionId.equals(list.session)) {
                        log("[ERROR] Invalid session in ListCommand from %s", name);
                        sendObject(new Error("Invalid session"));
                        continue;
                    }
                    log("[LIST] requested by %s", name);
                    sendObject(new UserList(chat.getUserNames()));

                } else if (obj instanceof LogoutCommand logout) {
                    if (!sessionId.equals(logout.session)) {
                        log("[ERROR] Invalid session in LogoutCommand from %s", name);
                        sendObject(new Error("Invalid session"));
                        continue;
                    }
                    log("[LOGOUT] from %s", name);
                    sendObject(new Success());
                    break;

                } else {
                    log("[ERROR] Unknown command from %s: %s", name, obj.getClass());
                    sendObject(new Error("Unknown command"));
                }
            }
        } catch (IOException|ClassNotFoundException e) {
            if (running) log("[ERROR] handler for %s: %s", name, e.getMessage());
        } finally {
            running = false;
            chat.unregister(this);
            log("[UNREGISTER] %s", name);
            chat.broadcast("[SERVER] " + name + " disconnected");
            scheduler.shutdownNow();
        }
    }

    @Override
    public synchronized void sendRaw(String message) {
        sendExecutor.submit(() -> {
            try {
                oos.writeObject(new EventMessage(message));
                oos.flush();
                log("[SEND] to %s: %s", name, message);
            } catch (IOException e) {
                log("[ERROR] sending to %s: %s", name, e.getMessage());
            }
        });
    }

    private void sendObject(Object obj) throws IOException {
        oos.writeObject(obj);
        oos.flush();
    }

    @Override
    public String getName() {
        return name;
    }

    private void log(String fmt, Object... args) {
        System.out.printf((fmt.endsWith("%n") ? fmt : fmt + "%n"), args);
    }
}
