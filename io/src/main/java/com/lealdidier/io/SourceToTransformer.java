package com.lealdidier.io;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;

public class SourceToTransformer implements Function<Source, Transformer> {
    @Override
    public Transformer apply(Source source) throws IOException {
        try {
            return TransformerFactory.newDefaultInstance().newTransformer(source);
        } catch (TransformerConfigurationException e) {
            throw new IOException(e);
        }

    }
}
