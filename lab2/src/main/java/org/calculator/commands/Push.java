package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.NoSuchVariableInMapException;

import java.util.Arrays;

public class Push implements Command {
    private final String[] args;

    public Push(String[] args) {
        this.args = args;

    }

    @Override
    public void apply(Context context) throws IllegalArgumentException, NoSuchVariableInMapException {
        System.out.println(Arrays.toString(args));

        if (args.length != 1){
            throw new IllegalArgumentException("Command push needs 1 argument: variable or value");
        }

        String varOrVal = args[0];

        try{
            double value = Double.parseDouble(varOrVal);
            context.push(value);
            System.out.println("📥 PUSH: " + value + " (число)");
        } catch (NumberFormatException e){
            double value = context.getFromMap(varOrVal);
            context.push(value);
            System.out.println("📥 PUSH: " + varOrVal + " = " + value + " (переменная)");
        }
    }
}
