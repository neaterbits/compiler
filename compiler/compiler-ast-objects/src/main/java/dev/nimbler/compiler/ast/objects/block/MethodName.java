package dev.nimbler.compiler.ast.objects.block;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;

public final class MethodName extends CallableName {

	public MethodName(Context context, String name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.METHOD_NAME;
	}
}
