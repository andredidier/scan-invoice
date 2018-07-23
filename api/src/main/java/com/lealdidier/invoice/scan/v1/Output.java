package com.lealdidier.invoice.scan.v1;

import java.io.IOException;

public interface Output<T> {
    void write(T t) throws IOException;
}
