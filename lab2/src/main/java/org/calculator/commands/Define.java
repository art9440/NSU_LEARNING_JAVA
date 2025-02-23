package org.calculator.commands;

import org.calculator.app.Context;

import java.util.Arrays;

public class Define implements Command{
    private final String[] args;

    public Define(String[] args) {
        this.args = args;
    }
    @Override
    public void apply(Context context){
        if (args.length != 2){
            throw new IllegalArgumentException("DEFINE need 2 arguments");
        }

        String variable = args[0];

        double value = Double.parseDouble(args[1]);

        context.putToMap(variable, value);

    }
}

