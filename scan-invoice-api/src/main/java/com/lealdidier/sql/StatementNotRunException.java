package com.lealdidier.sql;

public class StatementNotRunException extends DBException {
    private final DBStatement statement;
    private final Object[] params;

    public StatementNotRunException(DBStatement statement, Object... params) {
        this.statement = statement;
        this.params = params;
    }
}
