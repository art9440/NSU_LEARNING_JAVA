package org.calculator.app;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class ConfigParser {
    private final String inputFile;


    public ConfigParser(String inputFile){ this.inputFile = inputFile;}

    public void readConfig(BiConsumer<String, String[]> commandLine) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFile);
            if (inputStream == null){
                throw new FileNotFoundException("Not found file in folder resources: " + inputFile);
            }

            BufferedReader configReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            System.out.println("Calculating is started...\n");
            String command;
            while ((command = configReader.readLine()) != null){
                command = command.trim();
                if (command.startsWith("#") || command.isEmpty()){
                    continue;
                }
                else{
                    String[] commandArray = command.split(" ");
                    commandLine.accept(commandArray[0], Arrays.copyOfRange(commandArray, 1, commandArray.length));


                }
            }

            inputStream.close();

    }
}
