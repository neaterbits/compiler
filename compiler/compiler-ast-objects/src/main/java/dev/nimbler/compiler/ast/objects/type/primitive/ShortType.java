package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.util.name.BaseTypeName;

public class ShortType extends IntegerType {

	public ShortType(BaseTypeName name, boolean nullable) {
		super(name, nullable, 16, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onShort(this, param);
	}
}
