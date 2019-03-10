package com.neaterbits.compiler.ast.statement;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.util.Context;

public final class BreakStatement extends Statement {

	private final String label;

	public BreakStatement(Context context, String label) {
		super(context);
		
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onBreakStatement(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
