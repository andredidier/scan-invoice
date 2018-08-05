package com.lealdidier.io;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DocumentInput implements Input<Document> {

    private final URL url;

    public DocumentInput(URL url) {
        this.url = url;
    }

    @Override
    public Document read() throws IOException {
        try(InputStream is = url.openStream()) {
            return DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().parse(is);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }
}
