package com.lealdidier.io;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.util.logging.Logger;

public class SourceTransformerInput implements TransformerInput {
    private final static Logger logger = Logger.getLogger(SourceTransformerInput.class.getName());
    private final Input<Source> sourceInput;

    public SourceTransformerInput(Input<Source> sourceInput) {
        this.sourceInput = sourceInput;
    }

    @Override
    public Transformer read() throws IOException {
        try {
            logger.fine(String.format("Creating transformer from %s", sourceInput));
            return TransformerFactory.newDefaultInstance().newTransformer(sourceInput.read());
        } catch (TransformerConfigurationException e) {
            throw new IOException(e);
        }
    }
}
