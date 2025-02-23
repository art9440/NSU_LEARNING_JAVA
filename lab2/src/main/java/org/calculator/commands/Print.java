package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;

public class Print implements Command{
    public Print(String[] arguments) {}
    @Override
    public void apply(Context context){
        try {
            double val = context.pop();
            System.out.println("PRINT: " + val);
            context.push(val);
        } catch (StackIsEmptyException e) {
            System.err.println(e.getMessage());
        }
    }
}
