package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.type.BaseTypeName;

public abstract class DeclarationName<T extends BaseTypeName> extends BaseASTElement {

	private final T name;

	public DeclarationName(Context context, T name) {
		
		super(context);
		
		Objects.requireNonNull(name);
		
		this.name = name;
	}

	public T getName() {
		return name;
	}

	@Override
	protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
