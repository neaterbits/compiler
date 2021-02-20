package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;

public final class ModifiersVariableDeclarationElement extends VariableDeclarationElement {

	private final ASTSingle<VariableModifiers> modifiers;

	public ModifiersVariableDeclarationElement(Context context, VariableModifiers modifiers, TypeReference type, VarName name, int numDims) {
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
