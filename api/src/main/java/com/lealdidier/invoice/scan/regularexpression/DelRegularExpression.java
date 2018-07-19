package com.lealdidier.invoice.scan.regularexpression;

import spark.Request;
import spark.Response;

public class DelRegularExpression {
    public static Object del(Request req, Response res) {
        return PostRegularExpression.post(req, res);
    }
}
