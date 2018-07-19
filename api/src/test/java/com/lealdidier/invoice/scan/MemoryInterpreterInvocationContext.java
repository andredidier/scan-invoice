package com.lealdidier.invoice.scan;

import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class MemoryInterpreterInvocationContext implements TestTemplateInvocationContext {

    private Supplier<MemoryInterpreter> memoryInterpreter;
    private Supplier<Consumer<String>> verifier;

    public MemoryInterpreterInvocationContext(Supplier<MemoryInterpreter> memoryInterpreter,
                                              Supplier<Consumer<String>> verifier) {
        this.memoryInterpreter = memoryInterpreter;
        this.verifier = verifier;
    }

    @Override
    public String getDisplayName(int invocationIndex) {
        return String.format("[%d] MemoryInterpreter", invocationIndex);
    }

    @Override
    public List<Extension> getAdditionalExtensions() {

        ParameterResolver mipr = new ParameterResolver() {
            @Override
            public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return parameterContext.getParameter().getType().equals(MemoryInterpreter.class);
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return memoryInterpreter.get();
            }
        };
        ParameterResolver vpr = new ParameterResolver() {
            @Override
            public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return parameterContext.getParameter().getType().equals(Consumer.class);
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return verifier.get();
            }
        };

        List<Extension> resolvers = new ArrayList<>(2);
        resolvers.add(mipr);
        resolvers.add(vpr);

        return resolvers;
    }
}
