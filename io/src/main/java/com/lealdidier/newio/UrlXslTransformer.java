package com.lealdidier.newio;

import javax.xml.transform.Transformer;
import java.net.URL;
import java.util.function.Function;

public class UrlXslTransformer implements Function<URL, Transformer> {
    @Override
    public Transformer apply(URL url) {
        return null;
    }
}
