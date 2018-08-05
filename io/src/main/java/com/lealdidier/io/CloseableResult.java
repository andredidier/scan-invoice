package com.lealdidier.io;

import java.io.Closeable;

public class CloseableResult<T,R extends Closeable> implements Function<T,R> {

    private final Function<T,R> inner;

    public CloseableResult(Function<T, R> inner) {
        if (inner == null) {
            throw new IllegalArgumentException("inner");
        }
        this.inner = inner;
    }

    @Override
    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return t -> {
            try(R r = inner.apply(t)) {
                return after.apply(r);
            }
        };
    }

    @Override
    public final <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return new CloseableResult<>(v -> inner.apply(before.apply(v)));
    }

    @Override
    public final R apply(T t) {
        throw new RuntimeException("Cannot use a Closeable outside its try block.");
    }
}
