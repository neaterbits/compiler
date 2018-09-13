package com.neaterbits.compiler.common.ast.expression;

import com.neaterbits.compiler.common.ast.expression.arithemetic.binary.ArithmeticBinaryExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PostIncrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreDecrementExpression;
import com.neaterbits.compiler.common.ast.expression.arithemetic.unary.PreIncrementExpression;

public interface ExpressionVisitor<T, R> {

	R onVariable(VariableExpression expression, T param);
	
	R onFunctionCall(FunctionCallExpression expression, T param);

	R onConditionExpression(ConditionExpression expression, T param);
	
	R onNestedExpression(NestedExpression expression, T param);
	
	// Arithmetic expressions
	R onPreIncrement(PreIncrementExpression expression, T param);

	R onPreDecrement(PreDecrementExpression expression, T param);

	R onPostIncrement(PostIncrementExpression expression, T param);

	R onPostDecrement(PostDecrementExpression expression, T param);

	R onArithmeticBinary(ArithmeticBinaryExpression expression, T param);
}
