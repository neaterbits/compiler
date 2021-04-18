package dev.nimbler.compiler.ast.objects.variables;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifiers;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;

public abstract class VariableDeclarationElement extends BaseASTElement {
	
	private final ASTSingle<VarNameDeclaration> name;
	private final int numDims;
	
	public VariableDeclarationElement(Context context, VarNameDeclaration name, int numDims) {
		super(context);

		Objects.requireNonNull(name);

		this.name = makeSingle(name);
		this.numDims = numDims;
	}

	public VarNameDeclaration getNameDeclaration() {
		return name.get();
	}

	public final VarName getVarName() {
		return name.get().getVarName();
	}
	
	public final String getNameString() {
	    
	    return name.get().getVarName().getName();
	}

	public final int getNumDims() {
		return numDims;
	}

	public final VariableDeclaration makeVariableDeclaration(VariableModifiers modifiers, TypeReference type) {

		final VariableDeclaration variableDeclaration = new VariableDeclaration(
				modifiers,
				type,
				name.get().getVarName(),
				numDims);

		return variableDeclaration;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(name, recurseMode, iterator);
	}
}
