package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.TypeVisitor;
import com.neaterbits.compiler.util.name.BaseTypeName;

public final class DoubleType extends FloatingPointType {

	public DoubleType(BaseTypeName name, boolean nullable) {
		super(name, nullable, 64, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onDouble(this, param);
	}
}
