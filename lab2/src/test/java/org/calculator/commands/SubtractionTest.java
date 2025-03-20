package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.contextExceptions.StackIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubtractionTest {
    private Context context;
    private Subtraction subtraction;

    @BeforeEach
    void setUp() {
        context = new Context(); // Создаём новый контекст для каждого теста
    }

    @Test
    void testSubtraction_withValidInput() throws StackIsEmptyException {
        // Подготовка данных
        context.push(2.0);
        context.push(10.0);

        subtraction = new Subtraction(new String[]{});


        subtraction.apply(context);

        // Проверка результата
        assertEquals(8.0, context.pop(), "Result of 10.0 - 2.0 should be 8.0");
    }

    @Test
    void testDivision_withEmptyStack() {

        subtraction = new Subtraction( new String[]{});

        assertThrows(StackIsEmptyException.class, () -> context.pop());
    }

    @Test
    void testDivision_withSingleElementInStack() throws StackIsEmptyException {
        // Стек содержит только 1 элемент
        context.push(5.0);

        subtraction = new Subtraction(new String[]{});
        subtraction.apply(context);


        assertEquals(5.0, context.pop(), "Stack should contain the initial element, since there were not enough elements to perform addition");
    }
}
