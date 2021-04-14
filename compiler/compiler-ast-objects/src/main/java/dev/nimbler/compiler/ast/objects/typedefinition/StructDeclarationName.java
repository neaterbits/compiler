package dev.nimbler.compiler.ast.objects.typedefinition;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;

public final class StructDeclarationName extends DeclarationName<StructName> {

	public StructDeclarationName(Context context, StructName name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.STRUCT_DECLARATION_NAME;
	}
}
