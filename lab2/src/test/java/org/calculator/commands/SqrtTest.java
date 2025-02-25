package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SqrtTest {
    private Context context;
    private Sqrt sqrt;

    @BeforeEach
    void setUp() {
        context = new Context(); // Создаём новый контекст для каждого теста
    }

    @Test
    void testSqrt_successfullyUsed() throws StackIsEmptyException {
        context.push(12.0);

        sqrt = new Sqrt(new String[]{});
        sqrt.apply(context);
        assertEquals(144.0, context.pop());
    }

}
