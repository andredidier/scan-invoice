package com.lealdidier.io;

import org.json.JSONObject;
import spark.Response;

import java.io.IOException;
import java.util.logging.Logger;

public class JSONObjectRequestBodyOutput implements Output<JSONObject> {

    private final static Logger logger = Logger.getLogger(JSONObjectRequestBodyOutput.class.getName());

    private final Output<JSONObject> filler;
    private final Response response;

    public JSONObjectRequestBodyOutput(Output<JSONObject> filler, Response response) {
        this.filler = filler;
        this.response = response;
    }

    @Override
    public void write(JSONObject jsonObject) throws IOException {
        logger.fine("Filling JSONObject");
        filler.write(jsonObject);
        logger.fine("Addint JSONObject to response");
        response.body(jsonObject.toString());
    }
}
