package com.lealdidier.ynab;

import org.json.JSONObject;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public class XmlTransaction implements Transaction {

    private final Supplier<InputStream> xslStreamSupplier;
    private final Supplier<InputStream> transactionStreamSupplier;
    private final Charset transactionStreamCharset;

    public XmlTransaction(Supplier<InputStream> xslStreamSupplier, Supplier<InputStream> transactionStreamSupplier, Charset transactionStreamCharset) {
        this.xslStreamSupplier = xslStreamSupplier;
        this.transactionStreamSupplier = transactionStreamSupplier;
        this.transactionStreamCharset = transactionStreamCharset;
    }

    @Override
    public JSONObject createBasicPayload() {
        try(InputStream xslStream = xslStreamSupplier.get();
            InputStream transactionStream = transactionStreamSupplier.get();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            Source xslSource = new StreamSource(xslStream);
            Source transactionSource = new StreamSource(transactionStream);
            Transformer transformer = TransformerFactory.newDefaultInstance().newTransformer(xslSource);
            Result r = new StreamResult(baos);
            transformer.transform(transactionSource, r);
            return new JSONObject(new String(baos.toByteArray(), transactionStreamCharset));
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
