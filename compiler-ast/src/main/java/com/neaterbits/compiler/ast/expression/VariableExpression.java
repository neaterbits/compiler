package com.neaterbits.compiler.ast.expression;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.util.Context;

public final class VariableExpression extends Primary {

	private final ASTSingle<VariableReference> reference;

	public VariableExpression(Context context, VariableReference reference) {
		super(context);
		
		this.reference = makeSingle(reference);
	}

	public final VariableReference getReference() {
		return reference.get();
	}
	
	@Override
	public TypeReference getType() {
		return reference.get().getType();
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
