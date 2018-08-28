package com.lealdidier.sql;

import com.lealdidier.media.ExceptionConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class DBQuery {
    private final DBStatement statement;

    public DBQuery(DBStatement statement) {
        this.statement = statement;
    }

    public PreparedStatement prepare(Connection connection) throws DBException {
        try {
            return connection.prepareStatement(statement.sql());
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public <T> List<T> execute(Connection connection, Function<ResultSet, T> tr)
            throws DBException {
        try {
            PreparedStatement ps = this.prepare(connection);
            ResultSet rs = ps.executeQuery();
            List<T> ts = new LinkedList<>();
            if (rs.next()) {
                ts.add(tr.apply(rs));
            } else {
                throw new EmptyResultSetException();
            }
            while (rs.next()) {
                ts.add(tr.apply(rs));
            }
            return ts;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


}
