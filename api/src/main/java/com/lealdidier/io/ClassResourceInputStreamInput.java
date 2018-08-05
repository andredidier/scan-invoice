package com.lealdidier.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class ClassResourceInputStreamInput implements Input<InputStream> {

    private final static Logger logger = Logger.getLogger(ClassResourceInputStreamInput.class.getName());

    private final Class<?> resourceClass;
    private final String resourceName;

    public ClassResourceInputStreamInput(Class<?> resourceClass, String resourceName) {
        this.resourceClass = resourceClass;
        this.resourceName = resourceName;
    }

    @Override
    public InputStream read() throws IOException {
        logger.fine(String.format("Opening input stream from resource %s, using class loader for class %s",
                resourceName, resourceClass.getName()));
        try(InputStream inputStream = resourceClass.getResourceAsStream(resourceName)) {
            logger.fine("Input stream open");
            return inputStream;
        }
    }
}
