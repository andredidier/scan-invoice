package com.lealdidier.api;

import com.lealdidier.media.Media;
import com.lealdidier.media.MediaFieldConfiguration;

import java.util.ResourceBundle;

import static com.lealdidier.api.PartMessageField.Message;
import static com.lealdidier.api.PartMessageField.MessageCode;
import static com.lealdidier.api.PartMessageField.Part;

public class PartMessage implements MediaFieldConfiguration {
    private final Part part;
    private final String messageCode;

    public PartMessage(Part part, String messageCode) {
        this.part = part;
        this.messageCode = messageCode;
    }

    private String formatMessage() {
        final ResourceBundle bundle = ResourceBundle.getBundle("PartMessages");
        return String.format(bundle.getString(messageCode), part.toString());
    }

    @Override
    public <E extends Exception> Media<E> addTo(Media<E> media) {
        return media.addField(Part, part::toString)
                .addField(MessageCode, () -> this.messageCode)
                .addField(Message, this::formatMessage);
    }
}
