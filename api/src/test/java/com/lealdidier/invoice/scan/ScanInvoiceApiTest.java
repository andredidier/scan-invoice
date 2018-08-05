package com.lealdidier.invoice.scan;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.lealdidier.io.ResourceURIInput;
import com.lealdidier.io.ResourceUrlInput;
import com.lealdidier.io.URIContentsStringInput;
import com.lealdidier.io.InputStreamStringInput;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScanInvoiceApiTest {

    private static WireMockServer wm;
    private static ScanInvoiceApi api;

    @BeforeAll
    public static void startWiremock() {
        wm = new WireMockServer(8888);
        wm.start();
    }

    @BeforeAll
    private static void startApi() {
        api = new ScanInvoiceApi(8080);
        api.start();
    }

    @AfterAll
    public static void stopWiremock() {
        wm.stop();
    }

    @AfterAll
    private static void stopApi() {
        api.stop();
    }


    @AfterEach
    public void clear() {
        wm.resetAll();
    }

    @Test
    public void testInterpret() throws URISyntaxException, IOException {
        wm.addStubMapping(get(urlEqualTo("/invoice1"))
                .willReturn(aResponse().withBody(
                        new URIContentsStringInput(new ResourceURIInput("/invoice1.xml", getClass())).read()))
                .build()
        );

        Client client = ClientBuilder.newClient();
        WebTarget t = client.target("http://localhost:8080/v1").path("/interpreter");
        JSONObject j = new JSONObject(
                t.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.text("http://localhost:8888/invoice1"),
                        String.class));

        wm.verify(getRequestedFor(urlEqualTo("/invoice1")));
/*
        JSONObject expected = new JSONObject(
                new InputStreamStringInput(new ResourceUrlInput("/json1.js", getClass()), "UTF-8").read()
        );
        assertEquals(expected.toString(), j.toString());
        */

    }
}
