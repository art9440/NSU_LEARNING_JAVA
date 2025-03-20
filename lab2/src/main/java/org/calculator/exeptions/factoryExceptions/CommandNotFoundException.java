package org.calculator.exeptions.factoryExceptions;

public class CommandNotFoundException extends FactoryException {

    public CommandNotFoundException(String message){
        super(message);
    }
}
