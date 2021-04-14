package dev.nimbler.compiler.ast.objects;

import com.neaterbits.util.parse.context.Context;

public abstract class CompilationCode extends BaseASTElement {

	public abstract <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param);

	public CompilationCode(Context context) {
		super(context);
	}
}
