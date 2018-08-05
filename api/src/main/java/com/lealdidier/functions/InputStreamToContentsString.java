package com.lealdidier.functions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class InputStreamToContentsString implements Function<InputStream, String> {
    private static final Logger logger = Logger.getLogger(InputStreamToContentsString.class.getName());

    private final String charsetName;

    public InputStreamToContentsString(Charset charset) {
        this(charset.name());
    }

    public InputStreamToContentsString(String charsetName) {
        this.charsetName = charsetName;
    }

    @Override
    public String apply(InputStream inputStream) {
        String res = new Scanner(inputStream, charsetName).useDelimiter("\\A").next();
        logger.fine(String.format("Value read from \"%s\": %s", inputStream, res));
        return res;
    }
}
