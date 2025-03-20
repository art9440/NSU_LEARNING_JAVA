package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.contextExceptions.StackIsEmptyException;

import java.util.logging.Logger;

public class Addition implements Command{
    private static final Logger logger = Logger.getLogger(Addition.class.getName());

    public Addition(String[] arguments) {}
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
        context.push(first + second);
    }

}
