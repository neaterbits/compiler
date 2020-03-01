package com.neaterbits.compiler.ast.objects.statement;

import com.neaterbits.compiler.util.Context;

public abstract class ConditionStatement extends Statement {

	protected ConditionStatement(Context context) {
		super(context);
	}
}
