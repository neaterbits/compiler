package com.neaterbits.compiler.resolver.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.neaterbits.compiler.ast.expression.FieldAccess;
import com.neaterbits.compiler.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.expression.ParameterList;
import com.neaterbits.compiler.ast.expression.PrimaryList;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.parser.FieldAccessType;
import com.neaterbits.compiler.ast.parser.MethodInvocationType;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.typedefinition.FieldName;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.variables.StaticMemberReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeResolveMode;

class MethodInvocationExpressionResolver {

	static void updateOnResolve(
			ScopedName toResolve,
			BaseType type,
			TypeResolveMode resolveMode,
			MethodInvocationExpression methodInvocationExpression) {
		
		final NamedType namedType = (NamedType)type;
		
		final ScopedName typeScopedName = namedType.getCompleteName().toScopedName();
		
		final String [] toResolveParts = toResolve.getParts();
		final String [] typeScopedNameParts = typeScopedName.getParts();

		final String [] expressionPart = findExpressionPart(resolveMode, toResolveParts, typeScopedNameParts);
		
		final ParameterList parameters = methodInvocationExpression.getParameters();
		
		if (parameters == null) {
			throw new IllegalStateException();
		}
		
		parameters.take();
		
		final ComplexType<?, ?, ?> complexType = (ComplexType<?, ?, ?>)type;
		
		final MethodInvocationExpression updatedExpression;
		
		if (expressionPart != null && expressionPart.length != 0) {
			updatedExpression = new MethodInvocationExpression(
				methodInvocationExpression.getContext(),
				MethodInvocationType.PRIMARY,
				new ComplexTypeReference(methodInvocationExpression.getContext(), complexType),
				makePrimary(methodInvocationExpression.getContext(), complexType, expressionPart),
				methodInvocationExpression.getCallable(),
				parameters);
		}
		else {
			updatedExpression = new MethodInvocationExpression(
					methodInvocationExpression.getContext(),
					MethodInvocationType.NAMED_CLASS_STATIC,
					new ComplexTypeReference(methodInvocationExpression.getContext(), complexType),
					null,
					methodInvocationExpression.getCallable(),
					parameters);
		}
		
		methodInvocationExpression.replaceWith(updatedExpression);
		
	}

	private static Primary makePrimary(Context context, ComplexType<?, ?, ?> type, String [] expressionPart) {
		
		final Primary primary;
		
		if (expressionPart.length == 1) {
			primary = new StaticMemberReference(
					context,
					new ComplexTypeReference(context, type),
					expressionPart[0],
					context);
		}
		else {
			
			final List<Primary> primaries = new ArrayList<>(expressionPart.length);
			
			primaries.add(new StaticMemberReference(
					context,
					new ComplexTypeReference(context, type),
					expressionPart[0],
					context));
			
			ComplexType<?, ?, ?> fieldHolderType = type;
			
			for (int i = 1; i < expressionPart.length; ++ i) {
				
				final String fieldNameString = expressionPart[i];
				
				final FieldName fieldName = new FieldName(fieldNameString);
				
				primaries.add(new FieldAccess(
						context,
						FieldAccessType.FIELD,
						new ComplexTypeReference(context, fieldHolderType),
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
