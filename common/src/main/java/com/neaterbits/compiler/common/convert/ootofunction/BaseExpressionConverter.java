package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.ast.expression.ArrayAccessExpression;
import com.neaterbits.compiler.common.ast.expression.AssignmentExpression;
import com.neaterbits.compiler.common.ast.expression.ConditionalExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ExpressionList;
import com.neaterbits.compiler.common.ast.expression.FieldAccess;
import com.neaterbits.compiler.common.ast.expression.NestedExpression;
import com.neaterbits.compiler.common.ast.expression.PrimaryList;
import com.neaterbits.compiler.common.ast.expression.VariableExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.binary.ArithmeticBinaryExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostIncrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreIncrementExpression;
import com.neaterbits.compiler.common.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.common.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.common.ast.type.primitive.CharacterType;
import com.neaterbits.compiler.common.ast.type.primitive.FloatingPointType;
import com.neaterbits.compiler.common.ast.type.primitive.IntegerType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.convert.ConverterState;
import com.neaterbits.compiler.common.convert.ExpressionConverter;

public abstract class BaseExpressionConverter<T extends ConverterState<T>>
	extends BaseConverter<T>
	implements ExpressionConverter<T> {

	protected final Expression convertExpression(Expression expression, T param) {
		return expression.visit(this, param);
	}
	
	private Primary convertPrimary(Primary primary, T param) {
		return (Primary)convertExpression(primary, param);
	}
	
	@Override
	public final Expression onVariable(VariableExpression expression, T param) {
		return new VariableExpression(expression.getContext(), convertVariableReference(expression.getReference(), param));
	}

	@Override
	public final Expression onAssignment(AssignmentExpression expression, T param) {
		
		return new AssignmentExpression(
				expression.getContext(),
				convertVariableReference(expression.getVariable(), param),
				convertExpression(expression.getExpression(), param));
	}

	@Override
	public final Expression onExpressionList(ExpressionList expressionList, T param) {

		final List<Expression> convertedList = new ArrayList<>(expressionList.getExpressions().size());
		
		for (Expression expression : expressionList.getExpressions()) {
			convertedList.add(convertExpression(expression, param));
		}

		return new ExpressionList(expressionList.getContext(), expressionList.getOperators(), convertedList);
	}

	@Override
	public final Expression onPrimaryList(PrimaryList primaryList, T param) {
		
		final List<Primary> convertedList = new ArrayList<>(primaryList.getPrimaries().size());
		
		for (Primary primary : primaryList.getPrimaries()) {
			convertedList.add(convertPrimary(primary, param));
		}
		
		return new PrimaryList(primaryList.getContext(), convertedList);
	}

	@Override
	public final Expression onArrayAccessExpression(ArrayAccessExpression expression, T param) {
		
		return new ArrayAccessExpression(
				expression.getContext(),
				convertPrimary(expression.getArray(), param),
				convertExpression(expression.getIndex(), param));
	}

	@Override
	public final Expression onFieldAccess(FieldAccess expression, T param) {
		return new FieldAccess(
				expression.getContext(),
				expression.getFieldAccessType(),
				mapComplexType(expression.getClassType(), param),
				expression.getFieldName());
	}

	@Override
	public Expression onVariableReference(VariableReference expression, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Expression onConditionalExpression(ConditionalExpression expression, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Expression onNestedExpression(NestedExpression expression, T param) {
		return new NestedExpression(expression.getContext(), convertExpression(expression.getExpression(), param));
	}

	@Override
	public final Expression onIntegerLiteral(IntegerLiteral expression, T param) {
		return new IntegerLiteral(
				expression.getContext(),
				expression.getValue(),
				expression.getBase(),
				expression.isSigned(),
				expression.getBits(),
				(IntegerType)convertType(expression.getType(), param));
	}

	@Override
	public final Expression onStringLiteral(StringLiteral expression, T param) {

		return new StringLiteral(
				expression.getContext(),
				expression.getValue(),
				(StringType)convertType(expression.getType(), param));
	}

	@Override
	public final Expression onFloatingPointLiteral(FloatingPointLiteral expression, T param) {
		return new FloatingPointLiteral(
				expression.getContext(),
				expression.getValue(),
				expression.getBase(),
				expression.getBits(),
				(FloatingPointType)convertType(expression.getType(), param));
	}

	@Override
	public final Expression onBooleanLiteral(BooleanLiteral expression, T param) {
		return new BooleanLiteral(
				expression.getContext(),
				expression.getValue(),
				(BooleanType)convertType(expression.getType(), param));
	}

	@Override
	public final Expression onCharacterLiteral(CharacterLiteral expression, T param) {
		return new CharacterLiteral(
				expression.getContext(),
				expression.getValue(),
				(CharacterType)convertType(expression.getType(), param));
	}

	@Override
	public final Expression onNullLiteral(NullLiteral expression, T param) {
		return new NullLiteral(expression.getContext());
	}

	@Override
	public final Expression onPreIncrement(PreIncrementExpression expression, T param) {
		return new PreIncrementExpression(expression.getContext(), convertExpression(expression.getExpression(), param));
	}

	@Override
	public final Expression onPreDecrement(PreDecrementExpression expression, T param) {
		return new PreDecrementExpression(expression.getContext(), convertExpression(expression.getExpression(), param));
	}

	@Override
	public final Expression onPostIncrement(PostIncrementExpression expression, T param) {
		return new PostIncrementExpression(expression.getContext(), convertExpression(expression.getExpression(), param));
	}

	@Override
	public final Expression onPostDecrement(PostDecrementExpression expression, T param) {
		return new PostDecrementExpression(expression.getContext(), expression.getExpression());
	}

	@Override
	public final Expression onArithmeticBinary(ArithmeticBinaryExpression expression, T param) {

		return new ArithmeticBinaryExpression(
				expression.getContext(),
				convertExpression(expression.getLhs(), param),
				expression.getOperator(),
				convertExpression(expression.getRhs(), param));
	}
}