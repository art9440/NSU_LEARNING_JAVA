package org.calculator.app;

import org.calculator.exeptions.NoSuchVariableInMapException;
import org.calculator.exeptions.StackIsEmptyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

public class Context {
    private static final Logger logger = Logger.getLogger(Context.class.getName());

    private final Stack<Double> calcStack = new Stack<>();
    private final Map<String, Double> varValues = new HashMap<>();


    public void putToMap(String variable, double value){
        if (varValues.containsKey(variable)) {
            logger.info("Updating variable: " + variable + " = " + value);
        } else {
            logger.info("Creating variable: " + variable + " = " + value);
        }
        varValues.put(variable, value);

    }

    public double getFromMap(String variable) throws NoSuchVariableInMapException {
        if (!varValues.containsKey(variable)) {
            throw new NoSuchVariableInMapException("Can`t find definition of variable: " + variable);
        }
        return varValues.get(variable);
    }

    public void push(double value){
        calcStack.push(value);
        logger.info(value + " is on top of the stack");
    }

    public double pop() throws StackIsEmptyException {
        if (calcStack.empty()){
            throw new StackIsEmptyException("No values in calculator.");
        }
        else {
            logger.info("value was pop from stack.");
            return calcStack.pop();
        }
    }


}
