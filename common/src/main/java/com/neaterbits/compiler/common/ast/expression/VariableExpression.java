package com.neaterbits.compiler.common.ast.expression;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.variables.VariableReference;

public final class VariableExpression extends Expression {

	private final ASTSingle<VariableReference> reference;

	public VariableExpression(Context context, VariableReference reference) {
		super(context);
		
		this.reference = makeSingle(reference);
	}

	public final VariableReference getReference() {
		return reference.get();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onVariable(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(reference, recurseMode, iterator);
	}
}
