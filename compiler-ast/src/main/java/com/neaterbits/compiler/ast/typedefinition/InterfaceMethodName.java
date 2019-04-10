package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.block.CallableName;
import com.neaterbits.compiler.util.Context;

public final class InterfaceMethodName extends CallableName {

	public InterfaceMethodName(Context context, String name) {
		super(context, name);
	}
}
