package com.neaterbits.compiler.ast.block;

import com.neaterbits.compiler.util.Context;

public final class FunctionName extends CallableName {
	public FunctionName(Context context, String name) {
		super(context, name);
	}
}
