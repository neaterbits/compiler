package com.neaterbits.compiler.common.emit.base;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.ExpressionEmitter;

public abstract class BaseExpressionEmitter<T extends EmitterState> extends BaseEmitter<T> implements ExpressionEmitter<T> {

	protected final void emitExpression(Expression expression, T param) {
		expression.visit(this, param);
	}
}
