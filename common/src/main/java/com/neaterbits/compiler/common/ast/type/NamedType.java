package com.neaterbits.compiler.common.ast.type;

public abstract class NamedType extends BaseType {

	private final TypeName name;

	protected NamedType(TypeName name, boolean nullable) {
		super(nullable);

		this.name = name;
	}

	public final TypeName getName() {
		return name;
	}
}
