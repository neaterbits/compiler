package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class InterfaceMethodModifierHolder extends BaseASTElement implements InterfaceMethodModifier {

	private final InterfaceMethodModifier delegate;

	public InterfaceMethodModifierHolder(Context context, InterfaceMethodModifier delegate) {
		super(context);

		Objects.requireNonNull(delegate);

		this.delegate = delegate;
	}

	public InterfaceMethodModifier getModifier() {
		return delegate;
	}

	@Override
	public <T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param) {
		return delegate.visit(visitor, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
