package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.ResolvedPrimary;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class NestedExpression extends ResolvedPrimary {

	private final ASTSingle<Expression> expression;
	
	public NestedExpression(Context context, Expression expression) {
		super(context);
		
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
		return ParseTreeElement.NESTED_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onNestedExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expression, recurseMode, iterator);
	}
}
