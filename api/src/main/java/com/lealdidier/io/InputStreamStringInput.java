package com.lealdidier.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Logger;

public class InputStreamStringInput implements StringInput {
    private final static Logger logger = Logger.getLogger(InputStreamStringInput.class.getName());

    private final Input<InputStream> inputStreamInput;
    private final String charsetName;

    public InputStreamStringInput(Input<InputStream> inputStreamInput, String charsetName) {
        this.inputStreamInput = inputStreamInput;
        this.charsetName = charsetName;
    }

    @Override
    public String read() throws IOException {
        String res = new Scanner(inputStreamInput.read(), charsetName).useDelimiter("\\A").next();
        logger.fine(String.format("Value read from \"%s\": %s", inputStreamInput, res));
        return res;
    }

    @Override
    public String toString() {
        return String.format("%s[in=%s, charSet=%s]", getClass().getSimpleName(), inputStreamInput, charsetName);
    }
}
