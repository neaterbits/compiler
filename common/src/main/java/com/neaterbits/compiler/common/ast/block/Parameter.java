package com.neaterbits.compiler.common.ast.block;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.Name;

public final class Parameter {

	private final TypeReference type;
	private final ParameterName name;

	public Parameter(TypeReference type, ParameterName name) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		
		Name.check(name.getName());

		this.type = type;
		this.name = name;
	}
	
	public TypeReference getType() {
		return type;
	}
	
	public ParameterName getName() {
		return name;
	}
}
