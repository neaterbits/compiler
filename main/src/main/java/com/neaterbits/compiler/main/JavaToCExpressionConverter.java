package com.neaterbits.compiler.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.FunctionPointerTypeReference;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.expression.ArrayAccessExpression;
import com.neaterbits.compiler.common.ast.expression.ArrayCreationExpression;
import com.neaterbits.compiler.common.ast.expression.BlockLambdaExpression;
import com.neaterbits.compiler.common.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.FieldAccess;
import com.neaterbits.compiler.common.ast.expression.FunctionCallExpression;
import com.neaterbits.compiler.common.ast.expression.FunctionPointerInvocationExpression;
import com.neaterbits.compiler.common.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.common.ast.expression.ParameterList;
import com.neaterbits.compiler.common.ast.expression.PrimaryList;
import com.neaterbits.compiler.common.ast.expression.SingleLambdaExpression;
import com.neaterbits.compiler.common.ast.expression.ThisPrimary;
import com.neaterbits.compiler.common.ast.expression.literal.ClassExpression;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.common.ast.type.FunctionPointerType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.convert.MethodDispatch;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterUtil;
import com.neaterbits.compiler.common.convert.ootofunction.BaseExpressionConverter;
import com.neaterbits.compiler.common.parser.FieldAccessType;
import com.neaterbits.compiler.common.resolver.codemap.MethodInfo;
import com.neaterbits.compiler.java.JavaTypes;

final class JavaToCExpressionConverter<T extends MappingJavaToCConverterState<T>> extends BaseExpressionConverter<T> {

	@Override
	public Expression onFunctionCall(FunctionCallExpression expression, T param) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Expression onFunctionPointerInvocation(FunctionPointerInvocationExpression expression, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Expression onClassExpression(ClassExpression expression, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression onStringLiteral(StringLiteral expression, T param) {
		return new StringLiteral(expression.getContext(), expression.getValue(), JavaTypes.STRING_TYPE);
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
			throw new UnsupportedOperationException();
		}

		return converted;
	}
	
	private Expression convertInstanceInvocation(MethodInvocationExpression expression, T param) {

		final Expression object = convertExpression(expression.getObject(), param);

		final Expression converted;
		
		final ClassType classType = (ClassType)object.getType();
		
		final int typeNo = param.getTypeNo(classType);
		
		final MethodInfo methodInfo = param.getMethodInfo(
				classType,
				expression.getCallable(),
				expression.getParameters().getTypes());
		
		if (methodInfo == null) {
			throw new IllegalStateException("No methodinfo for " + expression.getCallable() + " of " + classType.getCompleteName());
		}
		
		final MethodDispatch methodDispatch = param.getMethodDispatch(methodInfo);
		
		final List<Expression> convertedParams = convertExpressions(expression.getParameters().getList(), param);
		
		switch (methodDispatch) {
		case ONE_IMPLEMENTATION:
		case NON_OVERRIDABLE:
			converted = null;
			break;
			
		case FEW_IMPLEMENTATIONS:
		case MANY_IMPLEMENTATIONS:
			throw new UnsupportedOperationException();
			
		case VTABLE:
			
			final ArrayAccessExpression arrayAccessExpression = makeStaticArrayAccess(
					expression.getContext(),
					param.getClassStaticVTableArrayName(),
					typeNo,
					param);
			
			final List<Expression> params = new ArrayList<>(convertedParams.size() + 1);

			params.add(object);
			params.addAll(convertedParams);
			
			final ClassMethod classMethod = OOToProceduralConverterUtil.findMethod(classType, expression.getCallable(), expression.getParameters());
			
			final FunctionPointerType functionPointerType = OOToProceduralConverterUtil.makeFunctionPointerType(
					classMethod,
					type -> param.convertType(type));
			
			final Context context = expression.getContext();
			
			final PrimaryList primaryList = new PrimaryList(
					context,
					Arrays.asList(
							arrayAccessExpression,
							new FieldAccess(
									context,
									FieldAccessType.FIELD,
									new FunctionPointerTypeReference(context, functionPointerType),
									param.getVTableFunctionFieldName(expression.getCallable())))
							);

			final FunctionPointerInvocationExpression invocationExpression = new FunctionPointerInvocationExpression(
					context,
					functionPointerType,
					primaryList,
					new ParameterList(context, params));
			
			converted = invocationExpression;
			
			break;
			
			
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
