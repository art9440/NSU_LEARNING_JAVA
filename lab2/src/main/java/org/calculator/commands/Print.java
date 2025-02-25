package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;

import java.util.logging.Logger;

public class Print implements Command{
    private static final Logger logger = Logger.getLogger(Print.class.getName());
    public Print(String[] arguments) {}
    @Override
    public void apply(Context context){
        try {
            double val = context.pop();
            System.out.println("PRINT: " + val);
            logger.info("Was printed: " + val);
            context.push(val);
        } catch (StackIsEmptyException e) {
            logger.warning(e.getMessage());
        }
    }
}
