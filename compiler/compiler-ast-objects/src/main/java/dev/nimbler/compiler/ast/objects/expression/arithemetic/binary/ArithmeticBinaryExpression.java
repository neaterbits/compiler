package dev.nimbler.compiler.ast.objects.expression.arithemetic.binary;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.BinaryExpression;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.operator.Arithmetic;

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
