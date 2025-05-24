package org.chat.client;

import org.chat.server.Config;

import java.io.*;
import java.net.Socket;

import java.util.Scanner;

public class Client {
    public void startInteractive() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Login: ");
            String login = scanner.nextLine().trim();

            String protocol = "";
            while (!protocol.equals("obj") && !protocol.equals("xml")) {
                System.out.print("Choose protocol (obj/xml): ");
                protocol = scanner.nextLine().trim().toLowerCase();
            }

            Socket socket = new Socket("localhost", Config.getPort());

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream  dis = new DataInputStream (socket.getInputStream());
            dos.writeUTF(protocol);
            dos.flush();

            if ("obj".equals(protocol)) {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                ObjectInputStream  ois = new ObjectInputStream(socket.getInputStream());

                ClientOBJ client = new ClientOBJ(socket, login, ois, oos);
                client.start();

            } else {

                ClientXML client = new ClientXML(socket, login);
                client.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
