package com.lealdidier.invoice.scan;

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
        List<String> data = new ArrayList<>();
        Supplier<MemoryInterpreter> memoryInterpreterSupplier = () -> new MemoryInterpreter(data);
        Supplier<Consumer<String>> verifierSupplier = () -> ((regex) -> assertEquals(regex, data.get(0)));
        return Stream.of(new MemoryInterpreterInvocationContext(memoryInterpreterSupplier, verifierSupplier));
    }
}
