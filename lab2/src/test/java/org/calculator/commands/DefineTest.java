package org.calculator.commands;

import static org.junit.jupiter.api.Assertions.*;

import org.calculator.app.Context;
import org.calculator.exeptions.mainExceptions.ManyArgumentsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefineTest {
    private Context context;
    private Define define;

    @BeforeEach
    void setUp() {
        context = new Context(); // Создаём новый контекст для каждого теста
    }

    @Test
    void testDefine_throwingException(){
        String[] args = {"a", "4", "10"};
        define = new Define(args);
        assertThrows(ManyArgumentsException.class, () -> define.apply(context));
    }
}
