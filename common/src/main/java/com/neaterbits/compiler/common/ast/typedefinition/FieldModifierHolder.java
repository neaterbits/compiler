package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class FieldModifierHolder extends BaseASTElement {

	private final FieldModifier modifier;
	
	public FieldModifierHolder(Context context, FieldModifier modifier) {
		super(context);
		
		Objects.requireNonNull(modifier);
		
		this.modifier = modifier;
	}

	public FieldModifier getModifier() {
		return modifier;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}

