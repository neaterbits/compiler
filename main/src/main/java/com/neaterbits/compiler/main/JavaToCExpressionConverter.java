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

final class JavaToCExpressionConverter<T extends MappingJavaToCConverterState<T>> extends BaseExpressionConverter<T> {

	@Override
	public Expression onFunctionCall(FunctionCallExpression expression, T param) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Expression onClassExpression(ClassExpression expression, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression onMethodInvocation(MethodInvocationExpression expression, T param) {

		final Expression converted;
		
		if (expression.getObject() != null) {
			
		}
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Expression onClassInstanceCreation(ClassInstanceCreationExpression expression, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Expression onArrayCreationExpression(ArrayCreationExpression expression, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Expression onThis(ThisPrimary expression, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Expression onSingleLambdaExpression(SingleLambdaExpression expression, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Expression onBlockLambdaExpression(BlockLambdaExpression expression, T param) {
		throw new UnsupportedOperationException();
	}
}
