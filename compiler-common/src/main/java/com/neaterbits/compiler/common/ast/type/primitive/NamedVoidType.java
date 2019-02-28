package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public class NamedVoidType extends ScalarType {

	public NamedVoidType(BaseTypeName typeName) {
		super(typeName, false);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onVoid(this, param);
	}
}
