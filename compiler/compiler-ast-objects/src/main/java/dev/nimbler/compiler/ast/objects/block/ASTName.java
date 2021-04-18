package dev.nimbler.compiler.ast.objects.block;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.util.name.Name;

public abstract class ASTName extends BaseASTElement {

	private final String name;

	public ASTName(Context context, String name) {
		super(context);

		Objects.requireNonNull(name);
		Name.check(name);

		this.name = name;
	}

	public final String getName() {
		return name;
	}

	@Override
	protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
