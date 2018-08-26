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
                    //TODO falta implementar
                } else {
                    handleNotProcessedHeader(url, request, response);
                }
            } else {
                t = new UrlXmlInvoiceTransaction(url);
                saveRequest(t, conn, response);
                response.status(HttpStatus.SC_CREATED);
                t.addTo(new Media<IOException>()
                        .addMapping(TransactionField.JSON, (JSONObject j) -> response.body(j.toString()))
                ).writeFields();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleNotProcessedHeader(URL url, Request request, Response response) {
        String urlHash = new AsciiHash().apply(url.toString());
        if (request.headers(HttpHeaders.IF_NONE_MATCH).equals(urlHash)) {
            response.status(HttpStatus.SC_NOT_MODIFIED);
        } else {
            response.status(HttpStatus.SC_ACCEPTED);
        }
        response.header(HttpHeaders.CACHE_CONTROL, String.format("public,max-age=%s", shortCache.toSeconds()));
        response.header(HttpHeaders.ETAG, urlHash);
    }

    private void saveRequest(Transaction t, Connection conn, Response response) throws DBException {
        Date date = new Date(new java.util.Date().getTime());
        DBStatement statement = TransactionDBStatement.Insert;
        try(PreparedStatement ps = conn.prepareStatement(statement.sql())) {
            ps.setDate(2, date);
            ps.setDate(3, date);
            t.addTo(new Media<SQLException>()
                    .addMapping(TransactionField.URL, url -> ps.setString(1, url.toString()))
            ).writeFields();
            if (!ps.execute()) {
                throw new StatementNotRunException(statement, "?", date, date);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
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
