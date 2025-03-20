package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.contextExceptions.StackIsEmptyException;

import java.util.logging.Logger;

public class Pop implements Command{
    private static final Logger logger = Logger.getLogger(Pop.class.getName());
    public Pop(String[] arguments) {}
    @Override
    public void apply(Context context) {
        try {
            double val = context.pop();
        } catch (StackIsEmptyException e) {
            logger.warning(e.getMessage());
        }
    }
}
