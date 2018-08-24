package com.lealdidier.ynab;

import com.lealdidier.media.Media;

import java.io.IOException;

public interface Transaction {
    <E extends Exception> Media<E> addTo(Media<E> media);
}
