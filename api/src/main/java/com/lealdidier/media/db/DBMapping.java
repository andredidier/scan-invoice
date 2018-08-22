package com.lealdidier.media.db;

import java.net.URL;

public class DBMapping<V> {
    private final String name;
    private final DBPreparedStatementConsumer<? extends V> consumer;

    public DBMapping(String name, DBPreparedStatementConsumer<? extends V> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    DBPreparedStatementConsumer<? extends V> consumer() {
        return consumer;
    }
    String name() { return name; }
}
