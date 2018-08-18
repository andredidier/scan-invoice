package com.lealdidier.media;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MapMedia implements Media<Map<String, Object>> {
    private Map<String, Object> map;
    private final Map<String, String> mapping;

    public MapMedia(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public Map<String, Object> map() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void write(Object o) throws IOException {
        if (o instanceof Map) {
            this.map = (Map<String, Object>)o;
        }
    }

    @Override
    @SafeVarargs
    public final void writeFields(Consumer<Map<String, Object>>... consumers) throws IOException {
        if (map == null) {
            map = new HashMap<>();
        }
        for(Consumer<Map<String, Object>> c : consumers) {
            c.accept(map);
        }
    }

    @Override
    public <V> Consumer<Map<String, Object>> create(String name, Supplier<V> value) {
        if (mapping.containsKey(name)) {
            return m -> m.put(mapping.get(name), value.get());
        } else {
            return m -> {};
        }
    }
}
