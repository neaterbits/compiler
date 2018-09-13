package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public final class Char8Type extends CharacterType {

	public Char8Type(TypeName name, boolean nullable) {
		super(name, nullable, 1);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onChar8(this, param);
	}
}
