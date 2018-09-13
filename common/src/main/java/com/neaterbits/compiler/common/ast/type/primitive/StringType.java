package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public final class StringType extends ScalarType {

	public StringType(TypeName name, boolean nullable) {
		super(name, nullable);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onString(this, param);
	}
}
