package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;

import java.util.logging.Logger;

public class Sqrt implements Command{
    private static final Logger logger = Logger.getLogger(Sqrt.class.getName());
    public Sqrt(String[] arguments) {}
    @Override
    public void apply(Context context) {
        try {
            double val = context.pop();
            context.push(val * val);
        } catch (StackIsEmptyException e) {
            logger.warning(e.getMessage());
        }
    }
}
