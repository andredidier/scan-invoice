package com.lealdidier.invoice.scan;

import com.lealdidier.io.*;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("XML Invoice Test")
public class XmlInvoiceTest {

    @DisplayName("Test converstion using XSLT")
    @ParameterizedTest(name = "[{index}]. {0} => {1}")
    @CsvSource({ "/invoice1.xml, /json1.js" })
    public void testSimple(String xmlFile, String jsonFile) throws IOException, URISyntaxException {
        try(InputStream fis = getClass().getResourceAsStream(xmlFile)) {
            Input<Source> s = new ScalarInput<>(new StreamSource(fis));
            Input<Transformer> t = new SourceTransformerInput(new UrlSourceInput(new ResourceUrlInput("/nfce-pe-to-json.xsl", getClass())));
            XmlInvoice xmlInvoice = new XmlInvoice(s, t);

            JSONObject expected = new JSONObject(
                    new UrlStringInput(new ResourceUrlInput(jsonFile, getClass()), "UTF-8").read()
            );
            assertEquals(expected.toString(), xmlInvoice.toJson().toString());
        }
    }
}
