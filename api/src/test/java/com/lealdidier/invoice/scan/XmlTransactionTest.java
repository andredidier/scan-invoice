package com.lealdidier.invoice.scan;

import com.lealdidier.ynab.XmlTransaction;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlTransactionTest {


    @Test
    public void test2() {
        Function<String, URL> resource = getClass()::getResource;
        Function<URL, JSONObject> jsonUrlToJson = null;
        Function<URL, Supplier<InputStream>> urlToInputStreamSupplier = new UrlInputStreamSupplier();
        Function<URL, JSONObject> xmlUrlToJson =
                url -> new XmlTransaction(urlToInputStreamSupplier.apply(resource.apply("xslfile")),
                        urlToInputStreamSupplier.apply(url), StandardCharsets.UTF_8).createBasicPayload();

        assertEquals(jsonUrlToJson.compose(resource).apply("jsonFile").toString(),
                xmlUrlToJson.compose(resource).apply("xmlFile").toString());
    }
}
