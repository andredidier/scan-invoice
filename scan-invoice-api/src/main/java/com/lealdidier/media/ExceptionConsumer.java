package com.lealdidier.media;

import java.sql.SQLException;

@FunctionalInterface
public interface ExceptionConsumer<E extends Exception,T> {
    void accept(T t) throws E;
}
