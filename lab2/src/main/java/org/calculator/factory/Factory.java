package org.calculator.factory;


import org.calculator.app.Context;
import org.calculator.exeptions.CommandNotFoundException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class Factory {
    private final String factoryConfig;

    public Factory(String factoryConfig) {
        this.factoryConfig = factoryConfig;
    }

    public void apply(Context context) {

    }

    public Object createCommand(String command, String[] arguments) throws ClassNotFoundException, IOException, CommandNotFoundException{
        try {
            String pathToCommand = search(command);
            System.out.println(pathToCommand);
            Object newCommand = Class.forName(pathToCommand).getDeclaredConstructor().newInstance();



            return newCommand;
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private String search(String command) throws IOException, CommandNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(factoryConfig);

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
