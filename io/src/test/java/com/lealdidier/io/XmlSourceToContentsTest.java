package com.lealdidier.io;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("XmlSourceToContents test")
public class XmlSourceToContentsTest {
    @Test
    @DisplayName("Transform contents using XSL")
    public void test() throws IOException {
        Function<String, InputStream> inputStreamOfResource = new CloseableResult<>(getClass()::getResourceAsStream);
        Transformer t = new ResourceToTransformer(getClass()).apply("nfce-pe-to-json.xsl");
        Function<String, String> normalizer = s -> new JSONObject(s).toString();
        assertEquals(
                normalizer.apply(inputStreamOfResource.andThen(new InputStreamToContents(StandardCharsets.UTF_8)).apply("json1.js")),
                new XmlUrlToJSONObject(t).apply(getClass().getResource("invoice1.xml")).toString());
    }

    @Test
    @DisplayName("Null transformer as parameter")
    public void nullTransformerInXmlUrlToJSONObject() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new XmlUrlToJSONObject((Transformer)null));
        assertEquals("transformer", iae.getMessage());
    }

    @Test
    @DisplayName("Null charset as parameter")
    public void nullCharsetInXmlUrlToJSONObject() throws IOException {
        Transformer t = new ResourceToTransformer(getClass()).apply("nfce-pe-to-json.xsl");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new XmlUrlToJSONObject(t, null));
        assertEquals("charset", iae.getMessage());
    }
}
