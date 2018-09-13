package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.BaseType;

public abstract class PrimitiveType extends BaseType {
	public PrimitiveType(String name, boolean nullable) {
		super(name, nullable);
	}
}
