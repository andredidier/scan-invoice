package com.lealdidier.sql;

import java.sql.Connection;

public interface DBCredentials {
    Connection openConnection() throws DBException;
}
