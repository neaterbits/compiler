package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.util.name.BaseTypeName;

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
