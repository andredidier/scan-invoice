package com.lealdidier.media;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Media<T> {
    <V> Media<T> addField(String name, Supplier<V> value);
    //<V> Media<T> addMapping(String name, BiConsumer<T, V> consumeValue);
    void writeFields() throws IOException;
}
