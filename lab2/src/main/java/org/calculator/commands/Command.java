package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.NoSuchVariableInMapException;
import org.calculator.exeptions.StackIsEmptyException;

public interface Command {
    public void apply(Context context) throws NoSuchVariableInMapException;
}
