package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;

public class Sqrt implements Command{
    public Sqrt(String[] arguments) {}
    @Override
    public void apply(Context context) {
        try {
            double val = context.pop();
            context.push(val * val);
        } catch (StackIsEmptyException e) {
            System.err.println(e.getMessage());
        }
    }
}
