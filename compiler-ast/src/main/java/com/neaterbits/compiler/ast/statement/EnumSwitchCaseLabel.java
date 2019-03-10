package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.util.Context;

public final class EnumSwitchCaseLabel extends SwitchCaseLabel {

	private final String enumConstant;

	public EnumSwitchCaseLabel(Context context, String enumConstant) {
		super(context);
	
		Objects.requireNonNull(enumConstant);
		
		this.enumConstant = enumConstant;
	}

	public String getEnumConstant() {
		return enumConstant;
	}
	
	@Override
	public <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param) {
		return visitor.onEnum(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
