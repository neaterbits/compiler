package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;

public interface ExpressionConverter<T extends ConverterState<T>> extends ExpressionVisitor<T, Expression> {

}
