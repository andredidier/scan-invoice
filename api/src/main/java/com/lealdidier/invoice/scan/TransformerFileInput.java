package com.lealdidier.invoice.scan;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TransformerFileInput implements Input<Transformer> {
    private final File file;

    public TransformerFileInput(File file) {
        this.file = file;
    }
    public TransformerFileInput(String filePath) {
        this(new File(filePath));
    }

    @Override
    public Transformer read() throws IOException {
        try(FileInputStream fis = new FileInputStream(file)) {
            TransformerFactory f = TransformerFactory.newDefaultInstance();
            return f.newTransformer(new StreamSource(fis));
        } catch (TransformerConfigurationException e) {
            throw new IOException(e);
        }
    }
}
