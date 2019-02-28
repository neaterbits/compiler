package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class ConstructorModifierHolder extends BaseASTElement implements ConstructorModifier {

	private final ConstructorModifier modifier;
	
	public ConstructorModifierHolder(Context context, ConstructorModifier modifier) {
		super(context);

		Objects.requireNonNull(modifier);

		this.modifier = modifier;
	}

	public ConstructorModifier getModifier() {
		return modifier;
	}
	
	@Override
	public <T, R> R visit(ConstructorModifierVisitor<T, R> visitor, T param) {
		return modifier.visit(visitor, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
