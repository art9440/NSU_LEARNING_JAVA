package org.chat.server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chat {
    private static final int HISTORY_SIZE = 10;
    private final Deque<String> history = new ArrayDeque<>();
    private final Set<ProtocolHandler> clients = ConcurrentHashMap.newKeySet();

    private final ExecutorService broadcaster =
            Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "chat-broadcaster");
                t.setDaemon(true);
                return t;
            });

    private final ExecutorService senderPool = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "chat-sender");
        t.setDaemon(true);
        return t;
    });

    public void register(ProtocolHandler h){
        clients.add(h);
    }

    public void unregister(ProtocolHandler h) {
        clients.remove(h);
    }

    public void broadcastExcept(String message) {
        synchronized (history) {
            if (history.size() == HISTORY_SIZE) history.removeFirst();
            history.addLast(message);
        }
        for (ProtocolHandler h : clients) {
            senderPool.submit(() -> h.sendRaw(message));

        }
    }

    public void broadcast(String message) {
        broadcastExcept(message);
    }

    public List<String> getHistory() {
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    public List<String> getUserNames() {
        List<String> names = new ArrayList<>();
        for (ProtocolHandler h : clients) {
            names.add(h.getName());
        }
        return names;
    }
}
