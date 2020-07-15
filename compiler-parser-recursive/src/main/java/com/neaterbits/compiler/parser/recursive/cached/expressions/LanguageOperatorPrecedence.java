package com.neaterbits.compiler.parser.recursive.cached.expressions;

import com.neaterbits.compiler.util.operator.Operator;

public interface LanguageOperatorPrecedence {

    int getPrecedence(Operator operator);
}
