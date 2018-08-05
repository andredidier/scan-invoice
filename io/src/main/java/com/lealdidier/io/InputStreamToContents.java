package com.lealdidier.io;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;

public class InputStreamToContents implements Function<InputStream, String> {
    private final Charset charset;

    public InputStreamToContents(Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("charset");
        }
        this.charset = charset;
    }

    @Override
    public String apply(InputStream inputStream) {
        return new Scanner(inputStream, charset.name()).useDelimiter("\\A").next();
    }
}
