package org.calculator.factory;


import org.calculator.app.Context;
import org.calculator.exeptions.CommandNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Factory {
    private final String factoryConfig;

    public Factory(String factoryConfig) {
        this.factoryConfig = factoryConfig;
    }

    public void apply(Context context) {

    }

    public void createCommand(String command, String[] arguments) throws ClassNotFoundException, IOException, CommandNotFoundException {
        String pathToCommand = search(command);
        System.out.println(pathToCommand);
        Class<?> newCommand = Class.forName(pathToCommand);

    }

    private String search(String command) throws IOException, CommandNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("factoryconfig.txt");

        if (inputStream == null) {
            throw new FileNotFoundException("Can`t find factory config.");
        }


        BufferedReader configFactoryReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        System.out.println("Searching for class path...");
        String line;
        while ((line = configFactoryReader.readLine()) != null) {
            String[] lineDiv = line.split("=");
            if (lineDiv[0].equals(command)) {
                return lineDiv[1];
            }
        }

        inputStream.close();

        throw new CommandNotFoundException("Can`t find "  + command + " in Factory config");


    }


}
