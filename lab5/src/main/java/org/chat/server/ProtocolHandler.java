package org.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ProtocolHandler {

    void handle();
    void sendRaw(String message);
    String getName();

}
