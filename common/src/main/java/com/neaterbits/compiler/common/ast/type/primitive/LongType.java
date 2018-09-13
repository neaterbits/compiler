package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public class LongType extends IntegerType {

	public LongType(TypeName name, boolean nullable) {
		super(name, nullable, 64, true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onLong(this, param);
	}
}
