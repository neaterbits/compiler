package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.TypeVisitor;

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
