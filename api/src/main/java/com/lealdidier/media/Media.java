package com.lealdidier.media;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Media<T> {
    void write(Object o) throws IOException;
    void writeFields(Consumer<T>... consumers) throws IOException;
    <V> Consumer<T> create(String name, Supplier<V> value);
}
