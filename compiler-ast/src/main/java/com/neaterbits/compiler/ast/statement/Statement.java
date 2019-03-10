package com.neaterbits.compiler.ast.statement;

import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.util.Context;

public abstract class Statement extends CompilationCode {

	public abstract <T, R> R visit(StatementVisitor<T, R> visitor, T param);
	
	public Statement(Context context) {
		super(context);
	}

	@Override
	public final <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		throw new UnsupportedOperationException();
	}
}
