package com.lealdidier.ynab;

import com.lealdidier.io.AsciiHash;
import com.lealdidier.io.XmlUrlInputStreamSupplier;
import com.lealdidier.media.Media;
import com.lealdidier.ynab.nfce.NfceXmlToJson;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

public class UrlXmlInvoiceTransaction implements Transaction {
    private final URL url;
    private final Supplier<InputStream> xslStreamSupplier;
    private final String urlHash;
    private transient JSONObject jsonObject;

    public UrlXmlInvoiceTransaction(URL url, Supplier<InputStream> xslStreamSupplier) {
        this.url = url;
        this.urlHash = new AsciiHash().apply(url.toString());
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

    private String contentsHash() {
        return new AsciiHash().apply(ynabJsonContents().toString());
    }

    @Override
    public <T> void saveTo(Media<T> media) throws IOException {
        media.addField("url", () -> url)
                .addField("xml", this::xmlContents)
                .addField("ynabJson", this::ynabJsonContents)
                .addField("urlHash", () -> urlHash)
                .addField("ynabJsonHash", this::contentsHash)
                .writeFields();
    }

}
