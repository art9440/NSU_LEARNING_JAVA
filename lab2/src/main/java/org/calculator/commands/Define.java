package org.calculator.commands;

import org.calculator.app.Context;

import java.util.Arrays;

public class Define implements Command{
    private final String[] args;

    public Define(String[] args) {
        this.args = args;
        System.out.println(Arrays.toString(args));
    }
    @Override
    public void apply(Context context){

    }
}

