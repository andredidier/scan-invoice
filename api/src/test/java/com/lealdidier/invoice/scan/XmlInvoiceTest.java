package com.lealdidier.invoice.scan;

import com.lealdidier.functions.InputStreamToContentsString;
import com.lealdidier.functions.ResourceNameToUrl;
import com.lealdidier.functions.UrlToInputStream;
import com.lealdidier.io.DocumentInput;
import com.lealdidier.io.Input;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.w3c.dom.Document;
import spark.Response;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("XML Invoice Test")
public class XmlInvoiceTest {

    @DisplayName("Test converstion using XSLT")
    @ParameterizedTest(name = "[{index}]. {0} => {1}")
    @CsvSource({ "/invoice1.xml, /json1.js" })
    public void testSimple(String xmlFile, String jsonFile) throws Exception {

        XmlInvoice xmlInvoice = new XmlInvoice(
                new DocumentInput(new File(xmlFile).toURL()),
                new DocumentInput(getClass().getResource("/nfce-pe-to-json.xsl")));

        Response response = mock(Response.class);

        xmlInvoice.respondWithJsonBody(response);

        verify(response).body(new JSONObject().toString());
    }
}
