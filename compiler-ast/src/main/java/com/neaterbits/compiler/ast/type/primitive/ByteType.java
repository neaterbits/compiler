package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.ast.type.TypeVisitor;

public final class ByteType extends IntegerType {

	public ByteType(BaseTypeName name, boolean nullable) {
		super(name, nullable, 8, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onByte(this, param);
	}
}
