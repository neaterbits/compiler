package com.neaterbits.compiler.ast.objects.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class VariableDeclarationStatement extends Statement {

	private final ASTSingle<VariableModifiers> modifiers;
	private final ASTSingle<TypeReference> typeReference;
	private final ASTList<InitializerVariableDeclarationElement> declarations;
	
	public VariableDeclarationStatement(
			Context context,
			VariableModifiers modifiers,
			TypeReference typeReference,
			List<InitializerVariableDeclarationElement> declarations) {
		
		super(context);
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(typeReference);
		Objects.requireNonNull(declarations);

		this.modifiers = makeSingle(modifiers);
		this.typeReference = makeSingle(typeReference);
		this.declarations = makeList(declarations);
	}

	public VariableModifiers getModifiers() {
		return modifiers.get();
	}

	public TypeReference getTypeReference() {
        return typeReference.get();
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
