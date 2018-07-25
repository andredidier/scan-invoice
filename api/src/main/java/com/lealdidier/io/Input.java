package com.lealdidier.io;

import java.io.IOException;

public interface Input<T> {
    T read() throws IOException;
}
