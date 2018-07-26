package com.lealdidier.invoice.scan.rest;

import com.lealdidier.invoice.scan.InvoiceRequest;
import com.lealdidier.invoice.scan.InvoiceRequestJSONOutput;
import com.lealdidier.invoice.scan.ScalarInvoiceRequest;
import com.lealdidier.io.Input;
import com.lealdidier.io.JSONObjectRequestBodyOutput;
import com.lealdidier.io.JSONObjectResponseBodyOutput;
import com.lealdidier.io.Output;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.UUID;

public class InterpretPost  {

    private final static String CALLBACK_URL_HEADER_NAME = "X-Callback-Url";

    private final Input<String[]> regexs;
    private final Input<String> nonMatchingRegexMessageInput;
    private final Output<InvoiceRequest> invoiceRequestOutput;

    public InterpretPost(Input<String[]> regexs, Input<String> nonMatchingRegexMessageInput,
                         Output<InvoiceRequest> invoiceRequestOutput) {
        this.regexs = regexs;
        this.nonMatchingRegexMessageInput = nonMatchingRegexMessageInput;
        this.invoiceRequestOutput = invoiceRequestOutput;
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
        if (request.headers().contains(CALLBACK_URL_HEADER_NAME)) {

        } else {
            InvoiceRequest invoiceRequest = new ScalarInvoiceRequest(
                    UUID.nameUUIDFromBytes(contents.getBytes()), contents);
            invoiceRequestOutput.write(invoiceRequest);
            response.status(202);
            new JSONObjectRequestBodyOutput(new InvoiceRequestJSONOutput(invoiceRequest), response).write(new JSONObject());
        }
    }
}
