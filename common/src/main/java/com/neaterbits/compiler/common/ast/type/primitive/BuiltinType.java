package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.TypeName;

public abstract class BuiltinType extends NamedType {

	public BuiltinType(TypeName name, boolean nullable) {
		super(null, name, nullable);
	}
}
