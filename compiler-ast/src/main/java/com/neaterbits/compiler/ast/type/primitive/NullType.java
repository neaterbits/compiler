package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.ast.type.TypeVisitor;

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