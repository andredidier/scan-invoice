package com.lealdidier.invoice.scan;

import com.lealdidier.io.Media;
import org.json.JSONObject;

import java.io.IOException;

public interface Invoice {
    void print(Media media);
}
