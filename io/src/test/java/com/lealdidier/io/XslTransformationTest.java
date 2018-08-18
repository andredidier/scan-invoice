package com.lealdidier.io;

import com.lealdidier.io.functions.ResourceToContents;
import com.lealdidier.io.functions.SourceToTransformer;
import com.lealdidier.io.functions.XslTransformation;
import com.lealdidier.newio.UrlInputStreamSupplier;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XslTransformationTest {

    @Test
    public void test() throws IOException {
        IOFunction<URL, InputStream> urlToInputStream = URL::openStream;
        IOFunction<InputStream, Source> inputStreamToSource = StreamSource::new;
        IOFunction<URL, Source> urlToSource = inputStreamToSource.compose(urlToInputStream);
        IOFunction<URL, Transformer> urlToTransformer = new SourceToTransformer().compose(urlToSource);
        IOFunction<String, JSONObject> contentsToJson = JSONObject::new;
        IOBiFunction<byte[], Charset, String> bytesCharsetToString = String::new;
        IOFunction<byte[], String> bytesToString = bytesCharsetToString.curry2(StandardCharsets.UTF_8);
        IOBiFunction<URL, URL, JSONObject> f = contentsToJson.compose(bytesToString).compose(
                new XslTransformation().compose1(urlToTransformer).compose2(urlToSource));

        IOFunction<String, JSONObject> resourceNameToJson = contentsToJson
                .compose(new ResourceToContents().curry2(StandardCharsets.UTF_8));

        assertEquals(
                resourceNameToJson.apply("json1.js").toString(),
                f.apply(getClass().getResource("nfce-pe-to-json.xsl"), getClass().getResource("invoice1.xml"))
        );
    }
}
