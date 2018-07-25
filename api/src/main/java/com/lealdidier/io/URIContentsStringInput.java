package com.lealdidier.io;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class URIContentsStringInput implements Input<String> {
    private static final Logger logger = Logger.getLogger(URIContentsStringInput.class.getName());
    private final Input<URI> uriInput;

    public URIContentsStringInput(Input<URI> uriInput) {
        this.uriInput = uriInput;
    }

    @Override
    public String read() throws IOException {
        logger.fine(String.format("Getting contents from %s", uriInput.read()));
        return String.join("\n",
                Files.readAllLines(Paths.get(uriInput.read())));
    }
}
