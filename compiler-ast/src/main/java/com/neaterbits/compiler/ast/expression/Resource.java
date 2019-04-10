package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.variables.VarNameDeclaration;
import com.neaterbits.compiler.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.util.Context;

public final class Resource extends InitializerVariableDeclarationElement {

	private final ASTSingle<VariableModifiers> modifiers;
	
	public Resource(Context context, VariableModifiers modifiers, TypeReference type, VarNameDeclaration name, int numDims, Expression initializer) {
		super(context, type, name, numDims, initializer);
		
		Objects.requireNonNull(modifiers);
		
		this.modifiers = makeSingle(modifiers);
	}
	
	public Resource(VariableModifiers modifiers, InitializerVariableDeclarationElement element) {
		this(element.getContext(), modifiers, element.getTypeReference(), element.getNameDeclaration(), element.getNumDims(), element.getInitializer());
	}

	public VariableModifiers getModifiers() {
		return modifiers.get();
	}

	public VariableDeclaration makeVariableDeclaration() {
		return super.makeVariableDeclaration(modifiers.get());
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(modifiers, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
