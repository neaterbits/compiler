package com.neaterbits.compiler.ast.objects.expression.literal;

import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.util.parse.context.Context;

public abstract class Primary extends Expression {

	public Primary(Context context) {
		super(context);
	}
}
