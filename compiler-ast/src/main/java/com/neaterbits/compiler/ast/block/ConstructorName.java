package com.neaterbits.compiler.ast.block;

import com.neaterbits.compiler.util.Context;

public final class ConstructorName extends CallableName {

	public ConstructorName(Context context, String name) {
		super(context, name);
	}
}
