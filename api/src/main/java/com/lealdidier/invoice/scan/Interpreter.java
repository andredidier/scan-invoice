package com.lealdidier.invoice.scan;

import java.util.List;

public interface Interpreter {

    void use(String regexPattern);
    void stopUsing(String... regexPatterns);
    List<String> usedRegularExpressions();
}
