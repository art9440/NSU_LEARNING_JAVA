package org.calculator.factory;


import org.calculator.commands.Command;
import org.calculator.exeptions.factoryExceptions.CommandNotFoundException;
import org.calculator.exeptions.factoryExceptions.ConfigFactoryNotFoundException;
import org.calculator.exeptions.factoryExceptions.CreatingCommandException;
import org.calculator.exeptions.factoryExceptions.FactoryException;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Factory {
    private final String factoryConfig;
    private static final Logger logger = Logger.getLogger(Factory.class.getName());
    private Map<String, Constructor> factoryMap = new HashMap<>();

    public Factory(String factoryConfig) {
        this.factoryConfig = factoryConfig;
        try {
            this.mappingConfig();
        }catch (ConfigFactoryNotFoundException | IOException e){
            logger.severe("Error: " + e.getMessage());
            System.exit(1);
        }
        catch (CommandNotFoundException | CreatingCommandException e){
            logger.severe("Error:" + e.getMessage());
            System.exit(1);
        }
        logger.info("Factory is created");
    }

    private void mappingConfig() throws ConfigFactoryNotFoundException, IOException, CommandNotFoundException, CreatingCommandException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(factoryConfig);
        if (inputStream == null) {
            throw new ConfigFactoryNotFoundException("Can`t find factory config.");
        }
        try {
            BufferedReader configFactoryReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = configFactoryReader.readLine()) != null) {
                String[] lineDiv = line.split("=");
                factoryMap.put(lineDiv[0], Class.forName(lineDiv[1]).getDeclaredConstructor(String[].class));
            }
        }
        catch (ClassNotFoundException e){
            logger.severe("Something went wrong while searching this class: " + e.getMessage());
            throw new CommandNotFoundException(e.getMessage());
        }
        catch (NoSuchMethodException e){
            logger.severe("Something went wrong in Creating command with using this class ->  " + e.getMessage());
            throw new CreatingCommandException(e.getMessage());
        }

        inputStream.close();
        logger.info("factory config file is closed.");
    }


    public Command createCommand(String command, String[] arguments) throws FactoryException {
        logger.info("Process of creating command: " + command + " is started");
        try {
            Constructor classConstructor = search(command);


            return (Command) classConstructor.newInstance((Object) arguments);

        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
            logger.severe("Something went wrong in Creating command with using this class ->  " + e.getMessage());
            throw new CreatingCommandException(e.getMessage());
        }
    }

    private Constructor search(String command) throws CommandNotFoundException {
        logger.info("Searching path for command: " + command + " is started.");

        Constructor path = factoryMap.get(command);
        if (path != null){
            return path;
        }

        throw new CommandNotFoundException("Can`t find "  + command + " in Factory config");
    }


}
