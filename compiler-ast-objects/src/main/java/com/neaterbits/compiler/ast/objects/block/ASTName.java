package com.neaterbits.compiler.ast.objects.block;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.util.name.Name;
import com.neaterbits.util.parse.context.Context;

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
