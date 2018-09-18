package com.neaterbits.compiler.common.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;

public final class VariableDeclarationStatement extends Statement {

	private final ASTSingle<VariableModifiers> modifiers;
	private final ASTList<InitializerVariableDeclarationElement> declarations;
	
	public VariableDeclarationStatement(
			Context context,
			VariableModifiers modifiers,
			List<InitializerVariableDeclarationElement> declarations) {
		
		super(context);
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(declarations);

		this.modifiers = makeSingle(modifiers);
		this.declarations = makeList(declarations);
	}

	public VariableModifiers getModifiers() {
		return modifiers.get();
	}

	public ASTList<InitializerVariableDeclarationElement> getDeclarations() {
		return declarations;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onVariableDeclaration(this, param);
	}
}
