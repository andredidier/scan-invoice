package com.lealdidier.io;

import java.io.Closeable;

public class CloseableResult<T,R extends Closeable> implements IOFunction<T,R> {

    private final IOFunction<T,R> inner;

    public CloseableResult(IOFunction<T, R> inner) {
        if (inner == null) {
            throw new IllegalArgumentException("inner");
        }
        this.inner = inner;
    }

    @Override
    public <V> IOFunction<T, V> andThen(IOFunction<? super R, ? extends V> after) {
        return t -> {
            try(R r = inner.apply(t)) {
                return after.apply(r);
            }
        };
    }

    @Override
    public final <V> IOFunction<V, R> compose(IOFunction<? super V, ? extends T> before) {
        return new CloseableResult<>(v -> inner.apply(before.apply(v)));
    }

    @Override
    public final R apply(T t) {
        throw new RuntimeException("Cannot use a Closeable outside its try block.");
    }
}
