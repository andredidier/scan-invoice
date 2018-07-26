package com.lealdidier.invoice.scan.rest;

import com.lealdidier.invoice.scan.InvoiceRequest;
import com.lealdidier.invoice.scan.InvoiceRequestJSONOutput;
import com.lealdidier.invoice.scan.ScalarInvoiceRequest;
import com.lealdidier.invoice.scan.SparkRequestInvoiceRequest;
import com.lealdidier.io.Input;
import com.lealdidier.io.JSONObjectResponseBodyOutput;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class InterpretPost  {

    private final static String CALLBACK_URL_HEADER_NAME = "X-Callback-Url";

    private final Input<String[]> regexs;
    private final Input<String> nonMatchingRegexMessageInput;

    public InterpretPost(Input<String[]> regexs, Input<String> nonMatchingRegexMessageInput) {
        this.regexs = regexs;
        this.nonMatchingRegexMessageInput = nonMatchingRegexMessageInput;
    }

    private boolean matches(String value) throws IOException {
        for(String regexPattern : regexs.read()) {
            if (regexPattern.matches(value)) {
                return true;
            }
        }
        return false;
    }

    public Object handle(Request request, Response response) throws Exception {
        String contents = request.body();
        if (matches(contents) && request.headers().contains(CALLBACK_URL_HEADER_NAME)) {
            handleContents(contents, request, response);
        } else {
            response.status(400);
            response.body(nonMatchingRegexMessageInput.read());
        }
    }

    private void handleContents(String contents, Request request, Response response) throws IOException {
        InvoiceRequest invoiceRequest = new SparkRequestInvoiceRequest(CALLBACK_URL_HEADER_NAME, request);
        invoiceRequest.process();
    }
}
