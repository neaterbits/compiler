package com.neaterbits.compiler.common.ast.type;

public class BaseType {

	private final String name;
	private final boolean nullable;

	protected BaseType(String name, boolean nullable) {
		this.name = name;
		this.nullable = nullable;
	}

	public final String getName() {
		return name;
	}

	public final boolean isNullable() {
		return nullable;
	}
}
