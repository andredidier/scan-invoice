package com.lealdidier.ynab;

import com.lealdidier.media.Media;
import org.json.JSONObject;

import java.io.IOException;

public interface Transaction {
    <T> void saveTo(Media<T> media) throws IOException;
}
