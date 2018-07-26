package com.lealdidier.invoice.scan;

import com.lealdidier.io.Input;
import com.lealdidier.io.Output;

public interface InvoiceRequest {
    void process(Output<Invoice> output);
}
