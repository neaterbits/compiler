package com.neaterbits.compiler.ast.block;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class MethodName extends CallableName {

	public MethodName(Context context, String name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.METHOD_NAME;
	}
}
