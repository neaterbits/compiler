package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.util.Context;

public final class EnumSwitchCaseLabel extends SwitchCaseLabel {

	private final String enumConstant;

	public EnumSwitchCaseLabel(Context context, Keyword keyword, String enumConstant) {
		super(context, keyword);
	
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
}
