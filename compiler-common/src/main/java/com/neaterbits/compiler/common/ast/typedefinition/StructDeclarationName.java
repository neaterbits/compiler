package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;

public final class StructDeclarationName extends DeclarationName<StructName> {

	public StructDeclarationName(Context context, StructName name) {
		super(context, name);
	}
}
