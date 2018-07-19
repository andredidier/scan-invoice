package com.lealdidier.invoice.scan.regularexpression;

import spark.Request;
import spark.Response;

public class GetRegularExpression {

    public static Object get(Request req, Response res) {
        return PostRegularExpression.post(req, res);
    }
}
