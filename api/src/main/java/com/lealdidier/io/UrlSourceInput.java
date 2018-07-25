package com.lealdidier.io;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class UrlSourceInput implements SourceInput {

    private final static Logger logger = Logger.getLogger(UrlSourceInput.class.getName());

    private Input<URL> urlInput;

    public UrlSourceInput(Input<URL> urlInput) {
        this.urlInput = urlInput;
    }
    public UrlSourceInput(URL url) {
        this(new ScalarInput<>(url));
    }

    @Override
    public Source read() throws IOException {
        logger.fine(String.format("Creating Stream from URL %s", urlInput));
        return new StreamSource(urlInput.read().openStream());
    }
}
