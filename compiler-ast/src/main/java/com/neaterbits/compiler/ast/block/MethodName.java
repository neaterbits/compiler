package com.neaterbits.compiler.ast.block;

import com.neaterbits.compiler.util.Context;

public final class MethodName extends CallableName {

	public MethodName(Context context, String name) {
		super(context, name);
	}
}
