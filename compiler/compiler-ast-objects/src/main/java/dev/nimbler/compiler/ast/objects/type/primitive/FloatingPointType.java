package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.util.name.BaseTypeName;

public abstract class FloatingPointType extends NumericType {
	
	protected FloatingPointType(BaseTypeName name, boolean nullable, int numBits, boolean signed) {
		super(name, nullable, numBits, signed);
	}

	@Override
	public final boolean isInteger() {
		return false;
	}
}
