package com.neaterbits.compiler.ast.block;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.Name;
import com.neaterbits.compiler.util.Context;

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
