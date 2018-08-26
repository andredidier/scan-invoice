package com.lealdidier.api;

import com.amazonaws.services.xray.model.Http;
import com.lealdidier.media.Media;
import com.lealdidier.media.MediaFieldConfiguration;
import com.lealdidier.sql.DBCredentials;
import com.lealdidier.sql.DBException;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import spark.Response;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static spark.Spark.exception;
import static spark.Spark.post;

public class Version1Api {

    private final UrlRequestApi urlRequestApi;
    private final UrlPollApi urlPollApi;

    public Version1Api(UrlRequestApi urlRequestApi, UrlPollApi urlPollApi) {
        this.urlRequestApi = urlRequestApi;
        this.urlPollApi = urlPollApi;
    }

    public Version1Api(DBCredentials credentials) {
        this(
                new UrlRequestApi("url", credentials),
                new UrlPollApi("url", Duration.ofHours(24), Duration.ofMinutes(5), credentials)
        );
    }

    public void configure() {
        Function<JSONObject, Media<RuntimeException>> exceptionMedia =
                json -> new Media<RuntimeException>()
                .addMapping(ExceptionFieldName.Message, (List<String> ls) -> json.put("message", ls))
                .addMapping(ExceptionFieldName.StackTrace, (String s) -> json.put("stackTrace", s));
        Function<MediaFieldConfiguration, Consumer<Response>> h = e -> res -> {
            JSONObject json = new JSONObject();
            e.addTo(exceptionMedia.apply(json)).writeFields();
            res.body(json.toString());
        };
        exception(PartValidationException.class, (e, req, res) -> {
            res.status(HttpStatus.SC_BAD_REQUEST);
            h.apply(e).accept(res);
        });
        exception(DBException.class, (e, req, res) -> {
            res.status(HttpStatus.SC_SERVICE_UNAVAILABLE);
            h.apply(e).accept(res);
        });
        post("/transaction", urlRequestApi);
    }
}
