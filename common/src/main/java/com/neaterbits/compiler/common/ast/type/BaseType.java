package com.neaterbits.compiler.common.ast.type;

public abstract class BaseType {
	private final boolean nullable;

	public abstract <T, R> R visit(TypeVisitor<T, R> visitor, T param);

	protected BaseType(boolean nullable) {
		this.nullable = nullable;
	}

	public final boolean isNullable() {
		return nullable;
	}
}
