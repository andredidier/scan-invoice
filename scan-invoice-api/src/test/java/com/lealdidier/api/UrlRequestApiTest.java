package com.lealdidier.api;

import com.lealdidier.io.AsciiHash;
import com.lealdidier.sql.DBCredentials;
import com.lealdidier.sql.DBException;
import com.lealdidier.sql.TransactionDBStatement;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@DisplayName("Test API for URL Request")
public class UrlRequestApiTest {

    @DisplayName("Test first request to create transaction, returns with success")
    @Test
    void testFirstRequest() throws Exception {
        DBCredentials credentials = mock(DBCredentials.class);
        Connection conn = mock(Connection.class);
        PreparedStatement ps1 = mock(PreparedStatement.class);
        PreparedStatement ps2 = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        URL url = getClass().getResource("/invoice1.xml");
        when(request.queryParams("url")).thenReturn(url.toString());
        when(credentials.openConnection()).thenReturn(conn);
        when(conn.prepareStatement(TransactionDBStatement.QueryJson.sql())).thenReturn(ps1);
        when(conn.prepareStatement(TransactionDBStatement.Insert.sql())).thenReturn(ps2);
        when(ps1.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        when(ps2.execute()).thenReturn(true);

        UrlRequestApi api = new UrlRequestApi("url", credentials);
        Object result = api.handle(request, response);

        JSONObject json = new JSONObject(IOUtils.toString(getClass().getResource("/json1.js")));

        assertNull(result);
        verify(response, times(1)).status(HttpStatus.SC_CREATED);
        verify(response, times(1)).body(json.toString());
        verify(response, times(0)).header(eq(HttpHeaders.ETAG), anyString());
        verify(response, times(0)).header(eq(HttpHeaders.CACHE_CONTROL), anyString());
        verify(ps1, times(1)).setString(1, url.toString());
        verify(rs, times(1)).next();
        verify(rs, times(0)).getString("PROCESSED");
        verify(rs, times(0)).getString("JSON");
        verify(rs, times(0)).getString("XML");
        verify(rs, times(0)).getString("URL");
        verify(ps2, times(1)).setDate(eq(2), any());
        verify(ps2, times(1)).setDate(eq(3), any());
        verify(ps2, times(1)).setBoolean(4, false);
        verify(ps2, times(1)).execute();
        verify(rs, times(1)).close();
        verify(ps1, times(1)).close();
        verify(ps2, times(1)).close();
        verify(conn, times(1)).close();
    }

    @DisplayName("Test second request to query processed result with success")
    @Test
    void testSecondRequest() throws Exception {
        DBCredentials credentials = mock(DBCredentials.class);
        Connection conn = mock(Connection.class);
        PreparedStatement ps1 = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        URL url = getClass().getResource("/invoice1.xml");
        when(request.queryParams("url")).thenReturn(url.toString());
        when(credentials.openConnection()).thenReturn(conn);
        when(conn.prepareStatement(TransactionDBStatement.QueryJson.sql())).thenReturn(ps1);
        when(ps1.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        UrlRequestApi api = new UrlRequestApi("url", credentials);
        Object result = api.handle(request, response);

        assertNull(result);
        verify(response, times(1)).status(HttpStatus.SC_CONFLICT);
        verify(response, times(0)).body(anyString());
        verify(response, times(0)).header(eq(HttpHeaders.ETAG), anyString());
        verify(response, times(0)).header(eq(HttpHeaders.CACHE_CONTROL), anyString());
        verify(ps1, times(1)).setString(1, url.toString());
        verify(rs, times(1)).next();
        verify(rs, times(0)).getString("PROCESSED");
        verify(rs, times(0)).getString("JSON");
        verify(rs, times(0)).getString("XML");
        verify(rs, times(0)).getString("URL");
        verify(rs, times(1)).close();
        verify(ps1, times(1)).close();
        verify(conn, times(1)).close();
    }

}
