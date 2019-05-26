package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.TypeVisitor;
import com.neaterbits.compiler.util.name.BaseTypeName;

public class Char16Type extends CharacterType {

	public Char16Type(BaseTypeName name, boolean nullable) {
		super(name, nullable, 2);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onChar16(this, param);
	}
}
