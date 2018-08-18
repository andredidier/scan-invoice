package com.lealdidier.io.functions;

import com.lealdidier.io.IOBiFunction;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class XslTransformation implements IOBiFunction<Transformer, Source, byte[]> {
    @Override
    public byte[] apply(Transformer transformer, Source source) throws IOException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            transformer.transform(source, new StreamResult(baos));
            return baos.toByteArray();
        } catch (TransformerException e) {
            throw new IOException(e);
        }

    }
}
