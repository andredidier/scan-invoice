package com.lealdidier.api;

import com.lealdidier.io.AsciiHash;
import com.lealdidier.media.Media;
import com.lealdidier.sql.*;
import com.lealdidier.ynab.ResultSetTransaction;
import com.lealdidier.ynab.Transaction;
import com.lealdidier.ynab.TransactionField;
import com.lealdidier.ynab.UrlXmlInvoiceTransaction;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class UrlPollApi implements Route {

    private final String urlFormFieldName;
    private final Duration longCache;
    private final Duration shortCache;
    private final DBCredentials credentials;

    public UrlPollApi(String urlFormFieldName, Duration longCache, Duration shortCache,
                      DBCredentials credentials) {
        this.longCache = longCache;
        this.shortCache = shortCache;
        this.urlFormFieldName = urlFormFieldName;
        this.credentials = credentials;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        URL url = createUrl(request.queryParams(urlFormFieldName));
        process(url, request, response);
        return null;
    }

    private void process(URL url, Request request, Response response) throws DBException {
        try(Connection conn = credentials.openConnection();
            PreparedStatement ps = conn.prepareStatement(TransactionDBStatement.QueryJson.sql())) {
            ps.setString(1, url.toString());
            processQuery(url, conn, ps, request, response);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private void processQuery(URL url, Connection conn, PreparedStatement ps, Request request, Response response) throws DBException {
        try(ResultSet rs = ps.executeQuery()) {
            Transaction t;
            if (rs.next()) {
                t = new ResultSetTransaction(rs, "URL", "XML", "JSON");
                if (rs.getBoolean("PROCESSED")) {
                    handleProcessed(t, request, response);
                } else {
                    handleNotProcessedHeader(url, request, response);
                }
            } else {
                t = new UrlXmlInvoiceTransaction(url);
                response.status(HttpStatus.SC_NOT_FOUND);
                response.header(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,must-revalidate");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private void handleProcessed(Transaction t, Request request, Response response) {
        Map<String, String> tData = new HashMap<>();
        t.addTo(new Media<RuntimeException>().addMapping(TransactionField.JSON, (JSONObject json) -> tData.put("json", json.toString()))).writeFields();
        String hash = new AsciiHash().apply(tData.get("json"));
        if (containsHeaderWithValue(HttpHeaders.IF_NONE_MATCH, hash, request)) {
            response.status(HttpStatus.SC_NOT_MODIFIED);
        } else {
            response.status(HttpStatus.SC_OK);
            response.body(tData.get("json"));
        }
        response.header(HttpHeaders.CACHE_CONTROL, String.format("public,max-age=%s", longCache.toSeconds()));
        response.header(HttpHeaders.ETAG, hash);
    }

    private boolean containsHeaderWithValue(String header, String value, Request request) {
        String headerValue = request.headers(header);
        return headerValue != null && headerValue.equals(value);
    }

    private void handleNotProcessedHeader(URL url, Request request, Response response) {
        String urlHash = new AsciiHash().apply(url.toString());
        if (containsHeaderWithValue(HttpHeaders.IF_NONE_MATCH, urlHash, request)) {
            response.status(HttpStatus.SC_NOT_MODIFIED);
        } else {
            response.status(HttpStatus.SC_ACCEPTED);
        }
        response.header(HttpHeaders.CACHE_CONTROL, String.format("public,max-age=%s", shortCache.toSeconds()));
        response.header(HttpHeaders.ETAG, urlHash);
    }

    private URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new PartValidationException(e,
                    new PartMessage(TransactionForm.UrlField, "malformed"));
        }
    }
}
