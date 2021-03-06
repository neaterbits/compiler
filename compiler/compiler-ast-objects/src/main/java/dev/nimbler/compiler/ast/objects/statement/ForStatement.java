package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ForStatement extends LoopStatement {

	private final ASTSingle<Keyword> keyword;
	private final ASTSingle<ForInit> forInit;
	private final ASTSingle<Expression> condition;
	private final ASTSingle<ForUpdateExpressionList> forUpdate;

	public ForStatement(Context context, Keyword keyword, ForInit forInit, Expression condition, ForUpdateExpressionList forUpdate, Block block) {
		super(context, block);
		
		Objects.requireNonNull(keyword);
		
		this.keyword = makeSingle(keyword);
		
		this.forInit   = forInit   != null ? makeSingle(forInit)   : null;
		this.condition = condition != null ? makeSingle(condition) : null;
		this.forUpdate = forUpdate != null ? makeSingle(forUpdate) : null;
	}

	public Keyword getKeyword() {
		return keyword.get();
	}

	public ForInit getForInit() {
		return forInit != null ? forInit.get() : null;
	}

	public Expression getCondition() {
		return condition != null ? condition.get() : null;
	}

	public ForUpdateExpressionList getForUpdate() {
		return forUpdate != null ? forUpdate.get() : null;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FOR_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onCFor(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(keyword, recurseMode, iterator);
		
		if (forInit != null) {
			doIterate(forInit, recurseMode, iterator);
		}
		
		if (condition != null) {
			doIterate(condition, recurseMode, iterator);
		}
		
		if (forUpdate != null) {
			doIterate(forUpdate, recurseMode, iterator);
		}
		
		super.doRecurse(recurseMode, iterator);
	}
}
