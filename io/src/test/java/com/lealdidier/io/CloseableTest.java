package com.lealdidier.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Closeable test")
public class CloseableTest {

    @DisplayName("Apply after compose")
    @Test
    public void applyAfterCompose() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        IOFunction<Long, String> before = l -> "";
        IOFunction<String, InputStream> f = new CloseableResult<>(s -> inputStream);

        IOFunction<Long, InputStream> composed = f.compose(before);

        RuntimeException ioe = assertThrows(RuntimeException.class, () -> composed.apply(null));
        assertEquals("Cannot use a Closeable outside its try block.", ioe.getMessage());
    }

    @DisplayName("Compose and close")
    @Test
    public void composeAndClose() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        InOrder inOrder = inOrder(inputStream);
        when(inputStream.available()).thenReturn(2);
        IOFunction<Long, String> before = l -> "";
        IOFunction<String, InputStream> f = new CloseableResult<>(s -> inputStream);
        IOFunction<InputStream, Integer> use = is -> is.available();

        IOFunction<Long, InputStream> composed = f.compose(before);

        assertEquals(2, (int)composed.andThen(use).apply(2l));

        inOrder.verify(inputStream, times(1)).available();
        inOrder.verify(inputStream, times(1)).close();
    }

    @DisplayName("AndThen and close")
    @Test
    public void thenAndClose() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        InOrder inOrder = inOrder(inputStream);
        when(inputStream.available()).thenReturn(2);
        IOFunction<String, InputStream> f = new CloseableResult<>(s -> inputStream);
        IOFunction<InputStream, Integer> use = is -> is.available();

        assertEquals(2, (int)f.andThen(use).apply(""));

        inOrder.verify(inputStream, times(1)).available();
        inOrder.verify(inputStream, times(1)).close();
    }

    @DisplayName("Read contents of file")
    @ParameterizedTest(name = "[{index}]. {0} => {1}")
    @CsvSource({ "file1.txt, Contents #1", "file2.txt, Contents #2" })
    public void testReadingContents(String fileName, String contents) throws IOException {
        IOFunction<String, URL> f1 = name -> getClass().getResource(name);
        IOFunction<URL, InputStream> f2 = new CloseableResult<>(URL::openStream);
        IOFunction<InputStream, String> f3 = new InputStreamToContents(StandardCharsets.UTF_8);

        assertEquals(contents, f1.andThen(f2.andThen(f3)).apply(fileName));
    }

    @DisplayName("Wrong order of andThen")
    @ParameterizedTest(name = "[{index}]. {0}")
    @CsvSource({ "file1.txt", "file2.txt" })
    public void testWrongOrderAndThen(String fileName) {
        IOFunction<String, URL> f1 = name -> getClass().getResource(name);
        IOFunction<URL, InputStream> f2 = new CloseableResult<>(URL::openStream);
        IOFunction<InputStream, String> f3 = new InputStreamToContents(StandardCharsets.UTF_8);

        RuntimeException ioe = assertThrows(RuntimeException.class, () -> f1.andThen(f2).andThen(f3).apply(fileName));
        assertEquals("Cannot use a Closeable outside its try block.", ioe.getMessage());
    }

    @Test
    @DisplayName("Exception when trying to use after apply")
    public void testExceptionApplyAndUse() throws IOException {
        IOFunction<URL, InputStream> f2 = new CloseableResult<>(URL::openStream);
        RuntimeException ioe = assertThrows(RuntimeException.class, () -> f2.apply(null));
        assertEquals("Cannot use a Closeable outside its try block.", ioe.getMessage());
    }

    @Test
    @DisplayName("Null inner as parameter")
    public void nullArgument() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new CloseableResult<>(null));
        assertEquals("inner", iae.getMessage());
    }
}
