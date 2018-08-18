package com.lealdidier.ynab;

import org.json.JSONObject;

public interface Transaction {
    JSONObject createBasicPayload();
}
