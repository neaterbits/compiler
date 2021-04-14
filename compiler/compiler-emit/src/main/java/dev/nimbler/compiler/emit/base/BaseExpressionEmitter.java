package dev.nimbler.compiler.emit.base;

import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.ExpressionEmitter;

public abstract class BaseExpressionEmitter<T extends EmitterState> extends BaseEmitter<T> implements ExpressionEmitter<T> {

	protected final void emitExpression(Expression expression, T param) {
		expression.visit(this, param);
	}
}
