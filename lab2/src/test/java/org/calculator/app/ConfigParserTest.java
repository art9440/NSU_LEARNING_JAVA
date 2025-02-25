package org.calculator.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;


public class ConfigParserTest {
    private BiConsumer<String, String[]> mockConsumer;

    @BeforeEach
    void setUp() {
        mockConsumer = Mockito.mock(BiConsumer.class);
    }

    @Test
    public void testReadConfig_successfullyParsing() throws IOException {

        ConfigParser configParser = new ConfigParser("testConfig.txt");

        configParser.readConfig(mockConsumer);

        verify(mockConsumer, times(1)).accept("DEFINE", new String[]{"a", "4"});
        verify(mockConsumer, times(1)).accept("DEFINE", new String[]{"b", "5"});
        verify(mockConsumer, times(1)).accept("PUSH", new String[]{"a"});
        verify(mockConsumer, times(1)).accept("PUSH", new String[]{"b"});
        verify(mockConsumer, times(1)).accept("SQRT", new String[]{});
        verify(mockConsumer, times(1)).accept("*", new String[]{});
        verify(mockConsumer, times(1)).accept("PRINT", new String[]{});

        verifyNoMoreInteractions(mockConsumer);
    }

    @Test
    void testReadConfig_FileNotFound() {
        ConfigParser testParser = new ConfigParser("nonexistent.txt");

        BiConsumer<String, String[]> mockCommandLine = mock(BiConsumer.class);

        Exception exception = assertThrows(FileNotFoundException.class, () -> testParser.readConfig(mockCommandLine));
        assertTrue(exception.getMessage().contains("Not found file in folder resources"));
    }
}
