package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public final class NullType extends ScalarType {

	public static final NullType INSTANCE = new NullType();
	
	private NullType() {
		super(new BaseTypeName("null"), true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onNullType(this, param);
	}
}
