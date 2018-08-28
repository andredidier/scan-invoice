package com.lealdidier.ynab;

import com.lealdidier.media.Media;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.lealdidier.ynab.TransactionField.JSON;
import static com.lealdidier.ynab.TransactionField.XML;

public class ResultSetTransaction implements Transaction {

    private final ResultSet resultSet;
    private final String urlName;
    private final String xmlName;
    private final String jsonName;

    public ResultSetTransaction(ResultSet resultSet, String urlName, String xmlName, String jsonName) {
        this.resultSet = resultSet;
        this.urlName = urlName;
        this.xmlName = xmlName;
        this.jsonName = jsonName;
    }

    @Override
    public <E extends Exception> Media<E> addTo(Media<E> media) {
        return media.addField(TransactionField.URL, this::url)
                .addField(XML, this::xmlContents)
                .addField(JSON, this::ynabJsonContents);
    }

    private URL url() {
        try {
            return new URL(resultSet.getString(urlName));
        } catch (SQLException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject ynabJsonContents() {
        try {
            return new JSONObject(resultSet.getString(jsonName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String xmlContents() {
        try {
            return resultSet.getString(xmlName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
