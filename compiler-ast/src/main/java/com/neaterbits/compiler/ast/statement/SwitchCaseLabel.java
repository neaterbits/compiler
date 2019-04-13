package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public abstract class SwitchCaseLabel extends BaseASTElement {

	private final ASTSingle<Keyword> keyword;
	
	public abstract <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param);
	
	public SwitchCaseLabel(Context context, Keyword keyword) {
		super(context);
		
		Objects.requireNonNull(keyword);
		
		this.keyword = makeSingle(keyword);
	}

	public final Keyword getKeyword() {
		return keyword.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(keyword, recurseMode, iterator);
	}
}
