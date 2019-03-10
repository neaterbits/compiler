package com.neaterbits.compiler.ast.parser;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.arithemetic.unary.PostDecrementExpression;
import com.neaterbits.compiler.ast.expression.arithemetic.unary.PostIncrementExpression;
import com.neaterbits.compiler.ast.expression.arithemetic.unary.PreDecrementExpression;
import com.neaterbits.compiler.ast.expression.arithemetic.unary.PreIncrementExpression;
import com.neaterbits.compiler.ast.operator.Arithmetic;
import com.neaterbits.compiler.ast.operator.Notation;
import com.neaterbits.compiler.ast.operator.Operator;
import com.neaterbits.compiler.ast.parser.stackstate.StackExpressionList;
import com.neaterbits.compiler.ast.parser.stackstate.StackIncrementDecrementExpression;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
