package com.lealdidier.io;

import org.json.JSONObject;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class XmlUrlToJSONObject implements Function<URL, JSONObject> {
    private final Function<Source, String> sourceToContents;
    public XmlUrlToJSONObject(Function<Source, String> sourceToContents) {
        this.sourceToContents = sourceToContents;
    }

    public XmlUrlToJSONObject(Transformer transformer, Charset charset) {
        this(new XmlSourceToContents(transformer, charset));
    }
    public XmlUrlToJSONObject(Transformer transformer) {
        this(transformer, StandardCharsets.UTF_8);
    }

    @Override
    public JSONObject apply(URL url) throws IOException {
        Function<URL, InputStream> urlToIs = new CloseableResult<>(URL::openStream);
        Function<InputStream, Source> isToSource = StreamSource::new;

        Function<InputStream, JSONObject> isToJson =
                isToSource.andThen(sourceToContents.andThen(JSONObject::new));

        return urlToIs.andThen(isToJson).apply(url);
    }
}
