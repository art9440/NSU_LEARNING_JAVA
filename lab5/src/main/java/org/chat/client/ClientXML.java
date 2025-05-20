package org.chat.client;

import java.net.Socket;

public class ClientXML implements ClientProtocol{
    private final Socket socket;
    private final String login;

    public ClientXML(Socket socket, String login) {
        this.socket = socket;
        this.login = login;
    }

    @Override
    public void start(){

    }
}
