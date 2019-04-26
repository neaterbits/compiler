package com.neaterbits.compiler.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.VARIABLE_DECLARATION_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onVariableDeclaration(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		doIterate(modifiers, recurseMode, iterator);
		doIterate(declarations, recurseMode, iterator);
	}
}
