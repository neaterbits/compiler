package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.util.name.BaseTypeName;

public abstract class ScalarType extends BuiltinType {
	
	protected ScalarType(BaseTypeName name, boolean nullable) {
		super(name, nullable);
	}
}
