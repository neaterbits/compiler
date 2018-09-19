package com.neaterbits.compiler.common.emit.base.c;

import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.AssignmentExpression;
import com.neaterbits.compiler.common.ast.expression.ConditionExpression;
import com.neaterbits.compiler.common.ast.expression.ConditionalExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.binary.ArithmeticBinaryExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostIncrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreIncrementExpression;
import com.neaterbits.compiler.common.ast.operator.Arithmetic;
import com.neaterbits.compiler.common.ast.operator.Bitwise;
import com.neaterbits.compiler.common.ast.operator.Logical;
import com.neaterbits.compiler.common.ast.operator.Operator;
import com.neaterbits.compiler.common.ast.operator.OperatorVisitor;
import com.neaterbits.compiler.common.ast.operator.Relational;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.BaseInfixExpressionEmitter;

public abstract class CLikeExpressionEmitter<T extends EmitterState> extends BaseInfixExpressionEmitter<T> {

	protected abstract void emitCondition(Condition condition, EmitterState param);

	@Override
	public final Void onConditionExpression(ConditionExpression expression, T param) {

		emitCondition(expression.getCondition(), param);
		
		return null;
	}

	@Override
	public final Void onPreIncrement(PreIncrementExpression expression, T param) {

		param.append("++");
		
		emitExpression(expression.getExpression(), param);
		
		return null;
	}

	@Override
	public final Void onPreDecrement(PreDecrementExpression expression, T param) {

		param.append("--");

		emitExpression(expression.getExpression(), param);
		
		return null;
	}

	@Override
	public final Void onPostIncrement(PostIncrementExpression expression, T param) {

		emitExpression(expression.getExpression(), param);
		
		param.append("++");
		
		return null;
	}

	@Override
	public final Void onPostDecrement(PostDecrementExpression expression, T param) {

		emitExpression(expression.getExpression(), param);
		
		param.append("--");
		
		return null;
	}
	
	private static final char getCArithmeticOperator(Arithmetic arithmetic) {
		final char operator;
		
		switch (arithmetic) {
		case PLUS: 		operator = '+'; break;
		case MINUS: 	operator = '-'; break;
		case MULTIPLY: 	operator = '*'; break;
		case DIVIDE: 	operator = '/'; break;
		case MODULUS: 	operator = '%'; break;
		
		default:
			throw new UnsupportedOperationException("Not an arithemetic operator: " + arithmetic);
		}

		return operator;
	}

	private static final String getCBitwiseOperator(Bitwise bitwise) {
		
		final String operator;
		
		switch (bitwise) {
		case AND: 			operator = "&"; break;
		case OR: 			operator = "|"; break;
		case XOR: 			operator = "^"; break;
		case LEFTSHIFT: 	operator = "<<"; break;
		case RIGHTSHIFT: 	operator = ">>"; break;
		
		default:
			throw new UnsupportedOperationException("Not a bitwise operator: " + bitwise);
		}

		return operator;
	}
	
	private static final String getCRelationalOperator(Relational relational) {
		final String operator;
		
		switch (relational) {
		case EQUALS:					operator = "=="; break;
		case NOT_EQUALS: 				operator = "!="; break;
		case LESS_THAN: 				operator = "<"; break;
		case LESS_THAN_OR_EQUALS: 		operator = "<="; break;
		case GREATER_THAN: 				operator = ">"; break;
		case GREATER_THAN_OR_EQUALS: 	operator = ">="; break;

		default:
			throw new UnsupportedOperationException("Not a relational operator: " + relational);
		}
		
		
		return operator;
	}
	
	private static final String getCLogicalOperator(Logical logical) {
		final String operator;
		
		switch (logical) {
		case AND: 	operator = "&&"; break;
		case OR: 	operator = "||"; break;
		case NOT: 	operator = "!"; break;
		
		default:
			throw new UnsupportedOperationException("Not a logical operator: " + logical);
		}

		return operator;
	}

	private static final OperatorVisitor<Void, String> OPERATOR_TO_STRING_VISITOR = new OperatorVisitor<Void, String>() {
		
		@Override
		public String onRelational(Relational relational, Void param) {
			return getCRelationalOperator(relational);
		}
		
		@Override
		public String onBitwise(Bitwise bitwise, Void param) {
			return getCBitwiseOperator(bitwise);
		}
		
		@Override
		public String onArithmetic(Arithmetic arithmetic, Void param) {
			return "" + getCArithmeticOperator(arithmetic);
		}

		@Override
		public String onLogical(Logical logical, Void param) {
			return getCLogicalOperator(logical);
		}
	};
	
	@Override
	protected final String getOperatorString(Operator operator) {
		return operator.visit(OPERATOR_TO_STRING_VISITOR, null);
	}

	@Override
	public final Void onArithmeticBinary(ArithmeticBinaryExpression expression, T param) {
		
		final char operator = getCArithmeticOperator(expression.getOperator());

		emitExpression(expression.getLhs(), param);

		param.append(' ').append(operator).append(' ');

		return null;
	}

	@Override
	public final Void onAssignment(AssignmentExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onConditionalExpression(ConditionalExpression expression, T param) {
		
		emitExpression(expression.getPart1(), param);
		
		param.append(" ? ");
		
		emitExpression(expression.getPart2(), param);
		
		param.append(" : ");
		
		emitExpression(expression.getPart3(), param);
		
		return null;
	}
}
