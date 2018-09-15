package com.neaterbits.compiler.common.ast.block;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.Name;

public final class Parameter {

	private final TypeReference type;
	private final String name;

	public Parameter(TypeReference type, String name) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		
		Name.check(name);

		this.type = type;
		this.name = name;
	}
	
	public TypeReference getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
}
