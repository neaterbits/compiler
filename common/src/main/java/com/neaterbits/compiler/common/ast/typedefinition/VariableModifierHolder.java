package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class VariableModifierHolder extends BaseASTElement implements VariableModifier {

	private final VariableModifier delegate;
	
	public VariableModifierHolder(Context context, VariableModifier delegate) {
		super(context);

		Objects.requireNonNull(delegate);

		this.delegate = delegate;
	}

	@Override
	public <T, R> R visit(VariableModifierVisitor<T, R> visitor, T param) {
		return delegate.visit(visitor, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
