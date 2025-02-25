package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.StackIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MultiplicationTest {
    private Context context;
    private Multiplication multiplication;

    @BeforeEach
    void setUp() {
        context = new Context(); // Создаём новый контекст для каждого теста
    }

    @Test
    void testMultiplication_withValidInput() throws StackIsEmptyException {
        // Подготовка данных
        context.push(2.0);
        context.push(10.0);

        multiplication = new Multiplication(new String[]{});


        multiplication.apply(context);

        // Проверка результата
        assertEquals(20.0, context.pop(), "Result of 2.0 * 10.0 should be 20.0");
    }

    @Test
    void testDivision_withEmptyStack() {

        multiplication = new Multiplication( new String[]{});

        assertThrows(StackIsEmptyException.class, () -> context.pop());
    }

    @Test
    void testDivision_withSingleElementInStack() throws StackIsEmptyException {
        // Стек содержит только 1 элемент
        context.push(5.0);

        multiplication = new Multiplication(new String[]{});
        multiplication.apply(context);


        assertEquals(5.0, context.pop(), "Stack should contain the initial element, since there were not enough elements to perform addition");
    }
}
