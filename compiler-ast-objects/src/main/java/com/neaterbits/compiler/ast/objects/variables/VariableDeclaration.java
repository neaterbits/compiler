package com.neaterbits.compiler.ast.objects.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;

public final class VariableDeclaration {

	private final VariableModifiers modifiers;
	private final TypeReference type;
	private final VarName name;
	private final int numDims;

	public VariableDeclaration(VariableModifiers modifiers, TypeReference type, VarName name, int numDims) {
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
		this.numDims = numDims;
	}

	public VariableModifiers getModifiers() {
		return modifiers;
	}

	public TypeReference getTypeReference() {
		return type;
	}
	
	/*
	public BaseType getType() {
		return getTypeReference().getType();
	}
	*/

	public VarName getName() {
		return name;
	}

	public int getNumDims() {
		return numDims;
	}

	@Override
	public String toString() {
		
		final StringBuilder sb = new StringBuilder();
		
		if (!modifiers.getModifiers().isEmpty()) {
			modifiers.getModifiers().foreachWithIndex((modifier, index) -> {
				sb.append(modifier.getClass().getSimpleName());
				
				sb.append(' ');
			});
		}
		
		sb.append(type.getDebugName()).append(' ').append(name.getName());
		
		for (int i = 0; i < numDims; ++ i) {
			sb.append("[]");
		}

		return sb.toString();
	}
}
