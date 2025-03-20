package org.calculator.exeptions.configExceptions;

public class ConfigFileNotFoundException extends ConfigParserException{
    public ConfigFileNotFoundException(String message){
        super(message);
    }
}
