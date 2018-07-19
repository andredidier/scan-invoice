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

@DisplayName("Interpreter implementations test")
public class InterpreterTest {

    @TestTemplate
    @ExtendWith(InterpreterUseRegexContextProvider.class)
    public void testUseRegularExpressionNew(Interpreter interpreter, Consumer<String> verifier) {
        String regex = "regex1";
        interpreter.use(regex);
        verifier.accept(regex);
    }

    @DisplayName("Register regular expression for later use")
    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("interpretersForUseTest")
    public void testUseRegularExpression(Supplier<Interpreter> interpreter, Consumer<String> verifier, String regex) {
        interpreter.get().use(regex);
        verifier.accept(regex);
    }

    @DisplayName("Remove regular expression")
    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("interpretersForStopUsingTest")
    public void testStopUsingRegularExpression(Supplier<Interpreter> interpreter, Consumer<String> verifier, String regex) {
        interpreter.get().stopUsing(regex);
        verifier.accept(regex);
    }

    @DisplayName("All regular expressions")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("interpretersForAllRegularExpressionsTest")
    public void testGetAllRegularExpressions(Supplier<Interpreter> interpreter, Consumer<List<String>> verifier) {
        verifier.accept(interpreter.get().usedRegularExpressions());
    }

    public static Stream<? extends Arguments> interpretersForAllRegularExpressionsTest() {
        List<String> regexs = new ArrayList<>();
        regexs.add("regex1");
        regexs.add("regex2");

        Map<Supplier<Interpreter>, Consumer<List<String>>> verifiers = new HashMap<>();
        verifiers.put(() -> new MemoryInterpreter(regexs), (rs) -> assertEquals(regexs, rs));
        verifiers.put(() -> new SqlInterpreter(), (rs) -> fail("Not implemented yet"));

        return verifiers.keySet().stream()
                .map((interpreter) -> Stream.of(Arguments.of(interpreter, verifiers.get(interpreter))))
                .flatMap((args)->args);
    }

    public static Stream<? extends Arguments> interpretersForUseTest() {
        List<String> regexs = new ArrayList<>();
        Consumer<String> regexsListVerifier = (r) -> assertEquals(r, regexs.get(0));
        Consumer<String> sqlVerifier = (r) -> fail("Not implemented yet");

        Map<Supplier<Interpreter>, Consumer<String>> verifiers = new HashMap<>();
        verifiers.put(() -> new MemoryInterpreter(regexs), regexsListVerifier);
        verifiers.put(() -> new SqlInterpreter(), sqlVerifier);
        return interpretersForTest(verifiers);
    }
    public static Stream<? extends Arguments> interpretersForStopUsingTest() {
        List<String> regexs = new ArrayList<>();
        regexs.add("regex1");

        Consumer<String> regexsListVerifier = (r) -> assertTrue(regexs.isEmpty());
        Consumer<String> sqlVerifier = (r) -> fail("Not implemented yet");

        Map<Supplier<Interpreter>, Consumer<String>> verifiers = new HashMap<>();
        verifiers.put(() -> new MemoryInterpreter(regexs), regexsListVerifier);
        verifiers.put(() -> new SqlInterpreter(), sqlVerifier);
        return interpretersForTest(verifiers);
    }

    public static <T> Stream<? extends Arguments> interpretersForTest(Map<Supplier<Interpreter>, Consumer<T>> verifiers) {
        Function<Supplier<Interpreter>, Stream<Arguments>> params =
                (interpreter) -> Stream.of(
                        Arguments.of(interpreter, verifiers.get(interpreter), "regex1"),
                        Arguments.of(interpreter, verifiers.get(interpreter), "regex2")
                );

        return verifiers.keySet().stream().map(params).flatMap((args)->args);
    }
}
