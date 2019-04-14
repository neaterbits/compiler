package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class EnumSwitchCaseLabel extends SwitchCaseLabel {

	private final ASTSingle<EnumConstant> enumConstant;
	
	public EnumSwitchCaseLabel(Context context, Keyword keyword, EnumConstant enumConstant) {
		super(context, keyword);
	
		Objects.requireNonNull(enumConstant);
		
		this.enumConstant = makeSingle(enumConstant);
	}

	public String getEnumConstant() {
		return enumConstant.get().getEnumConstant();
	}
	
	@Override
	public <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param) {
		return visitor.onEnum(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		super.doRecurse(recurseMode, iterator);
		
		doIterate(enumConstant, recurseMode, iterator);
	}
}
