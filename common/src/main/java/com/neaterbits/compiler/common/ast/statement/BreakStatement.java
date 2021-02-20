package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
