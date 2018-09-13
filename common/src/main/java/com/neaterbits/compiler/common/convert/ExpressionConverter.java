package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;

public abstract class ExpressionConverter implements ExpressionVisitor<ConverterState, Expression> {

}
