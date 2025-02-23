package org.calculator.commands;

import org.calculator.app.Context;

import java.util.Arrays;

public class Push implements Command {
    private final String[] args;

    public Push(String[] args) {
        this.args = args;
        System.out.println(Arrays.toString(args));
    }

    @Override
    public void apply(Context context) {

    }
}
