package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;

import java.util.logging.Logger;

public class Division implements Command{
    private static final Logger logger = Logger.getLogger(Division.class.getName());
    public Division(String[] arguments) {}
    @Override
    public void apply(Context context) {
        double first, second;
        try {
            first = context.pop();
        } catch (StackIsEmptyException e) {
            logger.warning(e.getMessage());
            return;
        }
        try{
            second = context.pop();
        } catch (StackIsEmptyException e) {
            context.push(first);
            logger.warning(e.getMessage());
            return;
        }

        if (second == 0.0){
            context.push(second);
            context.push(first);
            logger.severe("Division by zero");
            throw new ArithmeticException("Division by zero");
        }
        else{
            context.push(first / second);
        }


    }
}
