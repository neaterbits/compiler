package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;

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
