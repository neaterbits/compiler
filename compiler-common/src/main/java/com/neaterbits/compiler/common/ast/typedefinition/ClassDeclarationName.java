package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;

public final class ClassDeclarationName extends DeclarationName<ClassName> {

	public ClassDeclarationName(Context context, ClassName name) {
		super(context, name);
	}
}
