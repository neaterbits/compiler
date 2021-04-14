package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.util.name.BaseTypeName;

public class NamedVoidType extends ScalarType {

	public NamedVoidType(BaseTypeName typeName) {
		super(typeName, false);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onVoid(this, param);
	}
}
