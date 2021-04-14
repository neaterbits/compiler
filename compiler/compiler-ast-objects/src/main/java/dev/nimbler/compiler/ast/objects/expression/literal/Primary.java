package dev.nimbler.compiler.ast.objects.expression.literal;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.Expression;

public abstract class Primary extends Expression {

	public Primary(Context context) {
		super(context);
	}
}
