package org.calculator.commands;
import static org.junit.jupiter.api.Assertions.*;



import org.calculator.app.Context;
import org.calculator.exeptions.contextExceptions.StackIsEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdditionTest {
    private Context context;
    private Addition addition;

    @BeforeEach
    void setUp() {
        context = new Context(); // Создаём новый контекст для каждого теста
    }

    @Test
    void testApply_withValidInput() throws StackIsEmptyException {
        // Подготовка данных
        context.push(3.0);
        context.push(5.0);

        addition = new Addition(new String[]{});


        addition.apply(context);

        // Проверка результата
        assertEquals(8.0, context.pop(), "Result of 3.0 + 5.0 should be 8.0");
    }

    @Test
    void testApply_withEmptyStack() {

        addition = new Addition(new String[]{});

        assertThrows(StackIsEmptyException.class, () -> context.pop());
    }

    @Test
    void testApply_withSingleElementInStack() throws StackIsEmptyException {
        // Стек содержит только 1 элемент
        context.push(5.0);

        addition = new Addition(new String[]{});
        addition.apply(context);

        // Стек должен остаться с одним элементом, так как для выполнения сложения нужны два элемента
        assertEquals(5.0, context.pop(), "Stack should contain the initial element, since there were not enough elements to perform addition");
    }

}
