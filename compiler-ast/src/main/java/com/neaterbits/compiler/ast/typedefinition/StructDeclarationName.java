package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.util.Context;

public final class StructDeclarationName extends DeclarationName<StructName> {

	public StructDeclarationName(Context context, StructName name) {
		super(context, name);
	}
}
