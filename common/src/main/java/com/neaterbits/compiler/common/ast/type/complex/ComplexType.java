package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.type.BaseType;

public abstract class ComplexType extends BaseType {

	public ComplexType(String name, boolean nullable) {
		super(name, nullable);
	}
}
