package com.lealdidier.media.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface DBPreparedStatementConsumer<T>  {

    void accept(PreparedStatement preparedStatement, T t) throws SQLException;
}
