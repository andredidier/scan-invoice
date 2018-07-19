package com.lealdidier.invoice.scan.regularexpression;

import spark.Request;
import spark.Response;

public class PostRegularExpression {
    public static Object post(Request req, Response res) {
        return "{ \"id\": \"xy\", \"regular-expression\":\"" + req.body() + "\"}";
    }
}
