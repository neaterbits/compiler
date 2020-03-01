package com.neaterbits.compiler.ast.objects.type.primitive;

import com.neaterbits.compiler.ast.objects.type.TypeVisitor;
import com.neaterbits.compiler.util.name.BaseTypeName;

public final class Char8Type extends CharacterType {

	public Char8Type(BaseTypeName name, boolean nullable) {
		super(name, nullable, 1);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onChar8(this, param);
	}
}
