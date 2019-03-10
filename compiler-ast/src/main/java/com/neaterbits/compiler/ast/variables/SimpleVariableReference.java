package com.neaterbits.compiler.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.util.Context;

public final class SimpleVariableReference extends VariableReference {
	
	private final VariableDeclaration declaration;

	public SimpleVariableReference(Context context, VariableDeclaration declaration) {
		super(context);

		Objects.requireNonNull(declaration);

		this.declaration = declaration;
	}

	public VariableDeclaration getDeclaration() {
		return declaration;
	}
	
	public VarName getName() {
		return declaration.getName();
	}

	@Override
	public BaseType getType() {
		return declaration.getType();
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onSimpleVariableReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
