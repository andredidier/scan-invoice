package com.lealdidier.io;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class XmlSourceToContents implements IOBiFunction<Source, Charset, String> {
    private final Transformer transformer;

    public XmlSourceToContents(Transformer transformer, Charset charset) {
        if (transformer == null) {
            throw new IllegalArgumentException("transformer");
        }
        if (charset == null) {
            throw new IllegalArgumentException("charset");
        }
        this.transformer = transformer;
        this.charset = charset;
    }
    public XmlSourceToContents(Transformer transformer) {
        this(transformer, StandardCharsets.UTF_8);
    }

    @Override
    public String apply(Source source, Charset charset) throws IOException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            transformer.transform(source, new StreamResult(baos));
            return new String(baos.toByteArray(), charset);
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }
}
