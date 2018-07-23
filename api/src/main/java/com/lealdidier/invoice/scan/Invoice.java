package com.lealdidier.invoice.scan;

import org.json.JSONObject;

import java.io.IOException;

public interface Invoice {
    JSONObject toJson() throws IOException;
}
