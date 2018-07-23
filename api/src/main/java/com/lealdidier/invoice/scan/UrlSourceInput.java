package com.lealdidier.invoice.scan;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class UrlSourceInput implements Input<Source> {

    private final static Logger logger = Logger.getLogger(UrlSourceInput.class.getName());

    private URL url;

    public UrlSourceInput(URL url) {
        this.url = url;
    }

    @Override
    public Source read() throws IOException {
        logger.fine(() -> String.format("Reading contents from URL %s", url));
        return new StreamSource(url.openStream());
    }
}
