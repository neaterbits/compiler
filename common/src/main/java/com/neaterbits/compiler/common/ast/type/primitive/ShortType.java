package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public class ShortType extends IntegerType {

	public ShortType(TypeName name, boolean nullable) {
		super(name, nullable, 16, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onShort(this, param);
	}
}
