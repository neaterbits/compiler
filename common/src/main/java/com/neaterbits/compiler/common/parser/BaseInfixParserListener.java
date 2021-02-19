package com.neaterbits.compiler.common.parser;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostIncrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreIncrementExpression;
import com.neaterbits.compiler.common.ast.operator.Arithmetic;
import com.neaterbits.compiler.common.ast.operator.Notation;
import com.neaterbits.compiler.common.ast.operator.Operator;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.stackstate.StackExpressionList;
import com.neaterbits.compiler.common.parser.stackstate.StackIncrementDecrementExpression;

public abstract class BaseInfixParserListener extends BaseParserListener {

	protected BaseInfixParserListener(ParseLogger logger) {
		super(logger);
	}

	public final void onExpressionBinaryOperator(Context context, Operator operator) {
		
		logEnter(context);
		
		final StackExpressionList expressionList = get();
		
		expressionList.addOperator(operator);
		
		logExit(context);
	}
	
	public final void onIncrementDecrementExpressionStart(Context context, Arithmetic operator, Notation notation) {

		logEnter(context);
		
		push(new StackIncrementDecrementExpression(getLogger(), operator, notation));
		
		logExit(context);
	}

	public final void onIncrementDecrementExpressionEnd(Context context) {

		final StackIncrementDecrementExpression stackIncrementDecrementExpression = pop();
		
		final Expression expression;
		
		switch (stackIncrementDecrementExpression.getOperator()) {
		case INCREMENT:
			switch (stackIncrementDecrementExpression.getNotation()) {
			case POSTFIX:
				expression = new PostIncrementExpression(context, stackIncrementDecrementExpression.getExpression());
				break;
				
			case PREFIX:
				expression = new PreIncrementExpression(context, stackIncrementDecrementExpression.getExpression());
				break;
				
			default:
				throw new UnsupportedOperationException("Unknown notation " + stackIncrementDecrementExpression.getNotation());
			}
			break;
			
		case DECREMENT:
			switch (stackIncrementDecrementExpression.getNotation()) {
			case POSTFIX:
				expression = new PostDecrementExpression(context, stackIncrementDecrementExpression.getExpression());
				break;
				
			case PREFIX:
				expression = new PreDecrementExpression(context, stackIncrementDecrementExpression.getExpression());
				break;
				
			default:
				throw new UnsupportedOperationException("Unknown notation " + stackIncrementDecrementExpression.getNotation());
			}
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown operator " + stackIncrementDecrementExpression.getOperator());
		}
		
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(expression);
	}
}
