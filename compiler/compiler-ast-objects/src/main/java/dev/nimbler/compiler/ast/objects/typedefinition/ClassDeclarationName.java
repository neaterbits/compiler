package dev.nimbler.compiler.ast.objects.typedefinition;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.util.name.ClassName;

public final class ClassDeclarationName extends DeclarationName<ClassName> {

	public ClassDeclarationName(Context context, ClassName name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_DECLARATION_NAME;
	}
}
