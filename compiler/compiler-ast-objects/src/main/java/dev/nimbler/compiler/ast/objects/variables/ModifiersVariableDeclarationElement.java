package dev.nimbler.compiler.ast.objects.variables;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifiers;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ModifiersVariableDeclarationElement extends VariableDeclarationElement {

	private final ASTSingle<VariableModifiers> modifiers;
	private final ASTSingle<TypeReference> type;

	public ModifiersVariableDeclarationElement(
	        Context context,
	        VariableModifiers modifiers,
	        TypeReference type,
	        VarNameDeclaration name,
	        int numDims) {
	    
		super(context, name, numDims);

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.MODIFIERS_VARIABLE_DECLARATION_ELEMENT;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
