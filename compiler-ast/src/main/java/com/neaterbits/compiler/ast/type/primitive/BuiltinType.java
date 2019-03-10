package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.NamedType;

public abstract class BuiltinType extends NamedType {

	protected BuiltinType(BaseTypeName name, boolean nullable) {
		super(new CompleteName(null, null, name), nullable);
	}
}
