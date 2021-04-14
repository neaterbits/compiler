package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class BreakStatement extends Statement {

	private final ASTSingle<Keyword> keyword;

	private final String label;

	public BreakStatement(Context context, Keyword keyword, String label) {
		super(context);

		Objects.requireNonNull(keyword);
		
		this.keyword = makeSingle(keyword);
		
		this.label = label;
	}

	public Keyword getKeyword() {
		return keyword.get();
	}

	public String getLabel() {
		return label;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.BREAK_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onBreakStatement(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(keyword, recurseMode, iterator);
	}
}
