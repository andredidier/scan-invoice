package com.lealdidier.invoice.scan;

import com.google.gson.Gson;
import com.lealdidier.invoice.scan.regularexpression.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static spark.Spark.*;

public class ScanInvoiceApi {

    private final static Logger logger  = getLogger(ScanInvoiceApi.class.getName());

    public static void main(String[] args) {
        Gson gson = new Gson();
        setUpPort(args);
        path("/v1", () -> {
            before("/*", (req, res) -> logger.info("Calling API v1"));
            get("/regularexpressions", GetRegularExpressions::get, gson::toJson);

            post("/regularexpression", "application/xml", PostRegularExpression::post);
            post("/regularexpression", "text/plain", PostRegularExpression::post);

            path("/regularexpression", () -> {
                before("/:id", (req, res) -> logger.info("Calling for existing regular expression"));
                delete("/:id", DelRegularExpression::del);
                put("/:id", PutRegularExpression::put);
                get("/:id", GetRegularExpression::get);
            });
        });
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

}
