package org.chat.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HandlerXML implements ProtocolHandler{


    public HandlerXML(Socket socket, Chat chat) {

    }

    @Override
    public void handle(){

    }

    @Override
    public void sendPing() throws Exception{

    }

    @Override
    public void sendRaw(String message){

    }

    @Override
    public String getSessionId(){
        return "";
    }
}
