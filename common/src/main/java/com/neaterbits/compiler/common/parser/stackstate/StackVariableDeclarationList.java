package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;
import com.neaterbits.compiler.common.parser.VariableModifierSetter;

public final class StackVariableDeclarationList
	extends ListStackEntry<InitializerVariableDeclarationElement>
	implements VariableModifierSetter, TypeReferenceSetter {

	private final List<VariableModifier> modifiers;
	private TypeReference typeReference;

	public StackVariableDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);

		this.modifiers = new ArrayList<>();
	}

	public List<VariableModifier> getModifiers() {
		return modifiers;
	}

	@Override
	public void addModifier(VariableModifier modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	@Override
	public void setTypeReference(TypeReference typeReference) {
		if (this.typeReference != null) {
			throw new IllegalStateException("typeReference already set");
		}
		
		getParseLogger().onStackSetElement(typeReference.toString());

		this.typeReference = typeReference;
	}

	public TypeReference getTypeReference() {
		return typeReference;
	}
}
