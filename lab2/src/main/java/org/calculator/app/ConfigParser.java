package org.calculator.app;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigParser {
    String inputFile;
    public ConfigParser(String inputFile){ this.inputFile = inputFile;}

    public void readConfig(){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFile)){
            if (inputStream == null){
                throw new FileNotFoundException("Not found file in folder resources: " + inputFile);
            }

            BufferedReader configReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String command;
            System.out.println("Calculating is started...\n");
            while ((command = configReader.readLine()) != null){

            }

        }
        catch (IOException e){
            System.err.println("Error while reading file:" + e.getLocalizedMessage());
        }
    }
}
