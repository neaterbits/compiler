package dev.nimbler.compiler.ast.objects.statement;

import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class SwitchCaseStatement extends ConditionStatement {

	private final ASTSingle<Keyword> keyword;
	private final ASTSingle<Expression> expression;
	private final ASTList<SwitchCaseGroup> groups;

	public SwitchCaseStatement(Context context, Keyword keyword, Expression expression, List<SwitchCaseGroup> groups) {
		super(context);
	
		Objects.requireNonNull(keyword);
		Objects.requireNonNull(expression);
		Objects.requireNonNull(groups);

		this.keyword = makeSingle(keyword);
		this.expression = makeSingle(expression);
		this.groups = makeList(groups);
	}

	public Keyword getKeyword() {
		return keyword.get();
	}

	public Expression getExpression() {
		return expression.get();
	}

	public ASTList<SwitchCaseGroup> getGroups() {
		return groups;
	}

	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.SWITCH_CASE_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onSwitchCase(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(keyword, recurseMode, iterator);
		doIterate(groups, recurseMode, iterator);
	}
}
