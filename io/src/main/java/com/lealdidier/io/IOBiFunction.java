package com.lealdidier.io;

import java.io.IOException;

/**
 * Inspired by: <a>https://dzone.com/articles/higher-order-functions</a>
 * @param <T> First argument
 * @param <U> Second argument
 * @param <R> Result
 */

@FunctionalInterface
public interface IOBiFunction<T, U, R>  {
    R apply(T t, U u) throws IOException;

    default IOFunction<U, R> curry1(T t) {
        return u -> apply(t, u);
    }
    default IOFunction<T, R> curry2(U u) {
        return t -> apply(t, u);
    }

    default <V> IOBiFunction<V, U, R> compose1(IOFunction<? super V, ? extends T> before) {
        return (v, u) -> apply(before.apply(v), u);
    }
    default <V> IOBiFunction<T, V, R> compose2(IOFunction<? super V, ? extends U> before) {
        return (t, v) -> apply(t, before.apply(v));
    }
}
