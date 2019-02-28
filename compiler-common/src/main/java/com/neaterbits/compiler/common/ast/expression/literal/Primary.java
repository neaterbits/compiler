package com.neaterbits.compiler.common.ast.expression.literal;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Expression;

public abstract class Primary extends Expression {

	public Primary(Context context) {
		super(context);
	}
}
