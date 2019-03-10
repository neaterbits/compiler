package com.neaterbits.compiler.ast.expression.literal;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.util.Context;

public abstract class Primary extends Expression {

	public Primary(Context context) {
		super(context);
	}
}
