package org.calculator.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;


import org.calculator.exeptions.contextExceptions.NoSuchVariableInMapException;
import org.calculator.exeptions.contextExceptions.StackIsEmptyException;

public class ContextTest {
    private Context context;

    @BeforeEach
    void setUp(){
        context = new Context();
    }

    @Test
    public void putToMap_successfullyUsed(){
        context.putToMap("a", 4);
        context.putToMap("b", 5);
        assertDoesNotThrow(() -> assertEquals(4, context.getFromMap("a")));
        assertDoesNotThrow(() -> assertEquals(5, context.getFromMap("b")));
        context.putToMap("b", 6);
        assertDoesNotThrow(() -> assertEquals(6, context.getFromMap("b")));
    }

    @Test
    public void getFromMap_successfullyUsed() throws NoSuchVariableInMapException {
        context.putToMap("a", 4);
        assertDoesNotThrow(() -> assertEquals(4.0, context.getFromMap("a")));
    }

    @Test
    public void getFromMap_throwingException(){
        Exception exception = assertThrows(NoSuchVariableInMapException.class, () -> context.getFromMap("b"));
        assertEquals("Can`t find definition of variable: b", exception.getMessage());
    }

    @Test
    public void pushPop_SuccessfullyUsed(){
        context.push(4);
        assertDoesNotThrow(() -> assertEquals(4, context.pop()));
    }

    @Test
    public void pop_throwingException(){
        Exception exception = assertThrows(StackIsEmptyException.class, () -> context.pop());
        assertEquals("No values in calculator.", exception.getMessage());
    }


}
