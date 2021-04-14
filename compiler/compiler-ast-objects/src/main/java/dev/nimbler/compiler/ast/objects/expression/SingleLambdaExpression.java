package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class SingleLambdaExpression extends LambdaExpression {

	private final ASTSingle<Expression> expression;
	
	public SingleLambdaExpression(Context context, LambdaExpressionParameters parameters, Expression expression) {
		super(context, parameters);
		
		Objects.requireNonNull(expression);

		this.expression = makeSingle(expression);
	}

	public Expression getExpression() {
		return expression.get();
	}

	@Override
	public TypeReference getType() {
		return expression.get().getType();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.SINGLE_LAMBDA_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onSingleLambdaExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		super.doRecurse(recurseMode, iterator);
		
		doIterate(expression, recurseMode, iterator);
	}
}
