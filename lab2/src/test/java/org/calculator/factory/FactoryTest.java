package org.calculator.factory;

import org.calculator.app.Context;
import org.calculator.commands.Command;
import org.calculator.exeptions.factoryExceptions.FactoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FactoryTest {

    @BeforeEach
    void setUp() {
        Factory factory = new Factory("fakeconfig.txt");
        Context context = new Context();
    }

    @Test
    public void testCreateCommand_Successfully() throws Exception{
        Factory mockFactory = Mockito.spy(new Factory("testConfig.txt"));

        Mockito.doReturn("org.calculator.commands.Push")
                .when(mockFactory)
                .search("PUSH");


        Command command = mockFactory.createCommand("PUSH", new String[]{"10"});


        assertNotNull(command);
        assertInstanceOf(Command.class, command);


        Mockito.verify(mockFactory, times(1)).search("PUSH");
    }

    @Test
    void testCreateCommand_ThrowsCommandNotFoundException(){
        Factory mockFactory = Mockito.spy(new Factory("factoryconfig.txt"));
        String commandName = "UNKNOWN_COMMAND";
    try {

        Command command = mockFactory.createCommand(commandName, new String[]{});

        fail("Exception not thrown");
    } catch (FactoryException e){
        assertEquals("Can`t find UNKNOWN_COMMAND in Factory config", e.getMessage());
    }
    }


}
