package dev.nimbler.compiler.ast.objects.block;


import org.jutils.parse.context.Context;

public abstract class CallableName extends ASTName { // Name {

	protected CallableName(Context context, String name) {
		super(context, name);
	}
}
