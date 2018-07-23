package com.lealdidier.invoice.scan;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("XML Invoice Test")
public class XmlInvoiceTest {

    @DisplayName("Test converstion using XSLT")
    @ParameterizedTest(name = "[{index}]. {0} => {1}")
    @CsvSource({ "/invoice1.xml, /json1.js" })
    public void testSimple(String xmlFile, String jsonFile) throws IOException, URISyntaxException {
        try(InputStream fis = getClass().getResourceAsStream(xmlFile)) {
            Input<Source> s = new ScalarInput<>(new StreamSource(fis));
            TransformerFileInput t = new TransformerFileInput(getClass().getResource("/nfce-pe-to-json.xsl").getFile());
            XmlInvoice xmlInvoice = new XmlInvoice(s, t);

            JSONObject expected = new JSONObject(String.join("\n",
                    Files.readAllLines(Paths.get(getClass().getResource(jsonFile).toURI()))));
            assertEquals(expected.toString(), xmlInvoice.toJson().toString());
        }
    }
}
