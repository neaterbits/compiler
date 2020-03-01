package com.neaterbits.compiler.ast.objects.block;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class FunctionName extends CallableName {
	public FunctionName(Context context, String name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FUNCTION_NAME;
	}
}
