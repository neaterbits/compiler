package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class InterfaceDeclarationName extends DeclarationName<InterfaceName> {

	public InterfaceDeclarationName(Context context, InterfaceName name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_DECLARATION_NAME;
	}
}
