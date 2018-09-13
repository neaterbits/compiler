package com.neaterbits.compiler.common.ast;

import com.neaterbits.compiler.common.Context;

public abstract class BaseASTElement {
	private final Context context;

	public BaseASTElement(Context context) {
		this.context = context;
	}

	public final Context getContext() {
		return context;
	}
}
