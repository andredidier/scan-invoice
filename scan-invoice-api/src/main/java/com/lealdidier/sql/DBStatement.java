package com.lealdidier.sql;

import com.lealdidier.media.ExceptionConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DBStatement {
    String sql();
    List<DBStatement> cachesToBeInvalidated();
}
