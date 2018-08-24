package com.lealdidier.invoice.scan;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnvironmentSupplier implements Supplier<String> {

    private String envName;
    private String defaultValue;

    public EnvironmentSupplier(String envName, String defaultValue) {
        this.envName = envName;
    }

    @Override
    public String get() {
        String systemId = System.getenv(envName);
        if (systemId == null) {
            return defaultValue;
        }
        return systemId;

    }
}
