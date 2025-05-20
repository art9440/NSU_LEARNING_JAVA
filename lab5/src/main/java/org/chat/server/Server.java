package org.chat.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port = Config.getPort();
    private final ExecutorService pool =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Chat chat = new Chat();

    public void startServer() {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);

            while (true) {
                Socket client = server.accept();
                pool.submit(new ClientHandler(client, chat));
            }

        } catch (SocketException e) {
            System.err.println("Socket exception: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O exception: " + e.getMessage());
        }
    }
}
