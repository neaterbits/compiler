package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.util.Context;

public final class ClassDeclarationName extends DeclarationName<ClassName> {

	public ClassDeclarationName(Context context, ClassName name) {
		super(context, name);
	}
}
