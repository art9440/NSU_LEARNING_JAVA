package org.calculator.exeptions.commandsExceptions;

public class DivisionByZeroException extends ArithmeticCommandException{
    public DivisionByZeroException(String message){
        super(message);
    }

}
