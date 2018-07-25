package com.lealdidier.io;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import java.io.IOException;
import java.net.URL;

public class TransformerInputOf implements TransformerInput {
    private final TransformerInput inner;

    public TransformerInputOf(TransformerInput inner) {
        this.inner = inner;
    }

    public TransformerInputOf(SourceInput sourceInput) {
        this(new SourceTransformerInput(sourceInput));
    }

    public TransformerInputOf(UrlInput urlInput) {
        this(new UrlSourceInput(urlInput));
    }

    public TransformerInputOf(String resourceName, Class<?> classForResource) {
        this(new ResourceUrlInput(resourceName, classForResource));
    }

    @Override
    public Transformer read() throws IOException {
        return inner.read();
    }
}
