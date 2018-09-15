package com.neaterbits.compiler.common.ast.expression;

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
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;

public interface ExpressionVisitor<T, R> {

	R onVariable(VariableExpression expression, T param);
	
	R onAssignment(AssignmentExpression expression, T param);
	
	R onFunctionCall(FunctionCallExpression expression, T param);

	R onConditionExpression(ConditionExpression expression, T param);
	
	R onExpressionList(ExpressionList expression, T param);
	
	// Literals
	R onIntegerLiteral(IntegerLiteral expression, T param);
	R onFloatingPointLiteral(FloatingPointLiteral expression, T param);
	R onBooleanLiteral(BooleanLiteral expression, T param);
	R onCharacterLiteral(CharacterLiteral expression, T param);
	R onStringLiteral(StringLiteral expression, T param);
	R onNullLiteral(NullLiteral expression, T param);
	
	// Arithmetic expressions
	R onPreIncrement(PreIncrementExpression expression, T param);

	R onPreDecrement(PreDecrementExpression expression, T param);

	R onPostIncrement(PostIncrementExpression expression, T param);

	R onPostDecrement(PostDecrementExpression expression, T param);

	R onArithmeticBinary(ArithmeticBinaryExpression expression, T param);
}
