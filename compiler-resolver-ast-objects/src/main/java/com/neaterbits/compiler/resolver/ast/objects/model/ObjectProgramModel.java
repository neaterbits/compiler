package com.neaterbits.compiler.resolver.ast.objects.model;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.compiler.ast.objects.ASTVisitor;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Program;
import com.neaterbits.compiler.ast.objects.block.ClassMethod;
import com.neaterbits.compiler.ast.objects.block.Parameter;
import com.neaterbits.compiler.ast.objects.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.objects.expression.PrimaryList;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.ast.objects.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.LibraryTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.ScalarTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.objects.variables.NameReference;
import com.neaterbits.compiler.resolver.ast.objects.MethodInvocationExpressionResolver;
import com.neaterbits.compiler.resolver.util.SourceTokenUtil;
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
import com.neaterbits.compiler.util.model.SourceTokenVisitor;
import com.neaterbits.compiler.util.model.UpdateOnResolve;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.util.model.Visibility;
import com.neaterbits.compiler.util.parse.ScopesListener;
import com.neaterbits.compiler.util.statement.ASTMutability;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.util.typedefinition.FieldModifier;
import com.neaterbits.compiler.util.typedefinition.FieldStatic;
import com.neaterbits.compiler.util.typedefinition.FieldTransient;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldVolatile;
import com.neaterbits.compiler.util.typedefinition.Subclassing;

public class ObjectProgramModel
	extends ObjectImportsModel
	implements ProgramModel<Program, ASTParsedFile, CompilationUnit>,
				ResolveTypesModel<CompilationUnit> {
    
    private static final ObjectASTAccess AST_ACCESS = new ObjectASTAccess();

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

			return !element.isPlaceholderElement() && parseTreeRef == compilationUnit.getParseTreeRefFromElement(element);
		});
		
		return found != null ? makeSourceToken(found, compilationUnit, resolvedTypes) : null;
	}

	private SourceToken makeSourceToken(BaseASTElement element, CompilationUnit compilationUnit, ResolvedTypes resolvedTypes) {
	    
	    return SourceTokenUtil.makeSourceToken(element, compilationUnit, resolvedTypes, this, AST_ACCESS);
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
					
                    /*
					final InitializerVariableDeclarationElement declaration = (InitializerVariableDeclarationElement)element;
					
					scopesListener.onScopeVariableDeclaration(
							sourceFile.getParseTreeRefFromElement(declaration.getNameDeclaration()),
							declaration.getVarName().getName(),
							declaration.getTypeReference().getTypeName());
					*/
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
		
		return member.getInitializer(0).getNameString();
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
		
		for (InitializerVariableDeclarationElement element : dataField.getInitializers()) {
    
    		visitor.onField(
    				element.getNameString(),
    				dataField.getType().getTypeName(),
    				0,
    				isStatic,
    				visibility,
    				mutability,
    				isVolatile,
    				isTransient,
    				indexInType);
		}
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
