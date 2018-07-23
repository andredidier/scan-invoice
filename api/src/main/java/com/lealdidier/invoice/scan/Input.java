package com.lealdidier.invoice.scan;

import java.io.IOException;

public interface Input<T> {
    T read() throws IOException;
}
