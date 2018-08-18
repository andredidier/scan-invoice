package com.lealdidier.ynab.nfce;

import com.lealdidier.io.XmlInputStreamSupplier;
import org.json.JSONObject;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Supplier;

public class NfceXmlToJson implements Function<XmlInputStreamSupplier, JSONObject> {
    private final Supplier<InputStream> xslStreamSupplier;
    private final Charset transactionStreamCharset;

    public NfceXmlToJson(Supplier<InputStream> xslStreamSupplier, Charset transactionStreamCharset) {
        this.xslStreamSupplier = xslStreamSupplier;
        this.transactionStreamCharset = transactionStreamCharset;
    }
    public NfceXmlToJson(Supplier<InputStream> xslStreamSupplier) {
        this(xslStreamSupplier, StandardCharsets.UTF_8);
    }

    @Override
    public JSONObject apply(XmlInputStreamSupplier transactionStreamSupplier) {
        try(InputStream xslStream = xslStreamSupplier.get();
            InputStream transactionStream = transactionStreamSupplier.get();
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Source xslSource = new StreamSource(xslStream);
            Source transactionSource = new StreamSource(transactionStream);
            Transformer transformer = TransformerFactory.newDefaultInstance().newTransformer(xslSource);
            Result r = new StreamResult(baos);
            transformer.transform(transactionSource, r);
            String contents = new String(baos.toByteArray(), transactionStreamCharset);
            return new JSONObject(contents);
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
