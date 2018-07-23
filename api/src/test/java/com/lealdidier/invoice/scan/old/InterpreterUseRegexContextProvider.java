package com.lealdidier.invoice.scan.old;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterUseRegexContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext extensionContext) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
        List<String> data1 = new ArrayList<>();
        Supplier<Consumer<String>> verifierSupplier1 = () -> ((regex) -> {
            assertEquals(1, data1.size());
            assertEquals(regex, data1.get(0));
        });

        List<String> data2 = new ArrayList<>();
        data2.add("regex1");
        data2.add("regex2");
        List<String> initialData2 = new ArrayList<>(data2);
        Supplier<Consumer<String>> verifierSupplier2 = () -> ((regex) -> {
            if (initialData2.contains(regex)) {
                assertEquals(2, data2.size());
                assertEquals(regex, data2.get(data2.indexOf(regex)));
            } else {
                assertEquals(3, data2.size());
                assertEquals(regex, data2.get(2));
            }
        });

/*
        return Stream.of(new MemoryInterpreterInvocationContext(verifierSupplier1, data1),
                new MemoryInterpreterInvocationContext(verifierSupplier2, data2));
                */
        return null;
    }
}
