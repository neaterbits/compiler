package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public final class BooleanType extends ScalarType {

	public BooleanType(BaseTypeName name, boolean nullable) {
		super(name, nullable);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onBoolean(this, param);
	}
}
