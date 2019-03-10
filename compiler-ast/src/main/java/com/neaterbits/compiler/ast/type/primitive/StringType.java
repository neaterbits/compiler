package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.ast.type.TypeVisitor;

public final class StringType extends ScalarType {

	public StringType(BaseTypeName name, boolean nullable) {
		super(name, nullable);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onString(this, param);
	}
}
