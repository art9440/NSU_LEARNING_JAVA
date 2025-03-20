package org.calculator.exeptions.configExceptions;

import org.calculator.exeptions.mainExceptions.CalculatorException;

public class ConfigParserException extends CalculatorException {

    public ConfigParserException(String message){
        super(message);
    }
}
