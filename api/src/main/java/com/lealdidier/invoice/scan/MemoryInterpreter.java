package com.lealdidier.invoice.scan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MemoryInterpreter implements Interpreter {

    private final List<String> regularExpressions;

    public MemoryInterpreter(List<String> regularExpressions) {
        this.regularExpressions = regularExpressions;
    }

    public MemoryInterpreter() {
        this(new LinkedList<>());
    }

    @Override
    public void use(String regexPattern) {
        if (regularExpressions.contains(regexPattern)) {
            return;
        }
        regularExpressions.add(regexPattern);
    }

    @Override
    public void stopUsing(String... regexPattern) {
        regularExpressions.removeAll(Arrays.asList(regexPattern));
    }

    @Override
    public List<String> usedRegularExpressions() {
        return new ArrayList<>(regularExpressions);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
