package dev.nimbler.compiler.ast.objects.statement;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;

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
