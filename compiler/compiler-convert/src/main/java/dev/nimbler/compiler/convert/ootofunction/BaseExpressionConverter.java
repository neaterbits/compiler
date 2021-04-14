package dev.nimbler.compiler.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import dev.nimbler.compiler.ast.objects.expression.ArrayAccessExpression;
import dev.nimbler.compiler.ast.objects.expression.AssignmentExpression;
import dev.nimbler.compiler.ast.objects.expression.ConditionalExpression;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.ExpressionList;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.NestedExpression;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.expression.arithemetic.binary.ArithmeticBinaryExpression;
import dev.nimbler.compiler.ast.objects.expression.arithemetic.unary.UnaryExpression;
import dev.nimbler.compiler.ast.objects.expression.literal.BooleanLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.CharacterLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.FloatingPointLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.IntegerLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.NullLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.Primary;
import dev.nimbler.compiler.ast.objects.expression.literal.StringLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.UnresolvedNamePrimary;
import dev.nimbler.compiler.ast.objects.type.primitive.BooleanType;
import dev.nimbler.compiler.ast.objects.type.primitive.CharacterType;
import dev.nimbler.compiler.ast.objects.type.primitive.FloatingPointType;
import dev.nimbler.compiler.ast.objects.type.primitive.IntegerType;
import dev.nimbler.compiler.ast.objects.type.primitive.StringType;
import dev.nimbler.compiler.ast.objects.variables.VariableReference;
import dev.nimbler.compiler.convert.ConverterState;
import dev.nimbler.compiler.convert.ExpressionConverter;

public abstract class BaseExpressionConverter<T extends ConverterState<T>>
	extends BaseConverter<T>
	implements ExpressionConverter<T> {

	@Override
    protected final Expression convertExpression(Expression expression, T param) {
		return expression.visit(this, param);
	}

	private Primary convertPrimary(Primary primary, T param) {
		return (Primary)convertExpression(primary, param);
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
    public Expression onUnresolvedNamePrimary(UnresolvedNamePrimary primary, T param) {
	    throw new UnsupportedOperationException();
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
	public final Expression onVariableReference(VariableReference expression, T param) {

		final VariableReference convertedVariableReference = convertVariableReference(expression, param);

		return convertedVariableReference;
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
				(IntegerType)convertBuiltinType(expression.getType(), param),
				-1);
	}

	@Override
	public Expression onStringLiteral(StringLiteral expression, T param) {

		return new StringLiteral(
				expression.getContext(),
				expression.getValue(),
				(StringType)convertBuiltinType(expression.getType(), param),
				-1);
	}

	@Override
	public final Expression onFloatingPointLiteral(FloatingPointLiteral expression, T param) {
		return new FloatingPointLiteral(
				expression.getContext(),
				expression.getValue(),
				expression.getBase(),
				expression.getBits(),
				(FloatingPointType)convertBuiltinType(expression.getType(), param),
				-1);
	}

	@Override
	public final Expression onBooleanLiteral(BooleanLiteral expression, T param) {
		return new BooleanLiteral(
				expression.getContext(),
				expression.getValue(),
				(BooleanType)convertBuiltinType(expression.getType(), param),
				-1);
	}

	@Override
	public final Expression onCharacterLiteral(CharacterLiteral expression, T param) {
		return new CharacterLiteral(
				expression.getContext(),
				expression.getValue(),
				(CharacterType)convertBuiltinType(expression.getType(), param),
				-1);
	}

	@Override
	public final Expression onNullLiteral(NullLiteral expression, T param) {
		return new NullLiteral(expression.getContext());
	}

	@Override
    public Expression onUnaryExpression(UnaryExpression expression, T param) {
	    
        return new UnaryExpression(
                            expression.getContext(),
                            expression.getOperator(),
                            expression.getExpression());
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
