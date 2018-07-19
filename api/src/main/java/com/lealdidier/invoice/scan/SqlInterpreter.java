package com.lealdidier.invoice.scan;

import java.util.List;

public class SqlInterpreter implements Interpreter {
    @Override
    public void use(String regexPattern) {

    }

    @Override
    public void stopUsing(String... regexPatterns) {

    }

    @Override
    public List<String> usedRegularExpressions() {
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
