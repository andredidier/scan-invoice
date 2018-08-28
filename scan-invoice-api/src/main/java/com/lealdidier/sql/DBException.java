package com.lealdidier.sql;

import com.lealdidier.api.ExceptionFieldName;
import com.lealdidier.media.Media;
import com.lealdidier.media.MediaFieldConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

public class DBException extends Exception implements MediaFieldConfiguration {
    public DBException() {

    }
    public DBException(SQLException e) {
        super(e);
    }

    @Override
    public <E extends Exception> Media<E> addTo(Media<E> media) {
        return media.addField(ExceptionFieldName.Message, this::getMessage)
                .addField(ExceptionFieldName.StackTrace, this::stackTrace);
    }

    private String stackTrace() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            this.printStackTrace(ps);
            return new String(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
