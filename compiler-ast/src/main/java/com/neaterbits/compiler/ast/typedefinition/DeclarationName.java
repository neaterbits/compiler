package com.neaterbits.compiler.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.name.BaseTypeName;

public abstract class DeclarationName<T extends BaseTypeName> extends BaseASTElement {

	private final T name;

	public DeclarationName(Context context, T name) {
		
		super(context);
		
		Objects.requireNonNull(name);
		
		this.name = name;
	}

	public final T getName() {
		return name;
	}
	
	public final String getNameString() {
		return name.getName();
	}

	@Override
	protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
