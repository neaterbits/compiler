package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.BaseTypeName;

public abstract class ScalarType extends BuiltinType {
	
	protected ScalarType(BaseTypeName name, boolean nullable) {
		super(name, nullable);
	}
}
