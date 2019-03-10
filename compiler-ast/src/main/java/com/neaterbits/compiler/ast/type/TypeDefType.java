package com.neaterbits.compiler.ast.type;

public final class TypeDefType extends ResolvableType {

	public TypeDefType(CompleteName completeName, BaseType delegate) {
		super(completeName, delegate.isNullable());
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onTypeDef(this, param);
	}
}
