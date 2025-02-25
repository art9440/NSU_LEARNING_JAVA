package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.ManyArgumentsException;
import org.calculator.exeptions.NoSuchVariableInMapException;

import java.util.logging.Logger;

public class Push implements Command {
    private static final Logger logger = Logger.getLogger(Push.class.getName());
    private final String[] args;

    public Push(String[] args) {
        this.args = args;

    }

    @Override
    public void apply(Context context) throws ManyArgumentsException, NoSuchVariableInMapException {

        if (args.length != 1){
            throw new ManyArgumentsException("Command push needs 1 argument: variable or value");
        }

        String varOrVal = args[0];

        try{
            double value = Double.parseDouble(varOrVal);
            context.push(value);
            logger.info("PUSH: " + value + " (value)");
        } catch (NumberFormatException e){
            double value = context.getFromMap(varOrVal);
            context.push(value);
            logger.info("PUSH: " + varOrVal + " = " + value + " (variable)");
        }
    }
}
