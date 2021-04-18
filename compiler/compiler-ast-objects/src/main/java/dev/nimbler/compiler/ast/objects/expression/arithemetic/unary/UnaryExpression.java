package dev.nimbler.compiler.ast.objects.expression.arithemetic.unary;


import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.operator.Arity;
import dev.nimbler.compiler.types.operator.UnaryOperator;

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
