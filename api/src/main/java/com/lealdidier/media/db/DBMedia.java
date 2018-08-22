package com.lealdidier.media.db;

import com.lealdidier.media.Media;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DBMedia implements Media<PreparedStatement> {

    private final String sql;
    private final Connection connection;
    private final DBMapping[] mappings;
    private final Set<Consumer<PreparedStatement>> consumers;

    private DBMedia(String sql, Connection connection,
                    Set<Consumer<PreparedStatement>> consumers,
                    DBMapping... mappings) {
        this.sql = sql;
        this.connection = connection;
        this.consumers = Collections.unmodifiableSet(consumers);
        this.mappings = mappings;
    }

    public DBMedia(String sql, Connection connection,
                   DBMapping... mappings) {
        this(sql, connection, new HashSet<>(), mappings);
    }

    private PreparedStatement prepare(Connection c) throws SQLException {
        return c.prepareStatement(sql);
    }

    private void writeFields(Set<Consumer<PreparedStatement>> consumers) throws IOException {
        try(PreparedStatement s = prepare(connection)) {
            for(Consumer<PreparedStatement> consumer : consumers) {
                consumer.accept(s);
            }
            if (!s.execute()) {
                throw new IOException("not saved");
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private <V> DBPreparedStatementConsumer<V> find(String name) {
        for(DBMapping mapping : mappings) {
            if (mapping.name().equals(name)) {
                return (DBPreparedStatementConsumer<V>)mapping.consumer();
            }
        }
        return null;
    }

    private <V> Consumer<PreparedStatement> create(String name, Supplier<V> value) {
        DBPreparedStatementConsumer<V> c = find(name);

        if (c == null) {
            return p -> {};
        }

        return ps -> {
            try {
                c.accept(ps, value.get());
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public <V> Media<PreparedStatement> addField(String name, Supplier<V> value) {
        Set<Consumer<PreparedStatement>> consumers = new HashSet<>(this.consumers);
        consumers.add(create(name, value));
        return new DBMedia(sql, connection, consumers, mappings);
    }

    @Override
    public void writeFields() throws IOException {
        writeFields(consumers);
    }

}
