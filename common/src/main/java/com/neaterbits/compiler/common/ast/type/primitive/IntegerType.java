package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.BaseTypeName;

public abstract class IntegerType extends NumericType {
	
	protected IntegerType(BaseTypeName name, boolean nullable, int numBits, boolean signed) {
		super(name, nullable, numBits, signed);
		
	}

	@Override
	public final boolean isInteger() {
		return true;
	}
}
