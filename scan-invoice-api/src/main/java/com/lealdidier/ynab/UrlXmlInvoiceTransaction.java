package com.lealdidier.ynab;

import com.lealdidier.io.XmlUrlInputStreamSupplier;
import com.lealdidier.media.Media;
import com.lealdidier.ynab.nfce.NfceXmlToJson;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

import static com.lealdidier.ynab.TransactionField.*;

public class UrlXmlInvoiceTransaction implements Transaction {
    private final URL url;
    private final Supplier<InputStream> xslStreamSupplier;
    private transient JSONObject jsonObject;

    private UrlXmlInvoiceTransaction(URL url, Supplier<InputStream> xslStreamSupplier) {
        this.url = url;
        this.xslStreamSupplier = xslStreamSupplier;
    }

    public UrlXmlInvoiceTransaction(URL url) {
        this(url, () -> UrlXmlInvoiceTransaction.class.getResourceAsStream("nfce/nfce-pe-to-json.xsl"));
    }

    private String xmlContents() {
        try {
            return IOUtils.toString(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject ynabJsonContents() {
        if (jsonObject == null) {
            jsonObject = new NfceXmlToJson(xslStreamSupplier).compose(new XmlUrlInputStreamSupplier()).apply(url);
        }
        return jsonObject;
    }

    @Override
    public <E extends Exception> Media<E> addTo(Media<E> media) {
        return media.addField(URL, () -> url)
                .addField(XML, this::xmlContents)
                .addField(JSON, this::ynabJsonContents);
    }

}
