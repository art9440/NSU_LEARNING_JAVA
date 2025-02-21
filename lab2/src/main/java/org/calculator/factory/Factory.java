package org.calculator.factory;


import org.calculator.app.Context;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Factory {
    private final String factoryConfig;

    public Factory(String factoryConfig) {this.factoryConfig = factoryConfig;}

    public static void apply(Context context){

    }

    public static void createCommand(String command, String[] arguments) throws ClassNotFoundException, FileNotFoundException{
        String pathToCommand = search(command);
        System.out.println(pathToCommand);
        Class<?> newCommand = Class.forName(pathToCommand);

    }

    private static String search(String command) throws FileNotFoundException{
        try(InputStream inputStream = Factory.class.getResourceAsStream("factoryconfig.txt")){
            if (inputStream == null){
                throw new FileNotFoundException("Can`t find factory config.");
            }


            BufferedReader configFactoryReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            System.out.println("Searching for class path...");
            String line;
            while ((line = configFactoryReader.readLine()) != null){
                String[] lineDiv = line.split("=");
                if (lineDiv[0].equals(command)){
                    return lineDiv[1];
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return "null";
    }



}
