package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.expression.ArrayCreationExpression;
import com.neaterbits.compiler.common.ast.expression.BlockLambdaExpression;
import com.neaterbits.compiler.common.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.FunctionCallExpression;
import com.neaterbits.compiler.common.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.common.ast.expression.SingleLambdaExpression;
import com.neaterbits.compiler.common.ast.expression.ThisPrimary;
import com.neaterbits.compiler.common.ast.expression.literal.ClassExpression;
import com.neaterbits.compiler.common.convert.ootofunction.BaseExpressionConverter;

final class JavaToCExpressionConverter extends BaseExpressionConverter<JavaToCConverterState> {

	@Override
	public Expression onFunctionCall(FunctionCallExpression expression, JavaToCConverterState param) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Expression onClassExpression(ClassExpression expression, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression onMethodInvocation(MethodInvocationExpression expression, JavaToCConverterState param) {

		final Expression converted;
		
		if (expression.getObject() != null) {
			
		}
		
		return null;
	}

	@Override
	public Expression onClassInstanceCreation(ClassInstanceCreationExpression expression, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression onArrayCreationExpression(ArrayCreationExpression expression, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression onThis(ThisPrimary expression, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression onSingleLambdaExpression(SingleLambdaExpression expression, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression onBlockLambdaExpression(BlockLambdaExpression expression, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}
}
