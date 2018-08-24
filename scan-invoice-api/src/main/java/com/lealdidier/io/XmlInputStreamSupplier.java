package com.lealdidier.io;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * A typed input stream for XML contents.
 */
@FunctionalInterface
public interface XmlInputStreamSupplier {
    InputStream get();
}
