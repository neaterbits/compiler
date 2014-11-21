package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public final class NullType extends ScalarType {

	public static final NullType INSTANCE = new NullType();
	
	private NullType() {
		super(new TypeName("null"), true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onNullType(this, param);
	}
}
