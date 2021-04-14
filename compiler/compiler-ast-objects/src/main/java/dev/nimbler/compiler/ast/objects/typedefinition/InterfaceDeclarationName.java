package dev.nimbler.compiler.ast.objects.typedefinition;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;

public final class InterfaceDeclarationName extends DeclarationName<InterfaceName> {

	public InterfaceDeclarationName(Context context, InterfaceName name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_DECLARATION_NAME;
	}
}
