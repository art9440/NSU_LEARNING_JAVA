package org.calculator.app;

import org.calculator.exeptions.ManyArgumentsException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Console mode is on. Write exit to get out");
                Scanner input = new Scanner(System.in);
                String command = new String();
                while (command.equals("exit\n")) {
                    command = input.nextLine();
                    //Some working ....
                }

            } else if (args.length == 1) {
                String inputFile = args[0];
                ConfigParser calculCommands = new ConfigParser(inputFile);
                calculCommands.readConfig();
            } else {
                throw new ManyArgumentsException("Too many arguments\n");
            }
        }
        catch (ManyArgumentsException e){
            System.out.println(e.getMessage());
        }
    }
}