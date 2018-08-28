package com.lealdidier.sql;

import com.lealdidier.media.ExceptionConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum TransactionDBStatement implements DBStatement {
    Insert("INSERT INTO TRANSACTION (URL, CREATE_DATE, UPDATE_DATE, PROCESSED) VALUES (?,?,?,?)"),
    QueryJson("SELECT t1.JSON from TRANSACTION WHERE t1.URL=?"),
    UpdateXmlJson("UPDATE TRANSACTION SET NFC_CONTENTS=?, YNAB_TRANSACTION=?, UPDATE_DATE=? WHERE URL=?", QueryJson),
    CreateTable("");

    private String sql;
    private List<DBStatement> cachesToBeInvalidated;
    TransactionDBStatement(String sql, TransactionDBStatement... cachesToBeInvalidated) {
        this.sql = sql;
        this.cachesToBeInvalidated = Arrays.asList(cachesToBeInvalidated);
    }

    @Override
    public String sql() {
        return sql;
    }

    @Override
    public List<DBStatement> cachesToBeInvalidated() {
        return cachesToBeInvalidated;
    }
}
