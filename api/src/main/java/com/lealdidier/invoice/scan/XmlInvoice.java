package com.lealdidier.invoice.scan;

import org.json.JSONObject;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Logger;

public class XmlInvoice implements Invoice {

    private final static Logger logger = Logger.getLogger(XmlInvoice.class.getName());

    private final Input<Source> xmlInput;
    private final Input<Transformer> xsltInput;

    public XmlInvoice(Input<Source> xmlInput, Input<Transformer> xsltInput) {
        this.xmlInput = xmlInput;
        this.xsltInput = xsltInput;
    }

    @Override
    public JSONObject toJson() throws IOException {
        return toJson(xsltInput.read(), xmlInput.read());
    }

    private JSONObject toJson(Transformer transformer, Source source) throws IOException {
        try (StringWriter sw = new StringWriter()) {
            StreamResult result = new StreamResult(sw);
            logger.fine(() -> "Transforming XML input to JSON using XSLT");
            transformer.transform(source, result);
            logger.fine(() -> "Returning json object");
            return new JSONObject(sw.getBuffer().toString());
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }

}
