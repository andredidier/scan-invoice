package com.lealdidier.media;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Media<T> {
    <V> Media<T> addField(String name, Supplier<V> value);
    void writeFields() throws IOException;
}
