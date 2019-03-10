package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.TypeVisitor;

public final class UnnamedVoidType extends BaseType {

	public static final UnnamedVoidType INSTANCE = new UnnamedVoidType();

	private UnnamedVoidType() {
		super(false);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onUnnamedVoidType(this, param);
	}

}
