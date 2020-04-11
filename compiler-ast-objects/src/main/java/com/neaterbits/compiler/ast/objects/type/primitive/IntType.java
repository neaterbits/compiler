package com.neaterbits.compiler.ast.objects.type.primitive;

import com.neaterbits.compiler.ast.objects.type.TypeVisitor;
import com.neaterbits.compiler.util.name.BaseTypeName;

public final class IntType extends IntegerType {

	public IntType(BaseTypeName name, boolean nullable) {
		super(name, nullable, 32, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onInt(this, param);
	}
}