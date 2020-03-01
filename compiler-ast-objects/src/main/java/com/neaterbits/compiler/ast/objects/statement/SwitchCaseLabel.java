package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
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
