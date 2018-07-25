package com.lealdidier.io;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ResourceURIInput implements Input<URI> {

    private final String name;
    private final Class<?> classForResource;

    public ResourceURIInput(String name, Class<?> classForResource) {
        this.name = name;
        this.classForResource = classForResource;
    }

    @Override
    public URI read() throws IOException {
        try {
            return classForResource.getResource(name).toURI();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
