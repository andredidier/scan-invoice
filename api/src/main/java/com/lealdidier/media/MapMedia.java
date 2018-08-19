package com.lealdidier.media;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MapMedia implements Media<Map<String, Object>> {
    private Map<String, Object> map;
    private final Map<String, String> mapping;
    private final Set<Consumer<Map<String, Object>>> consumers;

    private MapMedia(Map<String, Object> map,
                    Map<String, String> mapping, Set<Consumer<Map<String, Object>>> consumers) {
        this.map = map;
        this.mapping = mapping;
        this.consumers = Collections.unmodifiableSet(consumers);
    }
    public MapMedia(Map<String, Object> map, Map<String, String> mapping) {
        this(map, mapping, new HashSet<>());
    }

    private void writeFields(Set<Consumer<Map<String, Object>>> consumers) {
        if (map == null) {
            map = new HashMap<>();
        }
        for(Consumer<Map<String, Object>> c : consumers) {
            c.accept(map);
        }
    }

    private <V> Consumer<Map<String, Object>> create(String name, Supplier<V> value) {
        if (mapping.containsKey(name)) {
            return m -> m.put(mapping.get(name), value.get());
        } else {
            return m -> {};
        }
    }

    @Override
    public <V> Media<Map<String, Object>> addField(String name, Supplier<V> value) {
        Set<Consumer<Map<String, Object>>> newConsumers = new HashSet<>(consumers);
        newConsumers.add(create(name, value));
        return new MapMedia(map, mapping, newConsumers);
    }

    @Override
    public void writeFields() {
        writeFields(consumers);
    }
}
