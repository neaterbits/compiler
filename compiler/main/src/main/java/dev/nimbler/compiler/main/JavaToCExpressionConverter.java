package dev.nimbler.compiler.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.expression.ArrayAccessExpression;
import dev.nimbler.compiler.ast.objects.expression.ArrayCreationExpression;
import dev.nimbler.compiler.ast.objects.expression.BlockLambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.CastExpression;
import dev.nimbler.compiler.ast.objects.expression.ClassInstanceCreationExpression;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.FunctionCallExpression;
import dev.nimbler.compiler.ast.objects.expression.FunctionPointerInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.ParameterList;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.expression.PrimaryMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.ResolvedMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.SingleLambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.ThisPrimary;
import dev.nimbler.compiler.ast.objects.expression.UnresolvedMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.literal.StringLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.UnresolvedClassExpression;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.compiler.ast.objects.typereference.FunctionPointerTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.convert.MethodDispatch;
import dev.nimbler.compiler.convert.OOToProceduralConverterUtil;
import dev.nimbler.compiler.convert.ootofunction.BaseExpressionConverter;
import dev.nimbler.compiler.language.java.JavaTypes;
import dev.nimbler.compiler.util.parse.FieldAccessType;
import dev.nimbler.language.codemap.MethodInfo;

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
	public Expression onClassExpression(UnresolvedClassExpression expression, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression onStringLiteral(StringLiteral expression, T param) {
		return new StringLiteral(expression.getContext(), expression.getValue(), JavaTypes.STRING_TYPE, -1);
	}

	@Override
    public Expression onUnresolvedMethodInvocation(UnresolvedMethodInvocationExpression expression, T param) {
	    throw new UnsupportedOperationException();
	}

    @Override
	public Expression onMethodInvocation(ResolvedMethodInvocationExpression expression, T param) {

		final Expression converted;

		switch (expression.getInvocationType()) {

		case PRIMARY:
		case VARIABLE_REFERENCE:
		    final PrimaryMethodInvocationExpression primaryMethodInvocationExpression
		        = (PrimaryMethodInvocationExpression)expression;
			if (primaryMethodInvocationExpression.getObject() == null) {
				throw new IllegalStateException();
			}
            converted = convertInstanceInvocation(primaryMethodInvocationExpression, param);
			break;

		default:
			throw new UnsupportedOperationException("Unknown method type " + expression.getInvocationType()
						+ " for " + expression.getCallable().getName());
		}

		return converted;
	}

	private Expression convertInstanceInvocation(PrimaryMethodInvocationExpression expression, T param) {

		final Expression object = convertExpression(expression.getObject(), param);

		final Expression converted;

		final TypeReference classType = object.getType();

		final int typeNo = param.getTypeNo(classType.getTypeName());

		final MethodInfo methodInfo = param.getMethodInfo(
				classType.getTypeName(),
				expression.getCallable().getName(),
				expression.getParameters().getTypeNames());

		if (methodInfo == null) {
			throw new IllegalStateException("No methodinfo for " + expression.getCallable() + " of " + classType.getTypeName());
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

			final ClassMethod classMethod = null; // OOToProceduralConverterUtil.findMethod(classType, expression.getCallable(), expression.getParameters());

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
									new FunctionPointerTypeReference(context, -1, functionPointerType),
									param.getVTableFunctionFieldName(expression.getCallable()).toFieldName()))
							);

			final FunctionPointerInvocationExpression invocationExpression = new FunctionPointerInvocationExpression(
					context,
					functionPointerType,
					primaryList,
					new ParameterList(params));

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
	public Expression onCastExpression(CastExpression expression, T param) {
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
