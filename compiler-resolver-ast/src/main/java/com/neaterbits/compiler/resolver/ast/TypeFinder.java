package com.neaterbits.compiler.resolver.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.NamespaceReference;
import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.expression.FieldAccess;
import com.neaterbits.compiler.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.expression.ParameterList;
import com.neaterbits.compiler.ast.expression.PrimaryList;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.parser.FieldAccessType;
import com.neaterbits.compiler.ast.parser.MethodInvocationType;
import com.neaterbits.compiler.ast.parser.ParsedFile;
import com.neaterbits.compiler.ast.statement.CatchBlock;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.complex.EnumType;
import com.neaterbits.compiler.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.ast.type.primitive.ScalarType;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassName;
import com.neaterbits.compiler.ast.typedefinition.DefinitionName;
import com.neaterbits.compiler.ast.typedefinition.EnumDefinition;
import com.neaterbits.compiler.ast.typedefinition.FieldName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.ast.typedefinition.InterfaceName;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.variables.StaticMemberReference;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Stack;
import com.neaterbits.compiler.util.StackDelegator;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeResolveMode;

class TypeFinder {

	private static final Boolean DEBUG = false;
	
	private static String getStackPrintout(TypeFinderStack stack) {
		
		return stack.stream()
			.map(entry -> entry.getElement().getClass().getSimpleName())
			.collect(Collectors.toList())
			.toString();
	}
	
	static List<CompiledType<ComplexType<?, ?, ?>>> findTypes(ParsedFile parsedFile, FileSpec compiledFileSpec) {

		final TypeFinderStack stack = new TypeFinderStack();
		
		final List<CompiledType<ComplexType<?, ?, ?>>> parsedTypes = new ArrayList<>();
		
		final Stack<TypeFinderStackEntry> stackWrapper = new StackDelegator<TypeFinderStackEntry>(stack) {

			@Override
			public TypeFinderStackEntry pop() {
				
				final TypeFinderStackEntry stackEntry = super.pop();

				final ParsedType parsedType = makeParsedType(compiledFileSpec, stack, stackEntry, stack.isEmpty() ? null : stack.get());
				
				if (parsedType != null) {
					parsedTypes.add(parsedType);

					if (DEBUG) {
						System.out.println("## add ParsedType: " + parsedType + " at " + getStackPrintout(stack));
					}
					
					for (int i = stack.size() - 1; i >= 0; --i) {
						final TypeFinderStackEntry entry = stack.get(i);
						
						if (entry.mayHaveNestedTypes()) {
							entry.addNestedType(parsedType);
						}
					}
				}

				return stackEntry;
			}
		};
		
		parsedFile.iterateNodeFirstWithStack(
				stackWrapper,
				e -> makeStackEntry(stack, e, stack.isEmpty() ? null : stack.get()),
				
				e -> {
					if (e instanceof TypeReference) {
						
						if (e instanceof BuiltinTypeReference) {
							final BuiltinTypeReference typeReference = (BuiltinTypeReference)e;
							
							if (!(typeReference.getType() instanceof ScalarType)) {
								throw new IllegalStateException("Expected only scalar types to be resolved");
							}
						}
						else if (e instanceof ResolveLaterTypeReference) {
							final ResolveLaterTypeReference typeReference = (ResolveLaterTypeReference)e;

							final ScopedName name = typeReference.getTypeName();
							
							// Check where we are at in stack
							final TypeFinderStackEntry lastStackEntry = stack.getFromTop(1);
							final BaseASTElement lastElement = lastStackEntry.getElement();
							
							final ReferenceType referenceType;
							
							final BiConsumer<BaseType, TypeResolveMode> updateOnResolve;
							
							if (lastElement instanceof ClassDefinition || lastElement instanceof InterfaceDefinition) {
								lastStackEntry.addExtendsFrom(name, TypeVariant.CLASS, typeReference);
								
								referenceType = null;
								updateOnResolve = null;
							}
							else if (lastElement instanceof Parameter) {
								referenceType = ReferenceType.PARAMETER;
								updateOnResolve = null;
							}
							else if (lastElement instanceof ClassMethod || lastElement instanceof InterfaceMethod) {
								referenceType = ReferenceType.RETURNTYPE;
								updateOnResolve = null;
							}
							else if (lastElement instanceof MethodInvocationExpression) {
								final MethodInvocationExpression methodInvocationExpression = (MethodInvocationExpression)lastElement;
								
								if (methodInvocationExpression.getInvocationType() != MethodInvocationType.NAMED_CLASS_STATIC_OR_STATIC_VAR) {
									throw new UnsupportedOperationException("Expected static class invocation");
								}
								
								final ScopedName toResolve = typeReference.getTypeName();
								
								updateOnResolve = (type, resolveMode) -> {
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
								};
								
								referenceType = ReferenceType.STATIC_OR_STATIC_INSTANCE_METHOD_CALL;
							}
							else if (lastElement instanceof InitializerVariableDeclarationElement) {
								referenceType = ReferenceType.VARIABLE_INITIALIZER;
								updateOnResolve = null;
							}
							else if (lastElement instanceof ClassDataFieldMember) {
								referenceType = ReferenceType.FIELD;
								updateOnResolve = null;
							}
							else if (lastElement instanceof CatchBlock) {
								referenceType = ReferenceType.CATCH_EXCEPTION;
								updateOnResolve = null;
							}
							else {
								throw new UnsupportedOperationException("Unknown scope for type reference " + typeReference.getTypeName()
									+ " for element " + lastElement
									+ " at \"" + lastElement.getContext() + "\" / " + parsedFile.getFile().getPath().getName());
							}
							
							if (referenceType != null) {

								final TypeFinderStackEntry typeFrame = findTypeFrame(stack);
								
								typeFrame.addDependency(name, referenceType, typeReference, updateOnResolve);
							}
						}
						else {
							throw new UnsupportedOperationException("Unknown type reference " + e.getClass().getName());
						}
					}
				}
		);

		return parsedTypes;
	}
	
	static Primary makePrimary(Context context, ComplexType<?, ?, ?> type, String [] expressionPart) {
		
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
					fieldHolderType = (ComplexType<?, ?, ?>)fieldHolderType.getFieldType(fieldName);
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

	private static TypeFinderStackEntry findTypeFrame(TypeFinderStack stack) {
		for (int i = stack.size() - 1; i >= 0; -- i) {
			
			final TypeFinderStackEntry stackEntry = stack.get(i);
			final TypeFinderStackEntry lastStackEntry = i > 0 ? stack.get(i - 1) : null;
			
			final TypeVariant typeVariant = processIfTypeElement(stackEntry, lastStackEntry, false, (n, v, t) -> v);
			
			if (typeVariant != null) {
				return stackEntry;
			}
		}

		return null;
	}
	
	interface ProcessTypeElement<R> {
		R onTypeElement(String name, TypeVariant typeVariant, ComplexType<?, ?, ?> type);
	}
	
	private static <R> R processIfTypeElement(TypeFinderStackEntry stackEntry, TypeFinderStackEntry lastStackEntry, boolean createType, ProcessTypeElement<R> process) {
		
		final BaseASTElement element = stackEntry.getElement();
		
		final R result;
		
		final List<DefinitionName> outerTypes = lastStackEntry != null ? lastStackEntry.getOuterTypes() : null;
		
		if (element instanceof ClassDefinition) {

			final ClassDefinition classDefinition = (ClassDefinition)element;
			final String name = classDefinition.getNameString();
			
			result = process.onTypeElement(
					name,
					TypeVariant.CLASS,
					createType
						? new ClassType(stackEntry.getNamespace(), outerTypes, classDefinition)
						: null);
		}
		else if (element instanceof InterfaceDefinition) {
			
			final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)element;
			final String name = interfaceDefinition.getNameString();

			result = process.onTypeElement(
					name,
					TypeVariant.INTERFACE, 
					createType
						? new InterfaceType(stackEntry.getNamespace(), outerTypes, interfaceDefinition)
						: null);
		}
		else if (element instanceof EnumDefinition) {
		
			final EnumDefinition enumDefinition = (EnumDefinition)element;
			
			final String name = enumDefinition.getNameString();
			
			result = process.onTypeElement(
					name,
					TypeVariant.ENUM,
					createType 
						? new EnumType(stackEntry.getNamespace(), outerTypes, enumDefinition)
						: null);
		}
		else {
			result = null;
		}
		
		return result;
	}

	
	private static ParsedType makeParsedType(
			FileSpec file,
			TypeFinderStack stack,
			TypeFinderStackEntry stackEntry,
			TypeFinderStackEntry lastStackEntry) {
		
		return processIfTypeElement(stackEntry, lastStackEntry, true, (name, typeVariant, type) -> makeParsedType(file, stack, stackEntry, name, typeVariant, type));
	}
	
	private static ParsedType makeParsedType(
			FileSpec file,
			TypeFinderStack stack,
			TypeFinderStackEntry stackEntry,
			String name,
			TypeVariant typeVariant,
			ComplexType<?, ?, ?> type) {

		return new ParsedType(
				file,
				makeTypeSpec(stack, name, TypeVariant.CLASS),
				type,
				stackEntry.getNestedTypes(),
				stackEntry.getExtendsFrom(),
				stackEntry.getDependencies());
	}
	
	private static TypeSpec makeTypeSpec(TypeFinderStack stack, String name, TypeVariant typeVariant) {
		return new TypeSpec(makeScopedName(stack, name), typeVariant);
	}
	
	private static ScopedName makeScopedName(TypeFinderStack stack, String name) {

		final List<String> names = new ArrayList<>();
		
		for (int i = 0; i < stack.size(); ++ i) {
			
			final TypeFinderStackEntry stackEntry = stack.get(i);

			final List<String> entryNames = stackEntry.getScope();
			
			if (entryNames != null) {
				names.addAll(entryNames);
			}
		}

		final ScopedName scopedName = new ScopedName(names, name);

		return scopedName;
	}

	private static TypeFinderStackEntry makeStackEntry(TypeFinderStack stack, BaseASTElement element, TypeFinderStackEntry last) {
		
		final List<String> scope;
		
		final boolean mayHaveNestedTypes;
		
		final NamespaceReference namespaceReference;
		
		final DefinitionName typeName;
		
		if (element instanceof Namespace) {

			final Namespace namespace = (Namespace)element;

			scope = Arrays.asList(namespace.getReference().getParts());
			
			typeName = null;
			
			namespaceReference = namespace.getReference();
			
			mayHaveNestedTypes = false;
		}
		else {
			
			namespaceReference = last != null ? last.getNamespace() : null;
			
			if (element instanceof ClassDefinition) {
	
				final ClassDefinition classDefinition = (ClassDefinition)element;
				final String name = classDefinition.getNameString();
				
				typeName = new ClassName(name);
				
				scope = Arrays.asList(name);
				
				mayHaveNestedTypes = true;
			}
			else if (element instanceof InterfaceDefinition) {
				
				final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)element;
				final String name = interfaceDefinition.getNameString();
				
				typeName = new InterfaceName(name);

				scope = Arrays.asList(name);
				
				mayHaveNestedTypes = true;
			}
			else if (element instanceof EnumDefinition) {
			
				final EnumDefinition enumDefinition = (EnumDefinition)element;
				final String name = enumDefinition.getNameString();
				
				typeName = new ClassName(name);
				
				scope = Arrays.asList(name);
				
				mayHaveNestedTypes = true;
			}
			else {
				
				typeName = null;
				
				scope = null;
				
				mayHaveNestedTypes = false;
			}
		}
		
		final List<DefinitionName> outerTypes;
		
		if (typeName == null) {
			outerTypes = last != null ? last.getOuterTypes() : null;
		}
		else {
		
			if (last != null && last.getOuterTypes() != null) {
				outerTypes = new ArrayList<>(last.getOuterTypes());
				outerTypes.add(typeName);
			}
			else {
				outerTypes = Arrays.asList(typeName);
			}
		}
		
		return new TypeFinderStackEntry(element, scope, namespaceReference, outerTypes, mayHaveNestedTypes);
	}
}
