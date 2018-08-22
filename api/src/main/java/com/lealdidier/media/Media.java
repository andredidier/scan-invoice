package com.lealdidier.media;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Media<E extends Exception> {

    private final Map<FieldName, Supplier<Object>> fields;
    private final Map<FieldName, List<ExceptionConsumer<E, Object>>> consumers;

    Media(Map<FieldName, Supplier<Object>> fields, Map<FieldName, List<ExceptionConsumer<E, Object>>> consumers) {
        this.fields = fields;
        this.consumers = consumers;
    }
    public Media() {
        this(new HashMap<>(), new HashMap<>());
    }


    public Media<E> addField(FieldName name, Supplier<Object> value) {
        Map<FieldName, Supplier<Object>> newFields = new HashMap<>(fields);
        newFields.put(name, value);
        return new Media(newFields, consumers);
    }

    public Media<E> addMapping(FieldName name, ExceptionConsumer<E, ?> consumeValue) {
        Map<FieldName, List<ExceptionConsumer<E, Object>>> newConsumers = new HashMap<>(consumers);
        List<ExceptionConsumer<E, Object>> newConsumersList;
        if (newConsumers.containsKey(name)) {
            newConsumersList = new LinkedList<>(newConsumers.get(name));
        } else {
            newConsumersList = new LinkedList<>();
        }
        newConsumers.put(name, newConsumersList);
        newConsumersList.add((ExceptionConsumer<E, Object>) consumeValue);
        return new Media(fields, newConsumers);
    }

    public void writeFields() throws E {
        for(FieldName name : consumers.keySet()) {
            if (!fields.containsKey(name)) {
                continue;
            }
            writeFields(consumers.get(name), fields.get(name));
        }
    }

    private void writeFields(List<ExceptionConsumer<E, Object>> exceptionConsumers, Supplier<Object> objectSupplier) throws E {
        Object value = exceptionConsumers.isEmpty() ? null : objectSupplier.get();
        for (ExceptionConsumer<E, Object> consumer : exceptionConsumers) {
            consumer.accept(value);
        }
    }
}
