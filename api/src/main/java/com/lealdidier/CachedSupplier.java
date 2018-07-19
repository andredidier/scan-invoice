package com.lealdidier;

import java.util.function.Supplier;

public class CachedSupplier<T> implements Supplier<T> {
    private Supplier<T> inner;
    private transient T cached;

    public CachedSupplier(Supplier<T> inner) {
        this.inner = inner;
    }

    @Override
    public T get() {
        if (cached == null) {
            cached = inner.get();
        }
        return cached;
    }
}
