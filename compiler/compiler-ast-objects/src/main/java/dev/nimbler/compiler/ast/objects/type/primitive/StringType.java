package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.util.name.BaseTypeName;

public final class StringType extends ScalarType {

	public StringType(BaseTypeName name, boolean nullable) {
		super(name, nullable);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onString(this, param);
	}
}
