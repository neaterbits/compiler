package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.BaseTypeName;

public abstract class BuiltinType extends NamedType {

	protected BuiltinType(BaseTypeName name, boolean nullable) {
		super(new CompleteName(null, null, name), nullable);
	}
}
