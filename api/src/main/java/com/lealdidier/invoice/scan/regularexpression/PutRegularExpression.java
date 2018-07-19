package com.lealdidier.invoice.scan.regularexpression;

import spark.Request;
import spark.Response;

public class PutRegularExpression {
    public static Object put(Request req, Response res) {
        return PostRegularExpression.post(req, res);
    }
}
