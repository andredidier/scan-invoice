package com.lealdidier.invoice.scan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("Interpreter implementations test")
public class InterpreterTest {

    @DisplayName("Register regular expression for later use")
    @TestTemplate
    @ExtendWith(InterpreterUseRegexContextProvider.class)
    public void testUseRegularExpression(Interpreter interpreter, Consumer<String> verifier) {
        String regex = "mynewregex";
        interpreter.use(regex);
        verifier.accept(regex);
    }

    @DisplayName("Register an existing regular expressions")
    @TestTemplate
    @ExtendWith(InterpreterUseRegexContextProvider.class)
    public void testUseExistingRegularExpression(Interpreter interpreter, Consumer<String> verifier, List<String> initialData) {
        if (initialData.isEmpty()) {
            return;
        }
        String regex = initialData.get(0);
        interpreter.use(regex);
        verifier.accept(regex);
    }

    @DisplayName("Remove regular expression")
    @TestTemplate
    @ExtendWith(InterpreterStopUsingRegexContextProvider.class)
    public void testStopUsingRegularExpression(Interpreter interpreter, Consumer<String> verifier,
                                                  String... regularExpressions) {
        assumeTrue(regularExpressions.length > 0);
        String regex = regularExpressions[0];
        interpreter.stopUsing(regex);
        verifier.accept(regex);
    }

    //@DisplayName("All regular expressions")
    //@TestTemplate
    //@ExtendWith(InterpreterGetaAllRegexContextProvider.class)
    public void testGetAllRegularExpressionsNew(Interpreter interpreter, Consumer<String> verifier,
                                                  String... regularExpressions) {
        String regex = regularExpressions[0];
        interpreter.stopUsing(regex);
        verifier.accept(regex);
    }
}
