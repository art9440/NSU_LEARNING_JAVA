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
    public static void main(String[] args) {
        try {
            if (args.length > 1){
                throw new ManyArgumentsException("Error: Too many arguments\n");
            }
            else{
                Factory FactoryForCalc = new Factory("factoryconfig.txt");
                Context context = new Context();

                if (args.length < 1) {
                    System.out.println("Console mode is on. Write exit to get out");
                    workInConsoleMode(FactoryForCalc, context);
                }
                else {
                    String inputFile = args[0];
                    ConfigParser calcCommands = new ConfigParser(inputFile);
                    BiConsumer<String, String[]> commandLine = (command, arguments) -> {
                        try {
                            System.out.println(command + " " + Arrays.toString(arguments));
                            Command newCommand = FactoryForCalc.createCommand(command, arguments);

                            System.out.println("✅ Команда успешно создана: " + newCommand.getClass().getSimpleName());

                            newCommand.apply(context);

                        }
                        catch (IOException e){
                            System.err.println(e.getMessage());
                            workInConsoleMode(FactoryForCalc, context);

                        } catch (CommandNotFoundException | ClassNotFoundException | NoSuchVariableInMapException e) {
                            System.err.println(e.getMessage());
                        }
                    };
                    try {
                        calcCommands.readConfig(commandLine);
                    } catch (IOException e) {
                        workInConsoleMode(FactoryForCalc, context);
                    }
                }
            }
        }
        catch (ManyArgumentsException e){
            System.err.println(e.getMessage());
        }
    }


    private static void workInConsoleMode(Factory factoryForCalc, Context context){
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("> "); // Символ приглашения
            String commandLine = input.nextLine().trim();
            if (commandLine.equalsIgnoreCase("exit")) {
                System.out.println("Выход из системы");
                break;
            }

            String[] parts = commandLine.split(" ");
            String command = parts[0];
            String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);

            try {
                Command newCommand = factoryForCalc.createCommand(command, arguments);
                System.out.println("Команда: " + newCommand.getClass().getSimpleName());
                newCommand.apply(context);
            } catch (IOException e) {
                System.err.println("Ошибка: " + e.getMessage());
            } catch (CommandNotFoundException | ClassNotFoundException | NoSuchVariableInMapException e) {
                System.err.println("Ошибка выполнения команды: " + e.getMessage());
            }
        }
    }
}

