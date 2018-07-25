package com.lealdidier.io;

import java.io.IOException;

public interface Output<T> {
    void write(T t) throws IOException;
}
