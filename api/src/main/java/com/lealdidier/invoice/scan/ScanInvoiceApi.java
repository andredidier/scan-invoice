package com.lealdidier.invoice.scan;

import com.lealdidier.invoice.scan.rest.ScanInvoiceV1;
import spark.Spark;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static spark.Spark.path;
import static spark.Spark.port;

public class ScanInvoiceApi {

    private final static Logger logger  = getLogger(ScanInvoiceApi.class.getName());

    private int port;

    public ScanInvoiceApi(int port) {
        this.port = port;
    }

    public void start() {
        ScanInvoiceV1 v1 = new ScanInvoiceV1();
        port(port);
        path("/v1", v1::methods);
        logger.info(String.format("Successfully configured API on port %d", port));
    }
    public void stop() {
        Spark.stop();
    }

    private static void setUpPort(String[] args) {
        try {
            if (args.length == 0) {
                return;
            }
            int portNumber = Integer.parseInt(args[0]);
            logger.log(Level.FINE, String.format("Port number: %d", portNumber));
            port(portNumber);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Given parameter is not a port number", e);
            throw e;
        }
    }
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        ScanInvoiceApi api = new ScanInvoiceApi(portNumber);
        api.start();
        /*
        path("/v1", () -> {
            before("/*", (req, res) -> logger.info("Calling API v1"));
            get("/regularexpressions", GetRegularExpressions::get, gson::toJson);

            post("/regularexpression", "application/xml", PostRegularExpression::post);
            post("/regularexpression", "text/plain", PostRegularExpression::post);

            path("/regularexpression", () -> {
                before("/:id", (req, res) -> logger.info("Calling for existing regular expression"));
                delete("/:id", DelRegularExpression::del);
                with("/:id", PutRegularExpression::with);
                get("/:id", GetRegularExpression::get);
            });
        });
        */
    }



}
