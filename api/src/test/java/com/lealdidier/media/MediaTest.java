package com.lealdidier.media;

import com.lealdidier.io.AsciiHash;
import com.lealdidier.media.ExceptionConsumer;
import com.lealdidier.media.Media;
import com.lealdidier.ynab.Transaction;
import com.lealdidier.ynab.UrlXmlInvoiceTransaction;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@DisplayName("DB Media")
public class MediaTest {

    @DisplayName("Invoke field value only on those mapped")
    @Test
    void invokeOnlyThoseOnMapping() {
        Map<String, Object> values = new HashMap<>();
        Function<String, String> s = mock(Function.class);
        new Media<RuntimeException>()
                .addMapping("f1", v -> values.put("v1", v))
                .addField("f1", () -> s.apply("v1"))
                .addField("f2", () -> s.apply("v2"))
                .writeFields();
        verify(s, times(1)).apply("v1");
        verify(s, times(0)).apply("v2");
    }

    @DisplayName("Save to DB Media")
    @Test
    public void saveDBMedia() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        when(ps.execute()).thenReturn(true);

        new Media<SQLException>()
                .addMapping("url", o -> ps.setString(1, o.toString()))
                .addField("url",() -> {
                    try {
                        return new URL("http://localhost");
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }).writeFields();

        verify(ps, times(1)).setString(1, "http://localhost");
    }

    @DisplayName("DB Media to save transaction")
    @Test
    public void saveTransactionToDBMedia() throws SQLException, IOException {
        Function<String, URL> resource = getClass()::getResource;
        Function<URL, String> getContents = url -> {
            try {
                return IOUtils.toString(url);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        };
        Function<String, JSONObject> contentsToJson = JSONObject::new;
        Function<String, JSONObject> jsonResourceFileNameToJson = contentsToJson.compose(getContents).compose(resource);

        URL url = resource.apply("/invoice1.xml");
        Transaction t = new UrlXmlInvoiceTransaction(url);

        PreparedStatement ps = mock(PreparedStatement.class);
        when(ps.execute()).thenReturn(true);

        Function<Integer, ExceptionConsumer<SQLException, String>> string = i -> s -> ps.setString(i, s);
        Function<Integer, ExceptionConsumer<SQLException, Date>> date = i -> d -> ps.setDate(i, new java.sql.Date(d.getTime()));
        Function<Integer, ExceptionConsumer<SQLException, Object>> toString = i -> o -> ps.setString(i, o.toString());

        Date d = new Date();

        t.addTo(new Media<SQLException>()
                .addField("updated", () -> d)
                .addMapping("url", toString.apply(1))
                .addMapping("xml", string.apply(2))
                .addMapping("ynabJson", toString.apply(3))
                .addMapping("updated", date.apply(4))
                .addMapping("ynabJson", j -> string.apply(5).accept(new AsciiHash().apply(j.toString()))))
                .writeFields();

        JSONObject json = jsonResourceFileNameToJson.apply("/json1.js");

        verify(ps, times(1)).setString(1, url.toString());
        verify(ps, times(1)).setString(2, IOUtils.toString(url));
        verify(ps, times(1)).setString(3, json.toString());
        verify(ps, times(1)).setDate(4, new java.sql.Date(d.getTime()));
        verify(ps, times(1)).setString(5, new AsciiHash().apply(json.toString()));
    }

    @DisplayName("Save ETag")
    @Test
    public void saveETag() throws SQLException, IOException {
        Function<String, URL> resource = getClass()::getResource;
        Function<URL, String> getContents = url -> {
            try {
                return IOUtils.toString(url);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        };
        Function<String, JSONObject> contentsToJson = JSONObject::new;
        Function<String, JSONObject> jsonResourceFileNameToJson = contentsToJson.compose(getContents).compose(resource);

        URL url = resource.apply("/invoice1.xml");
        Transaction t = new UrlXmlInvoiceTransaction(url);

        PreparedStatement ps = mock(PreparedStatement.class);
        when(ps.execute()).thenReturn(true);

        Function<Integer, ExceptionConsumer<SQLException, String>> calculateHash = i -> s -> ps.setString(i, new AsciiHash().apply(s));

        t.addTo(new Media<SQLException>()
                .addMapping("url", u -> calculateHash.apply(1).accept(u.toString()))
                .addMapping("ynabJson", j -> calculateHash.apply(2).accept(j.toString())))
                .writeFields();

        JSONObject json = jsonResourceFileNameToJson.apply("/json1.js");

        verify(ps, times(1)).setString(1, new AsciiHash().apply(url.toString()));
        verify(ps, times(1)).setString(2, new AsciiHash().apply(json.toString()));
    }
}
