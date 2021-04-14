package dev.nimbler.compiler.convert;

import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;

public interface ExpressionConverter<T extends ConverterState<T>> extends ExpressionVisitor<T, Expression> {

}
