package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class StructDeclarationName extends DeclarationName<StructName> {

	public StructDeclarationName(Context context, StructName name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.STRUCT_DECLARATION_NAME;
	}
}
