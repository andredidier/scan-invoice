package com.lealdidier.io;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONObjectMedia implements Media, Output<JSONObject> {
    private final Map<String, Object> values;

    public JSONObjectMedia(Map<String, Object> values) {
        this.values = values;
    }
    public JSONObjectMedia() {
        this(new HashMap<>());
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), values);
    }

    @Override
    public void write(JSONObject jsonObject) throws IOException {
        for(String key : values.keySet()) {
            jsonObject.put(key, values.get(key));
        }
    }

    public <M extends Media> M r() {
        return (M)this;
    }

    @Override
    public <T> Media with(String name, T value) {
        values.put(name, value);
        return this;
    }
}
