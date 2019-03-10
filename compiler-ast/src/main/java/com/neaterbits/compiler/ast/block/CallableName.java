package com.neaterbits.compiler.ast.block;

import com.neaterbits.compiler.ast.Name;

public abstract class CallableName extends Name {

	protected CallableName(String name) {
		super(name);
	}
}
