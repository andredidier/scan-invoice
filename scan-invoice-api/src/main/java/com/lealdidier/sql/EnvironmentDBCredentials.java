package com.lealdidier.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EnvironmentDBCredentials implements DBCredentials {

    @Override
    public Connection openConnection() throws DBException {

        try {
            String jdbcUrl = System.getenv("SCAN_INVOICE_JDBC_URL");
            String driverClassName = System.getenv("SCAN_INVOICE_DRIVER_CLASS_NAME");
            String username = System.getenv("SCAN_INVOICE_DB_USERNAME");
            String password = System.getenv("SCAN_INVOICE_DB_PASSWORD");

            Class.forName(driverClassName);

            if (username != null && !"".equals(username)) {
                return DriverManager.getConnection(jdbcUrl, username, password);
            } else {
                return DriverManager.getConnection(jdbcUrl);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
