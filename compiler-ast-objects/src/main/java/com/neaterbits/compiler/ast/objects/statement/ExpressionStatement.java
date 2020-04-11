package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ExpressionStatement extends Statement {

	private final ASTSingle<Expression> expression;
	
	public ExpressionStatement(Context context, Expression expression) {
		super(context);

		Objects.requireNonNull(expression);
		
		this.expression = makeSingle(expression);
	}

	public Expression getExpression() {
		return expression.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.EXPRESSION_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onExpressionStatement(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(expression, recurseMode, iterator);
	}
}