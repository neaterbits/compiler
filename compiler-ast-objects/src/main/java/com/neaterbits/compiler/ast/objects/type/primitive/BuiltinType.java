package com.neaterbits.compiler.ast.objects.type.primitive;

import com.neaterbits.compiler.ast.objects.type.CompleteName;
import com.neaterbits.compiler.ast.objects.type.NamedType;
import com.neaterbits.compiler.util.name.BaseTypeName;

public abstract class BuiltinType extends NamedType {

	protected BuiltinType(BaseTypeName name, boolean nullable) {
		super(new CompleteName(null, null, name), nullable);
	}
}