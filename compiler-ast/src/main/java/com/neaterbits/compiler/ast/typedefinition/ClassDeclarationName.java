package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ClassDeclarationName extends DeclarationName<ClassName> {

	public ClassDeclarationName(Context context, ClassName name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_DECLARATION_NAME;
	}
}
