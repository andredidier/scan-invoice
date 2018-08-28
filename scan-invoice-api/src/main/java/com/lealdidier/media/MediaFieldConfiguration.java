package com.lealdidier.media;

public interface MediaFieldConfiguration {
    default <E extends Exception> Media<E> addTo(Media<E> media) {
        return media;
    }
}
