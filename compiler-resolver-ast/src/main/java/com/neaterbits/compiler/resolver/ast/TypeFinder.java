package com.neaterbits.compiler.resolver.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.NamespaceReference;
import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.parser.MethodInvocationType;
import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.statement.CatchBlock;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassName;
import com.neaterbits.compiler.ast.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.ast.typedefinition.DefinitionName;
import com.neaterbits.compiler.ast.typedefinition.EnumDefinition;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.ast.typedefinition.InterfaceName;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceLocation;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Stack;
import com.neaterbits.compiler.util.StackDelegator;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UpdateOnResolve;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;

class TypeFinder {

	private static final Boolean DEBUG = false;
	
	private static String getStackPrintout(TypeFinderStack stack) {
		
		return stack.stream()
			.map(entry -> entry.getElement().getClass().getSimpleName())
			.collect(Collectors.toList())
			.toString();
	}
	
	static List<CompiledType> findTypes(ASTParsedFile parsedFile, FileSpec compiledFileSpec) {

		final CompilationUnit compilationUnit = parsedFile.getParsed();
		
		final TypeFinderStack stack = new TypeFinderStack();
		
		final List<CompiledType> parsedTypes = new ArrayList<>();
		
		final Stack<TypeFinderStackEntry> stackWrapper = new StackDelegator<TypeFinderStackEntry>(stack) {

			@Override
			public TypeFinderStackEntry pop() {
				
				final TypeFinderStackEntry stackEntry = super.pop();

				final CompiledType parsedType = makeParsedType(compiledFileSpec, compilationUnit, stack, stackEntry, stack.isEmpty() ? null : stack.get());
				
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
							
							if (!typeReference.isScalar()) {
								throw new IllegalStateException("Expected only scalar types to be resolved");
							}
						}
						else if (e instanceof ResolveLaterTypeReference) {
							final ResolveLaterTypeReference typeReference = (ResolveLaterTypeReference)e;

							final ScopedName name = typeReference.getScopedName();
							
							// Check where we are at in stack
							final TypeFinderStackEntry lastStackEntry = stack.getFromTop(1);
							final BaseASTElement lastElement = lastStackEntry.getElement();
							
							final ReferenceLocation referenceType;
							
							final UpdateOnResolve updateOnResolve;
							final BaseASTElement updateOnResolveElement;
							
							if (lastElement instanceof ClassDefinition || lastElement instanceof InterfaceDefinition) {
								lastStackEntry.addExtendsFrom(compilationUnit, name, TypeVariant.CLASS, typeReference);
								
								referenceType = null;
								updateOnResolve = null;
								updateOnResolveElement = null;
							}
							else if (lastElement instanceof Parameter) {
								referenceType = ReferenceLocation.PARAMETER;
								updateOnResolve = null;
								updateOnResolveElement = null;
							}
							else if (lastElement instanceof ClassMethod || lastElement instanceof InterfaceMethod) {
								referenceType = ReferenceLocation.RETURNTYPE;
								updateOnResolve = null;
								updateOnResolveElement = null;
							}
							else if (lastElement instanceof MethodInvocationExpression) {
								final MethodInvocationExpression methodInvocationExpression = (MethodInvocationExpression)lastElement;
								
								if (methodInvocationExpression.getInvocationType() != MethodInvocationType.NAMED_CLASS_STATIC_OR_STATIC_VAR) {
									throw new UnsupportedOperationException("Expected static class invocation");
								}
								
								updateOnResolve = UpdateOnResolve.METHOD_INVOCATION_EXPRESSION;
								updateOnResolveElement = methodInvocationExpression;

								referenceType = ReferenceLocation.STATIC_OR_STATIC_INSTANCE_METHOD_CALL;
							}
							else if (lastElement instanceof InitializerVariableDeclarationElement) {
								referenceType = ReferenceLocation.VARIABLE_INITIALIZER;
								updateOnResolve = null;
								updateOnResolveElement = null;
							}
							else if (lastElement instanceof ClassDataFieldMember) {
								referenceType = ReferenceLocation.FIELD;
								updateOnResolve = null;
								updateOnResolveElement = null;
							}
							else if (lastElement instanceof CatchBlock) {
								referenceType = ReferenceLocation.CATCH_EXCEPTION;
								updateOnResolve = null;
								updateOnResolveElement = null;
							}
							else if (lastElement instanceof ClassInstanceCreationExpression) {
								referenceType = ReferenceLocation.CLASS_INSTANCE_CREATION;
								updateOnResolve = null;
								updateOnResolveElement = null;
							}
							else {
								throw new UnsupportedOperationException("Unknown scope for type reference " + typeReference.getScopedName()
									+ " for element " + lastElement
									+ " at \"" + lastElement.getContext() + "\" / " + parsedFile.getFileSpec().getDebugName());
							}
							
							if (referenceType != null) {

								final TypeFinderStackEntry typeFrame = findTypeFrame(stack, compiledFileSpec, compilationUnit);
								
								typeFrame.addDependency(compilationUnit, name, referenceType, typeReference, updateOnResolve, updateOnResolveElement);
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
	

	private static TypeFinderStackEntry findTypeFrame(TypeFinderStack stack, FileSpec fileSpec, CompilationUnit compilationUnit) {
		for (int i = stack.size() - 1; i >= 0; -- i) {
			
			final TypeFinderStackEntry stackEntry = stack.get(i);
			final TypeFinderStackEntry lastStackEntry = i > 0 ? stack.get(i - 1) : null;
			
			final TypeVariant typeVariant = processIfTypeElement(stackEntry, lastStackEntry, false, (n, v, t) -> v, fileSpec, compilationUnit);
			
			if (typeVariant != null) {
				return stackEntry;
			}
		}

		return null;
	}
	
	interface ProcessTypeElement<R> {
		R onTypeElement(String name, TypeVariant typeVariant, UserDefinedTypeRef type);
	}
	
	private static UserDefinedTypeRef makeUserDefinedType(NamespaceReference namespaceReference, List<DefinitionName> outerTypes, ComplexTypeDefinition<?, ?> complexTypeDefinition, FileSpec sourceFile, CompilationUnit compilationUnit) {
		
		final String [] outer;
		
		if (outerTypes != null && !outerTypes.isEmpty()) {
			outer = new String[outerTypes.size()];
			
			for (int i = 0; i < outerTypes.size(); ++ i) {
				outer[i] = outerTypes.get(i).getName();
			}
		}
		else {
			outer = null;
		}
		
		return new UserDefinedTypeRef(
				new TypeName(namespaceReference.getParts(), null, complexTypeDefinition.getNameString()),
				sourceFile,
				compilationUnit.getParseTreeRefFromElement(complexTypeDefinition));
	}
	
	private static <R> R processIfTypeElement(TypeFinderStackEntry stackEntry, TypeFinderStackEntry lastStackEntry, boolean createType, ProcessTypeElement<R> process,
			FileSpec fileSpec, CompilationUnit compilationUnit) {
		
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
						? makeUserDefinedType(stackEntry.getNamespace(), outerTypes, classDefinition, fileSpec, compilationUnit)
						: null);
		}
		else if (element instanceof InterfaceDefinition) {
			
			final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)element;
			final String name = interfaceDefinition.getNameString();

			result = process.onTypeElement(
					name,
					TypeVariant.INTERFACE, 
					createType
						? makeUserDefinedType(stackEntry.getNamespace(), outerTypes, interfaceDefinition, fileSpec, compilationUnit)
						: null);
		}
		else if (element instanceof EnumDefinition) {
		
			final EnumDefinition enumDefinition = (EnumDefinition)element;
			
			final String name = enumDefinition.getNameString();
			
			result = process.onTypeElement(
					name,
					TypeVariant.ENUM,
					createType 
						? makeUserDefinedType(stackEntry.getNamespace(), outerTypes, enumDefinition, fileSpec, compilationUnit)
						: null);
		}
		else {
			result = null;
		}
		
		return result;
	}

	
	private static CompiledType makeParsedType(
			FileSpec file,
			CompilationUnit compilationUnit,
			TypeFinderStack stack,
			TypeFinderStackEntry stackEntry,
			TypeFinderStackEntry lastStackEntry) {
		
		return processIfTypeElement(stackEntry, lastStackEntry, true, (name, typeVariant, type) -> makeParsedType(file, stack, stackEntry, name, typeVariant, type), file, compilationUnit);
	}
	
	private static CompiledType makeParsedType(
			FileSpec file,
			TypeFinderStack stack,
			TypeFinderStackEntry stackEntry,
			String name,
			TypeVariant typeVariant,
			UserDefinedTypeRef type) {

		return new CompiledType(
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
