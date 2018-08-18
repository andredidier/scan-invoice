package com.lealdidier.ynab.db;

import com.lealdidier.media.db.DBMapping;
import com.lealdidier.media.db.DBMedia;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;

public class DBMediaTest {

    @Test
    public void saveDBMedia() throws SQLException, IOException {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(conn.prepareStatement(matches("INSERT INTO table VALUES \\(\\?\\)"))).thenReturn(ps);
        when(ps.execute()).thenReturn(true);
        DBMedia media = new DBMedia("table", () -> conn,
                new DBMapping<URL>("url", (p, u) -> p.setString(1, u.toString())));
        media.writeFields(media.create("url", () -> {
            try {
                return new URL("http://localhost");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }));
        verify(conn, times(1)).prepareStatement("INSERT INTO table VALUES (?)");
        verify(ps, times(1)).setString(1, "http://localhost");
        verify(ps, times(1)).execute();
    }
}
