package dev.nimbler.compiler.ast.objects.statement;

import org.jutils.parse.context.Context;

public abstract class ConditionStatement extends Statement {

	protected ConditionStatement(Context context) {
		super(context);
	}
}
