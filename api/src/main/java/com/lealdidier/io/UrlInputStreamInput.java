package com.lealdidier.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

public class UrlInputStreamInput implements Input<InputStream> {

    private final static Logger logger = Logger.getLogger(UrlInputStreamInput.class.getName());

    private final Input<URL> urlInput;

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), urlInput);
    }

    public UrlInputStreamInput(Input<URL> urlInput) {
        this.urlInput = urlInput;
    }

    @Override
    public InputStream read() throws IOException {
        logger.fine(String.format("Opening stream from %s", urlInput));
        try(InputStream inputStream = urlInput.read().openStream()) {
            logger.fine("Input stream open.");
            return inputStream;
        }
    }
}
