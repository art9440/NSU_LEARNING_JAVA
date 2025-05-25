package org.chat.client;

import org.chat.common.messagesOBJ.*;
import org.chat.common.messagesOBJ.Error;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientOBJ implements ClientProtocol {
    private final Socket socket;
    private final String login;
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    private volatile boolean running  = true;
    private volatile boolean skipPing = false;
    private String sessionId;

    public ClientOBJ(Socket socket,
                     String login,
                     ObjectInputStream ois,
                     ObjectOutputStream oos) {
        this.socket = socket;
        this.login  = login;
        this.ois    = ois;
        this.oos    = oos;
    }

    @Override
    public void start() {
        try {

            LoginCommand loginCmd = new LoginCommand();
            loginCmd.login = login;
            loginCmd.type  = "obj";
            oos.writeObject(loginCmd);
            oos.flush();

            Object resp = ois.readObject();
            if (resp instanceof Success s) {
                sessionId = s.sessionId;
                System.out.println("Successfully log in, sessionId=" + sessionId);
            } else if (resp instanceof Error e) {
                System.err.println("Error while entering: " + e.message);
                return;
            } else {
                System.err.println("Unknown answer : " + resp);
                return;
            }


            Thread readerThread = new Thread(this::receiveLoop, "client-receiver");
            readerThread.start();


            Scanner scanner = new Scanner(System.in);
            while (running) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;


                if (line.startsWith("/pause")) {
                    String[] parts = line.split("\\s+", 2);
                    int secs = 10;
                    if (parts.length == 2) {
                        try { secs = Integer.parseInt(parts[1]); }
                        catch (NumberFormatException ignored) {}
                    }
                    System.out.println("Pausing ping-response for " + secs + " seconds...");
                    skipPing = true;
                    int finalSecs = secs;
                    new Thread(() -> {
                        try { Thread.sleep(finalSecs * 1000L); }
                        catch (InterruptedException ignored) {}
                        skipPing = false;
                        System.out.println("Resuming ping-response.");
                    }, "pause-timer").start();
                    continue;
                }


                if (line.equalsIgnoreCase("/exit")) {
                    LogoutCommand logout = new LogoutCommand();
                    logout.session = sessionId;
                    oos.writeObject(logout);
                    oos.flush();
                    running = false;

                } else if (line.equalsIgnoreCase("/list")) {
                    ListCommand list = new ListCommand();
                    list.session = sessionId;
                    oos.writeObject(list);
                    oos.flush();

                } else {
                    System.out.print("\033[1A");
                    System.out.print("\033[2K");
                    System.out.flush();

                    System.out.println("[" + login + "] " + line);

                    MessageCommand msg = new MessageCommand();
                    msg.session = sessionId;
                    msg.message = line;
                    oos.writeObject(msg);
                    oos.flush();
                }
            }

            readerThread.join();

        } catch (Exception ex) {
            System.err.println("Client error: " + ex.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void receiveLoop() {
        try {
            while (running) {
                Object obj = ois.readObject();

                if (obj instanceof Ping) {

                    if (!skipPing) {
                        oos.writeObject(new Pong(sessionId));
                        oos.flush();
                    }
                    continue;
                }

                if (obj instanceof EventMessage m) {
                    System.out.println(m.message);
                } else if (obj instanceof UserList list) {
                    System.out.println("Users:");
                    list.users.forEach(user -> System.out.println("- " + user));
                } else if (obj instanceof Success) {
                    //System.out.println("OK");
                } else if (obj instanceof Error e) {
                    System.err.println("Error: " + e.message);
                } else {
                    System.out.println("Unknown object: " + obj);
                }
            }
        } catch (Exception e) {
            System.err.println("Disconnection " + e.getMessage());
        }
    }
}
