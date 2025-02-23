package org.calculator.commands;

import org.calculator.app.Context;

public interface Command {
    public void apply(Context context);
}
