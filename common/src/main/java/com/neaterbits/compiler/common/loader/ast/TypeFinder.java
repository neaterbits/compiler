package com.neaterbits.compiler.common.loader.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.common.ArrayStack;
import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.ResolvedTypeReference;
import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.StackDelegator;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.common.ast.statement.CatchBlock;
import com.neaterbits.compiler.common.ast.type.primitive.ScalarType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.EnumDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.TypeSpec;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.parser.MethodInvocationType;
import com.neaterbits.compiler.common.parser.ParsedFile;
import com.neaterbits.compiler.common.resolver.ReferenceType;

class TypeFinder {

	private static class StackEntry {
		private final BaseASTElement element;
		private final List<String> scope;
		private final boolean mayHaveNestedTypes;
		
		private List<CompiledType> nestedTypes;
		private List<ScopedName> extendsFrom;
		private List<CompiledTypeDependency> dependencies;
		
		public StackEntry(BaseASTElement element, List<String> scope, boolean mayHaveNestedTypes) {
			this.element = element;
			this.scope = scope;
			this.mayHaveNestedTypes = mayHaveNestedTypes;
		}
		
		void addNestedType(ParsedType nestedType) {
			Objects.requireNonNull(nestedType);
			
			if (this.nestedTypes == null) {
				this.nestedTypes = new ArrayList<>();
			}
			
			this.nestedTypes.add(nestedType);
		}
		
		void addExtendsFrom(ScopedName extendsFrom) {
			Objects.requireNonNull(extendsFrom);
			
			if (this.extendsFrom == null) {
				this.extendsFrom = new ArrayList<>();
			}
			
			this.extendsFrom.add(extendsFrom);
		}
		
		void addDependency(ScopedName scopedName, ReferenceType type) {

			final ParsedTypeReference dependency = new ParsedTypeReference(scopedName, type);
			
			Objects.requireNonNull(dependency);
			Objects.requireNonNull(type);
			
			if (this.dependencies == null) {
				this.dependencies = new ArrayList<>();
			}
			
			this.dependencies.add(dependency);
		}
	}
	
	private static final Boolean DEBUG = false;
	
	private static String getStackPrintout(ArrayStack<StackEntry> stack) {
		
		return stack.stream()
			.map(entry -> entry.element.getClass().getSimpleName())
			.collect(Collectors.toList())
			.toString();
	}
	
	static List<CompiledType> findTypes(ParsedFile parsedFile, FileSpec compiledFileSpec) {

		final ArrayStack<StackEntry> stack = new ArrayStack<>();
		
		final List<CompiledType> parsedTypes = new ArrayList<>();
		
		final Stack<StackEntry> stackWrapper = new StackDelegator<StackEntry>(stack) {

			@Override
			public StackEntry pop() {
				
				final StackEntry stackEntry = super.pop();

				final ParsedType parsedType = makeParsedType(compiledFileSpec, stack, stackEntry);
				
				if (parsedType != null) {
					parsedTypes.add(parsedType);

					if (DEBUG) {
						System.out.println("## add ParsedType: " + parsedType + " at " + getStackPrintout(stack));
					}
					
					for (int i = stack.size() - 1; i >= 0; --i) {
						final StackEntry entry = stack.get(i);
						
						if (entry.mayHaveNestedTypes) {
							entry.addNestedType(parsedType);
						}
					}
				}

				return stackEntry;
			}
		};
		
		parsedFile.iterateNodeFirstWithStack(
				stackWrapper,
				e -> makeStackEntry(stack, e),
				
				e -> {
					if (e instanceof TypeReference) {
						
						if (e instanceof ResolvedTypeReference) {
							final ResolvedTypeReference typeReference = (ResolvedTypeReference)e;
							
							if (!(typeReference.getType() instanceof ScalarType)) {
								throw new IllegalStateException("Expected only scalar types to be resolved");
							}
						}
						else if (e instanceof ResolveLaterTypeReference) {
							final ResolveLaterTypeReference typeReference = (ResolveLaterTypeReference)e;

							final ScopedName name = typeReference.getTypeName();
							
							// Check where we are at in stack
							final BaseASTElement lastElement = stack.getFromTop(1).element;
							
							final StackEntry curElement = stack.get();

							final ReferenceType referenceType;
							
							if (lastElement instanceof ClassDefinition || lastElement instanceof InterfaceDefinition) {
								curElement.addExtendsFrom(name);
								
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
								
								referenceType = ReferenceType.STATIC_METHOD_CALL;
							}
							else if (lastElement instanceof InitializerVariableDeclarationElement) {
								referenceType = ReferenceType.VARIABLE_INITIALIZER;
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
								curElement.addDependency(name, referenceType);
							}
						}
						else {
							throw new UnsupportedOperationException("Unknown typre reference " + e.getClass().getName());
						}
					}
				}
		);

		return parsedTypes;
	}

	
	private static ParsedType makeParsedType(FileSpec file, ArrayStack<StackEntry> stack, StackEntry stackEntry) {
		
		final ParsedType parsedType;
		
		final BaseASTElement element = stackEntry.element;
		
		if (element instanceof ClassDefinition) {

			final ClassDefinition classDefinition = (ClassDefinition)element;
			final String name = classDefinition.getName().getName();
			
			parsedType = makeParsedType(file, stack, stackEntry, name, TypeVariant.CLASS);
		}
		else if (element instanceof InterfaceDefinition) {
			
			final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)element;
			final String name = interfaceDefinition.getName().getName();

			parsedType = makeParsedType(file, stack, stackEntry, name, TypeVariant.INTERFACE);
		}
		else if (element instanceof EnumDefinition) {
		
			final EnumDefinition enumDefinition = (EnumDefinition)element;
			
			final String name = enumDefinition.getName().getName();
			
			parsedType = makeParsedType(file, stack, stackEntry, name, TypeVariant.ENUM);
		}
		else {
			parsedType = null;
		}
		
		return parsedType;
	}
	
	private static ParsedType makeParsedType(FileSpec file, Stack<StackEntry> stack, StackEntry stackEntry, String name, TypeVariant typeVariant) {

		return new ParsedType(
				file,
				makeTypeSpec(stack, name, TypeVariant.CLASS),
				stackEntry.nestedTypes,
				stackEntry.extendsFrom,
				stackEntry.dependencies);
	}
	
	private static TypeSpec makeTypeSpec(Stack<StackEntry> stack, String name, TypeVariant typeVariant) {
		return new TypeSpec(makeScopedName(stack, name), typeVariant);
	}
	
	private static ScopedName makeScopedName(Stack<StackEntry> stack, String name) {

		final List<String> names = new ArrayList<>();
		
		for (int i = 0; i < stack.size(); ++ i) {
			
			final StackEntry stackEntry = stack.get(i);

			final List<String> entryNames = stackEntry.scope;
			
			if (entryNames != null) {
				names.addAll(entryNames);
			}
		}

		final ScopedName scopedName = new ScopedName(names, name);

		return scopedName;
	}

	private static StackEntry makeStackEntry(ArrayStack<StackEntry> stack, BaseASTElement element) {
		
		final List<String> scope;
		
		final boolean mayHaveNestedTypes;
		
		if (element instanceof Namespace) {

			final Namespace namespace = (Namespace)element;

			scope = Arrays.asList(namespace.getReference().getParts());
			
			mayHaveNestedTypes = false;
		}
		else if (element instanceof ClassDefinition) {

			final ClassDefinition classDefinition = (ClassDefinition)element;
			final String name = classDefinition.getName().getName();
			
			scope = Arrays.asList(name);
			
			mayHaveNestedTypes = true;
		}
		else if (element instanceof InterfaceDefinition) {
			
			final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)element;
			final String name = interfaceDefinition.getName().getName();
			
			scope = Arrays.asList(name);
			
			mayHaveNestedTypes = true;
		}
		else if (element instanceof EnumDefinition) {
		
			final EnumDefinition enumDefinition = (EnumDefinition)element;
			final String name = enumDefinition.getName().getName();
			
			scope = Arrays.asList(name);
			
			mayHaveNestedTypes = true;
		}
		else {
			scope = null;
			
			mayHaveNestedTypes = false;
		}
		
		return new StackEntry(element, scope, mayHaveNestedTypes);
	}

}
