package com.lealdidier.invoice.scan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class InvoiceSupplierTest {

    @DisplayName("[{index}] {0}/{1}")
    @ParameterizedTest
    @CsvFileSource(resources = "/invoice.csv")
    void testInvoiceInput(String regularExpression, String contents, String urlType, String invoiceType, String typeConfiguration) {

    }
}
