package org.chat;


import org.chat.client.Client;
import org.chat.server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        switch (args[0]) {
            case "server" -> {
                System.out.println("Turn on server...");
                Server server = new Server();
                server.startServer(); // Блокирует main-поток
            }
            case "client" -> {
                System.out.println("Turn on client...");
                Client client = new Client();
                client.startInteractive(); // Блокирует main-поток для ввода
            }
            default -> {
                System.err.println("Unknown Mode. Write 'server' or 'client'.");
                System.exit(1);
            }
        }
    }
}