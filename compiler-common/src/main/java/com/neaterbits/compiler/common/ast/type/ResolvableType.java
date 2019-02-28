package com.neaterbits.compiler.common.ast.type;

public abstract class ResolvableType extends NamedType {

	public ResolvableType(CompleteName completeName, boolean nullable) {
		super(completeName, nullable);
	}
}
