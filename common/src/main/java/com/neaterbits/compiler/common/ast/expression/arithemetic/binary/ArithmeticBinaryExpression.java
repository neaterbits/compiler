package com.neaterbits.compiler.common.ast.expression.arithemetic.binary;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.BinaryExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.common.ast.operator.Arithmetic;
import com.neaterbits.compiler.common.ast.type.BaseType;

public class ArithmeticBinaryExpression extends BinaryExpression {

	private final Arithmetic operator;
	
	public ArithmeticBinaryExpression(Context context, Expression lhs, Arithmetic operator, Expression rhs) {
		super(context, lhs, rhs);

		Objects.requireNonNull(operator);
		
		this.operator = operator;
	}

	public final Arithmetic getOperator() {
		return operator;
	}

	@Override
	public BaseType getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onArithmeticBinary(this, param);
	}
}
