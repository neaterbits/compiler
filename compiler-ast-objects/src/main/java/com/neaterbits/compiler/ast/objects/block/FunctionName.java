package com.neaterbits.compiler.ast.objects.block;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class FunctionName extends CallableName {
	public FunctionName(Context context, String name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FUNCTION_NAME;
	}
}
