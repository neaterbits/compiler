package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.parser.StackEntry;
import com.neaterbits.compiler.ast.parser.TypeReferenceSetter;
import com.neaterbits.compiler.ast.parser.VariableModifierSetter;
import com.neaterbits.compiler.ast.typedefinition.VariableModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.VarName;
import com.neaterbits.compiler.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

abstract class BaseStackVariableDeclaration extends StackEntry
	implements VariableModifierSetter, TypeReferenceSetter, VariableNameSetter {
	private final List<VariableModifierHolder> modifiers;
	private TypeReference typeReference;
	private String name;
	private int numDims;

	BaseStackVariableDeclaration(ParseLogger parseLogger) {
		super(parseLogger);
	
		this.modifiers = new ArrayList<>();
	}
	
	@Override
	public final void addModifier(VariableModifierHolder modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public final List<VariableModifierHolder> getModifiers() {
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
	
	public final VariableDeclaration makeVariableDeclaration(Context context) {
		final VariableDeclaration variableDeclaration = new VariableDeclaration(
				new VariableModifiers(context, modifiers),
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
