package com.lealdidier.functions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class UrlToInputStream implements Function<URL, InputStream> {

    private final static Logger logger = Logger.getLogger(UrlToInputStream.class.getName());

    @Override
    public InputStream apply(URL url) {
        try {
            logger.fine(String.format("Opening stream from %s", url));
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
