package com.neaterbits.compiler.common.ast.variables;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;

public final class FieldAccessReference extends VariableReference {

	public FieldAccessReference(Context context) {
		super(context);
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onFieldAccessReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		// TODO Auto-generated method stub
		
	}
}


