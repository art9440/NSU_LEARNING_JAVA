package org.calculator.app;

import org.calculator.commands.Command;
import org.calculator.exeptions.CommandNotFoundException;
import org.calculator.exeptions.ManyArgumentsException;
import org.calculator.exeptions.NoSuchVariableInMapException;
import org.calculator.factory.Factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Calculator {
    private Factory factoryForCalc = new Factory("factoryconfig.txt");
    private Context context = new Context();
    private static final Logger logger = Logger.getLogger(Calculator.class.getName());


    public Calculator(){

    }

    public void launchWithConfig(String[] args){
        String inputFile = args[0];
        ConfigParser calcCommands = new ConfigParser(inputFile);
        logger.info("ConfigParser is created.");
        BiConsumer<String, String[]> commandLine = (command, arguments) -> {
            try {
                logger.info("Was written command: " + command + ". With arguments: " + Arrays.toString(arguments));

                Command newCommand = factoryForCalc.createCommand(command, arguments);
                logger.info("Command successfully created: " + newCommand.getClass().getSimpleName());

                logger.info("Applying command: " + newCommand.getClass().getSimpleName());
                newCommand.apply(context);
            }
            catch (IOException e){
                logger.severe("Error: " + e.getMessage());
                logger.info("Console mode is on");
                launchInConsoleMode();

            }
            catch (CommandNotFoundException | ClassNotFoundException | NoSuchVariableInMapException | ManyArgumentsException e) {
                logger.warning(e.getMessage());
            }
        };

        try {
            calcCommands.readConfig(commandLine);
        } catch (IOException e) {
            logger.info("Console mode is on");
            launchInConsoleMode();
        }
    }

    public void launchInConsoleMode(){
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

}
