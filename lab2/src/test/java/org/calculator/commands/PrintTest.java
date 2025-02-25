package org.calculator.commands;

import org.calculator.app.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintTest {
    private Context context;
    private Print print;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        context = new Context(); // Создаём новый контекст для каждого теста
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testPrint_successfullyUsed(){
        context.push(5.0);

        print = new Print(new String[]{});

        print.apply(context);

        String output = outputStreamCaptor.toString().trim();
        assertEquals("PRINT: 5.0", output, "Not expected output.");
    }

}

