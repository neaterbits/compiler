package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.objects.variables.VarNameDeclaration;
import com.neaterbits.compiler.util.Context;

public final class Resource extends InitializerVariableDeclarationElement {

	private final ASTSingle<VariableModifiers> modifiers;
	private final ASTSingle<TypeReference> type;
	
	public Resource(Context context, VariableModifiers modifiers, TypeReference type, VarNameDeclaration name, int numDims, Expression initializer) {
		super(context, name, numDims, initializer);
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(type);
		
		this.modifiers = makeSingle(modifiers);
		this.type = makeSingle(type);
	}

	public VariableModifiers getModifiers() {
		return modifiers.get();
	}

	public TypeReference getTypeReference() {
        return type.get();
    }

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(modifiers, recurseMode, iterator);
		doIterate(type, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
