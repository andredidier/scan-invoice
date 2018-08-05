package com.lealdidier.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResourceToTransformerTest {
    @Test
    @DisplayName("Null classForResource as parameter")
    public void nullCharset() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new ResourceToTransformer(null));
        assertEquals("classForResources", iae.getMessage());

    }
}
