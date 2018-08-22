package com.lealdidier.media.db;

import com.lealdidier.io.UrlInputStreamSupplier;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.function.Function;

@FunctionalInterface
public interface DBPreparedStatementConsumer<T>  {


    void accept(PreparedStatement preparedStatement, T t) throws SQLException;

    Function<Integer, DBPreparedStatementConsumer<String>> string = i -> ((p,t) -> p.setString(i, t));
    Function<Integer, DBPreparedStatementConsumer<?>> toString = i -> ((p,t) -> p.setString(i, t.toString()));
    Function<Integer, DBPreparedStatementConsumer<Date>> date = i -> ((p, d) -> p.setDate(i, new java.sql.Date(d.getTime())));
}
