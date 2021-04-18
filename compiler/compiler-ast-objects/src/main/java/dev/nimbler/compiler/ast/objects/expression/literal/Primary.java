package dev.nimbler.compiler.ast.objects.expression.literal;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.Expression;

public abstract class Primary extends Expression {

	public Primary(Context context) {
		super(context);
	}
}
