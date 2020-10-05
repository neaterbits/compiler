package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.util.name.BaseTypeName;
import com.neaterbits.util.parse.context.Context;

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
