package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.TypeName;

public abstract class ScalarType extends NamedType {
	
	protected ScalarType(TypeName name, boolean nullable) {
		super(name, nullable);
	}
}
