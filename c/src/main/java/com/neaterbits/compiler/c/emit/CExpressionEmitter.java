package com.neaterbits.compiler.c.emit;

import java.util.List;

import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.ConditionExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.FunctionCallExpression;
import com.neaterbits.compiler.common.ast.expression.NestedExpression;
import com.neaterbits.compiler.common.ast.expression.VariableExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.binary.ArithmeticBinaryExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostIncrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreIncrementExpression;
import com.neaterbits.compiler.common.ast.operator.Arithmetic;
import com.neaterbits.compiler.common.ast.operator.Bitwise;
import com.neaterbits.compiler.common.ast.operator.NumericOperator;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.ExpressionEmitter;

public final class CExpressionEmitter implements ExpressionEmitter<EmitterState> {

	private static final CConditionEmitter CONDITION_EMITTER = new CConditionEmitter();

	private void emitCondition(Condition condition, EmitterState param) {
		condition.visit(CONDITION_EMITTER, param);
	}

	private void emitExpression(Expression expression, EmitterState param) {
		expression.visit(this, param);
	}
	
	private void emitVariableReference(VariableReference variable, EmitterState param) {
		param.append(variable.getName().getName());
	}

	@Override
	public Void onVariable(VariableExpression expression, EmitterState param) {

		emitVariableReference(expression.getReference(), param);

		return null;
	}

	@Override
	public Void onFunctionCall(FunctionCallExpression expression, EmitterState param) {
		
		final List<Expression> parameters = expression.getParameters();
		
		for (int i = 0; i < parameters.size(); ++ i) {
			
			if (i > 0) {
				param.append(", ");
			}

			emitExpression(parameters.get(i), param);
		}
		
		return null;
	}

	@Override
	public Void onConditionExpression(ConditionExpression expression, EmitterState param) {

		emitCondition(expression.getCondition(), param);
		
		return null;
	}

	@Override
	public Void onPreIncrement(PreIncrementExpression expression, EmitterState param) {

		param.append("++");
		
		emitExpression(expression.getExpression(), param);
		
		return null;
	}

	@Override
	public Void onPreDecrement(PreDecrementExpression expression, EmitterState param) {

		param.append("--");

		emitExpression(expression.getExpression(), param);
		
		return null;
	}

	@Override
	public Void onPostIncrement(PostIncrementExpression expression, EmitterState param) {

		emitExpression(expression.getExpression(), param);
		
		param.append("++");
		
		return null;
	}

	@Override
	public Void onPostDecrement(PostDecrementExpression expression, EmitterState param) {

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
			throw new UnsupportedOperationException("Not a binary arithemetic operator: " + arithmetic);
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
			throw new UnsupportedOperationException("Not a binary bitwise operator: " + bitwise);
		}

		return operator;
	}

	@Override
	public Void onArithmeticBinary(ArithmeticBinaryExpression expression, EmitterState param) {
		
		final char operator = getCArithmeticOperator(expression.getOperator());

		emitExpression(expression.getLhs(), param);

		param.append(' ').append(operator).append(' ');

		return null;
	}

	@Override
	public Void onNestedExpression(NestedExpression expression, EmitterState param) {
		
		final List<Expression> expressions = expression.getExpressions();
		
		for (int i = 0; i < expressions.size(); ++ i) {
			if (i < expressions.size() - 1) {
				final NumericOperator numericOperator = expression.getOperators().get(i);
				
				param.append(' ');
				
				if (numericOperator.getArithmetic() != null) {
					param.append(getCArithmeticOperator(numericOperator.getArithmetic()));
				}
				else if (numericOperator.getBitwise() != null) {
					param.append(getCBitwiseOperator(numericOperator.getBitwise()));
				}
				
				param.append(' ');
			}
		}
		
		return null;
	}
}
