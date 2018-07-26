package com.lealdidier.invoice.scan;

import com.lealdidier.io.Output;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class InvoiceRequestJSONOutput implements Output<JSONObject> {
    private final static Logger logger = Logger.getLogger(InvoiceRequestJSONOutput.class.getName());

    private final InvoiceRequest invoiceRequest;

    public InvoiceRequestJSONOutput(InvoiceRequest invoiceRequest) {
        this.invoiceRequest = invoiceRequest;
    }

    @Override
    public void write(JSONObject jsonObject) throws IOException {
        String uuid = invoiceRequest.uuid().toString();
        logger.fine(String.format("Adding UUID (%s) to JSONObject", uuid));
        jsonObject.put("id", uuid);
    }
}
