package dev.nimbler.compiler.parser.recursive.cached.expressions;

import dev.nimbler.compiler.types.operator.Operator;

public interface LanguageOperatorPrecedence {

    int getPrecedence(Operator operator);
}
