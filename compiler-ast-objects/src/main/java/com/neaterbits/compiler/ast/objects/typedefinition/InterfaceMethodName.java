package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.ast.objects.block.CallableName;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class InterfaceMethodName extends CallableName {

	public InterfaceMethodName(Context context, String name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_METHOD_NAME;
	}
}
