package com.neaterbits.compiler.common.ast;


import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.list.ASTNode;

public abstract class BaseASTElement extends ASTNode {
	private final Context context;

	public BaseASTElement(Context context) {
		this.context = context;
	}

	public final Context getContext() {
		return context;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
