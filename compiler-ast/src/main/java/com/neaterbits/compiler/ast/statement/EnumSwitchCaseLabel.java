package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class EnumSwitchCaseLabel extends SwitchCaseLabel {

	private final ASTSingle<Keyword> keyword;
	private final String enumConstant;

	public EnumSwitchCaseLabel(Context context, Keyword keyword, String enumConstant) {
		super(context);
	
		Objects.requireNonNull(keyword);
		Objects.requireNonNull(enumConstant);
		
		this.keyword = makeSingle(keyword);
		this.enumConstant = enumConstant;
	}

	public Keyword getKeyword() {
		return keyword.get();
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
		doIterate(keyword, recurseMode, iterator);
	}
}
