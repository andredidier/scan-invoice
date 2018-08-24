package com.lealdidier.invoice.scan;

import spark.Response;

import java.io.IOException;

public interface Invoice {
    void respondWithJsonBody(Response response) throws IOException;
}
