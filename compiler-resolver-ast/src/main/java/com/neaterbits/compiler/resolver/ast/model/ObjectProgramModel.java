package com.neaterbits.compiler.resolver.ast.model;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.compiler.ast.ASTVisitor;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.FieldNameDeclaration;
import com.neaterbits.compiler.ast.ImportName;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.NamespaceDeclaration;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.expression.PrimaryList;
import com.neaterbits.compiler.ast.expression.ThisPrimary;
import com.neaterbits.compiler.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.statement.EnumConstant;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.ast.typedefinition.ConstructorModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.VariableModifierHolder;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.typereference.LibraryTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.ScalarTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.variables.NameReference;
import com.neaterbits.compiler.ast.variables.VarNameDeclaration;
import com.neaterbits.compiler.resolver.ScopedNameResolver;
import com.neaterbits.compiler.resolver.TypesMap;
import com.neaterbits.compiler.resolver.ast.MethodInvocationExpressionResolver;
import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Stack;
import com.neaterbits.compiler.util.StackDelegator;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;
import com.neaterbits.compiler.util.imports.TypeImport;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;
import com.neaterbits.compiler.util.model.FieldModifiers;
import com.neaterbits.compiler.util.model.FieldVisitor;
import com.neaterbits.compiler.util.model.ISourceToken;
import com.neaterbits.compiler.util.model.LibraryTypeRef;
import com.neaterbits.compiler.util.model.MethodVariant;
import com.neaterbits.compiler.util.model.MethodVisitor;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.ProgramModel;
import com.neaterbits.compiler.util.model.ResolveTypesModel;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.model.SourceToken;
import com.neaterbits.compiler.util.model.SourceTokenType;
import com.neaterbits.compiler.util.model.SourceTokenVisitor;
import com.neaterbits.compiler.util.model.TypeSources;
import com.neaterbits.compiler.util.model.UpdateOnResolve;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.util.model.Visibility;
import com.neaterbits.compiler.util.parse.ScopesListener;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.util.typedefinition.FieldModifier;
import com.neaterbits.compiler.util.typedefinition.FieldStatic;
import com.neaterbits.compiler.util.typedefinition.FieldTransient;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldVolatile;
import com.neaterbits.compiler.util.typedefinition.Subclassing;

import statement.ASTMutability;

public class ObjectProgramModel
	extends ObjectImportsModel
	implements ProgramModel<Program, ASTParsedFile, CompilationUnit>,
				ResolveTypesModel<CompilationUnit>{

	private final FieldModifiers dataFieldDefaultModifiers;
	
	public ObjectProgramModel() {
		this(Collections.emptyList(), new FieldModifiers(false, Visibility.NAMESPACE, Mutability.MUTABLE, false, false));
	}

	public ObjectProgramModel(List<TypeImport> implicitImports, FieldModifiers dataFieldDefaultModifiers) {
		super(implicitImports);
		
		this.dataFieldDefaultModifiers = dataFieldDefaultModifiers;
	}

	@Override
	public ASTParsedFile getParsedFile(Program program, FileSpec path) {
		
		return (ASTParsedFile)program.findElement(false, element -> {
			
			if (element instanceof ASTParsedFile) {
				final ASTParsedFile parsedFile = (ASTParsedFile)element;
				
				if (parsedFile.getFileSpec().equals(path)) {
					return true;
				}
			}
			
			return false;
		});
	}
	
	private static class Element {
		private final BaseASTElement astElement;
		private final ISourceToken token;
		
		Element(BaseASTElement astElement, ISourceToken token) {
			this.astElement = astElement;
			this.token = token;
		}
	}
	
	@Override
	public void iterate(CompilationUnit sourceFile, SourceTokenVisitor visitor, ResolvedTypes resolvedTypes, boolean visitPlaceholderElements) {

		final ArrayStack<Element> stack = new ArrayStack<>();
		
		final Stack<Element> stackWrapper = new StackDelegator<Element>(stack) {

			@Override
			public void push(Element element) {

				super.push(element);

				if (visitPlaceholderElements || !element.astElement.isPlaceholderElement()) {
					visitor.onPush(element.token);
				}
			}

			@Override
			public Element pop() {

				final Element element = super.pop();
				
				if (visitPlaceholderElements || !element.astElement.isPlaceholderElement()) {
					visitor.onPop(element.token);
				}
				
				return element;
			}
		};

		sourceFile.iterateNodeFirstWithStack(
				stackWrapper,
				
				baseASTElement -> new Element(
						baseASTElement,
					 	baseASTElement.isPlaceholderElement() && !visitPlaceholderElements
								? null
								: makeSourceToken(baseASTElement, sourceFile, resolvedTypes)),
				
				new ASTVisitor() {
			
			@Override
			public void onElement(BaseASTElement element) {
				
				if (stack.isEmpty()) {
					
					if (element.isPlaceholderElement()) {
						throw new IllegalStateException();
					}
					
					visitor.onToken(makeSourceToken(element, sourceFile, resolvedTypes));
				}
				else {
					
					final Element e = stack.get();
					
					if (e.token != null) {
						visitor.onToken(e.token);
					}
					else {
						if (!visitPlaceholderElements && !e.astElement.isPlaceholderElement()) {
							throw new IllegalStateException();
						}
					}
				}
			}
		});
	}


	@Override
	public CompilationUnit getCompilationUnit(ASTParsedFile sourceFile) {
		return sourceFile.getParsed();
	}

	@Override
	public ISourceToken getTokenAtOffset(CompilationUnit compilationUnit, long offset, ResolvedTypes resolvedTypes) {

		final BaseASTElement found = compilationUnit.findElement(true, element -> {

			final Context context = element.getContext();

			return     !element.isPlaceholderElement()
					&& offset >= context.getStartOffset()
					&& offset <= context.getEndOffset();
		});
		
		return found != null ? makeSourceToken(found, compilationUnit, resolvedTypes) : null;
	}
	
	
	@Override
	public ISourceToken getTokenAtParseTreeRef(CompilationUnit compilationUnit, int parseTreeRef, ResolvedTypes resolvedTypes) {

		final BaseASTElement found = compilationUnit.findElement(true, element -> {

			final Context context = element.getContext();

			return !element.isPlaceholderElement() && parseTreeRef == context.getTokenSequenceNo();
		});
		
		return found != null ? makeSourceToken(found, compilationUnit, resolvedTypes) : null;
	}

	private SourceToken makeSourceToken(BaseASTElement element, CompilationUnit compilationUnit, ResolvedTypes resolvedTypes) {
		
		final SourceToken sourceToken;
		
		if (element.isPlaceholderElement()) {
			sourceToken = new SourceToken(element.getClass().getSimpleName());
		}
		else {
			sourceToken = makeSourceTokenForNonPlaceholder(element, compilationUnit, resolvedTypes);
		}
		
		return sourceToken;
	}

	private SourceToken makeSourceTokenForNonPlaceholder(BaseASTElement element, CompilationUnit compilationUnit, ResolvedTypes resolvedTypes) {
		
		Objects.requireNonNull(element);

		final SourceTokenType sourceTokenType;
		TypeName typeName = null;
		
		if (element instanceof Keyword) {
			sourceTokenType = SourceTokenType.KEYWORD;
		}
		else if (   element instanceof InterfaceModifierHolder
				 || element instanceof ClassModifierHolder
				 || element instanceof ConstructorModifierHolder
				 || element instanceof ClassMethodModifierHolder
				 || element instanceof FieldModifierHolder
				 || element instanceof VariableModifierHolder) {
			
			sourceTokenType = SourceTokenType.KEYWORD;
		}
		else if (element instanceof CharacterLiteral) {
			sourceTokenType = SourceTokenType.CHARACTER_LITERAL;
		}
		else if (element instanceof StringLiteral) {
			sourceTokenType = SourceTokenType.STRING_LITERAL;
		}
		else if (element instanceof IntegerLiteral) {
			sourceTokenType = SourceTokenType.INTEGER_LITERAL;
		}
		else if (element instanceof BooleanLiteral) {
			sourceTokenType = SourceTokenType.BOOLEAN_LITERAL;
		}
		else if (element instanceof NullLiteral) {
			sourceTokenType = SourceTokenType.NULL_LITERAL;
		}
		else if (element instanceof ThisPrimary) {
			sourceTokenType = SourceTokenType.THIS_REFERENCE;
		}
		else if (element instanceof VarNameDeclaration) {
			sourceTokenType = SourceTokenType.LOCAL_VARIABLE_DECLARATION_NAME;
		}
		else if (element instanceof NameReference) {
			sourceTokenType = SourceTokenType.VARIABLE_REFERENCE;
		}
		else if (element instanceof NamespaceDeclaration) {
			sourceTokenType = SourceTokenType.NAMESPACE_DECLARATION_NAME;
		}
		else if (element instanceof ImportName) {
			sourceTokenType = SourceTokenType.IMPORT_NAME;
		}
		else if (element instanceof ClassDeclarationName) {
			sourceTokenType = SourceTokenType.CLASS_DECLARATION_NAME;
		}
		else if (element instanceof FieldNameDeclaration) {
			sourceTokenType = SourceTokenType.INSTANCE_VARIABLE_DECLARATION_NAME;
		}
		else if (element instanceof InterfaceDeclarationName) {
			sourceTokenType = SourceTokenType.INTERFACE_DECLARATION_NAME;
		}
		else if (element instanceof InterfaceMethodName) {
			sourceTokenType = SourceTokenType.METHOD_DECLARATION_NAME;
		}
		else if (element instanceof BuiltinTypeReference) {
			sourceTokenType = SourceTokenType.BUILTIN_TYPE_NAME;
			
			final BuiltinTypeReference builtinTypeReference = (BuiltinTypeReference)element;
			
			typeName = builtinTypeReference.getTypeName();
		}
		else if (element instanceof EnumConstant) {
			sourceTokenType = SourceTokenType.ENUM_CONSTANT;
		}
		else if (element instanceof ResolveLaterTypeReference) {

			// Resolve from already resolved types
			final ResolveLaterTypeReference typeReference = (ResolveLaterTypeReference)element;
			final TypesMap<TypeName> compiledTypesMap = new TypesMap<TypeName>() {

				@Override
				public TypeName lookupByScopedName(ScopedName scopedName) {
					return resolvedTypes.lookup(scopedName, TypeSources.ALL);
				}
			};

			final Namespace namespace = (Namespace)compilationUnit.findElement(false, e -> e instanceof Namespace);
			final ComplexTypeDefinition<?, ?> definition = (ComplexTypeDefinition<?, ?>)compilationUnit.findElement(false, e -> e instanceof ComplexTypeDefinition<?, ?>);

			if (definition != null) {
			
				final ScopedName referencedFrom = new ScopedName(
						namespace != null
							? Arrays.asList(namespace.getParts())
							: null,
						definition.getNameString());
				
				final TypeName resolved = ScopedNameResolver.resolveScopedName(
						typeReference.getScopedName(),
						null,
						compilationUnit,
						this,
						referencedFrom,
						compiledTypesMap);
				
				if (resolved != null) {
					sourceTokenType = SourceTokenType.CLASS_REFERENCE_NAME;
					typeName = resolved;
				}
				else {
					sourceTokenType = SourceTokenType.UNKNOWN;
				}
			}
			else {
				sourceTokenType = SourceTokenType.UNKNOWN;
			}
		}
		else {
			sourceTokenType = SourceTokenType.UNKNOWN;
		}
		
		final Context context = element.getContext();

		return new SourceToken(
				compilationUnit.getParseTreeRefFromElement(element),
				sourceTokenType,
				context,
				typeName,
				element.getClass().getSimpleName());
	}

	@Override
	public void iterateScopesAndVariables(CompilationUnit sourceFile, ScopesListener scopesListener) {

		final ArrayStack<BaseASTElement> stack = new ArrayStack<>();
		
		final Stack<BaseASTElement> stackWrapper = new StackDelegator<BaseASTElement>(stack) {

			@Override
			public void push(BaseASTElement element) {

				if (element instanceof ClassDefinition) {
					scopesListener.onClassStart(sourceFile.getParseTreeRefFromElement(element));
				}
				else if (element instanceof PrimaryList) {
					
					final PrimaryList primaryList = (PrimaryList)element;
					
					scopesListener.onPrimaryListStart(
							null,
							sourceFile.getParseTreeRefFromElement(element),
							primaryList.getPrimaries().size());
				}
					
				super.push(element);
			}

			@Override
			public BaseASTElement pop() {

				final BaseASTElement element = super.pop();
				
				if (element instanceof ClassDefinition) {
					scopesListener.onClassEnd(sourceFile.getParseTreeRefFromElement(element));
				}
				else if (element instanceof PrimaryList) {
					
					scopesListener.onPrimaryListEnd(null, sourceFile.getParseTreeRefFromElement(element));
				}
				
				return element;
			}
		};

		sourceFile.iterateNodeFirstWithStack(
				stackWrapper,
				Function.identity(),

				new ASTVisitor() {
			@Override
			public void onElement(BaseASTElement element) {
				
				if (element instanceof InitializerVariableDeclarationElement) {
					
					final InitializerVariableDeclarationElement declaration = (InitializerVariableDeclarationElement)element;
					
					scopesListener.onScopeVariableDeclaration(
							sourceFile.getParseTreeRefFromElement(declaration.getNameDeclaration()),
							declaration.getVarName().getName(),
							declaration.getTypeReference().getTypeName());
				}
				else if (element instanceof NameReference) {
					
					final NameReference nameReference = (NameReference)element;
					final int parseTreeRef = sourceFile.getParseTreeRefFromElement(element);
					
					final BaseASTElement stackElement = stack.get();
					
					if (!stack.isEmpty() && stackElement instanceof PrimaryList) {
					
						scopesListener.onPrimaryListNameReference(
								null,
								parseTreeRef,
								nameReference.getName());
						
					}
					else {
						scopesListener.onNonPrimaryListNameReference(parseTreeRef, nameReference.getName());
					}
				}
			}
		});
	}
	
	@Override
	public String getMethodName(CompilationUnit sourceFile, int parseTreemethodDeclarationRef) {

		final ClassMethodMember classmethodMember = (ClassMethodMember)sourceFile.getElementFromParseTreeRef(parseTreemethodDeclarationRef);
		
		return classmethodMember.getMethod().getName().getName();
	}

	@Override
	public String getVariableName(CompilationUnit sourceFile, int parseTreeVariableDeclarationRef) {

		final InitializerVariableDeclarationElement variable
				= (InitializerVariableDeclarationElement)sourceFile.getElementFromParseTreeRef(parseTreeVariableDeclarationRef);
		
		return variable.getNameDeclaration().getVarName().getName();
	}

	@Override
	public String getClassDataFieldMemberName(CompilationUnit sourceFile, int parseTreeDataMemberDeclarationRef) {

		final ClassDataFieldMember member = (ClassDataFieldMember)sourceFile.getElementFromParseTreeRef(parseTreeDataMemberDeclarationRef);
		
		return member.getNameString();
	}

	@Override
	public String getClassName(CompilationUnit sourceFile, int parseTreetypeDeclarationRef) {

		final ClassDefinition classDefinition = (ClassDefinition)sourceFile.getElementFromParseTreeRef(parseTreetypeDeclarationRef);
		
		return classDefinition.getNameString();
	}

	@Override
	public String getTokenString(CompilationUnit sourceFile, int parseTreeTokenRef) {
		return sourceFile.getElementFromParseTreeRef(parseTreeTokenRef).getContext().getText();
	}

	@Override
	public int getTokenOffset(CompilationUnit sourceFile, int parseTreeTokenRef) {
		return sourceFile.getElementFromParseTreeRef(parseTreeTokenRef).getContext().getStartOffset();
	}

	@Override
	public int getTokenLength(CompilationUnit sourceFile, int parseTreeTokenRef) {
		return sourceFile.getElementFromParseTreeRef(parseTreeTokenRef).getContext().getLength();
	}

	@Override
	public void print(CompilationUnit sourceFile, PrintStream out) {

		sourceFile.iterateNodeFirstWithStack((element, stack) -> {
			for (int i = 0; i < stack.size(); ++ i) {
				out.append("  ");
			}
			
			out.append(element.getClass().getSimpleName());
			
			if (element.getContext() != null) {
				out.append(" \"").append(element.getContext().getText()).append("\"");
			}
			out.println();
		});
		
		
	}

	@Override
	public void updateOnResolve(CompilationUnit compilationUnit, UpdateOnResolve mode, int elementParseTreeRef, UserDefinedTypeRef type, TypeResolveMode typeResolveMode) {
		
		switch (mode) {
		case METHOD_INVOCATION_EXPRESSION:
			
			final MethodInvocationExpression methodInvocationExpression = (MethodInvocationExpression)compilationUnit.getElementFromParseTreeRef(elementParseTreeRef);
			final ResolveLaterTypeReference resolveLaterTypeReference = (ResolveLaterTypeReference)methodInvocationExpression.getClassType();
			
			final ScopedName toResolve = resolveLaterTypeReference.getScopedName();
			
			MethodInvocationExpressionResolver.updateOnResolve(toResolve, type, typeResolveMode, methodInvocationExpression);
			break;
			
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public void replaceWithUserDefinedType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, UserDefinedTypeRef userType) {
		
		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);
		
		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}
		
		element.replaceWith(new ComplexTypeReference(element.getContext(), userType.getTypeName()));
	}

	@Override
	public void replaceWithBuiltinType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, BuiltinTypeRef builtinType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}

		element.replaceWith(new ScalarTypeReference(element.getContext(), builtinType.getTypeName()));
	}

	@Override
	public void replaceWithLibraryType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, LibraryTypeRef libraryType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}
		
		element.replaceWith(new LibraryTypeReference(element.getContext(), libraryType.getTypeName()));
	}

	@Override
	public int getNumMethods(CompilationUnit compilationUnit, UserDefinedTypeRef userDefinedType) {

		Objects.requireNonNull(compilationUnit);
		Objects.requireNonNull(userDefinedType);
		
		int numMethods = 0;
		
		final ComplexTypeDefinition<?, ?> complexType = (ComplexTypeDefinition<?, ?>)compilationUnit.getElementFromParseTreeRef(userDefinedType.getParseTreeRef());
		
		if (complexType != null && complexType.getMembers() != null) {
		
			for (ComplexMemberDefinition member : complexType.getMembers()) {
				if (member.isMethod()) {
					++ numMethods;
				}
			}
		}
		
		return numMethods;
	}

	@Override
	public void iterateClassMembers(CompilationUnit compilationUnit, UserDefinedTypeRef userDefinedType, FieldVisitor fieldVisitor, MethodVisitor methodVisitor) {

		final ClassDefinition complexType = (ClassDefinition)compilationUnit.getElementFromParseTreeRef(userDefinedType.getParseTreeRef());
		
		iterateClassMembers(complexType, fieldVisitor, methodVisitor);
	}

	private void iterateClassMembers(ClassDefinition complexType, FieldVisitor fieldVisitor, MethodVisitor methodVisitor) {
		
		final Subclassing subclassing = complexType.getModifiers().getModifier(Subclassing.class);
		
		int fieldIdx = 0;
		int methodIdx = 0;
		
		for (ComplexMemberDefinition memberDefinition : complexType.getMembers()) {
			
			if (memberDefinition instanceof ClassDataFieldMember) {
				
				final ClassDataFieldMember classDataFieldMember = (ClassDataFieldMember)memberDefinition;

				visitDataField(classDataFieldMember, fieldIdx, fieldVisitor);
				
				fieldIdx ++;
			}
			else if (memberDefinition instanceof ClassMethodMember) {

				final ClassMethodMember classMethodMember = (ClassMethodMember)memberDefinition;
				
				final MethodVariant methodVariant = findMethodVariant(classMethodMember, subclassing);
				
				visitClassMethod(classMethodMember, methodVariant, methodIdx, methodVisitor);
				
				++ methodIdx;
			}
		}
	}
	
	private void visitDataField(ClassDataFieldMember dataField, int indexInType, FieldVisitor visitor) {
		
		boolean isStatic = dataFieldDefaultModifiers.isStatic();
		boolean isTransient = dataFieldDefaultModifiers.isTransient();
		boolean isVolatile = dataFieldDefaultModifiers.isVolatile();
		Visibility visibility = dataFieldDefaultModifiers.getVisibility();
		Mutability mutability = dataFieldDefaultModifiers.getMutability();
		
		for (FieldModifierHolder fieldModifierHolder : dataField.getModifiers().getModifiers()) {
			
			final FieldModifier fieldModifier = fieldModifierHolder.getModifier();
			
			if (fieldModifier instanceof FieldStatic) {
				isStatic = true;
			}
			else if (fieldModifier instanceof FieldTransient) {
				isTransient = true;
			}
			else if (fieldModifier instanceof FieldVolatile) {
				isVolatile = true;
			}
			else if (fieldModifier instanceof FieldVisibility) {
				visibility = ((FieldVisibility)fieldModifier).getVisibility();
			}
			else if (fieldModifier instanceof ASTMutability) {
				mutability = ((ASTMutability)fieldModifier).getMutability();
			}
			else {
				throw new UnsupportedOperationException();
			}
		}

		visitor.onField(
				dataField.getNameString(),
				dataField.getType().getTypeName(),
				0,
				isStatic,
				visibility,
				mutability,
				isVolatile,
				isTransient,
				indexInType);
	}
	
	private MethodVariant findMethodVariant(ClassMethodMember classMethodMember, Subclassing subclassing) {

		final ClassMethodModifiers modifiers = classMethodMember.getModifiers();

		final MethodVariant methodVariant;
		
		if (modifiers.hasModifier(ClassMethodStatic.class)) {
			methodVariant = MethodVariant.STATIC;
		}
		else {
			final ClassMethodOverride methodOverride = modifiers.getModifier(ClassMethodOverride.class);
			
			if (methodOverride != null) {
				if (methodOverride == ClassMethodOverride.ABSTRACT) {
					methodVariant = MethodVariant.ABSTRACT;
				}
				else if (methodOverride == ClassMethodOverride.FINAL) {
					methodVariant = MethodVariant.FINAL_IMPLEMENTATION;
				}
				else {
					throw new UnsupportedOperationException();
				}
			}
			else {
				methodVariant = subclassing != null && subclassing == Subclassing.FINAL
						? MethodVariant.FINAL_IMPLEMENTATION
						: MethodVariant.OVERRIDABLE_IMPLEMENTATION;
			}
		}
		
		return methodVariant;
	}
	
	private void visitClassMethod(ClassMethodMember classMethodMember, MethodVariant methodVariant, int indexInType, MethodVisitor visitor) {

		final ClassMethod classMethod = classMethodMember.getMethod();
		
		final TypeName [] parameterTypes = new TypeName[classMethod.getParameters().size()];
		
		// final int [] parameterTypes = new int[classMethod.getParameters().size()]; 
		
		int i = 0;
		
		for (Parameter parameter : classMethod.getParameters()) {
			final TypeReference namedType = parameter.getType();
			
			parameterTypes[i ++] = namedType.getTypeName();
		}
		
		final TypeReference returnType = classMethod.getReturnType();
		
		visitor.onMethod(
				classMethod.getName().getName(),
				methodVariant,
				returnType.getTypeName(),
				parameterTypes,
				indexInType);
	}
}
