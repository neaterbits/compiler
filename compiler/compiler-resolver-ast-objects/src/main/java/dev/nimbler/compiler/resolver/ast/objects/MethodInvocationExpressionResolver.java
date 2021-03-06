package dev.nimbler.compiler.resolver.ast.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jutils.Strings;
import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.ParameterList;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.expression.PrimaryMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.ResolvedMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.StaticMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.literal.Primary;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldName;
import dev.nimbler.compiler.ast.objects.typereference.ComplexTypeReference;
import dev.nimbler.compiler.ast.objects.variables.StaticMemberReference;
import dev.nimbler.compiler.model.common.UserDefinedTypeRef;
import dev.nimbler.compiler.util.TypeResolveMode;
import dev.nimbler.compiler.util.parse.FieldAccessType;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

@Deprecated
public class MethodInvocationExpressionResolver {

	public static void updateOnResolve(
			ScopedName toResolve,
			UserDefinedTypeRef type,
			TypeResolveMode resolveMode,
			ResolvedMethodInvocationExpression methodInvocationExpression) {


		final ScopedName typeScopedName = type.getTypeName().toScopedName();

		final String [] toResolveParts = toResolve.getParts();
		final String [] typeScopedNameParts = typeScopedName.getParts();

		final String [] expressionPart = findExpressionPart(resolveMode, toResolveParts, typeScopedNameParts);

		final ParameterList parameters = methodInvocationExpression.getParameters();

		if (parameters == null) {
			throw new IllegalStateException();
		}

		parameters.take();

		final ResolvedMethodInvocationExpression updatedExpression;

		if (expressionPart != null && expressionPart.length != 0) {
			updatedExpression = new PrimaryMethodInvocationExpression(
				methodInvocationExpression.getContext(),
				// MethodInvocationType.PRIMARY,
				// new ComplexTypeReference(methodInvocationExpression.getContext(), -1, type.getTypeName()),
				makePrimary(methodInvocationExpression.getContext(), type.getTypeName(), expressionPart),
				methodInvocationExpression.getCallable(),
				parameters);
		}
		else {
			updatedExpression = new StaticMethodInvocationExpression(
					methodInvocationExpression.getContext(),
					// MethodInvocationType.NAMED_CLASS_STATIC,
					new ComplexTypeReference(methodInvocationExpression.getContext(), -1, type.getTypeName()),
					// null,
					methodInvocationExpression.getCallable(),
					parameters);
		}

		methodInvocationExpression.replaceWith(updatedExpression);

	}

	private static Primary makePrimary(Context context, TypeName type, String [] expressionPart) {

		final Primary primary;

		if (expressionPart.length == 1) {
			primary = new StaticMemberReference(
					context,
					new ComplexTypeReference(context, -1, type),
					expressionPart[0],
					context);
		}
		else {

			final List<Primary> primaries = new ArrayList<>(expressionPart.length);

			primaries.add(new StaticMemberReference(
					context,
					new ComplexTypeReference(context, -1, type),
					expressionPart[0],
					context));

			TypeName fieldHolderType = type;

			for (int i = 1; i < expressionPart.length; ++ i) {

				final String fieldNameString = expressionPart[i];

				final FieldName fieldName = new FieldName(fieldNameString);

				primaries.add(new FieldAccess(
						context,
						FieldAccessType.FIELD,
						new ComplexTypeReference(context, -1, fieldHolderType),
						fieldName));

				if (i < expressionPart.length - 1) {
					fieldHolderType = null; // (ComplexType<?, ?, ?>)fieldHolderType.getFieldType(fieldName);

					throw new UnsupportedOperationException();
				}
			}

			primary = new PrimaryList(context, primaries);
		}

		return primary;
	}

	static String [] findExpressionPart(TypeResolveMode resolveMode, String [] toResolveParts, String [] typeScopedNameParts) {

		final String [] expressionPart;

		switch (resolveMode) {
		case CLASSNAME_TO_COMPLETE:

			if (typeScopedNameParts.length < toResolveParts.length) {
				throw new IllegalStateException();
			}
			else {
				// Class and static instances, eg. SomeClass.someStaticInstance or SomeClass.someStaticInstance.someField

				// eg. com.test.SomeClass.staticVariable and SomeClass.staticVariable
				// typeScopedNameParts would then contain com.test.SomeClass

				// or com.test.SomeClass.staticVariable and SomeClass.SomeOtherClass.staticVariable.someField
				// typeScopedNameParts would then contain com.test.SomeClass.SomeOtherClass

				String [] foundExpressionPart = null;

				for (int i = typeScopedNameParts.length - 1; i >= 0; -- i) {

					final int numParts = typeScopedNameParts.length - i;


					final String [] lastOfType = Strings.lastOf(typeScopedNameParts, numParts);
					final String [] firstOfToResolve = Arrays.copyOf(toResolveParts, numParts);

					if (Arrays.equals(lastOfType, firstOfToResolve)) {
						final int remainingOfToResolve = toResolveParts.length - firstOfToResolve.length;

						if (remainingOfToResolve == 0) {
							foundExpressionPart = null;
						}
						else {
							foundExpressionPart = Strings.lastOf(toResolveParts, remainingOfToResolve);
						}
						break;
					}
				}

				expressionPart = foundExpressionPart;
			}
			break;

		case COMPLETE_TO_COMPLETE:
			if (typeScopedNameParts.length > toResolveParts.length) {
				throw new IllegalStateException("Length mismatch: "
							+ Arrays.toString(typeScopedNameParts) + "/"
						    + Arrays.toString(toResolveParts));
			}
			else if (typeScopedNameParts.length == toResolveParts.length) {
				expressionPart = null;
			}
			else {
				expressionPart = Strings.lastOf(toResolveParts, toResolveParts.length - typeScopedNameParts.length);
			}
			break;

		default:
			throw new UnsupportedOperationException();
		}

		return expressionPart;
	}
}
