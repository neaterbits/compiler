package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.TypeVisitor;
import com.neaterbits.compiler.util.name.BaseTypeName;

public final class FloatType extends FloatingPointType {

	public FloatType(BaseTypeName name, boolean nullable) {
		super(name, nullable, 32, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onFloat(this, param);
	}
}
