package org.chat.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String PROPERTIES_FILE = "server.properties";
    private static final Properties props = new Properties();


    static{
        try(InputStream in = Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream(PROPERTIES_FILE)){
            if (in == null) {
                throw new IllegalStateException(
                        "Не найден " + PROPERTIES_FILE + " в classpath");
            }
            props.load(in);
        } catch (IOException ex) {
            System.err.println("Error while reading file");
        }
    }

    private Config() {}

    public static int getPort(){
        String portStr = props.getProperty("port", "12345");
        return Integer.parseInt(portStr);
    }
}
