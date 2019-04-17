package com.neaterbits.compiler.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class VariableDeclarationElement extends BaseASTElement {
	
	private final ASTSingle<TypeReference> type;
	private final ASTSingle<VarNameDeclaration> name;
	private final int numDims;
	
	public VariableDeclarationElement(Context context, TypeReference type, VarNameDeclaration name, int numDims) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(name);

		this.type = makeSingle(type);
		this.name = makeSingle(name);
		this.numDims = numDims;
	}

	public final TypeReference getTypeReference() {
		return type.get();
	}

	public VarNameDeclaration getNameDeclaration() {
		return name.get();
	}

	public final VarName getVarName() {
		return name.get().getVarName();
	}

	public final int getNumDims() {
		return numDims;
	}

	public final VariableDeclaration makeVariableDeclaration(VariableModifiers modifiers) {

		final VariableDeclaration variableDeclaration = new VariableDeclaration(
				modifiers,
				type.get(),
				name.get().getVarName(),
				numDims);

		return variableDeclaration;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(type, recurseMode, iterator);
		doIterate(name, recurseMode, iterator);
	}
}
