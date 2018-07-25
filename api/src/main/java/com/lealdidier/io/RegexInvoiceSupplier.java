package com.lealdidier.io;

import com.lealdidier.invoice.scan.Invoice;

import java.util.function.Supplier;

public class RegexInvoiceSupplier implements Supplier<Invoice> {

    private String regularExpression;

    @Override
    public Invoice get() {
        return null;
    }
}
