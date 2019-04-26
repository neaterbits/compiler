package com.neaterbits.compiler.ast.block;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ParameterName extends ASTName {

	public ParameterName(Context context, String name) {
		super(context, name);

	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PARAMETER_NAME;
	}
}
