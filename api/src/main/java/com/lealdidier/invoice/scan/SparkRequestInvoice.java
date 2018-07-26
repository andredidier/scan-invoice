package com.lealdidier.invoice.scan;

import com.lealdidier.io.Media;
import spark.Request;

public class SparkRequestInvoice implements Invoice {
    private final Request request;

    public SparkRequestInvoice(Request request) {
        this.request = request;
    }

    @Override
    public void print(Media media) {

    }
}
