package com.lealdidier.api;

import com.lealdidier.media.ExceptionConsumer;
import com.lealdidier.media.Media;
import com.lealdidier.media.MediaFieldConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static com.lealdidier.api.ExceptionFieldName.Message;
import static com.lealdidier.api.ExceptionFieldName.PartValidationMessage;
import static com.lealdidier.api.ExceptionFieldName.StackTrace;

public class PartValidationException extends RuntimeException implements MediaFieldConfiguration {
    private final PartMessage[] partMessages;

    public PartValidationException(String message, Exception e, PartMessage... partMessages) {
        super(message, e);
        if (partMessages == null || partMessages.length == 0) {
            throw new IllegalArgumentException("partMessages");
        }
        this.partMessages = partMessages;
    }
    public PartValidationException(Exception e, PartMessage... partMessages) {
        this(null, e, partMessages);
    }
    public PartValidationException(PartMessage... partMessages) {
        this(null, null, partMessages);
    }

    @Override
    public <E extends Exception> Media<E> addTo(Media<E> media) {
        return media
                .addField(Message, this::getMessage)
                .addField(PartValidationMessage, this::formatPartMessages)
                .addField(StackTrace, this::stackTrace);
    }

    private List<String> formatPartMessages() {
        List<String> ms = new LinkedList<>();
        Media<RuntimeException> m = new Media<RuntimeException>()
                .addMapping(PartMessageField.Message, (ExceptionConsumer<RuntimeException, String>) ms::add);
        for(PartMessage pm : partMessages) {
            pm.addTo(m);
        }
        return ms;
    }

    private String stackTrace() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            this.printStackTrace(ps);
            return new String(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
