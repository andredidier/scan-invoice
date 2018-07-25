package com.lealdidier.io;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class ResourceUrlInput implements UrlInput {
    private final static Logger logger = Logger.getLogger(ResourceUrlInput.class.getName());

    private final String name;
    private final Class<?> classForResource;

    public ResourceUrlInput(String name, Class<?> classForResource) {
        this.name = name;
        this.classForResource = classForResource;
    }

    @Override
    public URL read() throws IOException {
        logger.fine(String.format("Obtainig resource \"%s\" using class %s", name, classForResource.getSimpleName()));
        return classForResource.getResource(name);
    }
}
