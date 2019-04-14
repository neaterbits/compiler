package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.util.Context;

public final class EnumConstant extends BaseASTElement {

	private final String enumConstant;

	public EnumConstant(Context context, String enumConstant) {
		super(context);
	
		Objects.requireNonNull(enumConstant);
		
		this.enumConstant = enumConstant;
	}

	public String getEnumConstant() {
		return enumConstant;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
