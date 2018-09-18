package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.ast.variables.VarName;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.StackEntry;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;
import com.neaterbits.compiler.common.parser.VariableModifierSetter;

abstract class BaseStackVariableDeclaration extends StackEntry
	implements VariableModifierSetter, TypeReferenceSetter, VariableNameSetter {
	private final List<VariableModifier> modifiers;
	private TypeReference typeReference;
	private String name;
	private int numDims;

	public BaseStackVariableDeclaration(ParseLogger parseLogger) {
		super(parseLogger);
	
		this.modifiers = new ArrayList<>();
	}
	
	@Override
	public final void addModifier(VariableModifier modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public final List<VariableModifier> getModifiers() {
		return modifiers;
	}

	@Override
	public final void setTypeReference(TypeReference typeReference) {
		Objects.requireNonNull(typeReference);
		
		if (this.typeReference != null) {
			throw new IllegalStateException("typeReference already set: " + typeReference);
		}

		this.typeReference = typeReference;
	}

	@Override
	public final void init(String name, int numDims) {
		Objects.requireNonNull(name);

		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
		this.numDims = numDims;
	}
	
	public final VariableDeclaration makeVariableDeclaration() {
		final VariableDeclaration variableDeclaration = new VariableDeclaration(
				new VariableModifiers(modifiers),
				typeReference,
				new VarName(name),
				numDims);
		
		return variableDeclaration;
	}

	public final String getName() {
		return name;
	}

	public final int getNumDims() {
		return numDims;
	}

	public final TypeReference getTypeReference() {
		return typeReference;
	}
}
