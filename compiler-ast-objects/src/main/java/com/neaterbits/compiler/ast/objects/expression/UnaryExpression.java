package com.neaterbits.compiler.ast.objects.expression;


import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.util.parse.context.Context;

public abstract class UnaryExpression extends Expression {

	private final ASTSingle<Expression> expression;

	public UnaryExpression(Context context, Expression expression) {
		super(context);

		this.expression = makeSingle(expression);
	}

	public abstract Operator getOperator();

    public final Expression getExpression() {
		return expression.get();
	}
	
	@Override
    public final ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.UNARY_EXPRESSION;
    }

    @Override
	public final TypeReference getType() {
		return expression.get().getType();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expression, recurseMode, iterator);
	}
}
