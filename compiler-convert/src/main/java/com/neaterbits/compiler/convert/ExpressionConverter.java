package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.ExpressionVisitor;

public interface ExpressionConverter<T extends ConverterState<T>> extends ExpressionVisitor<T, Expression> {

}
