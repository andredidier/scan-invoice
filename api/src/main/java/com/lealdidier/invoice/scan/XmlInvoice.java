package com.lealdidier.invoice.scan;

import com.lealdidier.io.Input;
import org.json.JSONObject;
import org.w3c.dom.Document;
import spark.Response;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class XmlInvoice implements Invoice {

    private final static Logger logger = Logger.getLogger(XmlInvoice.class.getName());

    private final Input<Document> xmlInput;
    private final Input<Document> xslInput;

    public XmlInvoice(Input<Document> xmlInput, Input<Document> xslInput) {
        this.xmlInput = xmlInput;
        this.xslInput = xslInput;
    }

    @Override
    public void respondWithJsonBody(Response response) throws IOException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Transformer t = TransformerFactory.newDefaultInstance().newTransformer(new DOMSource(xslInput.read()));
            t.transform(new DOMSource(xmlInput.read()), new StreamResult(baos));
            response.body(new JSONObject(new String(baos.toByteArray())).toString());
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }
}
