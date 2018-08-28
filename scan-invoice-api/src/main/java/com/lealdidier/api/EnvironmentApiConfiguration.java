package com.lealdidier.api;

import spark.Service;

import static spark.Spark.port;

public class EnvironmentApiConfiguration implements ApiConfiguration {

    private final String portEnvName;

    public EnvironmentApiConfiguration(String portEnvName) {
        this.portEnvName = portEnvName;
    }

    private void setPort() {
        String v = System.getenv(portEnvName);

        if (v != null) {
            port(Integer.parseInt(v));
        }
    }

    @Override
    public void use() {
        setPort();
    }
}
