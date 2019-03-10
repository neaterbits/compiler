package com.neaterbits.compiler.ast.type;

import java.util.Objects;

import com.neaterbits.compiler.ast.block.ParameterName;

public final class FunctionPointerParameter {

	private final BaseType type;
	private final ParameterName name;
	
	public FunctionPointerParameter(BaseType type, ParameterName name) {
		
		Objects.requireNonNull(type);
		
		this.type = type;
		this.name = name;
	}

	public BaseType getType() {
		return type;
	}

	public ParameterName getName() {
		return name;
	}
}
