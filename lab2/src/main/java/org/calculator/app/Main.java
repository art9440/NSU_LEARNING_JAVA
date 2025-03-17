package org.calculator.app;


import org.calculator.exeptions.ManyArgumentsException;
import java.util.logging.*;
import java.io.*;


public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        setupLogger();
        logger.info("Running program");
        try {
            if (args.length > 1){
                throw new ManyArgumentsException("Error: Too many arguments\n");
            }
            else{
                Calculator calculator = new Calculator();
                if (args.length < 1) {
                    System.out.println("Console mode is on. Write exit to get out");
                    logger.info("Console mode is on");
                    calculator.launchInConsoleMode();
                }
                else {
                    calculator.launchWithConfig(args);
                }
            }
        }
        catch (ManyArgumentsException e){
            logger.severe(e.getMessage());
        }
    }



    private static void setupLogger(){
        try {
            InputStream configStream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            if (configStream == null) {
                System.err.println("Error: logging.properties not found!");
            } else {
                LogManager.getLogManager().readConfiguration(configStream);
                System.out.println("Config logging is loaded!");
            }

        } catch (IOException e) {
            System.err.println("Error logging: " + e.getMessage());
        }
    }
}

