package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class InterfaceModifierHolder extends BaseASTElement implements InterfaceModifier {

	private final InterfaceModifier modifier;

	public InterfaceModifierHolder(Context context, InterfaceModifier modifier) {
		super(context);

		Objects.requireNonNull(modifier);

		this.modifier = modifier;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}

	@Override
	public <T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param) {
		return modifier.visit(visitor, param);
	}
}
