package com.lealdidier.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InputStreamToContentsTest {
    @Test
    @DisplayName("Null charset as parameter")
    public void nullCharset() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new InputStreamToContents(null));
        assertEquals("charset", iae.getMessage());

    }
}
