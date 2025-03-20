package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.commandsExceptions.ArithmeticCommandException;
import org.calculator.exeptions.contextExceptions.ContextException;
import org.calculator.exeptions.contextExceptions.ManyArgumentsCommandException;
import org.calculator.exeptions.mainExceptions.ManyArgumentsException;
import org.calculator.exeptions.contextExceptions.NoSuchVariableInMapException;

public interface Command {
    public void apply(Context context) throws ContextException, ArithmeticCommandException;
}
