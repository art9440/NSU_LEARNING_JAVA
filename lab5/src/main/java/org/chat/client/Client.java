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
            DataInputStream  dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF(protocol);
            dos.flush();

            if ("obj".equals(protocol)) {
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                ObjectOutputStream  oos = new ObjectOutputStream(bos);
                oos.flush();

                BufferedInputStream  bis = new BufferedInputStream(socket.getInputStream());
                ObjectInputStream   ois = new ObjectInputStream(bis);

                ClientOBJ client = new ClientOBJ(socket, login, ois, oos);
                client.start();
            } else {
                //new ClientXML(socket, login, dis, dos).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
