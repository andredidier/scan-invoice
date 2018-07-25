package com.lealdidier.io;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CachedInput<T extends Serializable> implements Input<T> {
    private final static Map<String, Object> cache = new HashMap<>();

    private final String key;
    private final Input<T> inner;

    public CachedInput(String key, Input<T> inner) {
        this.key = key;
        this.inner = inner;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read() throws IOException {
        T t;
        synchronized (cache) {
            if (cache.containsKey(key)) {
                t = (T) cache.get(key);
            } else {
                t = inner.read();
                cache.put(key, t);
            }
        }
        return t;
    }
}
