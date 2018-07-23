package com.lealdidier.invoice.scan;

import java.io.IOException;

public class ScalarInput<T> implements Input<T> {
    private final T t;

    public ScalarInput(T t) {
        this.t = t;
    }

    @Override
    public T read() throws IOException {
        return t;
    }
}
