package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ReturnStatement extends Statement {

	private final ASTSingle<Expression> expression;
	
	public ReturnStatement(Context context, Expression expression) {
		super(context);

		Objects.requireNonNull(expression);

		this.expression = makeSingle(expression);
	}

	public Expression getExpression() {
		return expression.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.RETURN_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onReturnStatement(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expression, recurseMode, iterator);
	}
}
