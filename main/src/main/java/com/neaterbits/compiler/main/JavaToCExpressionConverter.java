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
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.convert.MethodDispatch;
import com.neaterbits.compiler.common.convert.ootofunction.BaseExpressionConverter;
import com.neaterbits.compiler.common.resolver.codemap.MethodInfo;

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
		
		switch (expression.getInvocationType()) {
		
		case PRIMARY:
		case VARIABLE_REFERENCE:
			if (expression.getObject() == null) {
				throw new IllegalStateException();
			}
			break;
			
		case NAMED_CLASS_STATIC_OR_STATIC_VAR:
			
			System.out.println("## typeReference: " + expression.getClassType().getDebugName());
			
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown method type " + expression.getInvocationType()
						+ " for " + expression.getCallable().getName());
		}
		
		
		if (expression.getObject() != null) {
			converted = convertInstanceInvocation(expression, param);
		}
		else {
			// static invocation
		}
		
		throw new UnsupportedOperationException();
	}
	
	private Expression convertInstanceInvocation(MethodInvocationExpression expression, T param) {

		final Expression object = convertExpression(expression.getObject(), param);

		final Expression converted;
		
		final MethodInfo methodInfo = param.getMethodInfo(
				(ClassType)object.getType(),
				expression.getCallable(),
				expression.getParameters().getTypes());
		
		final MethodDispatch methodDispatch = param.getMethodDispatch(methodInfo);
		
		switch (methodDispatch) {
		case ONE_IMPLEMENTATION:
		case NON_OVERRIDABLE:
			converted = null;
			break;
			
		case FEW_IMPLEMENTATIONS:
		case MANY_IMPLEMENTATIONS:
			throw new UnsupportedOperationException();
			
		case VTABLE:
			throw new UnsupportedOperationException();
			
			
		default:
			throw new UnsupportedOperationException();
		}
		
		
		return converted;
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
