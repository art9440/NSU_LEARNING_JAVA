package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;

public class Pop implements Command{
    public Pop(String[] arguments) {}
    @Override
    public void apply(Context context) {
        try {
            double val = context.pop();
        } catch (StackIsEmptyException e) {
            System.err.println(e.getMessage());
        }
    }
}
