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

public class XmlUrlToJSONObject implements IOFunction<URL, JSONObject> {
    private final IOFunction<Source, String> sourceToContents;
    public XmlUrlToJSONObject(IOFunction<Source, String> sourceToContents) {
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
        IOFunction<URL, InputStream> urlToIs = new CloseableResult<>(URL::openStream);
        IOFunction<InputStream, Source> isToSource = StreamSource::new;

        IOFunction<InputStream, JSONObject> isToJson =
                isToSource.andThen(sourceToContents.andThen(JSONObject::new));

        return urlToIs.andThen(isToJson).apply(url);
    }
}
