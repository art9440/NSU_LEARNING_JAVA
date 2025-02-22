package org.calculator.exeptions;

public class CommandNotFoundException extends Exception{

    public CommandNotFoundException(String message){
        super(message);
    }
}
