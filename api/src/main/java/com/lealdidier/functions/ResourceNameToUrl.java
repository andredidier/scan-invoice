package com.lealdidier.functions;

import java.net.URL;
import java.util.function.Function;
import java.util.logging.Logger;

public class ResourceNameToUrl implements Function<String, URL> {

    private final static Logger logger = Logger.getLogger(ResourceNameToUrl.class.getName());

    private final Class<?> classForResource;

    public ResourceNameToUrl(Class<?> classForResource) {
        this.classForResource = classForResource;
    }
    public ResourceNameToUrl() {
        this(ResourceNameToUrl.class);
    }

    @Override
    public URL apply(String name) {
        logger.fine(String.format("Obtainig resource \"%s\" using class %s", name, classForResource.getSimpleName()));
        return classForResource.getResource(name);
    }
}
