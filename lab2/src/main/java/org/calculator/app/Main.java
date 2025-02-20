package org.calculator.app;

import org.calculator.exeptions.ManyArgumentsException;
import org.calculator.factory.Factory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.*;

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
                    String command = new String();
                    while (command.equals("exit\n")) {
                        command = input.nextLine();
                        //Some working ....
                    }

                } else {
                    String inputFile = args[0];
                    ConfigParser calculCommands = new ConfigParser(inputFile);
                    calculCommands.readConfig();
                }
            }
        }
        catch (ManyArgumentsException e){
            System.out.println(e.getMessage());
        }
    }
}