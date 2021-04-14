package com.neaterbits.compiler.emit.base;

import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.ExpressionEmitter;

public abstract class BaseExpressionEmitter<T extends EmitterState> extends BaseEmitter<T> implements ExpressionEmitter<T> {

	protected final void emitExpression(Expression expression, T param) {
		expression.visit(this, param);
	}
}
