package com.neaterbits.compiler.common.ast.expression.literal;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.expression.Expression;

public abstract class Literal extends Expression {

	public Literal(Context context) {
		super(context);
	}

	@Override
	public final void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
