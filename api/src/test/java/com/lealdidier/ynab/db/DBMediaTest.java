package com.lealdidier.ynab.db;

import com.lealdidier.media.db.DBMapping;
import com.lealdidier.media.db.DBMedia;
import com.lealdidier.ynab.Transaction;
import com.lealdidier.ynab.UrlXmlInvoiceTransaction;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;

@DisplayName("DB Media")
public class DBMediaTest {

    @DisplayName("Save to DB Media")
    @Test
    public void saveDBMedia() throws SQLException, IOException {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        DBMedia media = new DBMedia("table", () -> conn,
                new DBMapping<URL>("url", (p, u) -> p.setString(1, u.toString())));
        media.addField("url",() -> {
            try {
                return new URL("http://localhost");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).writeFields();

        verify(conn, times(1)).prepareStatement("INSERT INTO table VALUES (?)");
        verify(ps, times(1)).setString(1, "http://localhost");
        verify(ps, times(1)).execute();
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

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        DBMedia media = new DBMedia("table", () -> conn,
                new DBMapping<URL>("url", (p, u) -> p.setString(1, u.toString())),
                new DBMapping<String>("xml", (p, s) -> p.setString(2, s)),
                new DBMapping<JSONObject>("ynabJson", (p, json) -> p.setString(3, json.toString()))
                );
        t.saveTo(media);

        verify(conn, times(1)).prepareStatement("INSERT INTO table VALUES (?,?,?)");
        verify(ps, times(1)).setString(1, url.toString());
        verify(ps, times(1)).setString(2, IOUtils.toString(url));
        verify(ps, times(1)).setString(3,
                jsonResourceFileNameToJson.apply("/json1.js").toString());
        verify(ps, times(1)).execute();
    }
}
