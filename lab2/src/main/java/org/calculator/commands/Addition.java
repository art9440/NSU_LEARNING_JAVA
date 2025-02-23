package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;

public class Addition implements Command{

    public Addition(String[] arguments) {}
    @Override
    public void apply(Context context) {
        double first, second;
        try {
            first = context.pop();
        } catch (StackIsEmptyException e) {
            System.err.println(e.getMessage());
            return;
        }
        try{
             second = context.pop();
        } catch (StackIsEmptyException e) {
            context.push(first);
            System.err.println(e.getMessage());
            return;
        }
        context.push(first + second);
    }

}
