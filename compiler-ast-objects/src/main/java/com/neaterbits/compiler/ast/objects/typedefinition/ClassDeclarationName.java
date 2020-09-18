package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.name.ClassName;

public final class ClassDeclarationName extends DeclarationName<ClassName> {

	public ClassDeclarationName(Context context, ClassName name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_DECLARATION_NAME;
	}
}
