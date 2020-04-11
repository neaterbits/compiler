package com.neaterbits.compiler.ast.objects.block;


import com.neaterbits.compiler.util.Context;

public abstract class CallableName extends ASTName { // Name {

	protected CallableName(Context context, String name) {
		super(context, name);
	}
}