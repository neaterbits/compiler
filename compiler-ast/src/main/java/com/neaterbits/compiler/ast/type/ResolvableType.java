package com.neaterbits.compiler.ast.type;

public abstract class ResolvableType extends NamedType {

	public ResolvableType(CompleteName completeName, boolean nullable) {
		super(completeName, nullable);
	}
}
