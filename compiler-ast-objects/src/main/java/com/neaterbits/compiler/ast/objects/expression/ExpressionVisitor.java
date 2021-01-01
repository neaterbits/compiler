package com.neaterbits.compiler.ast.objects.expression;

import com.neaterbits.compiler.ast.objects.expression.arithemetic.binary.ArithmeticBinaryExpression;
import com.neaterbits.compiler.ast.objects.expression.arithemetic.unary.UnaryExpression;
import com.neaterbits.compiler.ast.objects.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.ast.objects.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.ast.objects.expression.literal.ClassExpression;
import com.neaterbits.compiler.ast.objects.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.ast.objects.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.ast.objects.expression.literal.NamePrimary;
import com.neaterbits.compiler.ast.objects.expression.literal.NullLiteral;
import com.neaterbits.compiler.ast.objects.expression.literal.StringLiteral;
import com.neaterbits.compiler.ast.objects.variables.VariableReference;

public interface ExpressionVisitor<T, R> {

	R onAssignment(AssignmentExpression expression, T param);
	
	R onFunctionCall(FunctionCallExpression expression, T param);
	
	R onFunctionPointerInvocation(FunctionPointerInvocationExpression expression, T param);
	
	R onClassInstanceCreation(ClassInstanceCreationExpression expression, T param);
	
	R onMethodInvocation(ResolvedMethodInvocationExpression expression, T param);

	R onArrayCreationExpression(ArrayCreationExpression expression, T param);
	
	R onExpressionList(ExpressionList expression, T param);
	
	R onNamePrimary(NamePrimary primary, T param);
	
	R onPrimaryList(PrimaryList expression, T param);
	
	R onArrayAccessExpression(ArrayAccessExpression expression, T param);
	
	R onClassExpression(ClassExpression expression, T param);
	
	R onFieldAccess(FieldAccess expression, T param);
	
	R onCastExpression(CastExpression expression, T param);
	
	R onThis(ThisPrimary expression, T param);
	
	R onVariableReference(VariableReference expression, T param);
	
	R onConditionalExpression(ConditionalExpression expression, T param);
	
	R onNestedExpression(NestedExpression expression, T param);

	R onSingleLambdaExpression(SingleLambdaExpression expression, T param);
	
	R onBlockLambdaExpression(BlockLambdaExpression expression, T param);
	
	// Literals
	R onIntegerLiteral(IntegerLiteral expression, T param);
	R onFloatingPointLiteral(FloatingPointLiteral expression, T param);
	R onBooleanLiteral(BooleanLiteral expression, T param);
	R onCharacterLiteral(CharacterLiteral expression, T param);
	R onStringLiteral(StringLiteral expression, T param);
	R onNullLiteral(NullLiteral expression, T param);
	
	// Arithmetic expressions
	R onArithmeticBinary(ArithmeticBinaryExpression expression, T param);
	
	R onUnaryExpression(UnaryExpression expression, T param);
}
