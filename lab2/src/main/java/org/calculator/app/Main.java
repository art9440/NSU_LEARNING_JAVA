package org.calculator.app;

import org.calculator.commands.Command;
import org.calculator.exeptions.CommandNotFoundException;
import org.calculator.exeptions.ManyArgumentsException;
import org.calculator.exeptions.NoSuchVariableInMapException;
import org.calculator.factory.Factory;

import java.util.logging.*;

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.function.BiConsumer;

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
                Factory FactoryForCalc = new Factory("factoryconfig.txt");
                logger.info("Factory is created");
                Context context = new Context();
                logger.info("Context is created");

                if (args.length < 1) {
                    System.out.println("Console mode is on. Write exit to get out");
                    logger.info("Console mode is on");
                    workInConsoleMode(FactoryForCalc, context);
                }
                else {
                    String inputFile = args[0];
                    ConfigParser calcCommands = new ConfigParser(inputFile);
                    logger.info("ConfigParser is created.");
                    BiConsumer<String, String[]> commandLine = (command, arguments) -> {
                        try {
                            logger.info("Was written command: " + command + ". With arguments: " + Arrays.toString(arguments));

                            Command newCommand = FactoryForCalc.createCommand(command, arguments);
                            logger.info("Command successfully created: " + newCommand.getClass().getSimpleName());

                            logger.info("Applying command: " + newCommand.getClass().getSimpleName());
                            newCommand.apply(context);

                        }
                        catch (IOException e){
                            logger.severe("Error: " + e.getMessage());
                            logger.info("Console mode is on");
                            workInConsoleMode(FactoryForCalc, context);

                        }
                        catch (CommandNotFoundException | ClassNotFoundException | NoSuchVariableInMapException | ManyArgumentsException e) {
                            logger.warning(e.getMessage());
                        }
                    };

                    try {
                        calcCommands.readConfig(commandLine);
                    } catch (IOException e) {
                        logger.info("Console mode is on");
                        workInConsoleMode(FactoryForCalc, context);
                    }
                }
            }
        }
        catch (ManyArgumentsException e){
            logger.severe(e.getMessage());
        }
    }


    private static void workInConsoleMode(Factory factoryForCalc, Context context){
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("> "); // Символ приглашения
            String commandLine = input.nextLine().trim();
            logger.info("Console line was read.");
            if (commandLine.equalsIgnoreCase("exit")) {
                logger.info("Exit from Console mode.");
                System.out.println("Exit from console mode");
                break;
            }

            String[] parts = commandLine.split(" ");
            String command = parts[0];
            String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);

            try {
                logger.info("Creating command.");
                Command newCommand = factoryForCalc.createCommand(command, arguments);
                logger.info("Command created.");
                logger.info("Applying command...");
                newCommand.apply(context);
                logger.info("Command was applied.");
            } catch (IOException e) {
                logger.severe("Error: " + e.getMessage());
            } catch (ManyArgumentsException e) {
                logger.warning(e.getMessage());
            } catch (CommandNotFoundException | ClassNotFoundException | NoSuchVariableInMapException e) {
                logger.warning("Error while applying command: " + e.getMessage());
            }
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

