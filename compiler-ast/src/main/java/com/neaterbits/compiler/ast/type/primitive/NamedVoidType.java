package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.TypeVisitor;
import com.neaterbits.compiler.util.name.BaseTypeName;

public class NamedVoidType extends ScalarType {

	public NamedVoidType(BaseTypeName typeName) {
		super(typeName, false);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onVoid(this, param);
	}
}
