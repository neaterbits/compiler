package dev.nimbler.compiler.ast.objects.expression;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.operator.Operator;

public final class ExpressionList extends Expression {

	private final List<Operator> operators;
	private final ASTList<Expression> expressions;
	
	public ExpressionList(Context context, List<Operator> operators, List<Expression> expressions) {
		super(context);

		Objects.requireNonNull(operators);
		Objects.requireNonNull(expressions);

		if (operators.size() != expressions.size() - 1) {
			throw new IllegalArgumentException("Expected one less operator than expression: " + operators.size() + "/" + expressions.size() + ": " + expressions);
		}

		this.operators 	 = Collections.unmodifiableList(operators);
		this.expressions = makeList(expressions);
	}
	
	public List<Operator> getOperators() {
		return operators;
	}

	public ASTList<Expression> getExpressions() {
		return expressions;
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.EXPRESSION_LIST;
	}

	@Override
	public TypeReference getType() {
		return expressions.iterator().next().getType();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onExpressionList(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expressions, recurseMode, iterator);
	}
}
