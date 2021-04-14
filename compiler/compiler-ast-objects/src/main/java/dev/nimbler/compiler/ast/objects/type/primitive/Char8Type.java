package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.util.name.BaseTypeName;

public final class Char8Type extends CharacterType {

	public Char8Type(BaseTypeName name, boolean nullable) {
		super(name, nullable, 1);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onChar8(this, param);
	}
}
