package com.lealdidier.invoice.scan;

import java.util.function.Supplier;

public class ScalarSupplier<T> implements Supplier<T> {
    private T t;

    public ScalarSupplier(T t) {
        this.t = t;
    }

    @Override
    public T get() {
        return t;
    }
}
