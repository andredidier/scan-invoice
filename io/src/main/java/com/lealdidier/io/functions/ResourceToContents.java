package com.lealdidier.io.functions;

import com.lealdidier.io.CloseableResult;
import com.lealdidier.io.IOBiFunction;
import com.lealdidier.io.IOFunction;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

public class ResourceToContents implements IOBiFunction<String, Charset, String> {
    public String apply(String name, Charset charset) throws IOException  {
        IOBiFunction<byte[], Charset, String> bytesCharsetToString = String::new;
        IOFunction<String, URL> f1 = n -> getClass().getResource(n);
        IOFunction<URL, InputStream> f2 = new CloseableResult<>(URL::openStream);
        IOFunction<InputStream, String> f3 = bytesCharsetToString.curry2(charset)
                .compose(new InputStreamToBytes());
        //return f1.andThen(f2.andThen(f3)).apply(name);
        return f3.compose(f2).compose(f1).apply(name);
    }
}
