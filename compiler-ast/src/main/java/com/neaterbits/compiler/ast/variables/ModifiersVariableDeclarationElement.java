package com.neaterbits.compiler.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

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
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
