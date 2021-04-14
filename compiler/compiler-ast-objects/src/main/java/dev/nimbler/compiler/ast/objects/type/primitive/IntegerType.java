package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.util.name.BaseTypeName;

public abstract class IntegerType extends NumericType {
	
	protected IntegerType(BaseTypeName name, boolean nullable, int numBits, boolean signed) {
		super(name, nullable, numBits, signed);
		
	}

	@Override
	public final boolean isInteger() {
		return true;
	}
}
