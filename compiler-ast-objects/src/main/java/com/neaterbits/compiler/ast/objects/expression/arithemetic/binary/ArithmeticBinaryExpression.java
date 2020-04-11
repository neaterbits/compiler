package com.neaterbits.compiler.ast.objects.expression.arithemetic.binary;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.expression.BinaryExpression;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.operator.Arithmetic;

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
	public TypeReference getType() {
		throw new UnsupportedOperationException();
	}

	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ARITHMETIC_BINARY_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onArithmeticBinary(this, param);
	}
}