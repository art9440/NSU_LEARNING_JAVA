package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;

public class Division implements Command{
    public Division(String[] arguments) {}
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

        if (second == 0.0){
            context.push(second);
            context.push(first);
            throw new ArithmeticException("Division by zero");
        }
        else{
            context.push(first / second);
        }


    }
}
