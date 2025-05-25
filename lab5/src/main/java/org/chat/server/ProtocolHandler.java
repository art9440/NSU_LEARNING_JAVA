package org.chat.server;



public interface ProtocolHandler {

    void handle();
    void sendRaw(String message);
    String getName();
}
