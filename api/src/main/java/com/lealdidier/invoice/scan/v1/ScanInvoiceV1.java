package com.lealdidier.invoice.scan.v1;

import com.lealdidier.invoice.scan.ScanInvoiceApi;
import com.lealdidier.invoice.scan.TransformerFileInput;
import com.lealdidier.invoice.scan.UrlSourceInput;
import com.lealdidier.invoice.scan.XmlInvoice;

import java.net.URL;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static spark.Spark.before;
import static spark.Spark.post;


public class ScanInvoiceV1 {
    private final static Logger logger  = getLogger(ScanInvoiceApi.class.getName());

    public void methods() {
        before("/*", (req, res) -> logger.info("Calling API v1"));
        post("/interpreter", (req, res)-> {
            String contents = new XmlInvoice(new UrlSourceInput(new URL(req.body())),
                    new TransformerFileInput(getClass().getResource("nfce-pe-to-json").getFile()))
                    .toJson().toString();
            res.status(200);
            return contents;
        });
    }
}
