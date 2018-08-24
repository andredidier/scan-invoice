package com.lealdidier.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;

public class XmlUrlInputStreamSupplier implements Function<URL, XmlInputStreamSupplier> {
    @Override
    public XmlInputStreamSupplier apply(URL url) {
        return () -> {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
