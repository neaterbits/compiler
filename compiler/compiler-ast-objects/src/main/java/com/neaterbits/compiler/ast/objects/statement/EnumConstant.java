package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ENUM_CONSTANT;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
