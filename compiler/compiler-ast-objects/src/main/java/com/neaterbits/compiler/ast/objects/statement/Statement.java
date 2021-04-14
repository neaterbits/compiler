package com.neaterbits.compiler.ast.objects.statement;

import com.neaterbits.compiler.ast.objects.CompilationCode;
import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.util.parse.context.Context;

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
