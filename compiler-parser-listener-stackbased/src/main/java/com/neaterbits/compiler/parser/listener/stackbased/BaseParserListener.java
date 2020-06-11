package com.neaterbits.compiler.parser.listener.stackbased;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.parser.listener.stackbased.state.BaseStackTryCatchFinally;
import com.neaterbits.compiler.parser.listener.stackbased.state.BaseStackVariableDeclaration;
import com.neaterbits.compiler.parser.listener.stackbased.state.BaseStackVariableDeclarationList;
import com.neaterbits.compiler.parser.listener.stackbased.state.CallableStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAnnotation;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAnonymousClass;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackArrayAccess;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackArrayCreationExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAssignmentExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAssignmentLHS;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackCastExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackCatchBlock;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackClass;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackClassInstanceCreationExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackClassMethod;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackCompilationUnit;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackConditionalExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackConstructor;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackConstructorInvocation;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackDoWhileStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackEnum;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackEnumConstant;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackExpressionList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackExpressionStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackFieldDeclarationList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackFinallyBlock;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackForInit;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackForStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackForUpdate;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackImplements;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackImport;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackInitializerVariableDeclarationElement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackInterface;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackInterfaceMethod;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackIteratorForStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackLambdaExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackLambdaFormalParameters;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackMethodInvocation;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackNamedClass;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackNamespace;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackParameter;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackParameterList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackParameterSignature;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackPrimaryList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackResource;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackResourceList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackReturnType;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackScopedName;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackScopedTypeReference;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackStaticInitializer;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackThrowStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackTryBlock;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackTryCatchFinallyStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackTryWithResourcesStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackVariableDeclaration;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackVariableDeclarationList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackWhileStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.VariableNameSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStack;
import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.base.VariablesMap;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ClassMethodMemberSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ClassModifierSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ConstructorMemberSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.InterfaceMethodMemberSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.NestedExpressionSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ParametersSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.VariableModifierSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextScopedName;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.block.ConstructorInvocation;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.parse.CompileException;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.statement.ASTMutability;
import com.neaterbits.compiler.util.typedefinition.ClassMethodModifier;
import com.neaterbits.compiler.util.typedefinition.ClassMethodNative;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.util.typedefinition.ClassMethodStrictfp;
import com.neaterbits.compiler.util.typedefinition.ClassMethodSynchronized;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassModifier;
import com.neaterbits.compiler.util.typedefinition.ClassStatic;
import com.neaterbits.compiler.util.typedefinition.ClassStrictfp;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.ConstructorModifier;
import com.neaterbits.compiler.util.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldModifier;
import com.neaterbits.compiler.util.typedefinition.FieldStatic;
import com.neaterbits.compiler.util.typedefinition.FieldTransient;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldVolatile;
import com.neaterbits.compiler.util.typedefinition.InterfaceAbstract;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodDefault;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodModifier;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodStatic;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodStrictfp;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceModifier;
import com.neaterbits.compiler.util.typedefinition.InterfaceStatic;
import com.neaterbits.compiler.util.typedefinition.InterfaceStrictfp;
import com.neaterbits.compiler.util.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.compiler.util.typedefinition.VariableModifier;
import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.io.strings.StringRef;

public abstract class BaseParserListener<

		KEYWORD,
		IDENTIFIER,
		TYPE_REFERENCE,
		INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
		VARIABLE_MODIFIER_HOLDER,
		VARIABLE_DECLARATION,
		COMPILATION_UNIT,
		IMPORT,
		COMPILATION_CODE,
		NAMESPACE,
		COMPLEX_MEMBER_DEFINITION,
		STATIC_INITIALIZER extends COMPLEX_MEMBER_DEFINITION,
		CLASS_MODIFIER_HOLDER,
		CLASS_DEFINITION,
		CONSTRUCTOR_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		CONSTRUCTOR_MODIFIER_HOLDER,
		CONSTRUCTOR_NAME,
		CONSTRUCTOR,
		CONSTRUCTOR_INVOCATION_STATEMENT extends STATEMENT,

		CLASS_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		CLASS_METHOD_MODIFIER_HOLDER,
		
		CLASS_FIELD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		FIELD_MODIFIER_HOLDER,
		
		INTERFACE_MODIFIER_HOLDER,
		INTERFACE_DEFINITION,
		
		INTERFACE_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		INTERFACE_METHOD_MODIFIER_HOLDER,
		
		ENUM_DEFINITION,
		ENUM_CONSTANT_DEFINITION,
		
		BLOCK,
		
		STATEMENT,

		EXPRESSION,
		PRIMARY extends EXPRESSION,
		VARIABLE_REFERENCE extends PRIMARY,
		LITERAL extends PRIMARY,
		
		EXPRESSION_LIST extends EXPRESSION,
		
		ASSIGNMENT_EXPRESSION extends EXPRESSION,
		
		CAST_EXPRESSION extends EXPRESSION,
		
		CONDITIONAL_EXPRESSION extends EXPRESSION,

		PRE_INCREMENT_EXPRESSION extends EXPRESSION,
		POST_INCREMENT_EXPRESSION extends EXPRESSION,
		
		PRE_DECREMENT_EXPRESSION extends EXPRESSION,
		POST_DECREMENT_EXPRESSION extends EXPRESSION,

		LAMBDA_EXPRESSION extends EXPRESSION,
		SINGLE_LAMBDA_EXPRESSION extends LAMBDA_EXPRESSION,
		BLOCK_LAMBDA_EXPRESSION extends LAMBDA_EXPRESSION,
		LAMBDA_EXPRESSION_PARAMETERS,

		PRIMARY_LIST extends PRIMARY,
		
		NESTED_EXPRESSION extends PRIMARY,
		FIELD_ACCESS extends PRIMARY,
		THIS_PRIMARY extends PRIMARY,
		CLASS_INSTANCE_CREATION_EXPRESSION extends PRIMARY,
		METHOD_INVOCATION_EXPRESSION extends PRIMARY,
		ARRAY_CREATION_EXPRESSION extends PRIMARY,
		ARRAY_ACCESS_EXPRESSION extends PRIMARY,
		CLASS_EXPRESSION extends PRIMARY,
		
		NAME_REFERENCE extends VARIABLE_REFERENCE,
		SIMPLE_VARIABLE_REFERENCE extends VARIABLE_REFERENCE,
		
		INTEGER_LITERAL extends LITERAL,
		FLOATING_POINT_LITERAL extends LITERAL,
		BOOLEAN_LITERAL extends LITERAL,
		CHARACTER_LITERAL extends LITERAL,
		STRING_LITERAL extends LITERAL,
		NULL_LITERAL extends LITERAL,
		
		PARAMETER,
		
		VARIABLE_DECLARATION_STATEMENT extends STATEMENT,
		EXPRESSION_STATEMENT extends STATEMENT,
		
		FOR_INIT,
		FOR_EXPRESSION_LIST,
		FOR_STATEMENT extends STATEMENT,
		ITERATOR_FOR_STATEMENT extends STATEMENT,
		
		WHILE_STATEMENT extends STATEMENT,
		
		DO_WHILE_STATEMENT extends STATEMENT,
		
		RESOURCE,
		CATCH_BLOCK,
		TRY_CATCH_FINALLY extends STATEMENT,
		TRY_WITH_RESOURCES extends STATEMENT,
		
		RETURN_STATEMENT extends STATEMENT,
		
		THROW_STATEMENT extends STATEMENT,
		
		IF_ELSE_IF_ELSE_STATEMENT extends STATEMENT,
        CONDITION_BLOCK,
        IF_CONDITION_BLOCK extends CONDITION_BLOCK,
        ELSE_IF_CONDITION_BLOCK extends CONDITION_BLOCK,
        ELSE_BLOCK,
		
		SWITCH_CASE_LABEL,
		CONSTANT_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
		ENUM_CONSTANT,
		ENUM_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
		DEFAULT_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
		SWITCH_CASE_GROUP,
		SWITCH_CASE_STATEMENT extends STATEMENT,
		
		BREAK_STATEMENT extends STATEMENT>

	implements ParserListener<COMPILATION_UNIT> {

	final StringSource stringSource;
	private final ParseLogger logger;

	final ParseTreeFactory<
			KEYWORD,
			IDENTIFIER,
			TYPE_REFERENCE,
			COMPILATION_UNIT,
			IMPORT,
			COMPILATION_CODE,
			NAMESPACE,
			COMPLEX_MEMBER_DEFINITION,
			STATIC_INITIALIZER,
			CLASS_MODIFIER_HOLDER,
			CLASS_DEFINITION,
			CONSTRUCTOR_MEMBER,
			CONSTRUCTOR_MODIFIER_HOLDER,
			CONSTRUCTOR_NAME,
			CONSTRUCTOR,
			CONSTRUCTOR_INVOCATION_STATEMENT,

			CLASS_METHOD_MEMBER,
			CLASS_METHOD_MODIFIER_HOLDER,
			
			CLASS_FIELD_MEMBER,
			FIELD_MODIFIER_HOLDER,

			INTERFACE_MODIFIER_HOLDER,
			INTERFACE_DEFINITION,

			INTERFACE_METHOD_MEMBER,
			INTERFACE_METHOD_MODIFIER_HOLDER,
			
			ENUM_DEFINITION,
			ENUM_CONSTANT_DEFINITION,

			VARIABLE_MODIFIER_HOLDER,
			VARIABLE_DECLARATION,

			BLOCK,
			
			STATEMENT,

			EXPRESSION,
			PRIMARY,
			VARIABLE_REFERENCE,

			LITERAL,
			
			EXPRESSION_LIST,

			ASSIGNMENT_EXPRESSION,
			
			CAST_EXPRESSION,
			
			CONDITIONAL_EXPRESSION,
			
			PRE_INCREMENT_EXPRESSION,
			POST_INCREMENT_EXPRESSION,
			
			PRE_DECREMENT_EXPRESSION,
			POST_DECREMENT_EXPRESSION,
			
			LAMBDA_EXPRESSION,
			SINGLE_LAMBDA_EXPRESSION,
			BLOCK_LAMBDA_EXPRESSION,
			LAMBDA_EXPRESSION_PARAMETERS,
			
			PRIMARY_LIST,

			NESTED_EXPRESSION,
			FIELD_ACCESS,
			THIS_PRIMARY,
			CLASS_INSTANCE_CREATION_EXPRESSION,
			METHOD_INVOCATION_EXPRESSION,
			ARRAY_CREATION_EXPRESSION,
			ARRAY_ACCESS_EXPRESSION,
			CLASS_EXPRESSION,
			
			NAME_REFERENCE,
			SIMPLE_VARIABLE_REFERENCE,
			
			INTEGER_LITERAL,
			FLOATING_POINT_LITERAL,
			BOOLEAN_LITERAL,
			CHARACTER_LITERAL,
			STRING_LITERAL,
			NULL_LITERAL,
			
			PARAMETER,
			
			INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
			VARIABLE_DECLARATION_STATEMENT,
			
			EXPRESSION_STATEMENT,
			
			FOR_INIT,
			FOR_STATEMENT,
			
			ITERATOR_FOR_STATEMENT,
			
			WHILE_STATEMENT,
			
			DO_WHILE_STATEMENT,
			
			RESOURCE,
			
			CATCH_BLOCK,
			TRY_CATCH_FINALLY,
			TRY_WITH_RESOURCES,
			
			RETURN_STATEMENT,
			
			THROW_STATEMENT,
			
			IF_ELSE_IF_ELSE_STATEMENT,
            CONDITION_BLOCK,
            IF_CONDITION_BLOCK,
            ELSE_IF_CONDITION_BLOCK,
            ELSE_BLOCK,

			SWITCH_CASE_LABEL,
			CONSTANT_SWITCH_CASE_LABEL,
			ENUM_CONSTANT,
			ENUM_SWITCH_CASE_LABEL,
			DEFAULT_SWITCH_CASE_LABEL,
			SWITCH_CASE_GROUP,
			SWITCH_CASE_STATEMENT,
			
			BREAK_STATEMENT

			> parseTreeFactory;

	// Stack for the main elements of a program
	private final ListStack mainStack;

	// Scope for variables
	private final ArrayStack<VariablesMap<VARIABLE_DECLARATION>> variableScopes;

	private boolean variableScopesContain(String name) {

		boolean contains = false;

		for (int i = variableScopes.size() - 1; i >= 0; --i) {
			if (variableScopes.get(i).hasVariable(name)) {
				contains = true;
				break;
			}
		}

		return contains;
	}

	@SuppressWarnings("unchecked")
	protected BaseParserListener(StringSource stringSource, ParseLogger logger,
			@SuppressWarnings("rawtypes") ParseTreeFactory parseTreeFactory) {

		this.stringSource = stringSource;
		this.logger = logger;
		this.parseTreeFactory = parseTreeFactory;

		this.mainStack = new ListStack();

		this.variableScopes = new ArrayStack<>();
	}

	protected final ParseLogger getLogger() {
		return logger;
	}

	protected final VARIABLE_DECLARATION findVariableDeclaration(String name) {
		Objects.requireNonNull(name);

		for (int i = variableScopes.size() - 1; i >= 0; --i) {
			final VARIABLE_DECLARATION variableDeclaration = variableScopes.get(i).findVariable(name);

			if (variableDeclaration != null) {
				return variableDeclaration;
			}
		}

		return null;
	}

	private static String getMethodName() {
		final StackTraceElement stackTraceElement[] = Thread.currentThread().getStackTrace();

		return stackTraceElement[3].getMethodName();
	}

	protected final void logEnter(Context context) {

		if (logger != null) {
			logger.onEnterListenerFunction(getMethodName(), context);
		}
	}

	protected final void logExit(Context context) {

		if (logger != null) {
			logger.onExitListenerFunction(getMethodName(), context);
		}
	}

	@Override
	public final void onCompilationUnitStart(Context context) {

		logEnter(context);

		if (!mainStack.isEmpty()) {
			throw new IllegalStateException("Expected empty stack");
		}

		push(new StackCompilationUnit<>(logger));

		logExit(context);
	}

	@Override
	public final COMPILATION_UNIT onCompilationUnitEnd(Context context) {

		logEnter(context);

		final StackCompilationUnit<COMPILATION_CODE, IMPORT> stackCompilationUnit = pop();

		final COMPILATION_UNIT compilationUnit = parseTreeFactory.createCompilationUnit(context,
				stackCompilationUnit.getImports(), stackCompilationUnit.getList());

		logExit(context);

		return compilationUnit;
	}

	@Override
	public final void onImportStart(Context context, long importKeyword, Context importKeywordContext, long staticKeyword, Context staticKeywordContext) {
		
	    logEnter(context);
	    
		Objects.requireNonNull(importKeyword);

		final String importKeywordString = stringSource.asString(importKeyword);
		final String staticKeywordString = staticKeyword != StringRef.STRING_NONE ? stringSource.asString(staticKeyword) : null;
		
		push(new StackImport<>(logger, importKeywordString, importKeywordContext, staticKeywordString, staticKeywordContext));
		
		logExit(context);
	}

	@Override
	public final void onImportIdentifier(Context context, long identifier) {
		
	    logEnter(context);
	    
		final StackImport<IDENTIFIER> stackImport = get();
		
		stackImport.addIdentifier(parseTreeFactory.createIdentifier(context, stringSource.asString(identifier)));
		
		logExit(context);
	}
		
	@Override
	public final void onImportEnd(Context context, boolean ondemand) {
		
	    logEnter(context);
	    
		final StackImport<IDENTIFIER> stackImport = pop();
		
		final StackCompilationUnit<COMPILATION_CODE, IMPORT> stackCompilationUnit = get(StackCompilationUnit.class);

		final IMPORT importStatement = parseTreeFactory.createImport(
				context,
				parseTreeFactory.createKeyword(stackImport.getImportKeywordContext(), stackImport.getImportKeyword()),
				stackImport.getStaticKeyword() != null
					? parseTreeFactory.createKeyword(stackImport.getStaticKeywordContext(), stackImport.getStaticKeyword())
					: null,
				stackImport.getIdentifiers(),
				ondemand);
		
		stackCompilationUnit.addImport(importStatement);
		
		logExit(context);
	}

	@Override
	public final void onNamespaceStart(Context context, long namespaceKeyword, Context namespaceKeywordContext) {

		logEnter(context);
		
		push(new StackNamespace<>(
		        logger,
		        stringSource.asString(namespaceKeyword),
		        namespaceKeywordContext));

		logExit(context);
	}

	@Override
    public final void onNamespacePart(Context context, long part) {

	    logEnter(context);
	    
	    final StackNamespace<COMPILATION_CODE> namespace = get();
	    
	    namespace.addPart(stringSource.asString(part), context);
	    
	    logExit(context);
    }

    @Override
	public final void onNameSpaceEnd(Context context) {

		logEnter(context);

		final StackNamespace<COMPILATION_CODE> stackNamespace = pop();

		final List<COMPILATION_CODE> namespaceCode = stackNamespace.getList();

		final NAMESPACE nameSpace = parseTreeFactory.createNamespace(context,
				parseTreeFactory.createKeyword(stackNamespace.getNamespaceKeywordContext(),
						stackNamespace.getNamespaceKeyword()),
				stackNamespace.getParts().toArray(new String[stackNamespace.getParts().size()]),
				stackNamespace.getNameContext(),
				namespaceCode);

		mainStack.addElement(nameSpace);

		logExit(context);
	}

	@Override
	public final void onClassStart(Context context, long classKeyword, Context classKeywordContext, long name,
			Context nameContext) {

		logEnter(context);

		push(new StackNamedClass<>(logger, stringSource.asString(classKeyword), classKeywordContext, stringSource.asString(name), nameContext));

		logExit(context);
	}

	private void addClassModifier(Context context, ClassModifier modifier) {

		logEnter(context);

		final ClassModifierSetter<CLASS_MODIFIER_HOLDER> stackClass = get();

		stackClass.addClassModifier(parseTreeFactory.createClassModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onVisibilityClassModifier(Context context, ClassVisibility visibility) {

		logEnter(context);

		addClassModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onSubclassingModifier(Context context, Subclassing subclassing) {
		logEnter(context);

		addClassModifier(context, subclassing);

		logExit(context);
	}

	@Override
	public final void onStaticClassModifier(Context context) {

		logEnter(context);

		addClassModifier(context, new ClassStatic());

		logExit(context);
	}

	@Override
	public final void onStrictfpClassModifier(Context context) {

		logEnter(context);

		addClassModifier(context, new ClassStrictfp());

		logExit(context);
	}

	@Override
	public final void onClassExtendsStart(Context context, long extendsKeyword, Context extendsKeywordContext) {

		logEnter(context);

		final StackNamedClass<
		        COMPLEX_MEMBER_DEFINITION,
		        CONSTRUCTOR_MEMBER,
		        CLASS_METHOD_MEMBER,
		        CLASS_MODIFIER_HOLDER,
		        TYPE_REFERENCE> stackNamedClass = get();

		stackNamedClass.setExtendsKeyword(stringSource.asString(extendsKeyword), extendsKeywordContext);

		final StackScopedName stackScopedName = new StackScopedName(getLogger());

		push(stackScopedName);

		logExit(context);
	}
	
	@Override
    public void onClassExtendsNamePart(Context context, long identifier) {
        
	    logEnter(context);

	    final StackScopedName stackScopedName = get();
    
	    stackScopedName.addPart(stringSource.asString(identifier), context);
	    
	    logExit(context);
	}
	

    @Override
    public void onClassExtendsEnd(Context context) {
        
        logEnter(context);

        final StackScopedName stackScopedName = pop();

        final StackNamedClass<
                COMPLEX_MEMBER_DEFINITION,
                CONSTRUCTOR_MEMBER,
                CLASS_METHOD_MEMBER,
                CLASS_MODIFIER_HOLDER,
                TYPE_REFERENCE> stackNamedClass = get();
                
        final TYPE_REFERENCE typeReference = parseTreeFactory.createResolveLaterTypeReference(
                stackScopedName.getNameContext(),
                stackScopedName.getScopedName(),
                ReferenceType.NAME);

        stackNamedClass.addExtendedClass(typeReference);
        
        logExit(context);
    }

    @Override
	public final void onClassImplementsStart(Context context, long implementsKeyword, Context implementsKeywordContext) {

		logEnter(context);

		push(new StackImplements(getLogger(), implementsKeyword, implementsKeywordContext));
		
		logExit(context);
	}
    
    @Override
    public void onClassImplementsTypeStart(Context context) {

        logEnter(context);

        final StackScopedName stackScopedName = new StackScopedName(getLogger());

        push(stackScopedName);

        logExit(context);
    }

    @Override
    public void onClassImplementsNamePart(Context context, long identifier) {
        
        logEnter(context);

        final StackScopedName stackScopedName = get();
    
        stackScopedName.addPart(stringSource.asString(identifier), context);
        
        logExit(context);
    }
    
    @Override
    public void onClassImplementsTypeEnd(Context context) {

        logEnter(context);

        final StackScopedName stackScopedName = pop();

        final StackImplements stackImplements = get();
        
        final ContextScopedName contextScopedName = new ContextScopedName(context, stackScopedName.getScopedName());
        
        stackImplements.add(contextScopedName);
        
        logExit(context);
    }

    @Override
    public void onClassImplementsEnd(Context context) {
        
        logEnter(context);

        final StackImplements stackImplements = pop();

        final StackNamedClass<
                COMPLEX_MEMBER_DEFINITION,
                CONSTRUCTOR_MEMBER,
                CLASS_METHOD_MEMBER,
                CLASS_MODIFIER_HOLDER,
                TYPE_REFERENCE> stackNamedClass = get();
        
        for (ContextScopedName contextScopedName : stackImplements.getList()) {
            
            final TYPE_REFERENCE typeReference = parseTreeFactory.createResolveLaterTypeReference(
                    contextScopedName.getContext(),
                    contextScopedName.getScopedName(),
                    ReferenceType.NAME);
    
            stackNamedClass.addImplementedInterface(
                    stringSource.asString(stackImplements.getImplementsKeyword()),
                    stackImplements.getImplementsKeywordContext(),
                    typeReference);
        }
        
        logExit(context);
    }

    @Override
	public final void onClassEnd(Context context) {

		logEnter(context);

		final StackNamedClass<COMPLEX_MEMBER_DEFINITION, COMPLEX_MEMBER_DEFINITION, COMPLEX_MEMBER_DEFINITION, CLASS_MODIFIER_HOLDER, TYPE_REFERENCE> entry = pop();

		final List<COMPLEX_MEMBER_DEFINITION> classCode = entry.getList();

		// final ClassModifiers classModifiers = new
		// ClassModifiers(entry.getModifiers());

		final CLASS_DEFINITION classDefinition = parseTreeFactory.createClassDefinition(context, entry.getModifiers(),
				entry.getClassKeyword() != null
						? parseTreeFactory.createKeyword(entry.getClassKeywordContext(), entry.getClassKeyword())
						: null,

				entry.getName(), entry.getNameContext(),

				entry.getExtendsKeyword() != null
						? parseTreeFactory.createKeyword(entry.getExtendsKeywordContext(), entry.getExtendsKeyword())
						: null,
				entry.getExtendedClasses(), entry.getImplementedInterfaces(), classCode);

		mainStack.addElement(classDefinition);

		logExit(context);
	}

	@Override
	public final void onAnonymousClassStart(Context context) {

		logEnter(context);

		push(new StackAnonymousClass<>(logger));

		logEnter(context);
	}

	@Override
	public final void onAnonymousClassEnd(Context context) {

		logEnter(context);

		final StackAnonymousClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> entry = pop();

		final List<COMPLEX_MEMBER_DEFINITION> classCode = entry.getList();

		final CLASS_DEFINITION classDefinition = parseTreeFactory.createClassDefinition(context, null, null, null, null,
				null, null, null, classCode);

		mainStack.addElement(classDefinition);

		logExit(context);
	}

	@Override
	public final void onStaticInitializerStart(Context context) {

		logEnter(context);

		pushVariableScope();

		push(new StackStaticInitializer<>(logger));

		logExit(context);
	}

	@Override
	public final void onStaticInitializerEnd(Context context) {

		logEnter(context);

		final StackStaticInitializer<STATEMENT> stackStaticInitializer = pop();

		final STATIC_INITIALIZER initializer = parseTreeFactory.createStaticInitializer(context,
				stackStaticInitializer.getList());

		final StackClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> stackClass = get();

		stackClass.add(initializer);

		popVariableScope();

		logExit(context);
	}

	@Override
	public final void onConstructorStart(Context context) {

		logEnter(context);

		push(new StackConstructor<>(logger));

		pushVariableScope();

		logExit(context);
	}

	private void addConstructorModifier(Context context, ConstructorModifier modifier) {

		logEnter(context);

		final StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, CONSTRUCTOR_MODIFIER_HOLDER> stackConstructor = get();

		stackConstructor.addModifier(parseTreeFactory.createConstructorModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onConstructorVisibilityModifier(Context context, ConstructorVisibility visibility) {

		logEnter(context);

		addConstructorModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onConstructorName(Context context, long constructorName) {

		logEnter(context);

		final StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, CONSTRUCTOR_MODIFIER_HOLDER> constructor = get();

		constructor.setName(stringSource.asString(constructorName), context);

		logExit(context);
	}

	@Override
	public final void onConstructorInvocationStart(Context context, ConstructorInvocation type) {

		logEnter(context);

		push(new StackConstructorInvocation<>(logger, type));

		logExit(context);
	}

	@Override
	public final void onConstructorInvocationEnd(Context context) {

		logEnter(context);

		final StackConstructorInvocation<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackConstructorInvocation = pop();

		final CONSTRUCTOR_INVOCATION_STATEMENT statement = parseTreeFactory.createConstructorInvocationStatement(
				context, stackConstructorInvocation.getType(), makeExpression(context, stackConstructorInvocation),
				stackConstructorInvocation.getParameters());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public final void onConstructorEnd(Context context) {

		logEnter(context);

		popVariableScope();

		final StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, CONSTRUCTOR_MODIFIER_HOLDER> stackConstructor = pop();

		final CONSTRUCTOR_MEMBER constructorMember = parseTreeFactory.createConstructorMember(context,
				stackConstructor.getModifiers(), stackConstructor.getName(), stackConstructor.getNameContext(),
				stackConstructor.getParameters(), stackConstructor.getList());

		final ConstructorMemberSetter<CONSTRUCTOR_MEMBER> constructorMemberSetter = get();

		constructorMemberSetter.addConstructorMember(constructorMember);

		logExit(context);
	}

	@Override
	public final void onClassMethodStart(Context context) {

		logEnter(context);

		final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, CLASS_METHOD_MODIFIER_HOLDER> method
			= new StackClassMethod<>(logger);

		push(method);

		pushVariableScope();

		logExit(context);
	}

	@Override
	public final void onMethodReturnTypeStart(Context context) {

		logEnter(context);

		push(new StackReturnType<>(logger));

		logExit(context);
	}

	@Override
	public final void onMethodReturnTypeEnd(Context context) {

		logEnter(context);

		final StackReturnType<TYPE_REFERENCE> stackReturnType = pop();

		final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> stackMethod = get();

		stackMethod.setReturnType(stackReturnType.getType());

		logExit(context);
	}

	@Override
	public final void onMethodName(Context context, long methodName) {

		logEnter(context);

		final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> method = get();

		method.setName(stringSource.asString(methodName), context);

		logExit(context);
	}

	@Override
	public final void onMethodSignatureParametersStart(Context context) {
	    
	    logEnter(context);

	    logExit(context);
	}

	@Override
	public final void onMethodSignatureParameterStart(Context context, boolean varArgs) {

		logEnter(context);

		push(new StackParameterSignature<>(logger, varArgs));

		logExit(context);
	}

	@Override
	public final void onMethodSignatureParameterEnd(Context context) {

		logEnter(context);

		final StackParameterSignature<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE> stackParameterSignature = pop();

		final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> stackCallable = get();

		final PARAMETER parameter = parseTreeFactory.createParameter(context,
				stackParameterSignature.getTypeReference(), stackParameterSignature.getName(),
				stackParameterSignature.getNameContext(), stackParameterSignature.isVarArgs());

		stackCallable.addParameter(parameter);

		final VARIABLE_DECLARATION variableDeclaration = makeVariableDeclaration(context, stackParameterSignature);

		variableScopes.get().add(stackParameterSignature.getName(), variableDeclaration);

		logExit(context);
	}

	@Override
	public final void onMethodSignatureParametersEnd(Context context) {

	    logEnter(context);
	    
	    logExit(context);
	}

	private final void addClassMethodModifier(Context context, ClassMethodModifier modifier) {

		logEnter(context);

		final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, CLASS_METHOD_MODIFIER_HOLDER> stackMethod = get();

		stackMethod.addModifier(parseTreeFactory.createClassMethodModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onVisibilityClassMethodModifier(Context context, ClassMethodVisibility visibility) {

		logEnter(context);

		addClassMethodModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onOverrideClassMethodModifier(Context context, ClassMethodOverride methodOverride) {

		logEnter(context);

		addClassMethodModifier(context, methodOverride);

		logExit(context);
	}

	@Override
	public final void onStaticClassMethodModifier(Context context) {

		logEnter(context);

		addClassMethodModifier(context, new ClassMethodStatic());

		logExit(context);
	}

	@Override
	public final void onStrictfpClassMethodModifier(Context context) {
		logEnter(context);

		addClassMethodModifier(context, new ClassMethodStrictfp());

		logExit(context);
	}

	@Override
	public final void onSynchronizedClassMethodModifier(Context context) {

		logEnter(context);

		addClassMethodModifier(context, new ClassMethodSynchronized());

		logExit(context);
	}

	@Override
	public final void onNativeClassMethodModifier(Context context) {

		logEnter(context);

		addClassMethodModifier(context, new ClassMethodNative());

		logExit(context);
	}

	@Override
	public final void onClassMethodEnd(Context context) {

		logEnter(context);

		popVariableScope();

		final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, CLASS_METHOD_MODIFIER_HOLDER> method = pop();

		final ClassMethodMemberSetter<CLASS_METHOD_MEMBER> methodMemberSetter = get();
		
		final CLASS_METHOD_MEMBER methodMember = parseTreeFactory.createClassMethodMember(
				context,
				method.getModifiers(),
				method.getReturnType(),
				method.getName(),
				method.getNameContext(),
				method.getParameters(),
				method.getList());

		methodMemberSetter.addMethod(methodMember);

		logExit(context);
	}

	@Override
	public final void onFieldDeclarationStart(Context context) {

		logEnter(context);

		push(new StackFieldDeclarationList<>(logger));

		logExit(context);
	}

	private void addFieldModifier(Context context, FieldModifier modifier) {
		final StackFieldDeclarationList<TYPE_REFERENCE, EXPRESSION, FIELD_MODIFIER_HOLDER> stackFieldDeclarationList = get();

		stackFieldDeclarationList.addFieldModifier(parseTreeFactory.createFieldModifierHolder(context, modifier));
	}

	@Override
	public final void onVisibilityFieldModifier(Context context, FieldVisibility visibility) {

		logEnter(context);

		addFieldModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onStaticFieldModifier(Context context) {

		logEnter(context);

		addFieldModifier(context, new FieldStatic());

		logExit(context);
	}

	@Override
	public final void onMutabilityFieldModifier(Context context, ASTMutability mutability) {

		logEnter(context);

		addFieldModifier(context, mutability);

		logExit(context);
	}

	@Override
	public final void onTransientFieldModifier(Context context) {

		logEnter(context);

		addFieldModifier(context, new FieldTransient());

		logExit(context);
	}

	@Override
	public final void onVolatileFieldModifier(Context context) {

		logEnter(context);

		addFieldModifier(context, new FieldVolatile());

		logExit(context);
	}

	@Override
	public final void onFieldDeclarationEnd(Context context) {

		logEnter(context);

		final StackFieldDeclarationList<TYPE_REFERENCE, EXPRESSION, FIELD_MODIFIER_HOLDER> stackFieldDeclarationList = pop();

		final StackClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> stackClass = get();

		final List<INITIALIZER_VARIABLE_DECLARATION_ELEMENT> initializers = new ArrayList<>(stackFieldDeclarationList.getList().size());
		
		for (StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION> element : stackFieldDeclarationList.getList()) {

			final INITIALIZER_VARIABLE_DECLARATION_ELEMENT initializerElement
			    = parseTreeFactory.createInitializerVariableDeclarationElement(
		                 element.getContext(),
    		             element.getVarName(),
    		             element.getVarNameContext(),
    		             element.getNumDims(),
    		             element.getInitializer());

			initializers.add(initializerElement);
		}

        final CLASS_FIELD_MEMBER dataFieldMember = parseTreeFactory.createClassFieldMember(
                context,
                stackFieldDeclarationList.getModifiers(),
                stackFieldDeclarationList.getTypeReference(),
                initializers
            );
            
        stackClass.add(dataFieldMember);

		logExit(context);
	}

	@Override
	public final void onInterfaceStart(Context context, long interfaceKeyword, Context interfaceKeywordContext,
			long name, Context nameContext) {

		logEnter(context);

		push(new StackInterface<>(logger, stringSource.asString(interfaceKeyword), interfaceKeywordContext, stringSource.asString(name), nameContext));

		logExit(context);
	}

	private void addInterfaceModifier(Context context, InterfaceModifier modifier) {

		logEnter(context);

		final StackInterface<COMPLEX_MEMBER_DEFINITION, INTERFACE_MODIFIER_HOLDER, TYPE_REFERENCE, INTERFACE_METHOD_MEMBER> stackInterface = get();

		stackInterface.addModifier(parseTreeFactory.createInterfaceModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onVisibilityInterfaceModifier(Context context, InterfaceVisibility visibility) {

		logEnter(context);

		addInterfaceModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onAbstractInterfaceModifier(Context context) {

		logEnter(context);

		addInterfaceModifier(context, new InterfaceAbstract());

		logExit(context);
	}

	@Override
	public final void onStaticInterfaceModifier(Context context) {

		logEnter(context);

		addInterfaceModifier(context, new InterfaceStatic());

		logExit(context);
	}

	@Override
	public final void onStrictfpInterfaceModifier(Context context) {

		logEnter(context);

		addInterfaceModifier(context, new InterfaceStrictfp());

		logExit(context);
	}

	@Override
	public final void onInterfaceExtends(Context context, ScopedName interfaceName) {

		logEnter(context);

		final StackInterface<COMPLEX_MEMBER_DEFINITION, INTERFACE_MODIFIER_HOLDER, TYPE_REFERENCE, INTERFACE_METHOD_MEMBER> entry = get();

		entry.addExtendedInterface(parseTreeFactory.createResolveLaterTypeReference(context, interfaceName, ReferenceType.NAME));

		logExit(context);
	}

	@Override
	public final void onInterfaceEnd(Context context) {

		logEnter(context);

		final StackInterface<COMPLEX_MEMBER_DEFINITION, INTERFACE_MODIFIER_HOLDER, TYPE_REFERENCE, INTERFACE_METHOD_MEMBER> entry = pop();

		final List<COMPLEX_MEMBER_DEFINITION> interfaceCode = entry.getList();

		final INTERFACE_DEFINITION classDefinition = parseTreeFactory.createInterfaceDefinition(
				context,
				entry.getModifiers(),
				entry.getInterfaceKeyword() != null
					? parseTreeFactory.createKeyword(entry.getInterfaceKeywordContext(), entry.getInterfaceKeyword())
					: null,
				entry.getName(),
				entry.getNameContext(),
				null,
				entry.getExtendedInterfaces(),
				interfaceCode);
		
		mainStack.addElement(classDefinition);

		logExit(context);
	}

	@Override
	public final void onEnumStart(Context context, long enumKeyword, Context enumKeywordContext, long name,
			Context nameContext) {

		logEnter(context);

		push(new StackEnum<>(logger, stringSource.asString(enumKeyword), enumKeywordContext, stringSource.asString(name), nameContext));

		logExit(context);
	}

	@Override
	public final void onEnumImplements(Context context, ScopedName interfaceName) {

		logEnter(context);

		final StackEnum<COMPLEX_MEMBER_DEFINITION, TYPE_REFERENCE, CLASS_MODIFIER_HOLDER, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER, ENUM_CONSTANT_DEFINITION> stackEnum = get();

		stackEnum.addImplementedInterface(parseTreeFactory.createResolveLaterTypeReference(context, interfaceName, ReferenceType.NAME));

		logExit(context);
	}

	@Override
	public final void onEnumConstantStart(Context context, long name) {

		logEnter(context);

		push(new StackEnumConstant<>(logger, stringSource.asString(name)));

		logExit(context);
	}

	@Override
	public final void onEnumConstantEnd(Context context) {

		logEnter(context);

		final StackEnumConstant<COMPLEX_MEMBER_DEFINITION, EXPRESSION> stackEnumConstant = pop();

		final ENUM_CONSTANT_DEFINITION enumConstant = parseTreeFactory.createEnumConstantDefinition(
				context,
				stackEnumConstant.getName(),
				stackEnumConstant.getParameters(),
				stackEnumConstant.getList());

		final StackEnum<COMPLEX_MEMBER_DEFINITION, TYPE_REFERENCE, CLASS_MODIFIER_HOLDER, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER, ENUM_CONSTANT_DEFINITION> stackEnum = get();

		stackEnum.addConstant(enumConstant);

		logExit(context);
	}

	@Override
	public final void onEnumEnd(Context context) {

		logEnter(context);

		final StackEnum<COMPLEX_MEMBER_DEFINITION, TYPE_REFERENCE, CLASS_MODIFIER_HOLDER, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER, ENUM_CONSTANT_DEFINITION>
			stackEnum = pop();

		final ENUM_DEFINITION enumDefinition = parseTreeFactory.createEnumDefinition(
				context,
				stackEnum.getModifiers(),
				stackEnum.getEnumKeyword() != null
				? parseTreeFactory.createKeyword(stackEnum.getEnumKeywordContext(), stackEnum.getEnumKeyword()) : null,
				stackEnum.getName(),
				stackEnum.getNameContext(),
				null,
				stackEnum.getImplementedInterfaces(),
				stackEnum.getConstants(),
				stackEnum.getList());
			
		mainStack.addElement(enumDefinition);

		logExit(context);
	}

	@Override
	public final void onInterfaceMethodStart(Context context) {

		logEnter(context);

		final StackInterfaceMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, INTERFACE_METHOD_MODIFIER_HOLDER> method = new StackInterfaceMethod<>(logger);

		push(method);

		pushVariableScope();

		logExit(context);
	}

	private void addInterfaceMethodModifier(Context context, InterfaceMethodModifier modifier) {

		logEnter(context);

		final StackInterfaceMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, INTERFACE_METHOD_MODIFIER_HOLDER> stackMethod = get();

		stackMethod.addModifier(parseTreeFactory.createInterfaceMethodModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onVisibilityInterfaceMethodModifier(Context context, InterfaceMethodVisibility visibility) {

		logEnter(context);

		addInterfaceMethodModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onAbstractInterfaceMethodModifier(Context context) {

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStatic());

		logExit(context);
	}

	@Override
	public final void onDefaultInterfaceMethodModifier(Context context) {

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodDefault());

		logExit(context);
	}

	@Override
	public final void onStaticInterfaceMethodModifier(Context context) {

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStatic());

		logExit(context);
	}

	@Override
	public final void onStrictfpInterfaceMethodModifier(Context context) {
		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStrictfp());

		logExit(context);
	}

	@Override
	public final void onInterfaceMethodEnd(Context context) {

		logEnter(context);

		popVariableScope();

		final StackInterfaceMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, INTERFACE_METHOD_MODIFIER_HOLDER> method = pop();

		final InterfaceMethodMemberSetter<INTERFACE_METHOD_MEMBER> methodMemberSetter = get();
		

		final INTERFACE_METHOD_MEMBER methodMember = parseTreeFactory.createInterfaceMethodMember(
				context,
				method.getModifiers(),
				method.getReturnType(),
				method.getName(),
				method.getNameContext(),
				method.getParameters(),
				method.getList());
		
		methodMemberSetter.addMethod(methodMember);

		logExit(context);
	}

	// Expressions
	@Override
	public final void onEnterAssignmentExpression(Context context) {

		logEnter(context);

		push(new StackAssignmentExpression<>(logger));

		logExit(context);
	}

	@Override
	public final void onEnterAssignmentLHS(Context context) {

		logEnter(context);

		push(new StackAssignmentLHS<>(logger));

		logExit(context);
	}

	@Override
	public final void onExitAssignmentLHS(Context context) {

		logEnter(context);

		final StackAssignmentLHS<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION> assignmentLHS = pop();

		final StackAssignmentExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> assignmentExpression = get();

		assignmentExpression.setLHS(parseTreeFactory.makeVariableReference(context, assignmentLHS.getList()));

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onExitAssignmentExpression(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackAssignmentExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackAssignmentExpression = pop();

		stackAssignmentExpression.setRHS(makeExpression(context, stackExpressionList));

		final ExpressionSetter<EXPRESSION> expressionSetter = get();

		expressionSetter.addExpression(parseTreeFactory.createAssignmentExpression(context, stackAssignmentExpression.getLHS(),
				stackAssignmentExpression.getRHS()));

		logExit(context);
	}

	@Override
	public final void onNestedExpressionStart(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onNestedExpressionEnd(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final NESTED_EXPRESSION nestedExpression = parseTreeFactory.createNestedExpression(context,
				makeExpression(context, stackExpressionList));

		final NestedExpressionSetter<NESTED_EXPRESSION> nestedExpressionSetter = get();

		nestedExpressionSetter.addNestedExpression(nestedExpression);

		logExit(context);
	}

	// Variables

	// Variable or class member
	@Override
	public final void onNameReference(Context context, long name) {

		logEnter(context);

		final VariableReferenceSetter<VARIABLE_REFERENCE> variableReferenceSetter = get();

		variableReferenceSetter.setVariableReference(parseTreeFactory.createNameReference(context, stringSource.asString(name)));

		logExit(context);
	}

	// Resolved as variable
	@Override
	public final void onVariableReference(Context context, long name) {

		logEnter(context);

		final VariableReferenceSetter<VARIABLE_REFERENCE> variableReferenceSetter = get();

		final VARIABLE_DECLARATION declaration = findVariableDeclaration(stringSource.asString(name));

		if (declaration == null) {
			throw new CompileException(context, "No variable declared for name " + name);
		}

		final SIMPLE_VARIABLE_REFERENCE variableReference = parseTreeFactory.createSimpleVariableReference(context, declaration);

		variableReferenceSetter.setVariableReference(variableReference);

		logExit(context);
	}

	// Field access

	@Override
	public final void onPrimaryStart(Context context) {

		logEnter(context);

		// Start of any primary expression, like a literal or a linked list of
		// field accesses
		push(new StackPrimaryList<>(logger));

		logExit(context);
	}

	@Override
	public final void onArrayAccessStart(Context context) {

		logEnter(context);

		push(new StackArrayAccess<>(logger));

		logExit(context);
	}

	@Override
	public final void onArrayIndexStart(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onArrayIndexEnd(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackArrayAccess<EXPRESSION, EXPRESSION, PRIMARY> stackArrayAccess = get();

		stackArrayAccess.addExpression(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onArrayAccessEnd(Context context) {

		logEnter(context);

		final StackArrayAccess<EXPRESSION, EXPRESSION, PRIMARY> stackArrayAccess = pop();

		final ARRAY_ACCESS_EXPRESSION expression = parseTreeFactory.createArrayAccessExpression(
				context,
				getArray(context, stackArrayAccess.getExpressions()),
				stackArrayAccess.getIndex());

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(expression);

		logExit(context);
	}

	@Override
	public final void onFieldAccess(Context context, FieldAccessType fieldAccessType, ScopedName typeName,
			ReferenceType referenceType, long fieldName, Context fieldNameContext) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		final FIELD_ACCESS fieldAccess = parseTreeFactory.createFieldAccess(
				context,
				fieldAccessType,
				typeName != null ? parseTreeFactory.createResolveLaterTypeReference(context, typeName, referenceType) : null,
				stringSource.asString(fieldName));

		primarySetter.addPrimary(fieldAccess);

		logExit(context);
	}

	@Override
	public final void onCastExpressionStart(Context context) {

		logEnter(context);

		push(new StackCastExpression<>(logger));

		logExit(context);
	}

	@Override
	public final void onCastExpressionEnd(Context context) {

		logEnter(context);

		final StackCastExpression<EXPRESSION, VARIABLE_REFERENCE, PRIMARY, TYPE_REFERENCE> stackCastExpression = pop();

		final ExpressionSetter<EXPRESSION> expressionSetter = get();

		final CAST_EXPRESSION castExpression = parseTreeFactory.createCastExpression(
				context,
				stackCastExpression.getTypeReference(),
				stackCastExpression.getExpression());

		expressionSetter.addExpression(castExpression);

		logExit(context);
	}

	@Override
	public final void onThisPrimary(Context context) {

		logEnter(context);

		final StackPrimaryList<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION> stackPrimaryList = get();

		@SuppressWarnings("unchecked")
		final StackNamedClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER, CLASS_MODIFIER_HOLDER, TYPE_REFERENCE> stackClass = mainStack.getFromTop(StackNamedClass.class);

		final TYPE_REFERENCE typeReference = parseTreeFactory.createResolveLaterTypeReference(context,
				new ScopedName(null, stackClass.getName()), ReferenceType.NAME);

		stackPrimaryList.add(parseTreeFactory.createThisPrimary(context, typeReference));

		logExit(context);
	}

	@Override
	public final void onPrimaryEnd(Context context) {

		logEnter(context);

		final StackPrimaryList<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION> stackPrimary = pop();

		final PRIMARY primary = makePrimary(context, stackPrimary.getList());

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(primary);

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionStart(Context context) {

		logEnter(context);

		push(new StackConditionalExpression<>(logger));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart1Start(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart1End(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart1(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart2Start(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart2End(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart2(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart3Start(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart3End(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart3(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionEnd(Context context) {

		logEnter(context);

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = pop();

		final CONDITIONAL_EXPRESSION conditionalExpression = parseTreeFactory.createConditionalExpression(context,
				stackConditionalExpression.getPart1(),
				stackConditionalExpression.getPart2(),
				stackConditionalExpression.getPart3());

		final ExpressionSetter<EXPRESSION> expressionSetter = get();

		expressionSetter.addExpression(conditionalExpression);

		logExit(context);
	}

	// Literals

	@Override
	public final void onIntegerLiteral(Context context, long value, Base base, boolean signed, int bits) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createIntegerLiteral(context, value, base, signed, bits));

		logExit(context);
	}

	@Override
	public final void onFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createFloatingPointLiteral(context, value, base, bits));

		logExit(context);
	}

	@Override
	public final void onBooleanLiteral(Context context, boolean value) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createBooleanLiteral(context, value));

		logExit(context);
	}

	@Override
	public final void onCharacterLiteral(Context context, char value) {

		logEnter(context);

		final PrimarySetter<PRIMARY> expressionSetter = get();

		expressionSetter.addPrimary(parseTreeFactory.createCharacterLiteral(context, value));

		logExit(context);
	}

	@Override
	public final void onStringLiteral(Context context, long value) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createStringLiteral(context, stringSource.asString(value)));

		logExit(context);
	}

	@Override
	public final void onNullLiteral(Context context) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createNullLiteral(context));

		logExit(context);
	}

	@Override
	public final void onClassInstanceCreationExpressionStart(Context context) {

		logEnter(context);

		push(new StackClassInstanceCreationExpression<>(logger));

		logExit(context);
	}

	@Override
	public final void onClassInstanceCreationTypeAndConstructorName(Context context, ScopedName name) {

		logEnter(context);

		final StackClassInstanceCreationExpression<TYPE_REFERENCE, CONSTRUCTOR_NAME, EXPRESSION, CLASS_METHOD_MEMBER> stackClassInstanceCreationExpression = get();

		stackClassInstanceCreationExpression.setType(parseTreeFactory.createResolveLaterTypeReference(context, name, ReferenceType.NAME));
		stackClassInstanceCreationExpression.setConstructorName(parseTreeFactory.createConstructorName(context, name.getName()));

		logExit(context);
	}

	@Override
	public final void onClassInstanceCreationExpressionEnd(Context context) {

		logEnter(context);

		final StackClassInstanceCreationExpression<TYPE_REFERENCE, CONSTRUCTOR_NAME, EXPRESSION, CLASS_METHOD_MEMBER> classInstanceCreationExpression = pop();

		final CLASS_INSTANCE_CREATION_EXPRESSION primary = parseTreeFactory.createClassInstanceCreationExpression(
				context,
				classInstanceCreationExpression.getType(),
				classInstanceCreationExpression.getConstructorName(),
				classInstanceCreationExpression.getParameters() != null
						? classInstanceCreationExpression.getParameters() : Collections.emptyList(),
				classInstanceCreationExpression.getMethods());

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(primary);

		logExit(context);
	}

	@Override
	public final void onMethodInvocationStart(
			Context context,
			MethodInvocationType type,
			ScopedName classTypeName,
			Context classTypeNameContext,
			ReferenceType referenceType,
			long methodName,
			Context methodNameContext) {

		logEnter(context);

		TYPE_REFERENCE classType = null;
		
		if (classTypeName != null && referenceType == ReferenceType.NAME) {
			classType = parseTreeFactory.createResolveLaterTypeReference(classTypeNameContext, classTypeName, referenceType);
			
			if (variableScopesContain(classTypeName.getName())) {
				// Likely a scoped variable, eg. variable.invokeMethod() instead
				// of Class.invokeStaticMethod()

				if (type == MethodInvocationType.NAMED_CLASS_STATIC) {
					type = MethodInvocationType.VARIABLE_REFERENCE;
					classType = null;
				}
			}
		}

		push(new StackMethodInvocation<>(logger, type, classType, stringSource.asString(methodName), methodNameContext));

		logExit(context);
	}

	@Override
	public final void onParametersStart(Context context) {

		logEnter(context);

		push(new StackParameterList<>(logger));

		logExit(context);
	}

	@Override
	public final void onParameterStart(Context context) {

		logEnter(context);

		push(new StackParameter<>(logger));

		logExit(context);
	}

	@Override
	public final void onParameterEnd(Context context) {

		logEnter(context);

		final StackParameter<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackParameter = pop();

		final StackParameterList<EXPRESSION> stackParameterList = get();

		stackParameterList.addExpression(makeExpression(context, stackParameter));

		logExit(context);
	}

	@Override
	public final void onParametersEnd(Context context) {

		logEnter(context);

		final StackParameterList<EXPRESSION> stackParameterList = pop();

		final ParametersSetter<EXPRESSION> parametersSetter = get();

		parametersSetter.setParameters(stackParameterList.getList());

		logExit(context);
	}

	@Override
	public final void onMethodInvocationEnd(Context context) {

		logEnter(context);

		final StackMethodInvocation<EXPRESSION, PRIMARY, TYPE_REFERENCE> stackMethodInvocation = pop();

		final METHOD_INVOCATION_EXPRESSION methodInvocation = parseTreeFactory.createMethodInvocationExpression(
				context,
				stackMethodInvocation.getType(),
				stackMethodInvocation.getClassType(),
				stackMethodInvocation.getObject(),
				stackMethodInvocation.getName(),
				stackMethodInvocation.getNameContext(),
				stackMethodInvocation.getParameters() != null
						? stackMethodInvocation.getParameters()
						: Collections.emptyList());

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(methodInvocation);

		logExit(context);
	}

	@Override
	public final void onArrayCreationExpressionStart(Context context, ScopedName typeName, ReferenceType referenceType, int numDims) {

		logEnter(context);

		final TYPE_REFERENCE typeReference = parseTreeFactory.createResolveLaterTypeReference(context, typeName, referenceType);
		
		push(new StackArrayCreationExpression<>(logger, typeReference, numDims));

		logExit(context);
	}

	@Override
	public final void onDimExpressionStart(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onDimExpressionEnd(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackArrayCreationExpression<TYPE_REFERENCE, EXPRESSION> stackArrayCreationExpression = get();

		stackArrayCreationExpression.addDimExpression(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onArrayCreationExpressionEnd(Context context) {

		logEnter(context);

		final StackArrayCreationExpression<TYPE_REFERENCE, EXPRESSION> stackArrayCreationExpression = pop();

		final ARRAY_CREATION_EXPRESSION expression = parseTreeFactory.createArrayCreationExpression(
				context,
				stackArrayCreationExpression.getType(),
				stackArrayCreationExpression.getDimExpressions(),
				stackArrayCreationExpression.getNumDims());

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(expression);

		logExit(context);
	}

	// Class expressions
	@Override
	public final void onClassExpression(Context context, long className, int numArrayDims) {

		logEnter(context);

		final CLASS_EXPRESSION expression = parseTreeFactory.createClassExpression(context, stringSource.asString(className), numArrayDims);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(expression);

		logExit(context);
	}

	// Lambda expressions
	@Override
	public final void onLambdaExpressionStart(Context context) {

		logEnter(context);

		push(new StackLambdaExpression<>(logger));

		pushVariableScope();

		logExit(context);
	}

	@Override
	public final void onSingleLambdaParameter(Context context, long varName, Context varNameContext) {

		logEnter(context);

		final StackLambdaExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = get();

		stackLambdaExpression.setSingleParameter(stringSource.asString(varName), varNameContext);

		logExit(context);
	}

	@Override
	public final void onFormalLambdaParameterListStart(Context context) {

		logEnter(context);

		push(new StackLambdaFormalParameters(logger));

		logExit(context);
	}

	@Override
	public final void onFormalLambdaParameterListEnd(Context context) {

		logEnter(context);

		pop();

		logExit(context);
	}

	@Override
	public final void onInferredLambdaParameterList(Context context, List<String> varNames, Context varNamesContext) {

		logEnter(context);

		final StackLambdaExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = get();

		stackLambdaExpression.setInferredParameterList(varNames, varNamesContext);

		logExit(context);
	}

	@Override
	public final void onLambdaBodyStart(Context context) {

		logEnter(context);

		logExit(context);
	}

	@Override
	public final void onLambdaBodyEnd(Context context) {

		logEnter(context);

		logExit(context);
	}

	@Override
	public final void onLambdaExpressionEnd(Context context) {

		logEnter(context);

		final StackLambdaExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = pop();

		final LAMBDA_EXPRESSION_PARAMETERS parameters;

		if (stackLambdaExpression.getSingleParameter() != null) {
			parameters = parseTreeFactory.createLambdaExpressionParameters(stackLambdaExpression.getSingleParameterContext(),
					stackLambdaExpression.getSingleParameter());
		} else if (stackLambdaExpression.getInferredParameterList() != null) {
			parameters = parseTreeFactory.createLambdaExpressionParameters(stackLambdaExpression.getInferredParametersContext(),
					stackLambdaExpression.getInferredParameterList());
		} else {
			throw new UnsupportedOperationException();
		}

		final LAMBDA_EXPRESSION lambdaExpression;

		final EXPRESSION expression = makeExpressionOrNull(context, stackLambdaExpression);

		if (expression != null) {
			lambdaExpression = parseTreeFactory.createSingleLambdaExpression(context, parameters, expression);
		} else {
			lambdaExpression = parseTreeFactory.createBlockLambdaExpression(context, parameters, stackLambdaExpression.getStatements());
		}

		final ExpressionSetter<EXPRESSION> expressionSetter = get();

		expressionSetter.addExpression(lambdaExpression);

		popVariableScope();

		logExit(context);
	}

	// Statements

	@Override
	public final void onMutabilityVariableModifier(Context context, ASTMutability mutability) {

		logEnter(context);

		addVariableModifier(context, mutability);

		logExit(context);
	}

	private void addVariableModifier(Context context, VariableModifier modifier) {

		logEnter(context);

		Objects.requireNonNull(modifier);

		final VariableModifierSetter<VARIABLE_MODIFIER_HOLDER> modifierSetter = get();

		modifierSetter.addModifier(parseTreeFactory.createVariableModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public void onVariableDeclarationStatementStart(Context context) {

		logEnter(context);

		push(new StackVariableDeclarationList<>(logger));

		logExit(context);
	}

	@Override
	public void onVariableDeclarationStatementEnd(Context context) {

		logEnter(context);

		final StackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION, VARIABLE_MODIFIER_HOLDER> variableDeclaration = pop();

		final StatementSetter<STATEMENT> statementSetter = get();

		final List<INITIALIZER_VARIABLE_DECLARATION_ELEMENT> elements = variableDeclaration.getList().stream()
				.map(e -> createInitializer(e))
				.collect(Collectors.toList());
		
		final VARIABLE_DECLARATION_STATEMENT statement = parseTreeFactory.createVariableDeclarationStatement(
				context,
				variableDeclaration.getModifiers(),
				variableDeclaration.getTypeReference(),
				elements);

		variableDeclaration.getList().forEach(e -> {
			
			final VARIABLE_DECLARATION var = parseTreeFactory.createVariableDeclaration(
					context,
					variableDeclaration.getModifiers(),
					variableDeclaration.getTypeReference(),
					e.getVarName(),
					e.getNumDims());
			
			variableScopes.get().add(e.getVarName(), var);
		});

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public void onVariableDeclaratorStart(Context context) {

		logEnter(context);

		push(new StackVariableDeclaration<>(logger));

		logExit(context);
	}

	@Override
	public void onVariableDeclaratorEnd(Context context) {

		logEnter(context);

		final StackVariableDeclaration<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackDeclaration = pop();

		final EXPRESSION initializer = makeExpressionOrNull(context, stackDeclaration);

		final BaseStackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION> declarationList = get();
		
		final StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION> variableDeclarationElement
			= new StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION>(
					context,
					stackDeclaration.getName(),
					stackDeclaration.getNameContext(),
					stackDeclaration.getNumDims(),
					initializer);

		declarationList.add(variableDeclarationElement);

		logExit(context);
	}

    @Override
    public final void onNonScopedTypeReference(Context context, long name, ReferenceType referenceType) {

        logEnter(context);

        Objects.requireNonNull(name);

        final TypeReferenceSetter<TYPE_REFERENCE> typeReferenceSetter = get();
        
        final TYPE_REFERENCE typeReference;
        
        switch (referenceType) {
        case SCALAR:
            typeReference = parseTreeFactory.createScalarTypeReference(context, stringSource.asString(name));
            break;
            
        default:
            final ScopedName scopedName = ScopedName.makeScopedName(stringSource.asString(name));
            
            typeReference = parseTreeFactory.createResolveLaterTypeReference(
                    context,
                    scopedName,
                    referenceType);
            break;
        }

        typeReferenceSetter.setTypeReference(typeReference);

        logExit(context);
    }
	
	@Override
	public final void onScopedTypeReferenceStart(Context context, ReferenceType referenceType) {

		logEnter(context);
		
		push(new StackScopedTypeReference(getLogger(), referenceType));
		
		push(new StackScopedName(getLogger()));

		logExit(context);
	}

	@Override
    public void onScopedTypeReferencePart(Context context, long part) {

	    logEnter(context);

	    final StackScopedName stackScopedName = get();
	            
        stackScopedName.addPart(stringSource.asString(part), context);
	    
	    logExit(context);
    }

    @Override
    public void onScopedTypeReferenceEnd(Context context) {

        logEnter(context);
        
        final StackScopedName stackScopedName = pop();
        
        final StackScopedTypeReference stackScopedTypeReference = pop();
        
        final TypeReferenceSetter<TYPE_REFERENCE> typeReferenceSetter = get();

        final TYPE_REFERENCE typeReference = parseTreeFactory.createResolveLaterTypeReference(
                            context,
                            stackScopedName.getScopedName(),
                            stackScopedTypeReference.getReferenceType());
        
        typeReferenceSetter.setTypeReference(typeReference);
        
        logExit(context);
    }

    @Override
	public final void onExpressionStatementStart(Context context) {

		logEnter(context);

		push(new StackExpressionStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onExpressionStatementEnd(Context context) {

		logEnter(context);

		final StackExpressionStatement<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> statement = pop();

		final EXPRESSION_STATEMENT expressionStatement = parseTreeFactory.createExpressionStatement(context, makeExpression(context, statement));

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(expressionStatement);

		logExit(context);
	}

	@Override
	public final void onForStatementStart(Context context, long keyword, Context keywordContext) {

		logEnter(context);

		push(new StackForStatement<>(logger, stringSource.asString(keyword), keywordContext));

		logExit(context);
	}

	@Override
	public final void onForInitStart(Context context) {

		logEnter(context);

		push(new StackForInit<>(logger));

		logExit(context);
	}

	@Override
	public final void onForInitEnd(Context context) {

		logEnter(context);

		final StackForInit<TYPE_REFERENCE, EXPRESSION> stackForInit = pop();

		final FOR_INIT forInit;

		if (!stackForInit.getList().isEmpty()) {

			final StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION> element = stackForInit.getList().get(0);
			
			forInit = parseTreeFactory.createForInit(context, createInitializer(element));
		} else if (!stackForInit.getExpressions().isEmpty()) {
			forInit = parseTreeFactory.createForInit(context, stackForInit.getExpressions());
		} else {
			forInit = null;
		}

		if (forInit != null) {
			final StackForStatement<EXPRESSION, EXPRESSION, VARIABLE_REFERENCE, STATEMENT, FOR_INIT> forStatement = get();

			forStatement.setForInit(forInit);
		}

		logExit(context);
	}

	@Override
	public final void onForUpdateStart(Context context) {

		logEnter(context);

		push(new StackForUpdate<>(logger));

		logExit(context);
	}

	@Override
	public final void onForUpdateEnd(Context context) {

		logEnter(context);

		final StackForUpdate<EXPRESSION> stackForUpdate = pop();

		if (!stackForUpdate.getList().isEmpty()) {
			final StackForStatement<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT, FOR_INIT> stackForStatement = get();

			stackForStatement.setForUpdate(stackForUpdate.getList());
		}

		logExit(context);
	}

	@Override
	public final void onForStatementEnd(Context context) {

		logEnter(context);

		final StackForStatement<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT, FOR_INIT> stackForStatement = pop();

		final FOR_STATEMENT statement = parseTreeFactory.createForStatement(
				context,
				parseTreeFactory.createKeyword(stackForStatement.getKeywordContext(), stackForStatement.getKeyword()),
				stackForStatement.getForInit(),
				makeExpressionOrNull(context, stackForStatement),
				stackForStatement.getForUpdate(),
				stackForStatement.getStatements());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public final void onIteratorForStatementStart(Context context) {

		logEnter(context);

		push(new StackIteratorForStatement<>(logger));

		pushVariableScope();

		logExit(context);
	}

	@Override
	public final void onIteratorForTestEnd(Context context) {

		logEnter(context);

		final StackIteratorForStatement<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackIteratorForStatement = get();

		// Must add variable declarations to scope so that can be found further
		// down in parsing
		final VARIABLE_DECLARATION variableDeclaration = makeVariableDeclaration(context, stackIteratorForStatement);

		variableScopes.get().add(stackIteratorForStatement.getName(), variableDeclaration);

		logExit(context);
	}

	@Override
	public final void onIteratorForStatementEnd(Context context) {

		logEnter(context);

		popVariableScope();

		final StackIteratorForStatement<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, VARIABLE_REFERENCE, PRIMARY, STATEMENT> stackIteratorForStatement = pop();

		final ITERATOR_FOR_STATEMENT statement = parseTreeFactory.createIteratorForStatement(
				context,
				stackIteratorForStatement.getModifiers(),
				stackIteratorForStatement.getTypeReference(),
				stackIteratorForStatement.getName(),
				stackIteratorForStatement.getNameContext(),
				stackIteratorForStatement.getNumDims(),
				stackIteratorForStatement.getExpression(),
				stackIteratorForStatement.getStatements());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public final void onWhileStatementStart(Context context) {

		logEnter(context);

		push(new StackWhileStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onWhileStatementEnd(Context context) {

		logEnter(context);

		final StackWhileStatement<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackWhileStatement = pop();

		final WHILE_STATEMENT whileStatement = parseTreeFactory.createWhileStatement(
				context,
				makeExpression(context, stackWhileStatement),
				stackWhileStatement.getStatements());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(whileStatement);

		logExit(context);
	}

	@Override
	public final void onDoWhileStatementStart(Context context) {

		logEnter(context);

		push(new StackDoWhileStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onDoWhileStatementEnd(Context context) {

		logEnter(context);

		final StackDoWhileStatement<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackDoWhileStatement = pop();

		final DO_WHILE_STATEMENT doWhileStatement = parseTreeFactory.createDoWhileStatement(
				context,
				makeExpression(context, stackDoWhileStatement),
				stackDoWhileStatement.getStatements());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(doWhileStatement);

		logExit(context);
	}

	@Override
	public final void onTryWithResourcesStatementStart(Context context) {

		logEnter(context);

		push(new StackTryWithResourcesStatement<>(logger));

		pushVariableScope(); // for the variables in resources

		logExit(context);
	}

	@Override
	public final void onTryWithResourcesSpecificationStart(Context context) {

		logEnter(context);

		push(new StackResourceList<>(logger));

		logExit(context);
	}

	@Override
	public final void onResourceStart(Context context) {

		logEnter(context);

		push(new StackResource<>(logger));

		logExit(context);
	}

	@Override
	public final void onVariableName(Context context, long name, int numDims) {

		logEnter(context);

		final VariableNameSetter variableNameSetter = get();

		variableNameSetter.init(stringSource.asString(name), context, numDims);

		logExit(context);
	}

	@Override
	public final void onResourceEnd(Context context) {

		logEnter(context);

		final StackResource<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, PRIMARY> stackResource = pop();

		final RESOURCE resource = parseTreeFactory.createResource(
				context,
				stackResource.getModifiers(),
				stackResource.getTypeReference(),
				stackResource.getName(),
				stackResource.getNameContext(),
				stackResource.getNumDims(),
				stackResource.getInitializer());

		variableScopes.get().add(
				stackResource.getName(),
				parseTreeFactory.createVariableDeclaration(
						context,
						stackResource.getModifiers(),
						stackResource.getTypeReference(),
						stackResource.getName(),
						stackResource.getNumDims()));

		final StackResourceList<RESOURCE> stackResourceList = get();

		stackResourceList.add(resource);

		logExit(context);
	}

	@Override
	public final void onTryWithResourcesSpecificationEnd(Context context) {

		logEnter(context);

		final StackResourceList<RESOURCE> stackResourceList = pop();

		final StackTryWithResourcesStatement<BLOCK, CATCH_BLOCK, RESOURCE> stackTryWithResourcesStatement = get();

		stackTryWithResourcesStatement.setResources(stackResourceList.getList());

		// Must push try-block to collect statements
		push(new StackTryBlock<>(logger));

		logExit(context);
	}

	@Override
	public final void onTryStatementStart(Context context) {

		logEnter(context);

		push(new StackTryCatchFinallyStatement<>(logger));

		push(new StackTryBlock<>(logger));

		logExit(context);
	}

	@Override
	public final void onTryBlockEnd(Context context) {

		logEnter(context);

		final StackTryBlock<STATEMENT> tryBlock = pop();

		final BaseStackTryCatchFinally<STATEMENT, TYPE_REFERENCE> baseStackTryCatchFinally = get();

		baseStackTryCatchFinally.setTryBlock(tryBlock);

		logExit(context);
	}
	
	@Override
	public final void onCatchStart(Context context) {

		logEnter(context);

		push(new StackCatchBlock<>(logger));

		logExit(context);
	}

	@Override
	public final void onCatchEnd(Context context) {

		logEnter(context);

		final StackCatchBlock<STATEMENT, TYPE_REFERENCE> stackCatchBlock = pop();

		final BaseStackTryCatchFinally<STATEMENT, CATCH_BLOCK> baseStackTryCatchFinally = get();

		final CATCH_BLOCK catchBlock = parseTreeFactory.createCatchBlock(
				context,
				stackCatchBlock.getExceptionTypes(),
				stackCatchBlock.getExceptionVarName(),
				stackCatchBlock.getList());
		
		baseStackTryCatchFinally.addCatchBlock(catchBlock);

		logExit(context);
	}

	@Override
	public final void onFinallyStart(Context context) {

		logEnter(context);

		push(new StackFinallyBlock<>(logger));

		logExit(context);
	}

	@Override
	public final void onFinallyEnd(Context context) {

		logEnter(context);

		final StackFinallyBlock<STATEMENT> stackFinallyBlock = pop();

		final BaseStackTryCatchFinally<STATEMENT, TYPE_REFERENCE> baseStackTryCatchFinally = get();

		baseStackTryCatchFinally.setFinallyBlock(stackFinallyBlock);

		logExit(context);
	}

	@Override
	public final void onTryStatementEnd(Context context) {

		logEnter(context);

		final StackTryCatchFinallyStatement<STATEMENT, CATCH_BLOCK> stackTryCatchFinallyStatement = pop();

		final TRY_CATCH_FINALLY statement = parseTreeFactory.createTryCatchFinallyStatement(
				context,
				stackTryCatchFinallyStatement.getTryBlock().getList(),
				stackTryCatchFinallyStatement.getCatchBlocks(),
				stackTryCatchFinallyStatement.getFinallyBlock().getList());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public final void onTryWithResourcesEnd(Context context) {

		logEnter(context);

		popVariableScope();

		final StackTryWithResourcesStatement<STATEMENT, CATCH_BLOCK, RESOURCE> stackTryWithResourcesStatement = pop();

		final TRY_WITH_RESOURCES statement = parseTreeFactory.createTryWithResourcesStatement(
				context,
				stackTryWithResourcesStatement.getResources(),
				stackTryWithResourcesStatement.getTryBlock().getList(),
				stackTryWithResourcesStatement.getCatchBlocks(),
				stackTryWithResourcesStatement.getFinallyBlock().getList());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public final void onReturnStatementStart(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onReturnStatementEnd(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpression = pop();

		final EXPRESSION expression = makeExpressionOrNull(context, stackExpression);

		final RETURN_STATEMENT returnStatement = parseTreeFactory.createReturnStatement(context, expression);

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(returnStatement);

		logExit(context);
	}

	@Override
	public final void onThrowStatementStart(Context context) {

		logEnter(context);

		push(new StackThrowStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onThrowStatementEnd(Context context) {

		logEnter(context);

		final StackThrowStatement<EXPRESSION, VARIABLE_REFERENCE, PRIMARY> stackThrowStatement = pop();

		final THROW_STATEMENT throwStatement = parseTreeFactory.createThrowStatement(context, stackThrowStatement.getExpression());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(throwStatement);

		logExit(context);
	}

	@Override
	public final void onAnnotationStart(Context context) {

		logEnter(context);

		push(new StackAnnotation<>(logger));

		logExit(context);
	}

	@Override
	public final void onAnnotationEnd(Context context) {

		logEnter(context);

		pop();

		logExit(context);
	}

	// Stack methods

	protected final void push(StackEntry element) {

		Objects.requireNonNull(element);

		final List<String> stack = mainStack.stream().map(o -> o.getClass().getSimpleName())
				.collect(Collectors.toList());

		if (logger != null) {
			logger.onStackPush(element.getClass().getSimpleName(), stack);
		}

		mainStack.push(element);
	}

	@SuppressWarnings("unchecked")
	protected final <T extends StackEntry> T pop() {
		final T result = (T) mainStack.pop();

		if (logger != null) {
			logger.onStackPop(result.getClass().getSimpleName());
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	protected final <T> T get() {
		return (T)mainStack.get();
	}

	@SuppressWarnings("unchecked")
	private <T extends StackEntry, R extends T> R get(Class<T> cl) {
		return (R)mainStack.getFromTop(cl);
	}

	protected final void pushVariableScope() {
		variableScopes.push(new VariablesMap<>());
	}

	protected final void popVariableScope() {
		variableScopes.pop();
	}

	private final VARIABLE_DECLARATION makeVariableDeclaration(Context context,
			BaseStackVariableDeclaration<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE> stackDeclaration) {

		final VARIABLE_DECLARATION variableDeclaration = parseTreeFactory.createVariableDeclaration(
				context,
				stackDeclaration.getModifiers(),
				stackDeclaration.getTypeReference(),
				stackDeclaration.getName(),
				stackDeclaration.getNumDims());

		return variableDeclaration;
	}

	private final EXPRESSION makeExpressionOrNull(Context context,
			StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList) {
		return makeExpressionOrNull(context, stackExpressionList.getOperators(context), stackExpressionList.getList());
	}

	private final EXPRESSION makeExpressionOrNull(Context context, List<Operator> operators,
			List<EXPRESSION> expressions) {
		return expressions.isEmpty() ? null : makeExpression(context, operators, expressions);
	}

	final EXPRESSION makeExpression(Context context,
			StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList) {
		return makeExpression(context, stackExpressionList.getOperators(context), stackExpressionList.getList());
	}

	private final EXPRESSION makeExpression(Context context, List<Operator> operators, List<EXPRESSION> expressions) {

		final EXPRESSION result;

		if (expressions.isEmpty()) {
			throw new IllegalStateException("No expressions");
		} else if (expressions.size() == 1) {
			result = expressions.get(0);
		} else {
			final Context listContext = Context.merge(expressions,
					expression -> parseTreeFactory.getExpressionContext(expression));

			result = parseTreeFactory.createExpressionList(listContext, operators, expressions);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public PRIMARY getArray(Context context, List<EXPRESSION> expressions) {

		final PRIMARY primary;
		
		if (expressions.size() < 3) {
			primary = (PRIMARY)expressions.get(0);
		}
		else {
		
			primary = parseTreeFactory.createPrimaryList(
					context,
					expressions
						.subList(0, expressions.size() - 1)
						.stream()
							.map(e -> (PRIMARY)e)
							.collect(Collectors.toList()));
		}
		
		return primary;
	}

	public final PRIMARY makePrimaryOrNull(Context context, List<PRIMARY> primaries) {
		return primaries.isEmpty()
				? null
				: makePrimary(context, primaries);
	}

	public final PRIMARY makePrimary(Context context, List<PRIMARY> primaries) {
		
		final PRIMARY result;
		
		if (primaries.isEmpty()) {
			throw new IllegalStateException("No expressions");
		}
		else if (primaries.size() == 1) {
			result = primaries.get(0);
		}
		else {
			result = parseTreeFactory.createPrimaryList(context, primaries);
		}
		
		return result;
	}

	private INITIALIZER_VARIABLE_DECLARATION_ELEMENT createInitializer(StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION> e) {

	    return parseTreeFactory.createInitializerVariableDeclarationElement(
				e.getContext(),
				e.getVarName(),
				e.getVarNameContext(),
				e.getNumDims(),
				e.getInitializer());
	}
}
