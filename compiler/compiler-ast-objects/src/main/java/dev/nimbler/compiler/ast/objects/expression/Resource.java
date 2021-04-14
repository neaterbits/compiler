package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifiers;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.ast.objects.variables.VarNameDeclaration;

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
