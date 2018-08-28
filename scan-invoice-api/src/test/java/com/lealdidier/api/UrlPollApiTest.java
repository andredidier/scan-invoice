package com.lealdidier.api;

import com.lealdidier.io.AsciiHash;
import com.lealdidier.sql.DBCredentials;
import com.lealdidier.sql.TransactionDBStatement;
import org.apache.commons.io.IOUtils;
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
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("API test for poll API")
public class UrlPollApiTest {


    @DisplayName("Test same ETag, not processed")
    @Test
    public void testValidETagNotProcessed() throws Exception {
        DBCredentials credentials = mock(DBCredentials.class);
        Connection conn = mock(Connection.class);
        PreparedStatement ps1 = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        URL url = getClass().getResource("/invoice1.xml");

        when(request.queryParams("url")).thenReturn(url.toString());
        when(request.headers(HttpHeaders.IF_NONE_MATCH)).thenReturn(new AsciiHash().apply(url.toString()));
        when(credentials.openConnection()).thenReturn(conn);
        when(conn.prepareStatement(TransactionDBStatement.QueryJson.sql())).thenReturn(ps1);
        when(ps1.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getBoolean("PROCESSED")).thenReturn(false);

        UrlPollApi api = new UrlPollApi("url", Duration.ofHours(24), Duration.ofMinutes(5), credentials);

        Object result = api.handle(request, response);

        assertNull(result);
        verify(response, times(0)).body(anyString());
        verify(response, times(1)).status(HttpStatus.SC_NOT_MODIFIED);
        verify(response, times(1)).header(HttpHeaders.ETAG, new AsciiHash().apply(url.toString()));
        verify(response, times(1)).header(HttpHeaders.CACHE_CONTROL, "public,max-age=300");
        verify(credentials, times(1)).openConnection();
        verify(ps1, times(1)).setString(1, url.toString());
        verify(ps1, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(1)).getBoolean("PROCESSED");
        verify(rs, times(0)).getString("JSON");
        verify(rs, times(0)).getString("XML");
        verify(rs, times(0)).getString("URL");
        verify(rs, times(1)).close();
        verify(ps1, times(1)).close();
        verify(conn, times(1)).close();


    }


    @DisplayName("Test same ETag, processed")
    @Test
    public void testValidETagProcessed() throws Exception {
        DBCredentials credentials = mock(DBCredentials.class);
        Connection conn = mock(Connection.class);
        PreparedStatement ps1 = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        URL url = getClass().getResource("/invoice1.xml");

        JSONObject json = new JSONObject(IOUtils.toString(getClass().getResource("/json1.js")));

        when(request.queryParams("url")).thenReturn(url.toString());
        when(request.headers(HttpHeaders.IF_NONE_MATCH)).thenReturn(new AsciiHash().apply(json.toString()));
        when(credentials.openConnection()).thenReturn(conn);
        when(conn.prepareStatement(TransactionDBStatement.QueryJson.sql())).thenReturn(ps1);
        when(ps1.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getBoolean("PROCESSED")).thenReturn(true);
        when(rs.getString("JSON")).thenReturn(json.toString());

        UrlPollApi api = new UrlPollApi("url", Duration.ofHours(24), Duration.ofMinutes(5), credentials);

        Object result = api.handle(request, response);

        assertNull(result);
        verify(response, times(0)).body(anyString());
        verify(response, times(1)).status(HttpStatus.SC_NOT_MODIFIED);
        verify(response, times(1)).header(HttpHeaders.ETAG, new AsciiHash().apply(json.toString()));
        verify(response, times(1)).header(HttpHeaders.CACHE_CONTROL, "public,max-age=86400");
        verify(credentials, times(1)).openConnection();
        verify(ps1, times(1)).setString(1, url.toString());
        verify(ps1, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(1)).getBoolean("PROCESSED");
        verify(rs, times(1)).getString("JSON");
        verify(rs, times(0)).getString("XML");
        verify(rs, times(0)).getString("URL");
        verify(rs, times(1)).close();
        verify(ps1, times(1)).close();
        verify(conn, times(1)).close();


    }




    @DisplayName("Test sent URL tag, but content already processed")
    @Test
    public void testInvalidETagProcessed() throws Exception {
        DBCredentials credentials = mock(DBCredentials.class);
        Connection conn = mock(Connection.class);
        PreparedStatement ps1 = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        URL url = getClass().getResource("/invoice1.xml");

        JSONObject json = new JSONObject(IOUtils.toString(getClass().getResource("/json1.js")));

        when(request.queryParams("url")).thenReturn(url.toString());
        when(request.headers(HttpHeaders.IF_NONE_MATCH)).thenReturn(new AsciiHash().apply(url.toString()));
        when(credentials.openConnection()).thenReturn(conn);
        when(conn.prepareStatement(TransactionDBStatement.QueryJson.sql())).thenReturn(ps1);
        when(ps1.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getBoolean("PROCESSED")).thenReturn(true);
        when(rs.getString("JSON")).thenReturn(json.toString());

        UrlPollApi api = new UrlPollApi("url", Duration.ofHours(24), Duration.ofMinutes(5), credentials);

        Object result = api.handle(request, response);

        assertNull(result);
        verify(response, times(1)).body(anyString());
        verify(response, times(1)).status(HttpStatus.SC_OK);
        verify(response, times(1)).header(HttpHeaders.ETAG, new AsciiHash().apply(json.toString()));
        verify(response, times(1)).header(HttpHeaders.CACHE_CONTROL, "public,max-age=86400");
        verify(credentials, times(1)).openConnection();
        verify(ps1, times(1)).setString(1, url.toString());
        verify(ps1, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(1)).getBoolean("PROCESSED");
        verify(rs, times(1)).getString("JSON");
        verify(rs, times(0)).getString("XML");
        verify(rs, times(0)).getString("URL");
        verify(rs, times(1)).close();
        verify(ps1, times(1)).close();
        verify(conn, times(1)).close();


    }

    @DisplayName("Test first request, not posted")
    @Test
    void testFirstRequestNotRegistered() throws Exception {
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
        when(rs.next()).thenReturn(false);

        UrlPollApi api = new UrlPollApi("url", Duration.ofHours(24), Duration.ofMinutes(5), credentials);
        Object result = api.handle(request, response);

        assertNull(result);
        verify(response, times(1)).status(HttpStatus.SC_NOT_FOUND);
        verify(response, times(0)).body(anyString());
        verify(response, times(0)).header(eq(HttpHeaders.ETAG), anyString());
        verify(response, times(1)).header(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,must-revalidate");
        verify(ps1, times(1)).setString(1, url.toString());
        verify(rs, times(1)).next();
        verify(rs, times(0)).getBoolean("PROCESSED");
        verify(rs, times(0)).getString("JSON");
        verify(rs, times(0)).getString("XML");
        verify(rs, times(0)).getString("URL");
        verify(rs, times(1)).close();
        verify(ps1, times(1)).close();
        verify(conn, times(1)).close();
    }

    @DisplayName("Test first request, posted, but not processed yet")
    @Test
    void testFirstRequest() throws Exception {
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

        UrlPollApi api = new UrlPollApi("url", Duration.ofHours(24), Duration.ofMinutes(5), credentials);
        Object result = api.handle(request, response);

        assertNull(result);
        verify(response, times(1)).status(HttpStatus.SC_ACCEPTED);
        verify(response, times(0)).body(anyString());
        verify(response, times(1)).header(HttpHeaders.ETAG, new AsciiHash().compose(URL::toString).apply(url));
        verify(response, times(1)).header(HttpHeaders.CACHE_CONTROL, "public,max-age=300");
        verify(ps1, times(1)).setString(1, url.toString());
        verify(rs, times(1)).next();
        verify(rs, times(1)).getBoolean("PROCESSED");
        verify(rs, times(0)).getString("JSON");
        verify(rs, times(0)).getString("XML");
        verify(rs, times(0)).getString("URL");
        verify(rs, times(1)).close();
        verify(ps1, times(1)).close();
        verify(conn, times(1)).close();
    }

    @DisplayName("Test first request, posted, processed")
    @Test
    void testFirstRequestAndProcessedTransaction() throws Exception {
        DBCredentials credentials = mock(DBCredentials.class);
        Connection conn = mock(Connection.class);
        PreparedStatement ps1 = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        JSONObject json = new JSONObject(IOUtils.toString(getClass().getResource("/json1.js")));

        URL url = getClass().getResource("/invoice1.xml");
        when(request.queryParams("url")).thenReturn(url.toString());
        when(credentials.openConnection()).thenReturn(conn);
        when(conn.prepareStatement(TransactionDBStatement.QueryJson.sql())).thenReturn(ps1);
        when(ps1.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getBoolean("PROCESSED")).thenReturn(true);
        when(rs.getString("JSON")).thenReturn(json.toString());

        UrlPollApi api = new UrlPollApi("url", Duration.ofHours(24), Duration.ofMinutes(5), credentials);
        Object result = api.handle(request, response);

        assertNull(result);
        verify(response, times(1)).status(HttpStatus.SC_OK);
        verify(response, times(1)).body(json.toString());
        verify(response, times(1)).header(HttpHeaders.ETAG, new AsciiHash().apply(json.toString()));
        verify(response, times(1)).header(HttpHeaders.CACHE_CONTROL, "public,max-age=86400");
        verify(ps1, times(1)).setString(1, url.toString());
        verify(rs, times(1)).next();
        verify(rs, times(1)).getBoolean("PROCESSED");
        verify(rs, times(1)).getString("JSON");
        verify(rs, times(0)).getString("XML");
        verify(rs, times(0)).getString("URL");
        verify(rs, times(1)).close();
        verify(ps1, times(1)).close();
        verify(conn, times(1)).close();
    }
}
