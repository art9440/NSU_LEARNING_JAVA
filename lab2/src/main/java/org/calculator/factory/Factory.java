package org.calculator.factory;


import org.calculator.commands.Command;
import org.calculator.exeptions.CommandNotFoundException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Factory {
    private final String factoryConfig;
    private static final Logger logger = Logger.getLogger(Factory.class.getName());

    public Factory(String factoryConfig) {
        this.factoryConfig = factoryConfig;
        logger.info("Factory is created");
    }


    public Command createCommand(String command, String[] arguments) throws ClassNotFoundException, IOException, CommandNotFoundException{
        logger.info("Process of creating command: " + command + " is started");
        try {
            String pathToCommand = search(command);
            logger.info("Path to command: " + command + " is: " + pathToCommand);


            return (Command) Class.forName(pathToCommand).getDeclaredConstructor(String[].class).newInstance((Object) arguments);

        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            logger.severe("Something went wrong in Creating command with using this class ->  " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    String search(String command) throws IOException, CommandNotFoundException {
        logger.info("Searching path for command: " + command + " is started.");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(factoryConfig);
        logger.info("factory config file was opened.");
        if (inputStream == null) {
            throw new FileNotFoundException("Can`t find factory config.");
        }


        BufferedReader configFactoryReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        while ((line = configFactoryReader.readLine()) != null) {
            String[] lineDiv = line.split("=");
            if (lineDiv[0].equals(command)) {
                logger.info("Command: " + command + " was found in factory config.");
                inputStream.close();
                logger.info("factory config file is closed.");
                return lineDiv[1];
            }
        }

        inputStream.close();
        logger.info("factory config file is closed.");

        throw new CommandNotFoundException("Can`t find "  + command + " in Factory config");


    }


}
