package org.calculator.commands;

import org.calculator.app.Context;
import org.calculator.exeptions.commandsExceptions.ArithmeticCommandException;
import org.calculator.exeptions.contextExceptions.StackIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DivisionTest {
    private Context context;
    private Division division;

    @BeforeEach
    void setUp() {
        context = new Context(); // Создаём новый контекст для каждого теста
    }

    @Test
    void testDivision_withValidInput() throws StackIsEmptyException, ArithmeticCommandException {
        // Подготовка данных
        context.push(2.0);
        context.push(10.0);

        division = new Division(new String[]{});


        division.apply(context);

        // Проверка результата
        assertEquals(5.0, context.pop(), "Result of 10.0 / 2.0 should be 5.0");
    }

    @Test
    void testDivision_withEmptyStack() {

        division= new Division( new String[]{});

        assertThrows(StackIsEmptyException.class, () -> context.pop());
    }

    @Test
    void testDivision_withSingleElementInStack() throws StackIsEmptyException, ArithmeticCommandException {
        // Стек содержит только 1 элемент
        context.push(5.0);

        division = new Division(new String[]{});
        division.apply(context);


        assertEquals(5.0, context.pop(), "Stack should contain the initial element, since there were not enough elements to perform addition");
    }

    @Test
    void testDivision_divisionByZero() throws StackIsEmptyException {
        context.push(0.0);
        context.push(10.0);

        division = new Division(new String[]{});

        assertThrows(ArithmeticException.class, () -> division.apply(context));
    }
}
