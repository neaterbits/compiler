package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.NamedType;
import dev.nimbler.compiler.util.name.BaseTypeName;

public abstract class BuiltinType extends NamedType {

	protected BuiltinType(BaseTypeName name, boolean nullable) {
		super(new CompleteName(null, null, name), nullable);
	}
}
