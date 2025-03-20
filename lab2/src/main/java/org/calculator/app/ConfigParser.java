package org.calculator.app;

import org.calculator.exeptions.configExceptions.ConfigFileNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class ConfigParser {
    private final String inputFile;
    private static final Logger logger = Logger.getLogger(ConfigParser.class.getName());


    public ConfigParser(String inputFile){ this.inputFile = inputFile;}

    public void readConfig(BiConsumer<String, String[]> commandLine) throws ConfigFileNotFoundException, IOException {
        logger.info("Reading config is started.");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFile);
            if (inputStream == null){
                logger.warning("Can`t find file in folder resources.");
                throw new ConfigFileNotFoundException("Not found file in folder resources: " + inputFile);
            }

            BufferedReader configReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            logger.info("Reading commands from config...\n");
            String command;
            while ((command = configReader.readLine()) != null){
                command = command.trim();
                if (command.startsWith("#") || command.isEmpty()){
                    logger.info("Comment was read.");
                }
                else{
                    String[] commandArray = command.split(" ");
                    logger.info("Command was read from config file.");
                    commandLine.accept(commandArray[0], Arrays.copyOfRange(commandArray, 1, commandArray.length));


                }
            }

            inputStream.close();
            logger.info("Config file was closed.");

    }
}
