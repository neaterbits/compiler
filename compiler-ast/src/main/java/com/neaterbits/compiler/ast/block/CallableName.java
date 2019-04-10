package com.neaterbits.compiler.ast.block;


import com.neaterbits.compiler.util.Context;

public abstract class CallableName extends ASTName { // Name {

	protected CallableName(Context context, String name) {
		super(context, name);
	}
}
