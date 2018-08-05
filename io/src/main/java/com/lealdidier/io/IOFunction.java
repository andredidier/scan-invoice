package com.lealdidier.io;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
public interface IOFunction<T,R> {

    R apply(T t) throws IOException;

    default <V> IOFunction<V, R> compose(IOFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }
    default <V1, V2> IOBiFunction<V1, V2, R> compose(IOBiFunction<? super V1, ? super V2, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V1 v1, V2 v2) -> apply(before.apply(v1, v2));
    }
    default <V> IOFunction<T, V> andThen(IOFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> IOFunction<T, T> identity() {
        return t -> t;
    }
}
