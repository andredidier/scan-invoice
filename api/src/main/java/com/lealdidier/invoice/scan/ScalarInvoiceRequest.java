package com.lealdidier.invoice.scan;

import java.util.UUID;

public class ScalarInvoiceRequest implements InvoiceRequest {
    private final UUID uuid;
    private final String contents;

    public ScalarInvoiceRequest(UUID uuid, String contents) {
        this.uuid = uuid;
        this.contents = contents;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }
}
