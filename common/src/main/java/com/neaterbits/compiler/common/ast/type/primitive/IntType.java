package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public final class IntType extends IntegerType {

	public IntType(TypeName name, boolean nullable) {
		super(name, nullable, 32, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onInt(this, param);
	}
}
