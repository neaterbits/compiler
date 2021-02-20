package com.neaterbits.compiler.common.ast.variables;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;

public class NameReference extends VariableReference {

	private final String name;

	public NameReference(Context context, String name) {
		super(context);

		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onNameReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
