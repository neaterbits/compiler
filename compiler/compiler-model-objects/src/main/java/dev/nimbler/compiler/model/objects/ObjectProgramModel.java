package dev.nimbler.compiler.model.objects;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.jutils.ArrayStack;
import org.jutils.Stack;
import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTVisitor;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.BaseASTIterator;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.Namespace;
import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.Parameter;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.expression.literal.UnresolvedNamePrimary;
import dev.nimbler.compiler.ast.objects.parser.ASTParsedFile;
import dev.nimbler.compiler.ast.objects.statement.VariableDeclarationStatement;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexTypeDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldName;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDefinition;
import dev.nimbler.compiler.ast.objects.typereference.ComplexTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.LibraryTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.ResolvedTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.ScalarTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.typereference.UnresolvedTypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.ast.objects.variables.NameReference;
import dev.nimbler.compiler.ast.objects.variables.StaticMemberReference;
import dev.nimbler.compiler.model.common.BuiltinTypeRef;
import dev.nimbler.compiler.model.common.ElementVisitor;
import dev.nimbler.compiler.model.common.ISourceToken;
import dev.nimbler.compiler.model.common.LanguageSpec;
import dev.nimbler.compiler.model.common.LibraryTypeRef;
import dev.nimbler.compiler.model.common.ProgramModel;
import dev.nimbler.compiler.model.common.ResolveTypesModel;
import dev.nimbler.compiler.model.common.ResolvedScopesListener;
import dev.nimbler.compiler.model.common.ResolvedTypes;
import dev.nimbler.compiler.model.common.ScopesListener;
import dev.nimbler.compiler.model.common.SourceToken;
import dev.nimbler.compiler.model.common.SourceTokenUtil;
import dev.nimbler.compiler.model.common.SourceTokenVisitor;
import dev.nimbler.compiler.model.common.TypeMemberVisitor;
import dev.nimbler.compiler.model.common.TypeReferenceVisitor;
import dev.nimbler.compiler.model.common.UnresolvedScopesListener;
import dev.nimbler.compiler.model.common.UpdateOnResolve;
import dev.nimbler.compiler.model.common.UserDefinedTypeRef;
import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.types.statement.ASTMutability;
import dev.nimbler.compiler.types.typedefinition.ClassMethodOverride;
import dev.nimbler.compiler.types.typedefinition.ClassMethodStatic;
import dev.nimbler.compiler.types.typedefinition.FieldModifier;
import dev.nimbler.compiler.types.typedefinition.FieldStatic;
import dev.nimbler.compiler.types.typedefinition.FieldTransient;
import dev.nimbler.compiler.types.typedefinition.FieldVisibility;
import dev.nimbler.compiler.types.typedefinition.FieldVolatile;
import dev.nimbler.compiler.types.typedefinition.Subclassing;
import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.StackDelegator;
import dev.nimbler.compiler.util.TypeResolveMode;
import dev.nimbler.compiler.util.parse.FieldAccessType;
import dev.nimbler.language.common.types.FieldModifiers;
import dev.nimbler.language.common.types.MethodVariant;
import dev.nimbler.language.common.types.Mutability;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.Visibility;

public class ObjectProgramModel
	extends ObjectImportsModel
	implements ProgramModel<ASTParsedFile, CompilationUnit>,
				ResolveTypesModel<CompilationUnit> {

    private static final ObjectASTAccess AST_ACCESS = new ObjectASTAccess();

	private final FieldModifiers dataFieldDefaultModifiers;

    public ObjectProgramModel() {
        this(
                Collections.emptyList(),
                new FieldModifiers(false, Visibility.NAMESPACE, Mutability.MUTABLE, false, false));
    }
	
	public ObjectProgramModel(LanguageSpec languageSpec) {
	    this(languageSpec.getImplicitImports(), languageSpec.getDefaultModifiers());
	}

	public ObjectProgramModel(
	        List<TypeImport> implicitImports,
	        FieldModifiers dataFieldDefaultModifiers) {
	    
		super(implicitImports);
		
		this.dataFieldDefaultModifiers = dataFieldDefaultModifiers;
	}

	@Override
	public ASTParsedFile getParsedFile(Collection<ASTParsedFile> module, FileSpec path) {

	    return module.stream()
	            .filter(pf -> pf.getFileSpec().equals(path))
	            .findFirst()
	            .orElse(null);
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
	public void iterate(CompilationUnit compilationUnit, SourceTokenVisitor visitor, ResolvedTypes resolvedTypes, boolean visitPlaceholderElements) {

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

		compilationUnit.iterateNodeFirstWithStack(
				stackWrapper,

				baseASTElement -> new Element(
						baseASTElement,
					 	baseASTElement.isPlaceholderElement() && !visitPlaceholderElements
								? null
								: makeSourceToken(baseASTElement, compilationUnit, resolvedTypes)),

				new ASTVisitor() {

			@Override
			public void onElement(BaseASTElement element) {

				if (stack.isEmpty()) {

					if (element.isPlaceholderElement()) {
						throw new IllegalStateException();
					}

					visitor.onToken(makeSourceToken(element, compilationUnit, resolvedTypes));
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
	public CompilationUnit getCompilationUnit(ASTParsedFile compilationUnit) {
		return compilationUnit.getParsed();
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
	
	private static class ScopesStackDelegator extends StackDelegator<BaseASTElement> {
	    
	    private final ScopesListener scopesListener;
	    private final CompilationUnit compilationUnit;
	    
        ScopesStackDelegator(
                Stack<BaseASTElement> delegate,
                CompilationUnit compilationUnit,
                ScopesListener scopesListener) {

            super(delegate);

            Objects.requireNonNull(compilationUnit);
            Objects.requireNonNull(scopesListener);
            
            this.compilationUnit = compilationUnit;
            this.scopesListener = scopesListener;
        }

        @Override
        public void push(BaseASTElement element) {
            
            if (element instanceof ClassDefinition) {
                scopesListener.onClassStart(compilationUnit.getParseTreeRefFromElement(element));
            }
            else if (element instanceof PrimaryList) {

                final PrimaryList primaryList = (PrimaryList)element;

                scopesListener.onPrimaryListStart(
                        null,
                        compilationUnit.getParseTreeRefFromElement(element),
                        primaryList.getPrimaries().size());
            }

            super.push(element);
        }

        @Override
        public BaseASTElement pop() {

            final BaseASTElement element = super.pop();

            if (element instanceof ClassDefinition) {
                scopesListener.onClassEnd(compilationUnit.getParseTreeRefFromElement(element));
            }
            else if (element instanceof PrimaryList) {

                scopesListener.onPrimaryListEnd(null, compilationUnit.getParseTreeRefFromElement(element));
            }

            return element;
        }
	}
	
	private boolean processScopeElement(
	        BaseASTElement element,
	        Stack<BaseASTElement> stack,
	        CompilationUnit compilationUnit,
	        ScopesListener scopesListener) {

	    final boolean processed;
	    
	    if (element instanceof VariableDeclarationStatement) {
            
	        final VariableDeclarationStatement declaration
                = (VariableDeclarationStatement)element;
	        
	        final ResolvedTypeReference resolvedTypeReference
	            = (ResolvedTypeReference)declaration.getTypeReference();
	        
	        scopesListener.onScopeVariableDeclarationStatementStart(
	                compilationUnit.getParseTreeRefFromElement(element),
	                resolvedTypeReference.getTypeNo(),
                    compilationUnit.getParseTreeRefFromElement(declaration.getTypeReference()));

	        processed = true;
	    }
	    else if (element instanceof InitializerVariableDeclarationElement) {
            
            final BaseASTElement last = stack.getFromTop(1);
            
            if (last instanceof VariableDeclarationStatement) {

                final InitializerVariableDeclarationElement initializer
                    = (InitializerVariableDeclarationElement)element;

                scopesListener.onScopeVariableDeclarator(
                        compilationUnit.getParseTreeRefFromElement(initializer.getNameDeclaration()),
                        initializer.getVarName().getName());
            }
            
            processed = true;
        }
	    else {
	        processed = false;
	    }
	    
	    return processed;
	}

	@Override
	public void iterateUnresolvedScopesAndVariables(CompilationUnit compilationUnit, UnresolvedScopesListener scopesListener) {

		final ArrayStack<BaseASTElement> stack = new ArrayStack<>();

		final Stack<BaseASTElement> stackWrapper
		    = new ScopesStackDelegator(stack, compilationUnit, scopesListener);

		compilationUnit.iterateNodeFirstWithStack(
				stackWrapper,
				Function.identity(),

				new ASTVisitor() {
			@Override
			public void onElement(BaseASTElement element) {

			    if (processScopeElement(element, stackWrapper, compilationUnit, scopesListener)) {
			        // already processed
			    }
				else if (element instanceof UnresolvedNamePrimary) {
				    
				    final UnresolvedNamePrimary namePrimary = (UnresolvedNamePrimary)element;
				    final int parseTreeRef = compilationUnit.getParseTreeRefFromElement(namePrimary);
				    
				    scopesListener.onUnresolvedNamePrimary(parseTreeRef, namePrimary.getName());
				}
			}
		});
	}

	@Override
    public void iterateResolvedScopesAndVariables(CompilationUnit compilationUnit,
            ResolvedScopesListener scopesListener) {

        final ArrayStack<BaseASTElement> stack = new ArrayStack<>();

        final Stack<BaseASTElement> stackWrapper
            = new ScopesStackDelegator(stack, compilationUnit, scopesListener);

        compilationUnit.iterateNodeFirstWithStack(
                stackWrapper,
                Function.identity(),

                new ASTVisitor() {
            @Override
            public void onElement(BaseASTElement element) {

                if (processScopeElement(element, stackWrapper, compilationUnit, scopesListener)) {
                    // already processed
                }
                else if (element instanceof NameReference) {

                    final NameReference nameReference = (NameReference)element;
                    final int parseTreeRef = compilationUnit.getParseTreeRefFromElement(element);

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
    public List<String> getNamespace(CompilationUnit compilationUnit, int parseTreeRef) {

	    final Namespace namespace = (Namespace)compilationUnit.getElementFromParseTreeRef(parseTreeRef);
	    
	    return Arrays.asList(namespace.getParts());
    }

    @Override
    public String getMethodName(CompilationUnit compilationUnit, int parseTreemethodDeclarationRef) {

		final ClassMethodMember classmethodMember = (ClassMethodMember)compilationUnit.getElementFromParseTreeRef(parseTreemethodDeclarationRef);

		return classmethodMember.getMethod().getName().getName();
	}

	@Override
	public String getVariableName(CompilationUnit compilationUnit, int parseTreeVariableDeclarationRef) {

		final InitializerVariableDeclarationElement variable
				= (InitializerVariableDeclarationElement)compilationUnit.getElementFromParseTreeRef(parseTreeVariableDeclarationRef);

		return variable.getNameDeclaration().getVarName().getName();
	}

	@Override
	public String getClassDataFieldMemberName(CompilationUnit compilationUnit, int parseTreeDataMemberDeclarationRef) {

		final ClassDataFieldMember member = (ClassDataFieldMember)compilationUnit.getElementFromParseTreeRef(parseTreeDataMemberDeclarationRef);

		return member.getInitializer(0).getNameString();
	}

	@Override
	public String getClassName(CompilationUnit compilationUnit, int parseTreetypeDeclarationRef) {

		final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getElementFromParseTreeRef(parseTreetypeDeclarationRef);

		return classDefinition.getNameString();
	}

	@Override
    public String getEnumName(CompilationUnit compilationUnit, int parseTreeTypeDeclarationRef) {

        final EnumDefinition enumDefinition = (EnumDefinition)compilationUnit.getElementFromParseTreeRef(parseTreeTypeDeclarationRef);

        return enumDefinition.getNameString();
    }

    @Override
    public String getInterfaceName(CompilationUnit compilationUnit, int parseTreeTypeDeclarationRef) {

        final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)compilationUnit.getElementFromParseTreeRef(parseTreeTypeDeclarationRef);

        return interfaceDefinition.getNameString();
    }

    @Override
    public String getTokenString(CompilationUnit compilationUnit, int parseTreeTokenRef) {

	    return compilationUnit.getTokenString(parseTreeTokenRef);
	}

	@Override
	public int getTokenOffset(CompilationUnit compilationUnit, int parseTreeTokenRef) {
		return compilationUnit.getElementFromParseTreeRef(parseTreeTokenRef).getContext().getStartOffset();
	}

	@Override
	public int getTokenLength(CompilationUnit compilationUnit, int parseTreeTokenRef) {

	    return compilationUnit.getTokenLength(parseTreeTokenRef);
	}

	@Override
	public void print(CompilationUnit compilationUnit, PrintStream out) {

		compilationUnit.iterateNodeFirstWithStack((element, stack) -> {
			for (int i = 0; i < stack.size(); ++ i) {
				out.append("  ");
			}

			out.append(element.getClass().getSimpleName());

			if (element.getContext() != null) {

			    final String text = compilationUnit.getTokenString(element.getContext());

				out.append(" \"").append(text).append("\"");
			}
			out.println();
		});
	}

	@Override
	public void updateOnResolve(CompilationUnit compilationUnit, UpdateOnResolve mode, int elementParseTreeRef, UserDefinedTypeRef type, TypeResolveMode typeResolveMode) {

		switch (mode) {
		case METHOD_INVOCATION_EXPRESSION:

			// final MethodInvocationExpression methodInvocationExpression = (MethodInvocationExpression)compilationUnit.getElementFromParseTreeRef(elementParseTreeRef);
			// final UnresolvedTypeReference resolveLaterTypeReference = (UnresolvedTypeReference)methodInvocationExpression.getClassType();

			// final ScopedName toResolve = resolveLaterTypeReference.getScopedName();

			// MethodInvocationExpressionResolver.updateOnResolve(toResolve, type, typeResolveMode, methodInvocationExpression);
			// break;
			throw new UnsupportedOperationException();

		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public void replaceWithUserDefinedType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, UserDefinedTypeRef userType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof UnresolvedTypeReference)) {
			throw new IllegalStateException();
		}

		element.replaceWith(new ComplexTypeReference(element.getContext(), -1, userType.getTypeName()));
	}

	@Override
	public void replaceWithBuiltinType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, BuiltinTypeRef builtinType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof UnresolvedTypeReference)) {
			throw new IllegalStateException();
		}

		element.replaceWith(new ScalarTypeReference(element.getContext(), -1, builtinType.getTypeName()));
	}

	@Override
	public void replaceWithLibraryType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, LibraryTypeRef libraryType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof UnresolvedTypeReference)) {
			throw new IllegalStateException();
		}

		element.replaceWith(new LibraryTypeReference(element.getContext(), -1, libraryType.getTypeName()));
	}

	@Override
	public int getNumMethods(CompilationUnit compilationUnit, UserDefinedTypeRef userDefinedType) {

		Objects.requireNonNull(compilationUnit);
		Objects.requireNonNull(userDefinedType);

		int numMethods = 0;

		final ComplexTypeDefinition<?, ?> complexType
		    = (ComplexTypeDefinition<?, ?>)compilationUnit.getElementFromParseTreeRef(userDefinedType.getParseTreeRef());

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
    public void iterateElements(CompilationUnit compilationUnit, ElementVisitor<CompilationUnit> visitor) {
	    
        compilationUnit.iterateNodeFirst(new BaseASTIterator() {

            @Override
            public void onPush(BaseASTElement element) {
                
                if (!element.isPlaceholderElement()) {
                    
                    visitor.onElementStart(
                            compilationUnit,
                            compilationUnit.getParseTreeRefFromElement(element),
                            element.getParseTreeElement());
                }
            }

            @Override
            public boolean onPop(BaseASTElement element) {
                
                if (!element.isPlaceholderElement()) {
    
                    visitor.onElementEnd(
                            compilationUnit,
                            compilationUnit.getParseTreeRefFromElement(element),
                            element.getParseTreeElement());
                }

                return true;
            }

        });

    }

    @Override
	public void iterateTypesAndMembers(
	        CompilationUnit compilationUnit,
	        TypeMemberVisitor visitor,
	        boolean fields,
	        boolean methods) {

        compilationUnit.iterateNodeFirst(new BaseASTIterator() {

            @Override
            public void onPush(BaseASTElement element) {

                if (element instanceof Namespace) {

                    final Namespace namespace = (Namespace)element;

                    visitor.onNamespaceStart();

                    for (String part : namespace.getNamespaceDeclaration().getNamespaceReference().getParts()) {
                        visitor.onNamespacePart(part);
                    }
                }
                else if (element instanceof ClassDefinition) {

                    final ClassDefinition classDefinition = (ClassDefinition)element;

                    visitor.onClassStart(classDefinition.getNameString());
                }
                else if (element instanceof InterfaceDefinition) {

                    final InterfaceDefinition interfaceDefinition = (InterfaceDefinition)element;

                    visitor.onInterfaceStart(interfaceDefinition.getNameString());
                }
                else if (element instanceof EnumDefinition) {

                    final EnumDefinition enumDefinition = (EnumDefinition)element;

                    visitor.onEnumStart(enumDefinition.getNameString());
                }
            }

            @Override
            public boolean onElement(BaseASTElement element) {
                
                if ((fields || methods) && element instanceof ComplexTypeDefinition<?, ?>) {
                    
                    final ComplexTypeDefinition<?, ?> complexType = (ComplexTypeDefinition<?, ?>)element;
                    
                    iterateClassMembers(complexType, visitor, fields, methods);
                }

                return true;
            }

            @Override
            public boolean onPop(BaseASTElement element) {

                if (element instanceof Namespace) {
                    visitor.onNamespaceEnd();
                }
                else if (element instanceof ClassDefinition) {
                    visitor.onClassEnd();
                }
                else if (element instanceof InterfaceDefinition) {
                    visitor.onInterfaceEnd();
                }
                else if (element instanceof EnumDefinition) {
                    visitor.onEnumEnd();
                }

                return true;
            }

        });
	}

	private void iterateClassMembers(
	        ComplexTypeDefinition<?, ?> complexType,
	        TypeMemberVisitor memberVisitor,
	        boolean fields,
	        boolean methods) {

		final Subclassing subclassing;
		
		if (complexType instanceof ClassDefinition) {
		    subclassing = ((ClassDefinition)complexType).getModifiers().getModifier(Subclassing.class);
		}
		else {
		    subclassing = null;
		}

		int fieldIdx = 0;
		int methodIdx = 0;

		for (ComplexMemberDefinition memberDefinition : complexType.getMembers()) {

			if (fields && memberDefinition instanceof ClassDataFieldMember) {

				final ClassDataFieldMember classDataFieldMember = (ClassDataFieldMember)memberDefinition;

				visitDataField(classDataFieldMember, fieldIdx, memberVisitor);

				fieldIdx ++;
			}
			else if (methods && memberDefinition instanceof ClassMethodMember) {

				final ClassMethodMember classMethodMember = (ClassMethodMember)memberDefinition;

				final MethodVariant methodVariant = findMethodVariant(classMethodMember, subclassing);

				visitClassMethod(classMethodMember, methodVariant, methodIdx, memberVisitor);

				++ methodIdx;
			}
		}
	}

	private void visitDataField(ClassDataFieldMember dataField, int indexInType, TypeMemberVisitor visitor) {

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
    				dataField.getType() instanceof ResolvedTypeReference
    				    ? dataField.getType().getTypeName()
			            : null,
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

	private void visitClassMethod(
	        ClassMethodMember classMethodMember,
	        MethodVariant methodVariant,
	        int indexInType,
	        TypeMemberVisitor visitor) {

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

    @Override
    public void replaceTypeReference(
            CompilationUnit compilationUnit,
            int toReplace,
            int typeNo,
            TypeName typeName) {

        final BaseASTElement elementToReplace
            = compilationUnit.getElementFromParseTreeRef(toReplace);

        final ComplexTypeReference typeReference
            = new ComplexTypeReference(elementToReplace.getContext(), typeNo, typeName);

        compilationUnit.replace(elementToReplace, typeReference);
    }

    @Override
    public void iterateTypeReferences(CompilationUnit compilationUnit, TypeReferenceVisitor<CompilationUnit> visitor) {

        compilationUnit.iterateNodeFirst(new BaseASTIterator() {

            @Override
            public void onPush(BaseASTElement element) {

                if (element instanceof Namespace) {

                    visitor.onNamespaceStart();
                    
                    final Namespace namespace = (Namespace)element;
                    
                    for (String part : namespace.getParts()) {
                        visitor.onNamespacePart(part);
                    }
                }
            }

            @Override
            public boolean onElement(BaseASTElement element) {
                
                if (element instanceof UnresolvedTypeReference) {

                    final UnresolvedTypeReference typeReference = (UnresolvedTypeReference)element;

                    final ScopedName scopedName = typeReference.getScopedName();

                    if (scopedName.hasScope()) {
                        throw new UnsupportedOperationException();
                    }
                    else {

                        visitor.onNonScopedTypeReference(
                                compilationUnit,
                                compilationUnit.getParseTreeRefFromElement(element),
                                scopedName.getName());
                    }
                }
                else if (element instanceof ComplexTypeReference) {

                    final ComplexTypeReference typeReference = (ComplexTypeReference)element;

                    visitor.onResolvedTypeReference(
                            compilationUnit,
                            compilationUnit.getParseTreeRefFromElement(element),
                            typeReference.getTypeNo());
                }
                
                return true;
            }

            @Override
            public boolean onPop(BaseASTElement element) {

                if (element instanceof Namespace) {
                    visitor.onNamespaceEnd();
                }
                
                return true;
            }
        });
    }

    @Override
    public void replaceNamePrimaryWithNameReference(
            CompilationUnit compilationUnit,
            int namePrimaryParseTreeRef,
            String name) {

        final BaseASTElement elementToReplace
            = compilationUnit.getElementFromParseTreeRef(namePrimaryParseTreeRef);

        final NameReference nameReference = new NameReference(
                                                elementToReplace.getContext(),
                                                name);

        compilationUnit.replace(elementToReplace, nameReference);
    }

    @Override
    public void replaceNamePrimaryWithFieldAccess(
            CompilationUnit compilationUnit,
            int namePrimaryParseTreeRef,
            int classTypeParseTreeRef,
            String name) {

        final BaseASTElement elementToReplace
            = compilationUnit.getElementFromParseTreeRef(namePrimaryParseTreeRef);
        
        final ResolvedTypeReference typeReference
            = (ResolvedTypeReference)compilationUnit.getElementFromParseTreeRef(classTypeParseTreeRef);

        final FieldAccess fieldAccess = new FieldAccess(
                                                elementToReplace.getContext(),
                                                FieldAccessType.FIELD,
                                                typeReference.makeCopy(),
                                                new FieldName(name));

        compilationUnit.replace(elementToReplace, fieldAccess);
    }

    @Override
    public void replaceNamePrimaryWithStaticReference(
            CompilationUnit compilationUnit,
            int namePrimaryParseTreeRef,
            int classTypeParseTreeRef,
            String name) {
        
        final BaseASTElement elementToReplace
            = compilationUnit.getElementFromParseTreeRef(namePrimaryParseTreeRef);
    
        final TypeReference typeReference
            = (TypeReference)compilationUnit.getElementFromParseTreeRef(classTypeParseTreeRef);
        
        final StaticMemberReference staticMemberReference
            = new StaticMemberReference(
                    elementToReplace.getContext(),
                    typeReference,
                    name,
                    null);
            

        compilationUnit.replace(elementToReplace, staticMemberReference);
    }

}
