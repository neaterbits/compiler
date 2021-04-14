package com.neaterbits.compiler.ast.objects.block;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class MethodName extends CallableName {

	public MethodName(Context context, String name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.METHOD_NAME;
	}
}
