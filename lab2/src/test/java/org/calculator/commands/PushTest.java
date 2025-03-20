package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.contextExceptions.ManyArgumentsCommandException;
import org.calculator.exeptions.mainExceptions.ManyArgumentsException;
import org.calculator.exeptions.contextExceptions.NoSuchVariableInMapException;
import org.calculator.exeptions.contextExceptions.StackIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PushTest {
    private Context context;
    private Push push;

    @BeforeEach
    void setUp() {
        context = new Context(); // Создаём новый контекст для каждого теста
    }

    @Test
    void testPush_successfullyUsed() throws ManyArgumentsException, NoSuchVariableInMapException, StackIsEmptyException, ManyArgumentsCommandException {
        context.putToMap("a", 4.0);

        push = new Push(new String[]{"a"});

        push.apply(context);

        assertEquals(4.0, context.pop());
    }

    @Test
    void testPush_throwingException() {
        context.putToMap("a", 4.0);

        push = new Push(new String[]{"a", "b"});

        assertThrows(ManyArgumentsException.class, () -> push.apply(context));
    }
}
