package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

	}
}
