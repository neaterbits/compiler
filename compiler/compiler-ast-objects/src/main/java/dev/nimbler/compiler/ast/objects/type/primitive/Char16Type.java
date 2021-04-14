package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.util.name.BaseTypeName;

public class Char16Type extends CharacterType {

	public Char16Type(BaseTypeName name, boolean nullable) {
		super(name, nullable, 2);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onChar16(this, param);
	}
}
