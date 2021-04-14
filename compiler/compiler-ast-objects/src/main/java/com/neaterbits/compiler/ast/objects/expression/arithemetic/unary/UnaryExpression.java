package com.neaterbits.compiler.ast.objects.expression.arithemetic.unary;


import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.operator.Arity;
import com.neaterbits.compiler.types.operator.UnaryOperator;
import com.neaterbits.util.parse.context.Context;

public final class UnaryExpression extends Expression {

    private final UnaryOperator operator;
	private final ASTSingle<Expression> expression;

	public UnaryExpression(
	        Context context,
            UnaryOperator operator,
	        Expression expression) {
	    
		super(context);

        Objects.requireNonNull(operator);
        
        if (operator.getArity() != Arity.UNARY) {
            throw new IllegalArgumentException();
        }

        this.operator = operator;
		this.expression = makeSingle(expression);
	}

	public UnaryOperator getOperator() {
	    return operator;
	}

    public Expression getExpression() {
		return expression.get();
	}
	
	@Override
    public ParseTreeElement getParseTreeElement() {
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

    @Override
    public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
        return visitor.onUnaryExpression(this, param);
    }
}
