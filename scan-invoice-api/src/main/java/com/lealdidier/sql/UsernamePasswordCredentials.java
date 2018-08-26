package com.lealdidier.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UsernamePasswordCredentials implements DBCredentials {
    private final String username;
    private final String password;
    private final String jdbcUrl;
    private final String driverClassName;

    public UsernamePasswordCredentials(String username, String password, String jdbcUrl, String driverClassName) {
        this.username = username;
        this.password = password;
        this.jdbcUrl = jdbcUrl;
        this.driverClassName = driverClassName;
    }

    @Override
    public Connection openConnection() throws DBException {
        try {
            Class.forName(driverClassName);
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new DBException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
