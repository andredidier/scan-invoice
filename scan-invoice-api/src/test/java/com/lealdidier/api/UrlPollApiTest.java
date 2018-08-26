package com.lealdidier.api;

import com.lealdidier.io.AsciiHash;
import com.lealdidier.sql.DBCredentials;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("API test for poll API")
public class UrlPollApiTest {


    @DisplayName("Test same ETag")
    @Test
    public void testValidETag() throws Exception {
        DBCredentials credentials = mock(DBCredentials.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        URL url = getClass().getResource("/invoice1.xml");

        when(request.queryParams("url")).thenReturn(url.toString());

        when(request.headers(HttpHeaders.IF_NONE_MATCH)).thenReturn(new AsciiHash().apply(url.toString()));

        UrlRequestApi api = new UrlRequestApi("url", credentials);

        Object result = api.handle(request, response);

        assertNull(result);
        verify(response, times(0)).body(anyString());
        verify(response, times(1)).status(HttpStatus.SC_NOT_MODIFIED);
        verify(response, times(0)).header(HttpHeaders.ETAG, new AsciiHash().apply(url.toString()));
        verify(response, times(0)).header(HttpHeaders.CACHE_CONTROL, anyString());
        verify(credentials, times(0)).openConnection();
    }
}
