package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public class Char16Type extends CharacterType {

	public Char16Type(BaseTypeName name, boolean nullable) {
		super(name, nullable, 2);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onChar16(this, param);
	}
}
