package com.neaterbits.compiler.ast;

import com.neaterbits.compiler.util.Context;

public abstract class CompilationCode extends BaseASTElement {

	public abstract <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param);

	public CompilationCode(Context context) {
		super(context);
	}
}