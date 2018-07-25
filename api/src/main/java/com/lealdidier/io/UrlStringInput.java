package com.lealdidier.io;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class UrlStringInput implements StringInput {
    private final static Logger logger = Logger.getLogger(UrlStringInput.class.getName());

    private final UrlInput urlInput;
    private final String charsetName;

    public UrlStringInput(UrlInput urlInput, String charsetName) {
        this.urlInput = urlInput;
        this.charsetName = charsetName;
    }

    @Override
    public String read() throws IOException {
        String res = new Scanner(urlInput.read().openStream(), charsetName).useDelimiter("\\A").next();
        logger.fine(String.format("Value read from \"%s\": %s", urlInput, res));
        return res;
    }
}
