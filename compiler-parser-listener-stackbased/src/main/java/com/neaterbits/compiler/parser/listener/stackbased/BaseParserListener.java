package com.neaterbits.compiler.parser.listener.stackbased;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.compiler.parser.listener.common.ContextAccess;
import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;
import com.neaterbits.compiler.parser.listener.stackbased.state.BaseStackTryCatchFinally;
import com.neaterbits.compiler.parser.listener.stackbased.state.BaseStackVariableDeclarationList;
import com.neaterbits.compiler.parser.listener.stackbased.state.CallStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.CallableStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAnnotation;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAnnotationElement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAnonymousClass;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackArrayAccess;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackArrayCreationExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAssignmentExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAssignmentLHS;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackAssignmentStatement;
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
import com.neaterbits.compiler.parser.listener.stackbased.state.StackGenericParameter;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackImplements;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackImport;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackInitializerVariableDeclarationElement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackInterface;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackInterfaceMethod;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackIteratorForStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackLambdaExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackLambdaFormalParameters;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackUnresolvedMethodInvocation;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackNamedClass;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackReferenceTypeArgument;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackNamespace;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackNonScopedTypeReference;
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
import com.neaterbits.compiler.parser.listener.stackbased.state.StackTypeBound;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackGenericTypeArgumentList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackGenericTypeParameters;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackTypeDefinition;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackTypeReference;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackTypeReferenceList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackTypeReferenceSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackVariableDeclaration;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackVariableDeclarationList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackWhileStatement;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackWildcardTypeArgument;
import com.neaterbits.compiler.parser.listener.stackbased.state.VariableNameSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStack;
import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.AnnotationElementSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.AnnotationSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ClassFieldMemberSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ClassMethodMemberSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ClassModifierSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ConstructorMemberSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.InterfaceMethodMemberSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.InterfaceSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.NamedGenericParametersSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.NestedExpressionSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ParametersSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeBoundSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.VariableModifierSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import com.neaterbits.compiler.types.ReferenceType;
import com.neaterbits.compiler.types.block.ConstructorInvocation;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.compiler.types.statement.ASTMutability;
import com.neaterbits.compiler.types.typedefinition.ClassMethodModifier;
import com.neaterbits.compiler.types.typedefinition.ClassMethodNative;
import com.neaterbits.compiler.types.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.types.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.types.typedefinition.ClassMethodStrictfp;
import com.neaterbits.compiler.types.typedefinition.ClassMethodSynchronized;
import com.neaterbits.compiler.types.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.types.typedefinition.ClassModifier;
import com.neaterbits.compiler.types.typedefinition.ClassStatic;
import com.neaterbits.compiler.types.typedefinition.ClassStrictfp;
import com.neaterbits.compiler.types.typedefinition.ClassVisibility;
import com.neaterbits.compiler.types.typedefinition.ConstructorModifier;
import com.neaterbits.compiler.types.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.types.typedefinition.FieldModifier;
import com.neaterbits.compiler.types.typedefinition.FieldStatic;
import com.neaterbits.compiler.types.typedefinition.FieldTransient;
import com.neaterbits.compiler.types.typedefinition.FieldVisibility;
import com.neaterbits.compiler.types.typedefinition.FieldVolatile;
import com.neaterbits.compiler.types.typedefinition.InterfaceAbstract;
import com.neaterbits.compiler.types.typedefinition.InterfaceMethodDefault;
import com.neaterbits.compiler.types.typedefinition.InterfaceMethodModifier;
import com.neaterbits.compiler.types.typedefinition.InterfaceMethodStatic;
import com.neaterbits.compiler.types.typedefinition.InterfaceMethodStrictfp;
import com.neaterbits.compiler.types.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.types.typedefinition.InterfaceModifier;
import com.neaterbits.compiler.types.typedefinition.InterfaceStatic;
import com.neaterbits.compiler.types.typedefinition.InterfaceStrictfp;
import com.neaterbits.compiler.types.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.types.typedefinition.Subclassing;
import com.neaterbits.compiler.types.typedefinition.TypeBoundType;
import com.neaterbits.compiler.types.typedefinition.VariableModifier;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.ContextScopedName;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.io.strings.StringRef;

public abstract class BaseParserListener<

		KEYWORD,
		NAME,
		NAME_LIST,
		TYPE_REFERENCE,
		INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
		VARIABLE_MODIFIER_HOLDER,
		COMPILATION_UNIT,
		IMPORT,
		COMPILATION_CODE,
		NAMESPACE,
		TYPE_DEFINITION,
		COMPLEX_MEMBER_DEFINITION,
		STATIC_INITIALIZER extends COMPLEX_MEMBER_DEFINITION,
	    ANNOTATION,
	    ANNOTATION_ELEMENT,
		CLASS_MODIFIER_HOLDER,
		
		GENERIC_TYPE_ARGUMENT,
		REFERENCE_GENERIC_TYPE_ARGUMENT extends GENERIC_TYPE_ARGUMENT,
	    WILDCARD_GENERIC_TYPE_ARGUMENT extends GENERIC_TYPE_ARGUMENT,
	    
	    TYPE_BOUND,
	    
	    NAMED_GENERIC_TYPE_PARAMETER,
	    
	    CLASS_DEFINITION extends TYPE_DEFINITION,
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
		INTERFACE_DEFINITION extends TYPE_DEFINITION,
		
		INTERFACE_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		INTERFACE_METHOD_MODIFIER_HOLDER,
		
		ENUM_DEFINITION extends TYPE_DEFINITION,
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
		
		UNARY_EXPRESSION extends EXPRESSION,

		PRE_INCREMENT_EXPRESSION extends EXPRESSION,
		POST_INCREMENT_EXPRESSION extends EXPRESSION,
		
		PRE_DECREMENT_EXPRESSION extends EXPRESSION,
		POST_DECREMENT_EXPRESSION extends EXPRESSION,

		LAMBDA_EXPRESSION extends EXPRESSION,
		SINGLE_LAMBDA_EXPRESSION extends LAMBDA_EXPRESSION,
		BLOCK_LAMBDA_EXPRESSION extends LAMBDA_EXPRESSION,
		LAMBDA_EXPRESSION_PARAMETERS,

		PRIMARY_LIST extends PRIMARY,
		
		NAME_PRIMARY extends PRIMARY,
		NESTED_EXPRESSION extends PRIMARY,
		FIELD_ACCESS extends PRIMARY,
		THIS_PRIMARY extends PRIMARY,
		CLASS_INSTANCE_CREATION_EXPRESSION extends PRIMARY,
        UNRESOLVED_METHOD_INVOCATION_EXPRESSION extends PRIMARY,
		METHOD_INVOCATION_EXPRESSION extends PRIMARY,
		ARRAY_CREATION_EXPRESSION extends PRIMARY,
		ARRAY_ACCESS_EXPRESSION extends PRIMARY,
		CLASS_EXPRESSION extends PRIMARY,
		
		NAME_REFERENCE extends VARIABLE_REFERENCE,
		
		INTEGER_LITERAL extends LITERAL,
		FLOATING_POINT_LITERAL extends LITERAL,
		BOOLEAN_LITERAL extends LITERAL,
		CHARACTER_LITERAL extends LITERAL,
		STRING_LITERAL extends LITERAL,
		NULL_LITERAL extends LITERAL,
		
		PARAMETER_MODIFIER_HOLDER,
		
		PARAMETER,
		
		VARIABLE_DECLARATION_STATEMENT extends STATEMENT,
		EXPRESSION_STATEMENT extends STATEMENT,
		ASSIGNMENT_STATEMENT extends STATEMENT,

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

	implements ParseTreeListener<COMPILATION_UNIT> {

	final StringSource stringSource;
	final ContextAccess contextAccess;
	
	private final FullContextProvider fullContextProvider;
	
	private final ParseLogger logger;

	final ParseTreeFactory<
			KEYWORD,
			NAME,
			NAME_LIST,
			TYPE_REFERENCE,
			COMPILATION_UNIT,
			IMPORT,
			COMPILATION_CODE,
			NAMESPACE,
			COMPLEX_MEMBER_DEFINITION,
			STATIC_INITIALIZER,
		    ANNOTATION,
		    ANNOTATION_ELEMENT,
			CLASS_MODIFIER_HOLDER,
			GENERIC_TYPE_ARGUMENT,
			REFERENCE_GENERIC_TYPE_ARGUMENT,
	        WILDCARD_GENERIC_TYPE_ARGUMENT,
	        TYPE_BOUND,
            NAMED_GENERIC_TYPE_PARAMETER,
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
			
			UNARY_EXPRESSION,
			
			PRE_INCREMENT_EXPRESSION,
			POST_INCREMENT_EXPRESSION,
			
			PRE_DECREMENT_EXPRESSION,
			POST_DECREMENT_EXPRESSION,
			
			LAMBDA_EXPRESSION,
			SINGLE_LAMBDA_EXPRESSION,
			BLOCK_LAMBDA_EXPRESSION,
			LAMBDA_EXPRESSION_PARAMETERS,
			
			PRIMARY_LIST,
			
			NAME_PRIMARY,

			NESTED_EXPRESSION,
			FIELD_ACCESS,
			THIS_PRIMARY,
			CLASS_INSTANCE_CREATION_EXPRESSION,
			METHOD_INVOCATION_EXPRESSION,
			ARRAY_CREATION_EXPRESSION,
			ARRAY_ACCESS_EXPRESSION,
			CLASS_EXPRESSION,
			
			NAME_REFERENCE,
			
			INTEGER_LITERAL,
			FLOATING_POINT_LITERAL,
			BOOLEAN_LITERAL,
			CHARACTER_LITERAL,
			STRING_LITERAL,
			NULL_LITERAL,
			
			PARAMETER_MODIFIER_HOLDER,
			
			PARAMETER,
			
			INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
			VARIABLE_DECLARATION_STATEMENT,
			
			EXPRESSION_STATEMENT,
			ASSIGNMENT_STATEMENT,
			
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

	@SuppressWarnings("unchecked")
	protected BaseParserListener(
	        StringSource stringSource,
	        ContextAccess contextAccess,
	        FullContextProvider fullContextProvider,
	        ParseLogger logger,
			@SuppressWarnings("rawtypes") ParseTreeFactory parseTreeFactory) {

		this.stringSource = stringSource;
		this.contextAccess = contextAccess;
		this.fullContextProvider = fullContextProvider;
		this.logger = logger;
		this.parseTreeFactory = parseTreeFactory;

		this.mainStack = new ListStack();
	}

	protected final ParseLogger getLogger() {
		return logger;
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

    final Context getStartContext(int index) {

        return contextAccess.getContext(index);
    }

    final Context getLeafContext(int index) {

        return contextAccess.getContext(index);
    }

    final Context getOtherContext(int index) {

        return contextAccess.getContext(index);
    }

    final Context getEndContext(int startContext, Context endContext) {
        
        return contextAccess.getContext(startContext);
    }

    @Override
    public int writeContext(Context context) {
        return contextAccess.writeContext(context);
    }

    @Override
    public int writeContext(int otherContext) {
        return contextAccess.writeContext(otherContext);
    }

    @Override
	public final void onCompilationUnitStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		if (!mainStack.isEmpty()) {
			throw new IllegalStateException("Expected empty stack");
		}

		push(new StackCompilationUnit<>(logger));

		logExit(context);
	}

	@Override
	public final COMPILATION_UNIT onCompilationUnitEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackCompilationUnit<COMPILATION_CODE, IMPORT> stackCompilationUnit = pop();

		final COMPILATION_UNIT compilationUnit = parseTreeFactory.createCompilationUnit(
		        context,
				stackCompilationUnit.getImports(),
				stackCompilationUnit.getList(),
				fullContextProvider);

		logExit(context);

		return compilationUnit;
	}

	@Override
	public final void onImportStart(
	        int startContext,
	        long importKeyword, int importKeywordContext,
	        long staticKeyword, int staticKeywordContext) {
		
	    final Context context = getStartContext(startContext);
	    
	    logEnter(context);
	    
		Objects.requireNonNull(importKeyword);

		final String importKeywordString = stringSource.asString(importKeyword);
		final String staticKeywordString = staticKeyword != StringRef.STRING_NONE ? stringSource.asString(staticKeyword) : null;
		
		push(new StackImport<>(
		        logger,
		        importKeywordString, getOtherContext(importKeywordContext),
		        staticKeywordString,
		        staticKeywordContext != ContextRef.NONE
		            ? getOtherContext(staticKeywordContext)
                    : null));
		
		logExit(context);
	}

	@Override
	public final void onImportName(int leafContext, long identifier) {
		
	    final Context context = getLeafContext(leafContext);
	    
	    logEnter(context);
	    
		final StackImport<NAME> stackImport = get();
		
		stackImport.addIdentifier(parseTreeFactory.createName(context, stringSource.asString(identifier)));
		
		logExit(context);
	}
		
	@Override
	public final void onImportEnd(int startContext, Context endContext, boolean ondemand) {
	    
	    final Context context = getEndContext(startContext, endContext);
		
	    logEnter(context);
	    
		final StackImport<NAME> stackImport = pop();
		
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
	public final void onNamespaceStart(int startContext, long namespaceKeyword, int namespaceKeywordContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);
		
		push(new StackNamespace<>(
		        logger,
		        stringSource.asString(namespaceKeyword),
		        getOtherContext(namespaceKeywordContext)));

		logExit(context);
	}

	@Override
    public final void onNamespacePart(int leafContext, long part) {

	    final Context context = getLeafContext(leafContext);
	    
	    logEnter(context);
	    
	    final StackNamespace<COMPILATION_CODE> namespace = get();
	    
	    namespace.addPart(stringSource.asString(part), context);
	    
	    logExit(context);
    }

    @Override
	public final void onNameSpaceEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);

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
    public void onTypeDefinitionStart(int startContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);

        push(new StackTypeDefinition<>(getLogger()));
        
        logExit(context);
    }

    @Override
    public void onTypeDefinitionEnd(int startContext, Context endContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        final StackTypeDefinition<TYPE_DEFINITION, ANNOTATION, CLASS_MODIFIER_HOLDER> stackType = pop();
        
        for (TYPE_DEFINITION stackEntry : stackType.getList()) {
            mainStack.addElement(stackEntry);
        }

        logExit(context);
    }

    @Override
	public final void onClassStart(
	        int startContext,
	        long classKeyword, int classKeywordContext,
	        long name, int nameContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackNamedClass<>(
		        logger,
		        stringSource.asString(classKeyword), getOtherContext(classKeywordContext),
		        stringSource.asString(name), getOtherContext(nameContext)));

		logExit(context);
	}

	private void addClassModifier(Context context, ClassModifier modifier) {
	    
		logEnter(context);

		final ClassModifierSetter<CLASS_MODIFIER_HOLDER> stackClass = get();

		stackClass.addClassModifier(parseTreeFactory.createClassModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onVisibilityClassModifier(int leafContext, ClassVisibility visibility) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addClassModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onSubclassingModifier(int leafContext, Subclassing subclassing) {
	    
	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addClassModifier(context, subclassing);

		logExit(context);
	}

	@Override
	public final void onStaticClassModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addClassModifier(context, new ClassStatic());

		logExit(context);
	}

	@Override
	public final void onStrictfpClassModifier(int leafContext) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addClassModifier(context, new ClassStrictfp());

		logExit(context);
	}

	@Override
    public void onGenericTypeArgumentsStart(int startContext) {

	    final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        push(new StackGenericTypeArgumentList<GENERIC_TYPE_ARGUMENT>(getLogger()));

        logExit(context);
    }


    @Override
    public void onGenericReferenceTypeArgumentStart(int startContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        push(new StackReferenceTypeArgument<>(getLogger()));

        logExit(context);
    }

    @Override
    public void onGenericReferenceTypeArgumentEnd(int startContext, Context endContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        final StackReferenceTypeArgument<TYPE_REFERENCE> stackReferenceTypeArgument = pop();
        
        final StackGenericTypeArgumentList<GENERIC_TYPE_ARGUMENT> typeArgumentList = get();
        
        final REFERENCE_GENERIC_TYPE_ARGUMENT typeArgument
            = parseTreeFactory.createReferenceTypeArgument(context, stackReferenceTypeArgument.getTypeReference());
        
        typeArgumentList.addTypeArgument(typeArgument);
        
        logExit(context);
    }

    @Override
    public void onGenericWildcardTypeArgumentStart(int startContext) {
        
        final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        push(new StackWildcardTypeArgument<TYPE_BOUND>(getLogger(), context));

        logExit(context);
    }

    @Override
    public void onGenericWildcardTypeArgumentEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        final StackWildcardTypeArgument<TYPE_BOUND> stackWildcardTypeArgument = pop();
        
        final StackGenericTypeArgumentList<GENERIC_TYPE_ARGUMENT> list = get();
        
        final WILDCARD_GENERIC_TYPE_ARGUMENT wildcardType = parseTreeFactory.createWildcardTypeArgument(
                stackWildcardTypeArgument.getContext(),
                stackWildcardTypeArgument.getList());
        
        list.addTypeArgument(wildcardType);

        logExit(context);
    }

    @Override
    public void onTypeBoundStart(int startContext, TypeBoundType type) {
        
        final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        push(new StackTypeBound<>(getLogger(), type));

        logExit(context);
    }

    @Override
    public void onTypeBoundEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);
        
        final StackTypeBound<NAME, TYPE_REFERENCE> stackTypeBound = pop();

        final TYPE_BOUND typeBound = parseTreeFactory.createTypeBound(
                context,
                stackTypeBound.getType(),
                stackTypeBound.getTypeReference());
        
        final TypeBoundSetter<TYPE_BOUND> typeArgument = get();
        
        typeArgument.addTypeBound(typeBound);
        
        logExit(context);
    }

    @Override
    public void onGenericTypeArgumentsEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        final StackGenericTypeArgumentList<GENERIC_TYPE_ARGUMENT> stackGenericTypeArgumentList = pop();
        
        final StackTypeReference<GENERIC_TYPE_ARGUMENT> stackTypeReference = get();
                
        stackTypeReference.setTypeParameters(stackGenericTypeArgumentList.getList());
        
        logExit(context);
    }

    @Override
	public final void onClassExtendsStart(int startContext, long extendsKeyword, int extendsKeywordContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		final StackNamedClass<
		        COMPLEX_MEMBER_DEFINITION,
		        CLASS_FIELD_MEMBER,
		        CONSTRUCTOR_MEMBER,
		        CLASS_METHOD_MEMBER,
		        CLASS_MODIFIER_HOLDER,
		        GENERIC_TYPE_ARGUMENT,
		        TYPE_REFERENCE> stackNamedClass = get();

		stackNamedClass.setExtendsKeyword(
		        stringSource.asString(extendsKeyword),
		        getOtherContext(extendsKeywordContext));

		push(new StackTypeReferenceSetter<>(getLogger()));

		logExit(context);
	}

    @Override
    public void onClassExtendsEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        final StackTypeReferenceSetter<TYPE_REFERENCE> stackTypeReferenceSetter = pop();

        final StackNamedClass<
                COMPLEX_MEMBER_DEFINITION,
                CLASS_FIELD_MEMBER,
                CONSTRUCTOR_MEMBER,
                CLASS_METHOD_MEMBER,
                CLASS_MODIFIER_HOLDER,
                GENERIC_TYPE_ARGUMENT,
                TYPE_REFERENCE> stackNamedClass = get();
                
        stackNamedClass.addExtendedClass(stackTypeReferenceSetter.getTypeReference());
        
        logExit(context);
    }

    @Override
	public final void onImplementsStart(int startContext, long implementsKeyword, int implementsKeywordContext) {

        final Context context = getStartContext(startContext);
        
		logEnter(context);

		push(new StackImplements(getLogger(), implementsKeyword, getOtherContext(implementsKeywordContext)));
		
		logExit(context);
	}
    
    @Override
    public void onImplementsTypeStart(int startContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);

        final StackScopedName stackScopedName = new StackScopedName(getLogger());

        push(stackScopedName);

        logExit(context);
    }

    @Override
    public void onImplementsNamePart(int startContext, long identifier) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);

        final StackScopedName stackScopedName = get();
    
        stackScopedName.addPart(stringSource.asString(identifier), context);
        
        logExit(context);
    }
    
    @Override
    public void onImplementsTypeEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        final StackScopedName stackScopedName = pop();

        final StackImplements stackImplements = get();
        
        final ContextScopedName contextScopedName = new ContextScopedName(context, stackScopedName.getScopedName());
        
        stackImplements.add(contextScopedName);
        
        logExit(context);
    }

    @Override
    public void onImplementsEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        final StackImplements stackImplements = pop();

        final InterfaceSetter<TYPE_REFERENCE> stackNamedClass = get();
        
        for (ContextScopedName contextScopedName : stackImplements.getList()) {
            
            final TYPE_REFERENCE typeReference = parseTreeFactory.createUnresolvedTypeReference(
                    contextScopedName.getContext(),
                    contextScopedName.getScopedName(),
                    null,
                    ReferenceType.NAME);
    
            stackNamedClass.addImplementedInterface(
                    stringSource.asString(stackImplements.getImplementsKeyword()),
                    stackImplements.getImplementsKeywordContext(),
                    typeReference);
        }
        
        logExit(context);
    }

    @Override
	public final void onClassEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
		logEnter(context);

		final StackNamedClass<
		    COMPLEX_MEMBER_DEFINITION,
		    COMPLEX_MEMBER_DEFINITION,
		    COMPLEX_MEMBER_DEFINITION,
		    COMPLEX_MEMBER_DEFINITION,
		    CLASS_MODIFIER_HOLDER,
		    NAMED_GENERIC_TYPE_PARAMETER,
		    TYPE_REFERENCE> entry = pop();

		final List<COMPLEX_MEMBER_DEFINITION> classCode = entry.getList();

		// Must get modifiers from type
		final StackTypeDefinition<TYPE_DEFINITION, ANNOTATION, CLASS_MODIFIER_HOLDER> stackType = get();

		final CLASS_DEFINITION classDefinition = parseTreeFactory.createClassDefinition(
		        context,
		        stackType.getAnnotations(),
		        stackType.getModifiers(),
				entry.getClassKeyword() != null
						? parseTreeFactory.createKeyword(entry.getClassKeywordContext(), entry.getClassKeyword())
						: null,

				entry.getName(), entry.getNameContext(),

				entry.getGenericTypes(),
				entry.getExtendsKeyword() != null
						? parseTreeFactory.createKeyword(entry.getExtendsKeywordContext(), entry.getExtendsKeyword())
						: null,
				entry.getExtendedClasses(), entry.getImplementedInterfaces(), classCode);

		mainStack.addElement(classDefinition);

		logExit(context);
	}

	@Override
	public final void onAnonymousClassStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackAnonymousClass<>(logger));

		logEnter(context);
	}

	@Override
	public final void onAnonymousClassEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackAnonymousClass<COMPLEX_MEMBER_DEFINITION, CLASS_FIELD_MEMBER, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> entry = pop();

		final List<COMPLEX_MEMBER_DEFINITION> classCode = entry.getList();

		final CLASS_DEFINITION classDefinition = parseTreeFactory.createClassDefinition(context, null, null, null, null, null, null,
				null, null, null, classCode);

		mainStack.addElement(classDefinition);

		logExit(context);
	}

	@Override
	public final void onStaticInitializerStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackStaticInitializer<>(logger));

		logExit(context);
	}

	@Override
	public final void onStaticInitializerEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackStaticInitializer<STATEMENT> stackStaticInitializer = pop();

		final STATIC_INITIALIZER initializer = parseTreeFactory.createStaticInitializer(context,
				stackStaticInitializer.getList());

		final StackClass<COMPLEX_MEMBER_DEFINITION, CLASS_FIELD_MEMBER, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> stackClass = get();

		stackClass.add(initializer);

		logExit(context);
	}

	@Override
	public final void onConstructorStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackConstructor<>(logger));

		logExit(context);
	}

	private void addConstructorModifier(Context context, ConstructorModifier modifier) {
	    
		logEnter(context);

		final StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, CONSTRUCTOR_MODIFIER_HOLDER> stackConstructor = get();

		stackConstructor.addModifier(parseTreeFactory.createConstructorModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onConstructorVisibilityModifier(int leafContext, ConstructorVisibility visibility) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addConstructorModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onConstructorName(int leafContext, long constructorName) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, CONSTRUCTOR_MODIFIER_HOLDER> constructor = get();

		constructor.setName(stringSource.asString(constructorName), context);

		logExit(context);
	}

	@Override
	public final void onConstructorInvocationStart(int startContext, ConstructorInvocation type) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackConstructorInvocation<>(logger, type));

		logExit(context);
	}

	@Override
	public final void onConstructorInvocationEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackConstructorInvocation<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackConstructorInvocation = pop();

		final CONSTRUCTOR_INVOCATION_STATEMENT statement = parseTreeFactory.createConstructorInvocationStatement(
				context, stackConstructorInvocation.getType(), makeExpression(context, stackConstructorInvocation),
				stackConstructorInvocation.getParameters());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public final void onConstructorEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, CONSTRUCTOR_MODIFIER_HOLDER> stackConstructor = pop();

		final CONSTRUCTOR_MEMBER constructorMember = parseTreeFactory.createConstructorMember(context,
		        stackConstructor.getAnnotations(), stackConstructor.getModifiers(),
		        stackConstructor.getName(), stackConstructor.getNameContext(),
				stackConstructor.getParameters(), stackConstructor.getThrownExceptions(), stackConstructor.getList());

		final ConstructorMemberSetter<CONSTRUCTOR_MEMBER> constructorMemberSetter = get();

		constructorMemberSetter.addConstructorMember(constructorMember);

		logExit(context);
	}

	@Override
	public final void onClassMethodStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, CLASS_METHOD_MODIFIER_HOLDER, NAMED_GENERIC_TYPE_PARAMETER> method
			= new StackClassMethod<>(logger);

		push(method);

		logExit(context);
	}

	@Override
	public final void onMethodReturnTypeStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackReturnType<>(logger));

		logExit(context);
	}

	@Override
	public final void onMethodReturnTypeEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext); 
	    
		logEnter(context);

		final StackReturnType<TYPE_REFERENCE> stackReturnType = pop();

		final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> stackMethod = get();

		stackMethod.setReturnType(stackReturnType.getTypeReference());

		logExit(context);
	}

	@Override
	public final void onMethodName(int leafContext, long methodName) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> method = get();

		method.setName(stringSource.asString(methodName), context);

		logExit(context);
	}

	@Override
	public final void onMethodSignatureParametersStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);
	    
	    logEnter(context);

	    logExit(context);
	}

	@Override
	public final void onMethodSignatureParameterStart(int startContext, boolean varArgs) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackParameterSignature<>(logger, varArgs));

		logExit(context);
	}

	@Override
    public void onMethodSignatureParameterVarargs(int leafContext) {

	    final Context context = getLeafContext(leafContext);
        
        logEnter(context);
	    
	    final StackParameterSignature<ANNOTATION, VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE> stackParameterSignature = get();

	    stackParameterSignature.setVarargs();
	    
        logExit(context);
    }

    @Override
	public final void onMethodSignatureParameterEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackParameterSignature<ANNOTATION, PARAMETER_MODIFIER_HOLDER, TYPE_REFERENCE> stackParameterSignature = pop();

		final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> stackCallable = get();

		final PARAMETER parameter = parseTreeFactory.createParameter(context,
		        stackParameterSignature.getAnnotations(), stackParameterSignature.getModifiers(),
				stackParameterSignature.getTypeReference(), stackParameterSignature.getName(),
				stackParameterSignature.getNameContext(), stackParameterSignature.isVarargs());

		stackCallable.addParameter(parameter);

		logExit(context);
	}

	@Override
	public final void onMethodSignatureParametersEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
	    logEnter(context);
	    
	    logExit(context);
	}

	@Override
    public void onThrowsStart(int startContext) {

	    final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        push(new StackTypeReferenceList<>(getLogger()));
        
        logExit(context);
    }

    @Override
    public void onThrowsEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);
        
        final StackTypeReferenceList<TYPE_REFERENCE> stackTypeReferenceList = pop();
        
        final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> callableStackEntry = get();
        
        for (TYPE_REFERENCE thrownException : stackTypeReferenceList.getList()) {
            callableStackEntry.addThrownException(thrownException);
        }
        
        logExit(context);
    }

    private final void addClassMethodModifier(Context context, ClassMethodModifier modifier) {

		logEnter(context);

		final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, CLASS_METHOD_MODIFIER_HOLDER, NAMED_GENERIC_TYPE_PARAMETER> stackMethod = get();

		stackMethod.addModifier(parseTreeFactory.createClassMethodModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onVisibilityClassMethodModifier(int leafContext, ClassMethodVisibility visibility) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addClassMethodModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onOverrideClassMethodModifier(int leafContext, ClassMethodOverride methodOverride) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addClassMethodModifier(context, methodOverride);

		logExit(context);
	}

	@Override
	public final void onStaticClassMethodModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addClassMethodModifier(context, new ClassMethodStatic());

		logExit(context);
	}

	@Override
	public final void onStrictfpClassMethodModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addClassMethodModifier(context, new ClassMethodStrictfp());

		logExit(context);
	}

	@Override
	public final void onSynchronizedClassMethodModifier(int leafContext) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addClassMethodModifier(context, new ClassMethodSynchronized());

		logExit(context);
	}

	@Override
	public final void onNativeClassMethodModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addClassMethodModifier(context, new ClassMethodNative());

		logExit(context);
	}

	@Override
	public final void onClassMethodEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, CLASS_METHOD_MODIFIER_HOLDER, NAMED_GENERIC_TYPE_PARAMETER> method = pop();

		final ClassMethodMemberSetter<CLASS_METHOD_MEMBER> methodMemberSetter = get();
		
		final CLASS_METHOD_MEMBER methodMember = parseTreeFactory.createClassMethodMember(
				context,
				method.getAnnotations(),
				method.getModifiers(),
				method.getGenericTypes(),
				method.getReturnType(),
				method.getName(),
				method.getNameContext(),
				method.getParameters(),
				method.getThrownExceptions(),
				method.getList());

		methodMemberSetter.addMethod(methodMember);

		logExit(context);
	}

	@Override
	public final void onFieldDeclarationStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackFieldDeclarationList<>(logger));

		logExit(context);
	}

	private void addFieldModifier(Context context, FieldModifier modifier) {
		final StackFieldDeclarationList<TYPE_REFERENCE, EXPRESSION, ANNOTATION, FIELD_MODIFIER_HOLDER> stackFieldDeclarationList = get();

		stackFieldDeclarationList.addFieldModifier(parseTreeFactory.createFieldModifierHolder(context, modifier));
	}

	@Override
	public final void onVisibilityFieldModifier(int leafContext, FieldVisibility visibility) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addFieldModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onStaticFieldModifier(int leafContext) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addFieldModifier(context, new FieldStatic());

		logExit(context);
	}

	@Override
	public final void onMutabilityFieldModifier(int leafContext, ASTMutability mutability) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addFieldModifier(context, mutability);

		logExit(context);
	}

	@Override
	public final void onTransientFieldModifier(int leafContext) {

	    final Context context = getLeafContext(leafContext);
	        
		logEnter(context);

		addFieldModifier(context, new FieldTransient());

		logExit(context);
	}

	@Override
	public final void onVolatileFieldModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addFieldModifier(context, new FieldVolatile());

		logExit(context);
	}

	@Override
	public final void onFieldDeclarationEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
		
	    logEnter(context);

		final StackFieldDeclarationList<TYPE_REFERENCE, EXPRESSION, ANNOTATION, FIELD_MODIFIER_HOLDER> stackFieldDeclarationList = pop();

		final ClassFieldMemberSetter<COMPLEX_MEMBER_DEFINITION> fieldSetter = get();

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
                stackFieldDeclarationList.getAnnotations(),
                stackFieldDeclarationList.getModifiers(),
                stackFieldDeclarationList.getTypeReference(),
                initializers
            );
            
        fieldSetter.addField(dataFieldMember);

		logExit(context);
	}

	@Override
	public final void onInterfaceStart(int startContext, long interfaceKeyword, int interfaceKeywordContext,
			long name, int nameContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackInterface<>(
		        logger,
		        stringSource.asString(interfaceKeyword),
		        getOtherContext(interfaceKeywordContext),
		        stringSource.asString(name),
		        getOtherContext(nameContext)));

		logExit(context);
	}

	private void addInterfaceModifier(Context context, InterfaceModifier modifier) {

		logEnter(context);

		final StackInterface<COMPLEX_MEMBER_DEFINITION, ANNOTATION, INTERFACE_MODIFIER_HOLDER, TYPE_REFERENCE, INTERFACE_METHOD_MEMBER> stackInterface = get();

		stackInterface.addModifier(parseTreeFactory.createInterfaceModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onVisibilityInterfaceModifier(int leafContext, InterfaceVisibility visibility) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addInterfaceModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onAbstractInterfaceModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addInterfaceModifier(context, new InterfaceAbstract());

		logExit(context);
	}

	@Override
	public final void onStaticInterfaceModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addInterfaceModifier(context, new InterfaceStatic());

		logExit(context);
	}

	@Override
	public final void onStrictfpInterfaceModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addInterfaceModifier(context, new InterfaceStrictfp());

		logExit(context);
	}

	@Override
	public final void onInterfaceExtends(int otherContext, ScopedName interfaceName) {

	    final Context context = getOtherContext(otherContext);
	    
		logEnter(context);

		final StackInterface<COMPLEX_MEMBER_DEFINITION, ANNOTATION, INTERFACE_MODIFIER_HOLDER, TYPE_REFERENCE, INTERFACE_METHOD_MEMBER> entry = get();

		final TYPE_REFERENCE typeReference = parseTreeFactory.createUnresolvedTypeReference(
		        context,
		        interfaceName,
		        null,
		        ReferenceType.NAME);
		
		entry.addExtendedInterface(typeReference);

		logExit(context);
	}

	@Override
	public final void onInterfaceEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackInterface<COMPLEX_MEMBER_DEFINITION, ANNOTATION, INTERFACE_MODIFIER_HOLDER, TYPE_REFERENCE, INTERFACE_METHOD_MEMBER> entry = pop();

		final List<COMPLEX_MEMBER_DEFINITION> interfaceCode = entry.getList();

		final INTERFACE_DEFINITION classDefinition = parseTreeFactory.createInterfaceDefinition(
				context,
				entry.getAnnotations(),
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
	public final void onEnumStart(int startContext, long enumKeyword, int enumKeywordContext, long name,
			int nameContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackEnum<>(
		        logger,
		        stringSource.asString(enumKeyword),
		        getOtherContext(enumKeywordContext),
		        stringSource.asString(name),
		        getOtherContext(nameContext)));

		logExit(context);
	}

	@Override
	public final void onEnumConstantStart(int startContext, long name) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackEnumConstant<>(logger, stringSource.asString(name)));

		logExit(context);
	}

	@Override
	public final void onEnumConstantEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackEnumConstant<COMPLEX_MEMBER_DEFINITION, EXPRESSION> stackEnumConstant = pop();

		final ENUM_CONSTANT_DEFINITION enumConstant = parseTreeFactory.createEnumConstantDefinition(
				context,
				stackEnumConstant.getName(),
				stackEnumConstant.getParameters(),
				stackEnumConstant.getList());

		final StackEnum<
		        COMPLEX_MEMBER_DEFINITION,
		        TYPE_REFERENCE,
		        ANNOTATION,
		        CLASS_MODIFIER_HOLDER,
		        CLASS_FIELD_MEMBER,
		        CONSTRUCTOR_MEMBER,
		        CLASS_METHOD_MEMBER,
		        ENUM_CONSTANT_DEFINITION> stackEnum = get();

		stackEnum.addConstant(enumConstant);

		logExit(context);
	}

	@Override
	public final void onEnumEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackEnum<
		        COMPLEX_MEMBER_DEFINITION,
		        TYPE_REFERENCE,
		        ANNOTATION,
		        CLASS_MODIFIER_HOLDER,
		        CLASS_FIELD_MEMBER,
		        CONSTRUCTOR_MEMBER,
		        CLASS_METHOD_MEMBER,
		        ENUM_CONSTANT_DEFINITION> stackEnum = pop();

		final ENUM_DEFINITION enumDefinition = parseTreeFactory.createEnumDefinition(
				context,
				stackEnum.getAnnotations(),
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
	public final void onInterfaceMethodStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		final StackInterfaceMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, INTERFACE_METHOD_MODIFIER_HOLDER> method = new StackInterfaceMethod<>(logger);

		push(method);

		logExit(context);
	}

	private void addInterfaceMethodModifier(Context context, InterfaceMethodModifier modifier) {

		logEnter(context);

		final StackInterfaceMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, INTERFACE_METHOD_MODIFIER_HOLDER> stackMethod = get();

		stackMethod.addModifier(parseTreeFactory.createInterfaceMethodModifierHolder(context, modifier));

		logExit(context);
	}

	@Override
	public final void onVisibilityInterfaceMethodModifier(int leafContext, InterfaceMethodVisibility visibility) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addInterfaceMethodModifier(context, visibility);

		logExit(context);
	}

	@Override
	public final void onAbstractInterfaceMethodModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStatic());

		logExit(context);
	}

	@Override
	public final void onDefaultInterfaceMethodModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodDefault());

		logExit(context);
	}

	@Override
	public final void onStaticInterfaceMethodModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStatic());

		logExit(context);
	}

	@Override
	public final void onStrictfpInterfaceMethodModifier(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStrictfp());

		logExit(context);
	}

	@Override
	public final void onInterfaceMethodEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackInterfaceMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, INTERFACE_METHOD_MODIFIER_HOLDER> method = pop();

		final InterfaceMethodMemberSetter<INTERFACE_METHOD_MEMBER> methodMemberSetter = get();
		

		final INTERFACE_METHOD_MEMBER methodMember = parseTreeFactory.createInterfaceMethodMember(
				context,
				method.getAnnotations(),
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
    public void onAssignmentStatementStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
	    logEnter(context);
	    
	    push(new StackAssignmentStatement<>(getLogger()));
	    
	    logExit(context);
    }

    @Override
    public void onAssignmentStatementEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);
        
        final StackAssignmentStatement<EXPRESSION, ASSIGNMENT_EXPRESSION> stackAssignmentStatement = pop();
        
        final StatementSetter<STATEMENT> statementSetter = get();
        
        final ASSIGNMENT_EXPRESSION assignmentExpression = stackAssignmentStatement.getExpression();
        
        statementSetter.addStatement(parseTreeFactory.createAssignmentStatement(context, assignmentExpression));
        
        logExit(context);
    }

    // Expressions
	@Override
	public final void onEnterAssignmentExpression(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackAssignmentExpression<>(logger));

		logExit(context);
	}

	@Override
	public final void onEnterAssignmentLHS(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackAssignmentLHS<>(logger));

		logExit(context);
	}

	@Override
	public final void onExitAssignmentLHS(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackAssignmentLHS<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION> assignmentLHS = pop();

		final StackAssignmentExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> assignmentExpression = get();

		assignmentExpression.setLHS(parseTreeFactory.makeVariableReference(context, assignmentLHS.getList()));

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onExitAssignmentExpression(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackAssignmentExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackAssignmentExpression = pop();

		stackAssignmentExpression.setRHS(makeExpression(context, stackExpressionList));

		final ExpressionSetter<EXPRESSION> expressionSetter = get();

		expressionSetter.addExpression(parseTreeFactory.createAssignmentExpression(context, stackAssignmentExpression.getLHS(),
				stackAssignmentExpression.getRHS()));

		logExit(context);
	}

	@Override
	public final void onNestedExpressionStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onNestedExpressionEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final NESTED_EXPRESSION nestedExpression = parseTreeFactory.createNestedExpression(context,
				makeExpression(context, stackExpressionList));

		final NestedExpressionSetter<NESTED_EXPRESSION> nestedExpressionSetter = get();

		nestedExpressionSetter.addNestedExpression(nestedExpression);

		logExit(context);
	}

	// Variables

	// Variable or class member
	@Override
	public final void onNameReference(int leafContext, long name) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final VariableReferenceSetter<VARIABLE_REFERENCE> variableReferenceSetter = get();

		variableReferenceSetter.setVariableReference(parseTreeFactory.createNameReference(context, stringSource.asString(name)));

		logExit(context);
	}

	// Resolved as variable
	@Override
	public final void onVariableReference(int leafContext, long name) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final VariableReferenceSetter<VARIABLE_REFERENCE> variableReferenceSetter = get();

		variableReferenceSetter.setVariableReference(parseTreeFactory.createNameReference(context, stringSource.asString(name)));

		logExit(context);
	}

	// Field access

	@Override
	public final void onPrimaryStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		// Start of any primary expression, like a literal or a linked list of
		// field accesses
		push(new StackPrimaryList<>(logger));

		logExit(context);
	}

	@Override
    public void onNamePrimary(int leafContext, long name) {

        final Context context = getLeafContext(leafContext);

        logEnter(context);

	    final NAME_PRIMARY namePrimary = parseTreeFactory.createNamePrimary(context, stringSource.asString(name));

	    final PrimarySetter<PRIMARY> primarySetter = get();
	    
	    primarySetter.addPrimary(namePrimary);
	    
        logExit(context);
    }

    @Override
	public final void onArrayAccessStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackArrayAccess<>(logger));

		logExit(context);
	}

	@Override
	public final void onArrayIndexStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onArrayIndexEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackArrayAccess<EXPRESSION, EXPRESSION, PRIMARY> stackArrayAccess = get();

		stackArrayAccess.addExpression(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onArrayAccessEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

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
	public final void onFieldAccess(int leafContext, FieldAccessType fieldAccessType, ScopedName typeName,
			ReferenceType referenceType, long fieldName, int fieldNameContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		final FIELD_ACCESS fieldAccess = parseTreeFactory.createFieldAccess(
				context,
				fieldAccessType,
				typeName != null ? parseTreeFactory.createUnresolvedTypeReference(context, typeName, null, referenceType) : null,
				stringSource.asString(fieldName));

		primarySetter.addPrimary(fieldAccess);

		logExit(context);
	}

	@Override
	public final void onCastExpressionStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackCastExpression<>(logger));

		logExit(context);
	}

	@Override
	public final void onCastExpressionEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
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
	public final void onThisPrimary(int leafContext) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final StackPrimaryList<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION> stackPrimaryList = get();

		@SuppressWarnings("unchecked")
		final StackNamedClass<
		        COMPLEX_MEMBER_DEFINITION,
		        CLASS_FIELD_MEMBER,
		        CONSTRUCTOR_MEMBER,
		        CLASS_METHOD_MEMBER,
		        CLASS_MODIFIER_HOLDER,
		        GENERIC_TYPE_ARGUMENT,
		        TYPE_REFERENCE> stackClass = mainStack.getFromTop(StackNamedClass.class);

		final TYPE_REFERENCE typeReference = parseTreeFactory.createUnresolvedTypeReference(
		        context,
				new ScopedName(null, stackClass.getName()),
				null,
				ReferenceType.NAME);

		stackPrimaryList.add(parseTreeFactory.createThisPrimary(context, typeReference));

		logExit(context);
	}

	@Override
	public final void onPrimaryEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackPrimaryList<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION> stackPrimary = pop();

		final PRIMARY primary = makePrimary(context, stackPrimary.getList());

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(primary);

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackConditionalExpression<>(logger));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart1Start(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart1End(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart1(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart2Start(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart2End(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart2(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart3Start(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionPart3End(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart3(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onConditionalExpressionEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
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
    public void onExpressionListStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
	    logEnter(context);
	    
	    push(new StackExpressionList<>(getLogger()));
	    
	    logExit(context);
    }

    @Override
    public void onExpressionListEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);
        
        final StackExpressionList<
            EXPRESSION,
            NESTED_EXPRESSION,
            PRIMARY,
            VARIABLE_REFERENCE> stackExpressionList = pop(); 
        
        final EXPRESSION_LIST expressionList = parseTreeFactory.createExpressionList(
                context,
                stackExpressionList.getOperators(),
                stackExpressionList.getList());
        
        final ExpressionSetter<EXPRESSION> expressionSetter = get();
        
        expressionSetter.addExpression(expressionList);
            
        logExit(context);
    }

    @Override
	public final void onIntegerLiteral(int leafContext, long value, Base base, boolean signed, int bits) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createIntegerLiteral(context, value, base, signed, bits));

		logExit(context);
	}

	@Override
	public final void onFloatingPointLiteral(int leafContext, BigDecimal value, Base base, int bits) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createFloatingPointLiteral(context, value, base, bits));

		logExit(context);
	}

	@Override
	public final void onBooleanLiteral(int leafContext, boolean value) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createBooleanLiteral(context, value));

		logExit(context);
	}

	@Override
	public final void onCharacterLiteral(int leafContext, char value) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		final PrimarySetter<PRIMARY> expressionSetter = get();

		expressionSetter.addPrimary(parseTreeFactory.createCharacterLiteral(context, value));

		logExit(context);
	}

	@Override
	public final void onStringLiteral(int leafContext, long value) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createStringLiteral(context, stringSource.asString(value)));

		logExit(context);
	}

	@Override
	public final void onNullLiteral(int leafContext) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createNullLiteral(context));

		logExit(context);
	}

	@Override
	public final void onClassInstanceCreationExpressionStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackClassInstanceCreationExpression<>(logger));

		logExit(context);
	}

	private void processClassInstanceCreationTypeAndConstructorName(Context context, ScopedName name) {
	    
		logEnter(context);

		final StackClassInstanceCreationExpression<TYPE_REFERENCE, CONSTRUCTOR_NAME, EXPRESSION, CLASS_METHOD_MEMBER> stackClassInstanceCreationExpression = get();

		final TYPE_REFERENCE typeReference = parseTreeFactory.createUnresolvedTypeReference(
                context,
                name,
                null,
                ReferenceType.NAME);
		
		stackClassInstanceCreationExpression.setType(typeReference);
		
		stackClassInstanceCreationExpression.setConstructorName(parseTreeFactory.createConstructorName(context, name.getName()));

		logExit(context);
	}

	@Override
    public final void onClassInstanceCreationTypeAndConstructorName(int otherContext, Names names) {

	    final Context context = getOtherContext(otherContext);

	    processClassInstanceCreationTypeAndConstructorName(context, makeScopedName(names));
	}
	
	@Override
    public final void onClassInstanceCreationTypeAndConstructorName(int leafContext, long name) {

	    final Context context = getLeafContext(leafContext);
        
	    processClassInstanceCreationTypeAndConstructorName(
	            context,
	            ScopedName.makeScopedName(stringSource.asString(name)));
    }

    @Override
	public final void onClassInstanceCreationExpressionEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
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
			int startContext,
			// MethodInvocationType type,
			long methodName,
			int methodNameContextRef) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);
		
		final CallStackEntry<EXPRESSION> methodInvocation;
		
		final String methodNameString = stringSource.asString(methodName);
		final Context methodNameContext = getOtherContext(methodNameContextRef);
		
	    methodInvocation = new StackUnresolvedMethodInvocation<>(
                logger,
                // type,
                // null,
                methodNameString,
                methodNameContext);

		push(methodInvocation);

		logExit(context);
	}

	@Override
	public final void onParametersStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackParameterList<>(logger));

		logExit(context);
	}

	@Override
	public final void onParameterStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackParameter<>(logger));

		logExit(context);
	}

	@Override
	public final void onParameterEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackParameter<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackParameter = pop();

		final StackParameterList<EXPRESSION> stackParameterList = get();

		stackParameterList.addExpression(makeExpression(context, stackParameter));

		logExit(context);
	}

	@Override
	public final void onParametersEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackParameterList<EXPRESSION> stackParameterList = pop();

		final ParametersSetter<EXPRESSION> parametersSetter = get();

		parametersSetter.setParameters(stackParameterList.getList());

		logExit(context);
	}

	@Override
	public final void onMethodInvocationEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

        final StackUnresolvedMethodInvocation<EXPRESSION, PRIMARY, TYPE_REFERENCE> stackMethodInvocation = pop(); 

        final METHOD_INVOCATION_EXPRESSION methodInvocation = parseTreeFactory.createUnresolvedMethodInvocationExpression(
                context,
                // stackMethodInvocation.getType(),
                // stackMethodInvocation.getClassType(),
                // stackMethodInvocation.getObject(),
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
	public final void onArrayCreationExpressionStart(int startContext, ScopedName typeName, ReferenceType referenceType, int numDims) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		final TYPE_REFERENCE typeReference = parseTreeFactory.createUnresolvedTypeReference(
		        context,
		        typeName,
		        null,
		        referenceType);
		
		push(new StackArrayCreationExpression<>(logger, typeReference, numDims));

		logExit(context);
	}

	@Override
	public final void onDimExpressionStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onDimExpressionEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackArrayCreationExpression<TYPE_REFERENCE, EXPRESSION> stackArrayCreationExpression = get();

		stackArrayCreationExpression.addDimExpression(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	@Override
	public final void onArrayCreationExpressionEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

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
	@Deprecated
	@Override
	public final void onClassExpression(int leafContext, long className, int numArrayDims) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final CLASS_EXPRESSION expression = parseTreeFactory.createClassExpression(context, stringSource.asString(className), numArrayDims);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(expression);

		logExit(context);
	}

	// Lambda expressions
	@Override
	public final void onLambdaExpressionStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackLambdaExpression<>(logger));

		logExit(context);
	}

	@Override
	public final void onSingleLambdaParameter(int leafContext, long varName) {

	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final StackLambdaExpression<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = get();

		stackLambdaExpression.setSingleParameter(stringSource.asString(varName), context);

		logExit(context);
	}

	@Override
	public final void onFormalLambdaParameterListStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackLambdaFormalParameters(logger));

		logExit(context);
	}

	@Override
	public final void onFormalLambdaParameterListEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		pop();

		logExit(context);
	}

	@Override
	public final void onInferredLambdaParameterList(int otherContext, List<String> varNames, int varNamesContext) {

	    final Context context = getOtherContext(otherContext);
	    
		logEnter(context);

		final StackLambdaExpression<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = get();

		stackLambdaExpression.setInferredParameterList(varNames, getOtherContext(varNamesContext));

		logExit(context);
	}

	@Override
	public final void onLambdaBodyStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		logExit(context);
	}

	@Override
	public final void onLambdaBodyEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		logExit(context);
	}

	@Override
	public final void onLambdaExpressionEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackLambdaExpression<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = pop();

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

		logExit(context);
	}

	// Statements

	@Override
	public final void onMutabilityVariableModifier(int leafContext, ASTMutability mutability) {
	    
	    final Context context = getLeafContext(leafContext);

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
	public void onVariableDeclarationStatementStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackVariableDeclarationList<>(logger));

		logExit(context);
	}

	@Override
	public void onVariableDeclarationStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION, ANNOTATION, VARIABLE_MODIFIER_HOLDER> variableDeclaration = pop();

		final StatementSetter<STATEMENT> statementSetter = get();

		final List<INITIALIZER_VARIABLE_DECLARATION_ELEMENT> elements = variableDeclaration.getList().stream()
				.map(e -> createInitializer(e))
				.collect(Collectors.toList());
		
		final VARIABLE_DECLARATION_STATEMENT statement = parseTreeFactory.createVariableDeclarationStatement(
				context,
				variableDeclaration.getAnnotations(),
				variableDeclaration.getModifiers(),
				variableDeclaration.getTypeReference(),
				elements);

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public void onVariableDeclaratorStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackVariableDeclaration<>(logger));

		logExit(context);
	}

	@Override
	public void onVariableDeclaratorEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackVariableDeclaration<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackDeclaration = pop();

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
    public void onLeafTypeReference(int leafContext, long name, ReferenceType referenceType) {

        final Context context = getLeafContext(leafContext);

        logEnter(context);

        final TypeReferenceSetter<TYPE_REFERENCE> typeReferenceSetter = get();
        
        final TYPE_REFERENCE typeReference;
        
        switch (referenceType) {
        case SCALAR:
            typeReference = parseTreeFactory.createScalarTypeReference(context, stringSource.asString(name));
            break;
            
        default:
            throw new UnsupportedOperationException();
        }

        typeReferenceSetter.setTypeReference(typeReference);

        logExit(context);
    }

    @Override
    public final void onNonScopedTypeReferenceStart(int startContext, long name, ReferenceType referenceType) {
        
        final Context context = getStartContext(startContext);

        logEnter(context);

        Objects.requireNonNull(name);

        push(new StackNonScopedTypeReference<TYPE_REFERENCE>(getLogger(), stringSource.asString(name), referenceType));
        
        logExit(context);
    }
	
	@Override
    public void onNonScopedTypeReferenceEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        final TYPE_REFERENCE typeReference;
        
        final StackNonScopedTypeReference<GENERIC_TYPE_ARGUMENT> stackNonScopedTypeReference = pop();

        final ScopedName scopedName = ScopedName.makeScopedName(stackNonScopedTypeReference.getName());

        typeReference = parseTreeFactory.createUnresolvedTypeReference(
                context,
                scopedName,
                stackNonScopedTypeReference.getGenericTypeParameters(),
                stackNonScopedTypeReference.getReferenceType());

        final TypeReferenceSetter<TYPE_REFERENCE> typeReferenceSetter = get();

        typeReferenceSetter.setTypeReference(typeReference);

        logExit(context);
    }

    @Override
	public final void onScopedTypeReferenceStart(int startContext, ReferenceType referenceType) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);
		
		push(new StackScopedTypeReference<>(getLogger(), referenceType));

		logExit(context);
	}

	@Override
    public void onScopedTypeReferenceNameStart(int startContext) {

	    final Context context = getStartContext(startContext);

        logEnter(context);

        push(new StackScopedName(getLogger()));

        logExit(context);
    }

    @Override
    public void onScopedTypeReferenceNamePart(int leafContext, long part) {
	    
	    final Context context = getLeafContext(leafContext);

	    logEnter(context);

	    final StackScopedName stackScopedName = get();
	            
        stackScopedName.addPart(stringSource.asString(part), context);
	    
	    logExit(context);
    }

    @Override
    public void onScopedTypeReferenceNameEnd(int startContext, Context endContext) {

        final Context context = getStartContext(startContext);

        logEnter(context);

        final StackScopedName stackScopedName = pop();
        
        final StackScopedTypeReference<TYPE_REFERENCE> stackScopedTypeReference = get();
        
        stackScopedTypeReference.setScopedName(stackScopedName.getScopedName());

        logExit(context);
    }

    @Override
    public void onScopedTypeReferenceEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);

        logEnter(context);
        
        final StackScopedTypeReference<GENERIC_TYPE_ARGUMENT> stackScopedTypeReference = pop();
        
        final TypeReferenceSetter<TYPE_REFERENCE> typeReferenceSetter = get();

        final TYPE_REFERENCE typeReference = parseTreeFactory.createUnresolvedTypeReference(
                            context,
                            stackScopedTypeReference.getScopedName(),
                            stackScopedTypeReference.getGenericTypeParameters(),
                            stackScopedTypeReference.getReferenceType());
        
        typeReferenceSetter.setTypeReference(typeReference);
        
        logExit(context);
    }

    @Override
    public void onGenericTypeParametersStart(int startContext) {

        final Context context = getStartContext(startContext);

        logEnter(context);
        
        push(new StackGenericTypeParameters<>(getLogger()));

        logExit(context);
    }

    @Override
    public void onNamedGenericTypeParameterStart(int startContext, long nameRef, int nameContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        final NAME name = parseTreeFactory.createName(context, stringSource.asString(nameRef));
        
        push(new StackGenericParameter<NAME, TYPE_BOUND>(getLogger(), name));

        logExit(context);
    }

    @Override
    public void onNamedGenericTypeParameterEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);
        
        final StackGenericParameter<NAME, TYPE_BOUND> stackNamedParameter = pop();
        
        final StackGenericTypeParameters<NAMED_GENERIC_TYPE_PARAMETER> list = get();
        
        final NAMED_GENERIC_TYPE_PARAMETER namedType = parseTreeFactory.createNamedTypeParameter(
                context,
                stackNamedParameter.getName(),
                stackNamedParameter.getList());
        
        list.add(namedType);

        logExit(context);
    }

    @Override
    public void onGenericTypeParametersEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        final StackGenericTypeParameters<NAMED_GENERIC_TYPE_PARAMETER> stackGenericTypeParameters = pop();
        
        final NamedGenericParametersSetter<NAMED_GENERIC_TYPE_PARAMETER> entry = get();

        entry.setGenericTypes(stackGenericTypeParameters.getList());

        logExit(context);
    }

    @Override
	public final void onExpressionStatementStart(int startContext) {

        final Context context = getStartContext(startContext);
        
		logEnter(context);

		push(new StackExpressionStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onExpressionStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackExpressionStatement<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> statement = pop();

		final EXPRESSION_STATEMENT expressionStatement = parseTreeFactory.createExpressionStatement(context, makeExpression(context, statement));

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(expressionStatement);

		logExit(context);
	}

	@Override
	public final void onForStatementStart(int startContext, long keyword, int keywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackForStatement<>(logger, stringSource.asString(keyword), getOtherContext(keywordContext)));

		logExit(context);
	}

	@Override
	public final void onForInitStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackForInit<>(logger));

		logExit(context);
	}

	@Override
	public final void onForInitEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackForInit<TYPE_REFERENCE, EXPRESSION> stackForInit = pop();

		final FOR_INIT forInit;

		if (!stackForInit.getList().isEmpty()) {

			final VARIABLE_DECLARATION_STATEMENT statement
			    = parseTreeFactory.createVariableDeclarationStatement(
			            context,
			            null,
			            Collections.emptyList(),
			            stackForInit.getTypeReference(),
			            stackForInit.getList().stream()
			                .map(this::createInitializer)
			                .collect(Collectors.toList()));
			
			forInit = parseTreeFactory.createForInit(context, statement);
			
		} else if (!stackForInit.getExpressions().isEmpty()) {
			forInit = parseTreeFactory.createForInit(context, stackForInit.getExpressions());
		} else {
			forInit = null;
		}

		if (forInit != null) {
			final StackForStatement<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT, FOR_INIT> forStatement = get();

			forStatement.setForInit(forInit);
		}

		logExit(context);
	}

	@Override
    public void onForExpressionStart(int startContext) {
        final Context context = getStartContext(startContext);

        logEnter(context);

        logExit(context);
    }

    @Override
    public void onForExpressionEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        logExit(context);
    }

    @Override
	public final void onForUpdateStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackForUpdate<>(logger));

		logExit(context);
	}

	@Override
	public final void onForUpdateEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackForUpdate<EXPRESSION> stackForUpdate = pop();

		if (!stackForUpdate.getList().isEmpty()) {
			final StackForStatement<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT, FOR_INIT> stackForStatement = get();

			stackForStatement.setForUpdate(stackForUpdate.getList());
		}

		logExit(context);
	}

	@Override
	public final void onForStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackForStatement<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT, FOR_INIT> stackForStatement = pop();

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
	public final void onIteratorForStatementStart(int startContext, long forKeyword, int forKeywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackIteratorForStatement<>(logger));

		logExit(context);
	}

	@Override
    public void onIteratorForTestStart(int startContext) {

	    final Context context = getStartContext(startContext);
	    
	    logEnter(context);
	    
	    logExit(context);
    }

    @Override
	public final void onIteratorForTestEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		logExit(context);
	}

	@Override
	public final void onIteratorForStatementEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackIteratorForStatement<ANNOTATION, VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, VARIABLE_REFERENCE, PRIMARY, STATEMENT> stackIteratorForStatement = pop();

		final ITERATOR_FOR_STATEMENT statement = parseTreeFactory.createIteratorForStatement(
				context,
				stackIteratorForStatement.getAnnotations(),
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
	public final void onWhileStatementStart(int startContext, long whileKeyword, int whileKeywordContext) {

	    final Context context = getStartContext(startContext);
		
	    logEnter(context);

		push(new StackWhileStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onWhileStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackWhileStatement<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackWhileStatement = pop();

		final WHILE_STATEMENT whileStatement = parseTreeFactory.createWhileStatement(
				context,
				makeExpression(context, stackWhileStatement),
				stackWhileStatement.getStatements());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(whileStatement);

		logExit(context);
	}

	@Override
	public final void onDoWhileStatementStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackDoWhileStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onDoWhileStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackDoWhileStatement<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackDoWhileStatement = pop();

		final DO_WHILE_STATEMENT doWhileStatement = parseTreeFactory.createDoWhileStatement(
				context,
				makeExpression(context, stackDoWhileStatement),
				stackDoWhileStatement.getStatements());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(doWhileStatement);

		logExit(context);
	}

	@Override
	public final void onTryWithResourcesStatementStart(int startContext, long tryKeyword, int tryKeywordContext) {

	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		push(new StackTryWithResourcesStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onTryWithResourcesSpecificationStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackResourceList<>(logger));

		logExit(context);
	}

	@Override
	public final void onResourceStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackResource<>(logger));

		logExit(context);
	}

	@Override
	public final void onVariableName(int leafContext, long name, int numDims) {
	    
	    final Context context = getLeafContext(leafContext);

		logEnter(context);

		final VariableNameSetter variableNameSetter = get();

		variableNameSetter.init(stringSource.asString(name), context, numDims);

		logExit(context);
	}

	@Override
	public final void onResourceEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackResource<ANNOTATION, VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, PRIMARY> stackResource = pop();

		final RESOURCE resource = parseTreeFactory.createResource(
				context,
				stackResource.getAnnotations(),
				stackResource.getModifiers(),
				stackResource.getTypeReference(),
				stackResource.getName(),
				stackResource.getNameContext(),
				stackResource.getNumDims(),
				stackResource.getInitializer());

		final StackResourceList<RESOURCE> stackResourceList = get();

		stackResourceList.add(resource);

		logExit(context);
	}

	@Override
	public final void onTryWithResourcesSpecificationEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackResourceList<RESOURCE> stackResourceList = pop();

		final StackTryWithResourcesStatement<BLOCK, CATCH_BLOCK, RESOURCE> stackTryWithResourcesStatement = get();

		stackTryWithResourcesStatement.setResources(stackResourceList.getList());

		// Must push try-block to collect statements
		push(new StackTryBlock<>(logger));

		logExit(context);
	}

    @Override
    public final void onTryWithResourcesStatementEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        final StackTryWithResourcesStatement<STATEMENT, CATCH_BLOCK, RESOURCE> stackTryWithResourcesStatement = pop();

        final TRY_WITH_RESOURCES statement = parseTreeFactory.createTryWithResourcesStatement(
                context,
                stackTryWithResourcesStatement.getResources(),
                stackTryWithResourcesStatement.getTryBlock().getList(),
                stackTryWithResourcesStatement.getCatchBlocks(),
                stackTryWithResourcesStatement.getFinallyBlock() != null
                    ? stackTryWithResourcesStatement.getFinallyBlock().getList()
                    : null);

        final StatementSetter<STATEMENT> statementSetter = get();

        statementSetter.addStatement(statement);

        logExit(context);
    }
	
	@Override
	public final void onTryStatementStart(int startContext, long tryKeyword, int tryKeywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackTryCatchFinallyStatement<>(logger));

		push(new StackTryBlock<>(logger));

		logExit(context);
	}

	@Override
	public final void onTryBlockEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackTryBlock<STATEMENT> tryBlock = pop();

		final BaseStackTryCatchFinally<STATEMENT, TYPE_REFERENCE> baseStackTryCatchFinally = get();

		baseStackTryCatchFinally.setTryBlock(tryBlock);

		logExit(context);
	}
	
	@Override
	public final void onCatchStart(int startContext, long catchKeyword, int catchKeywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackCatchBlock<>(logger));

		logExit(context);
	}

	@Override
	public final void onCatchEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

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
	public final void onFinallyStart(int startContext, long finallyKeyword, int finallyKeywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackFinallyBlock<>(logger));

		logExit(context);
	}

	@Override
	public final void onFinallyEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackFinallyBlock<STATEMENT> stackFinallyBlock = pop();

		final BaseStackTryCatchFinally<STATEMENT, TYPE_REFERENCE> baseStackTryCatchFinally = get();

		baseStackTryCatchFinally.setFinallyBlock(stackFinallyBlock);

		logExit(context);
	}

	@Override
	public final void onTryStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackTryCatchFinallyStatement<STATEMENT, CATCH_BLOCK> stackTryCatchFinallyStatement = pop();

		final TRY_CATCH_FINALLY statement = parseTreeFactory.createTryCatchFinallyStatement(
				context,
				stackTryCatchFinallyStatement.getTryBlock().getList(),
				stackTryCatchFinallyStatement.getCatchBlocks(),
				stackTryCatchFinallyStatement.getFinallyBlock() != null
				    ? stackTryCatchFinallyStatement.getFinallyBlock().getList()
		            : null);

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
	public final void onTryWithResourcesEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackTryWithResourcesStatement<STATEMENT, CATCH_BLOCK, RESOURCE> stackTryWithResourcesStatement = pop();

		final TRY_WITH_RESOURCES statement = parseTreeFactory.createTryWithResourcesStatement(
				context,
				stackTryWithResourcesStatement.getResources(),
				stackTryWithResourcesStatement.getTryBlock().getList(),
				stackTryWithResourcesStatement.getCatchBlocks(),
				stackTryWithResourcesStatement.getFinallyBlock() != null
				    ? stackTryWithResourcesStatement.getFinallyBlock().getList()
		            : null);

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}

	@Override
    public void onReturnStatementStart(int startContext, long returnKeyword, int returnKeywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	@Override
	public final void onReturnStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpression = pop();

		final EXPRESSION expression = makeExpressionOrNull(context, stackExpression);

		final RETURN_STATEMENT returnStatement = parseTreeFactory.createReturnStatement(context, expression);

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(returnStatement);

		logExit(context);
	}

	@Override
	public final void onThrowStatementStart(int startContext, long keyword, int keywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackThrowStatement<>(logger));

		logExit(context);
	}

	@Override
	public final void onThrowStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		final StackThrowStatement<EXPRESSION, VARIABLE_REFERENCE, PRIMARY> stackThrowStatement = pop();

		final THROW_STATEMENT throwStatement = parseTreeFactory.createThrowStatement(context, stackThrowStatement.getExpression());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(throwStatement);

		logExit(context);
	}

	@Override
	public final void onAnnotationStart(int startContext, Names names) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);

		push(new StackAnnotation<>(logger, makeScopedName(names)));

		logExit(context);
	}

	@Override
    public void onAnnotationElementStart(int startContext, long name, int nameContext) {

        final Context context = getStartContext(startContext);

        logEnter(context);
        
        final NAME annotationName = name != StringRef.STRING_NONE 
                ? parseTreeFactory.createName(getLeafContext(nameContext), stringSource.asString(name))
                : null;
        
        push(new StackAnnotationElement<>(getLogger(), annotationName));

        logExit(context);
    }

    @Override
    public void onAnnotationElementEnd(int startContext, Context endContext) {

        final Context context = getStartContext(startContext);

        logEnter(context);
        
        final StackAnnotationElement<
                NAME,
                EXPRESSION,
                PRIMARY,
                VARIABLE_REFERENCE,
                ANNOTATION,
                ANNOTATION_ELEMENT> stackAnnotationElement = pop();
        
        final ANNOTATION_ELEMENT annotationElement;
        
        if (stackAnnotationElement.getExpression() != null) {
            
            annotationElement = parseTreeFactory.createAnnotationElementFromExpression(
                    context,
                    stackAnnotationElement.getName(),
                    stackAnnotationElement.getExpression());
        }
        else if (stackAnnotationElement.getAnnotation() != null) {
            
            annotationElement = parseTreeFactory.createAnnotationElementFromAnnotation(
                    context,
                    stackAnnotationElement.getName(),
                    stackAnnotationElement.getAnnotation());
        }
        else if (stackAnnotationElement.getElements() != null) {
            
            annotationElement = parseTreeFactory.createAnnotationElementFromElements(
                    context,
                    stackAnnotationElement.getName(),
                    stackAnnotationElement.getElements());

        }
        else {
            throw new IllegalStateException();
        }
        
        final AnnotationElementSetter<ANNOTATION_ELEMENT> setter = get();
        
        setter.addAnnotationElement(annotationElement);

        logExit(context);
    }

    @Override
	public final void onAnnotationEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);

		final StackAnnotation<ANNOTATION_ELEMENT> stackAnnotation = pop();
		
		final ANNOTATION annotation = parseTreeFactory.createAnnotation(
		        getStartContext(startContext),
		        stackAnnotation.getScopedName(),
		        stackAnnotation.getList());
		
		final AnnotationSetter<ANNOTATION> setter = get();
		
		setter.addAnnotation(annotation);

		logExit(context);
	}
    
    private ScopedName makeScopedName(Names names) {
        
        final int len = names.count();
        
        final ScopedName scopedName;
        
        if (len == 0) {
            throw new IllegalArgumentException();
        }
        else if (len == 1) {
            scopedName = ScopedName.makeScopedName(stringSource.asString(names.getStringAt(0)));
        }
        else {
            
            final String [] parts = new String[len - 1];
            
            for (int i = 0; i < parts.length; ++ i) {
                
                parts[i] = stringSource.asString(names.getStringAt(i));
            }
            
            scopedName = ScopedName.makeScopedName(parts, stringSource.asString(names.getStringAt(len - 1)));
        }
        
        return scopedName;
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

	private final EXPRESSION makeExpressionOrNull(Context context,
			StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList) {
		return makeExpressionOrNull(context, stackExpressionList.getOperators(), stackExpressionList.getList());
	}

	private final EXPRESSION makeExpressionOrNull(Context context, List<Operator> operators,
			List<EXPRESSION> expressions) {
		return expressions.isEmpty() ? null : makeExpression(context, operators, expressions);
	}

	final EXPRESSION makeExpression(Context context,
			StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList) {
		return makeExpression(context, stackExpressionList.getOperators(), stackExpressionList.getList());
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
