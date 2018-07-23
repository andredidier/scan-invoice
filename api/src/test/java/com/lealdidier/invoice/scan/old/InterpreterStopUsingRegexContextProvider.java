package com.lealdidier.invoice.scan.old;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InterpreterStopUsingRegexContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext extensionContext) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
        /*
        String[] data = { "regex1", "regex2" };
        List<String> list = new ArrayList<>(Arrays.asList(data));
        Supplier<MemoryInterpreter> memoryInterpreterSupplier = () -> new MemoryInterpreter(list);
        Supplier<Consumer<String>> verifierSupplier = () -> ((regex) -> assertFalse(list.contains(regex)));

        return Stream.of(new MemoryInterpreterInvocationContext(memoryInterpreterSupplier, verifierSupplier, data));
        */
        return null;
    }
}
