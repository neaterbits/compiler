package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.ast.variables.VarName;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;

public final class Resource extends InitializerVariableDeclarationElement {

	private final ASTSingle<VariableModifiers> modifiers;
	
	public Resource(Context context, VariableModifiers modifiers, TypeReference type, VarName name, int numDims, Expression initializer) {
		super(context, type, name, numDims, initializer);
		
		Objects.requireNonNull(modifiers);
		
		this.modifiers = makeSingle(modifiers);
	}
	
	public Resource(VariableModifiers modifiers, InitializerVariableDeclarationElement element) {
		this(element.getContext(), modifiers, element.getTypeReference(), element.getName(), element.getNumDims(), element.getInitializer());
	}

	public VariableModifiers getModifiers() {
		return modifiers.get();
	}

	public VariableDeclaration makeVariableDeclaration() {
		return super.makeVariableDeclaration(modifiers.get());
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(modifiers, recurseMode, visitor);
		
		super.doRecurse(recurseMode, visitor);
	}
}
