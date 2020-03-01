package com.neaterbits.compiler.ast.objects.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ModifiersVariableDeclarationElement extends VariableDeclarationElement {

	private final ASTSingle<VariableModifiers> modifiers;

	public ModifiersVariableDeclarationElement(Context context, VariableModifiers modifiers, TypeReference type, VarNameDeclaration name, int numDims) {
		super(context, type, name, numDims);

		Objects.requireNonNull(modifiers);

		this.modifiers = makeSingle(modifiers);
	}

	public VariableModifiers getModifiers() {
		return modifiers.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.MODIFIERS_VARIABLE_DECLARATION_ELEMENT;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
