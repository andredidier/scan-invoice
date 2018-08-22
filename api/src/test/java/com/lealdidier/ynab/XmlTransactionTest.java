package com.lealdidier.ynab;

import com.lealdidier.media.Media;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("XML Transaction")
public class XmlTransactionTest {

    @DisplayName("Parsing and output to Map Media")
    @Test
    public void verifyJSONMedia() {
        Function<String, URL> resource = getClass()::getResource;
        Function<URL, String> getContents = url -> {
            try {
                return IOUtils.toString(url);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        };
        Function<String, JSONObject> contentsToJson = JSONObject::new;
        Function<URL, JSONObject> jsonUrlToJson = contentsToJson.compose(getContents);

        Transaction t = new UrlXmlInvoiceTransaction(resource.apply("/invoice1.xml"));
        Map<String, Object> result = new HashMap<>();

        t.addTo(new Media<RuntimeException>()
                .addMapping("ynabJson", v -> result.put("json", v))).writeFields();

        assertEquals(1, result.size());
        assertTrue(result.keySet().contains("json"));
        assertEquals(jsonUrlToJson.compose(resource).apply("/json1.js").toString(),
                result.get("json").toString());
    }
}
