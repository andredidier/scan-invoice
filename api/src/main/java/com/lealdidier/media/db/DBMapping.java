package com.lealdidier.media.db;

import java.net.URL;

public class DBMapping<V> {
    private final String name;
    private final DBPreparedStatementConsumer<V> consumer;

    public DBMapping(String name, DBPreparedStatementConsumer<V> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    DBPreparedStatementConsumer<V> consumer() {
        return consumer;
    }
    String name() { return name; }
}
