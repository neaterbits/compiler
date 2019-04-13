package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class DefaultSwitchCaseLabel extends SwitchCaseLabel {

	private final ASTSingle<Keyword> keyword;
	
	public DefaultSwitchCaseLabel(Context context, Keyword keyword) {
		super(context);
		
		Objects.requireNonNull(keyword);
		
		this.keyword = makeSingle(keyword);
	}
	
	public Keyword getKeyword() {
		return keyword.get();
	}

	@Override
	public <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param) {
		return visitor.onDefault(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		doIterate(keyword, recurseMode, iterator);
	}
}
