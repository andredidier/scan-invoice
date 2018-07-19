package com.lealdidier.invoice.scan;

import java.util.function.Supplier;

public class RegexInvoiceSupplier implements Supplier<Invoice> {

    private String regularExpression;

    @Override
    public Invoice get() {
        return null;
    }
}
