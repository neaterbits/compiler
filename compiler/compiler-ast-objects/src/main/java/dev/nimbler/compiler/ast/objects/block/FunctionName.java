package dev.nimbler.compiler.ast.objects.block;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;

public final class FunctionName extends CallableName {
	public FunctionName(Context context, String name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FUNCTION_NAME;
	}
}
