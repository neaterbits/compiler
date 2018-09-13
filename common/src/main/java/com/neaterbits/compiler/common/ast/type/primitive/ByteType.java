package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public final class ByteType extends IntegerType {

	public ByteType(TypeName name, boolean nullable) {
		super(name, nullable, 8, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onByte(this, param);
	}
}
