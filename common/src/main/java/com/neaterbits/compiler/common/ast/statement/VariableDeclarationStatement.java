package com.neaterbits.compiler.common.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.ast.variables.VariableDeclarationElement;

public final class VariableDeclarationStatement extends Statement {

	private final VariableModifiers modifiers;
	private final List<VariableDeclarationElement> declarations;
	
	public VariableDeclarationStatement(
			Context context,
			VariableModifiers modifiers,
			List<VariableDeclarationElement> declarations) {
		
		super(context);
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(declarations);

		this.modifiers = modifiers;
		this.declarations = declarations;
	}

	public VariableModifiers getModifiers() {
		return modifiers;
	}

	public List<VariableDeclarationElement> getDeclarations() {
		return declarations;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onVariableDeclaration(this, param);
	}
}
