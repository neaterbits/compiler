package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.ast.type.TypeVisitor;

public final class IntType extends IntegerType {

	public IntType(BaseTypeName name, boolean nullable) {
		super(name, nullable, 32, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onInt(this, param);
	}
}
