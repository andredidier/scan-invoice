package com.lealdidier.io;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONMedia implements Media {
    private final String[] names;
    private final List<Object> values;

    public JSONMedia(String[] names) {
        this.names = names;
        this.values = new ArrayList<>(names.length);
    }

    @Override
    public Media put(Object value) {
        values.add(value);
        return this;
    }

    public JSONObject json() {
        JSONObject jsonObject = new JSONObject();
        for(int i = 0; i < names.length; i++) {
            jsonObject.put(names[i], values.get(i));
        }
        return jsonObject;
    }
}
