package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.util.name.BaseTypeName;

public final class DoubleType extends FloatingPointType {

	public DoubleType(BaseTypeName name, boolean nullable) {
		super(name, nullable, 64, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onDouble(this, param);
	}
}
