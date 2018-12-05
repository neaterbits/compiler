package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.BaseTypeName;

public abstract class ScalarType extends BuiltinType {
	
	protected ScalarType(BaseTypeName name, boolean nullable) {
		super(name, nullable);
	}
}
