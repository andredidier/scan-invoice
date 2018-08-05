package com.lealdidier.io;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
public interface Function<T,R> {

    R apply(T t) throws IOException;

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) throws IOException {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) throws IOException {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
