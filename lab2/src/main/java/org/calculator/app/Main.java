package org.calculator.app;

import org.calculator.exeptions.CommandNotFoundException;
import org.calculator.exeptions.ManyArgumentsException;
import org.calculator.factory.Factory;

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
                    Scanner input = new Scanner(System.in);
                    String command = " ";
                    while (command.equals("exit\n")) {
                        command = input.nextLine();
                        //Some working ....
                    }

                } else {
                    String inputFile = args[0];
                    ConfigParser calcCommands = new ConfigParser(inputFile);
                    BiConsumer<String, String[]> commandLine = (command, arguments) -> {
                        try {
                            System.out.println(command + " " + Arrays.toString(arguments));
                            Object newCommand = FactoryForCalc.createCommand(command, arguments);
                            System.out.println("✅ Команда успешно создана: " + newCommand.getClass().getSimpleName());
                            FactoryForCalc.apply(context);

                        }
                        catch (IOException e){
                            System.err.println(e.getMessage() + "/");
                            System.exit(1);

                        } catch (CommandNotFoundException | ClassNotFoundException e) {
                            System.err.println(e.getMessage());
                        }
                    };

                    calcCommands.readConfig(commandLine);
                }
            }
        }
        catch (ManyArgumentsException e){
            System.err.println(e.getMessage());
        }
    }
}

//через BiConsuming буду возвращать команды и потом в фабрике создавать команду и затем с помощью метода apply, передавая
//в него контекст, буду ее выполнять