package com.neaterbits.compiler.common.loader.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.BuiltinTypeReference;
import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.StackDelegator;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.common.ast.statement.CatchBlock;
import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.type.complex.EnumType;
import com.neaterbits.compiler.common.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.common.ast.type.primitive.ScalarType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.EnumDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceName;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.TypeSpec;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.parser.MethodInvocationType;
import com.neaterbits.compiler.common.parser.ParsedFile;
import com.neaterbits.compiler.common.resolver.ReferenceType;

class TypeFinder {

	private static final Boolean DEBUG = false;
	
	private static String getStackPrintout(TypeFinderStack stack) {
		
		return stack.stream()
			.map(entry -> entry.getElement().getClass().getSimpleName())
			.collect(Collectors.toList())
			.toString();
	}
	
	static List<CompiledType> findTypes(ParsedFile parsedFile, FileSpec compiledFileSpec) {

		final TypeFinderStack stack = new TypeFinderStack();
		
		final List<CompiledType> parsedTypes = new ArrayList<>();
		
		final Stack<TypeFinderStackEntry> stackWrapper = new StackDelegator<TypeFinderStackEntry>(stack) {

			@Override
			public TypeFinderStackEntry pop() {
				
				final TypeFinderStackEntry stackEntry = super.pop();

				final ParsedType parsedType = makeParsedType(compiledFileSpec, stack, stackEntry);
				
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
							
							if (lastElement instanceof ClassDefinition || lastElement instanceof InterfaceDefinition) {
								lastStackEntry.addExtendsFrom(name, TypeVariant.CLASS, typeReference);
								
								referenceType = null;
							}
							else if (lastElement instanceof Parameter) {
								referenceType = ReferenceType.PARAMETER;
							}
							else if (lastElement instanceof ClassMethod || lastElement instanceof InterfaceMethod) {
								referenceType = ReferenceType.RETURNTYPE;
							}
							else if (lastElement instanceof MethodInvocationExpression) {
								final MethodInvocationExpression methodInvocationExpression = (MethodInvocationExpression)lastElement;
								
								if (methodInvocationExpression.getType() != MethodInvocationType.NAMED_CLASS_STATIC) {
									throw new UnsupportedOperationException("Expected static class invocation");
								}
								
								referenceType = ReferenceType.STATIC_OR_STATIC_INSTANCE_METHOD_CALL;
							}
							else if (lastElement instanceof InitializerVariableDeclarationElement) {
								referenceType = ReferenceType.VARIABLE_INITIALIZER;
							}
							else if (lastElement instanceof ClassDataFieldMember) {
								referenceType = ReferenceType.FIELD;
							}
							else if (lastElement instanceof CatchBlock) {
								referenceType = ReferenceType.CATCH_EXCEPTION;
							}
							else {
								throw new UnsupportedOperationException("Unknown scope for type reference " + typeReference.getTypeName()
									+ " for element " + lastElement
									+ " at \"" + lastElement.getContext() + "\" / " + parsedFile.getFile().getPath().getName());
							}
							
							if (referenceType != null) {

								final TypeFinderStackEntry typeFrame = findTypeFrame(stack);
								
								typeFrame.addDependency(name, referenceType, typeReference);
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

	private static TypeFinderStackEntry findTypeFrame(TypeFinderStack stack) {
		for (int i = stack.size() - 1; i >= 0; -- i) {
			
			final TypeFinderStackEntry stackEntry = stack.get(i);
			
			final TypeVariant typeVariant = processIfTypeElement(stackEntry, false, (n, v, t) -> v);
			
			if (typeVariant != null) {
				return stackEntry;
			}
		}

		return null;
	}
	
	interface ProcessTypeElement<R> {
		R onTypeElement(String name, TypeVariant typeVariant, ComplexType<?> type);
	}
	
	private static <R> R processIfTypeElement(TypeFinderStackEntry stackEntry, boolean createType, ProcessTypeElement<R> process) {
		
		final BaseASTElement element = stackEntry.getElement();
		
		final R result;
		
		if (element instanceof ClassDefinition) {

			final ClassDefinition classDefinition = (ClassDefinition)element;
			final String name = classDefinition.getName().getName();
			
			result = process.onTypeElement(
					name,
					TypeVariant.CLASS,
					createType
						? new ClassType(stackEntry.getNamespace(), stackEntry.getOuterTypes(), classDefinition)
						: null);
		}
		else if (element instanceof InterfaceDefinition) {
			
			final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)element;
			final String name = interfaceDefinition.getName().getName();

			result = process.onTypeElement(
					name,
					TypeVariant.INTERFACE, 
					createType
						? new InterfaceType(stackEntry.getNamespace(), stackEntry.getOuterTypes(), interfaceDefinition)
						: null);
		}
		else if (element instanceof EnumDefinition) {
		
			final EnumDefinition enumDefinition = (EnumDefinition)element;
			
			final String name = enumDefinition.getName().getName();
			
			result = process.onTypeElement(
					name,
					TypeVariant.ENUM,
					createType 
						? new EnumType(stackEntry.getNamespace(), stackEntry.getOuterTypes(), enumDefinition)
						: null);
		}
		else {
			result = null;
		}
		
		return result;
	}

	
	private static ParsedType makeParsedType(FileSpec file, TypeFinderStack stack, TypeFinderStackEntry stackEntry) {
		return processIfTypeElement(stackEntry, true, (name, typeVariant, type) -> makeParsedType(file, stack, stackEntry, name, typeVariant, type));
	}
	
	private static ParsedType makeParsedType(FileSpec file, TypeFinderStack stack, TypeFinderStackEntry stackEntry, String name, TypeVariant typeVariant, ComplexType<?> type) {

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
		
		final TypeName typeName;
		
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
				final String name = classDefinition.getName().getName();
				
				typeName = new ClassName(name);
				
				scope = Arrays.asList(name);
				
				mayHaveNestedTypes = true;
			}
			else if (element instanceof InterfaceDefinition) {
				
				final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)element;
				final String name = interfaceDefinition.getName().getName();
				
				typeName = new InterfaceName(name);

				scope = Arrays.asList(name);
				
				mayHaveNestedTypes = true;
			}
			else if (element instanceof EnumDefinition) {
			
				final EnumDefinition enumDefinition = (EnumDefinition)element;
				final String name = enumDefinition.getName().getName();
				
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
		
		final List<TypeName> outerTypes;
		
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
