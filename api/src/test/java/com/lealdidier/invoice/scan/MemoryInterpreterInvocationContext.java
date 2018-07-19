package com.lealdidier.invoice.scan;

import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class MemoryInterpreterInvocationContext implements TestTemplateInvocationContext {

    private Supplier<MemoryInterpreter> memoryInterpreter;
    private Supplier<Consumer<String>> verifier;
    private String[] initialRegularExpressions;

    public MemoryInterpreterInvocationContext(Supplier<MemoryInterpreter> memoryInterpreter,
                                              Supplier<Consumer<String>> verifier,
                                              String... initialRegularExpressions) {
        this.memoryInterpreter = memoryInterpreter;
        this.verifier = verifier;
        this.initialRegularExpressions = initialRegularExpressions;
    }

    public MemoryInterpreterInvocationContext(Supplier<Consumer<String>> verifier,
                                              List<String> data) {
        this(() -> new MemoryInterpreter(data), verifier, data.toArray(new String[]{}));
    }



    @Override
    public String getDisplayName(int invocationIndex) {
        return String.format("[%d] MemoryInterpreter, initial: %d", invocationIndex, initialRegularExpressions.length);
    }

    @Override
    public List<Extension> getAdditionalExtensions() {

        ParameterResolver mipr = new ParameterResolver() {
            @Override
            public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return parameterContext.getParameter().getType().isAssignableFrom(Interpreter.class);
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

        ParameterResolver initialArray = new ParameterResolver() {
            @Override
            public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return parameterContext.getParameter().getType().equals(String[].class);
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return initialRegularExpressions;
            }
        };


        ParameterResolver initialList = new ParameterResolver() {
            @Override
            public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return parameterContext.getParameter().getType().equals(List.class);
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return new ArrayList<>(Arrays.asList(initialRegularExpressions));
            }
        };
        List<Extension> resolvers = new ArrayList<>(2);
        resolvers.add(mipr);
        resolvers.add(vpr);
        resolvers.add(initialArray);
        resolvers.add(initialList);

        return resolvers;
    }
}
