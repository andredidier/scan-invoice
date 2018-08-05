package com.lealdidier.io;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

public class ResourceToTransformer implements Function<String, Transformer> {
    private final Class<?> classForResources;

    public ResourceToTransformer(Class<?> classForResources) {
        if (classForResources == null) {
            throw new IllegalArgumentException("classForResources");
        }
        this.classForResources = classForResources;
    }

    @Override
    public Transformer apply(String resourceName) throws IOException {
        Function<String, InputStream> inputStreamOfResource = new CloseableResult<>(classForResources::getResourceAsStream);
        Function<InputStream, Source> inputStreamToSource = StreamSource::new;
        Function<String, Transformer> resourceTransformer =
                inputStreamOfResource.andThen(inputStreamToSource.andThen(new SourceToTransformer()));
        return resourceTransformer.apply(resourceName);
    }
}
