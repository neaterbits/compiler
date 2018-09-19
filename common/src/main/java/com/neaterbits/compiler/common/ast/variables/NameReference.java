package com.neaterbits.compiler.common.ast.variables;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
