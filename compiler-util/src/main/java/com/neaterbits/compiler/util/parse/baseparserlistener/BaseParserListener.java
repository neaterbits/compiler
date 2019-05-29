package com.neaterbits.compiler.util.parse.baseparserlistener;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.block.ConstructorInvocation;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.parse.CompileException;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.BaseStackTryCatchFinally;
import com.neaterbits.compiler.util.parse.stackstate.BaseStackVariableDeclaration;
import com.neaterbits.compiler.util.parse.stackstate.BaseStackVariableDeclarationList;
import com.neaterbits.compiler.util.parse.stackstate.CallableStackEntry;
import com.neaterbits.compiler.util.parse.stackstate.StackAnnotation;
import com.neaterbits.compiler.util.parse.stackstate.StackAnonymousClass;
import com.neaterbits.compiler.util.parse.stackstate.StackArrayAccess;
import com.neaterbits.compiler.util.parse.stackstate.StackArrayCreationExpression;
import com.neaterbits.compiler.util.parse.stackstate.StackAssignmentExpression;
import com.neaterbits.compiler.util.parse.stackstate.StackAssignmentLHS;
import com.neaterbits.compiler.util.parse.stackstate.StackCastExpression;
import com.neaterbits.compiler.util.parse.stackstate.StackCatchBlock;
import com.neaterbits.compiler.util.parse.stackstate.StackClass;
import com.neaterbits.compiler.util.parse.stackstate.StackClassInstanceCreationExpression;
import com.neaterbits.compiler.util.parse.stackstate.StackClassMethod;
import com.neaterbits.compiler.util.parse.stackstate.StackCompilationUnit;
import com.neaterbits.compiler.util.parse.stackstate.StackConditionalExpression;
import com.neaterbits.compiler.util.parse.stackstate.StackConstructor;
import com.neaterbits.compiler.util.parse.stackstate.StackConstructorInvocation;
import com.neaterbits.compiler.util.parse.stackstate.StackDoWhileStatement;
import com.neaterbits.compiler.util.parse.stackstate.StackEnum;
import com.neaterbits.compiler.util.parse.stackstate.StackEnumConstant;
import com.neaterbits.compiler.util.parse.stackstate.StackExpressionList;
import com.neaterbits.compiler.util.parse.stackstate.StackExpressionStatement;
import com.neaterbits.compiler.util.parse.stackstate.StackFieldDeclarationList;
import com.neaterbits.compiler.util.parse.stackstate.StackFinallyBlock;
import com.neaterbits.compiler.util.parse.stackstate.StackForInit;
import com.neaterbits.compiler.util.parse.stackstate.StackForStatement;
import com.neaterbits.compiler.util.parse.stackstate.StackForUpdate;
import com.neaterbits.compiler.util.parse.stackstate.StackImport;
import com.neaterbits.compiler.util.parse.stackstate.StackInitializerVariableDeclarationElement;
import com.neaterbits.compiler.util.parse.stackstate.StackInterface;
import com.neaterbits.compiler.util.parse.stackstate.StackInterfaceMethod;
import com.neaterbits.compiler.util.parse.stackstate.StackIteratorForStatement;
import com.neaterbits.compiler.util.parse.stackstate.StackLambdaExpression;
import com.neaterbits.compiler.util.parse.stackstate.StackLambdaFormalParameters;
import com.neaterbits.compiler.util.parse.stackstate.StackMethodInvocation;
import com.neaterbits.compiler.util.parse.stackstate.StackNamedClass;
import com.neaterbits.compiler.util.parse.stackstate.StackNamespace;
import com.neaterbits.compiler.util.parse.stackstate.StackParameter;
import com.neaterbits.compiler.util.parse.stackstate.StackParameterList;
import com.neaterbits.compiler.util.parse.stackstate.StackParameterSignature;
import com.neaterbits.compiler.util.parse.stackstate.StackPrimaryList;
import com.neaterbits.compiler.util.parse.stackstate.StackResource;
import com.neaterbits.compiler.util.parse.stackstate.StackResourceList;
import com.neaterbits.compiler.util.parse.stackstate.StackReturnType;
import com.neaterbits.compiler.util.parse.stackstate.StackStaticInitializer;
import com.neaterbits.compiler.util.parse.stackstate.StackThrowStatement;
import com.neaterbits.compiler.util.parse.stackstate.StackTryBlock;
import com.neaterbits.compiler.util.parse.stackstate.StackTryCatchFinallyStatement;
import com.neaterbits.compiler.util.parse.stackstate.StackTryWithResourcesStatement;
import com.neaterbits.compiler.util.parse.stackstate.StackVariableDeclaration;
import com.neaterbits.compiler.util.parse.stackstate.StackVariableDeclarationList;
import com.neaterbits.compiler.util.parse.stackstate.StackWhileStatement;
import com.neaterbits.compiler.util.parse.stackstate.VariableNameSetter;
import com.neaterbits.compiler.util.parse.stackstate.base.ListStack;
import com.neaterbits.compiler.util.parse.stackstate.base.StackEntry;
import com.neaterbits.compiler.util.parse.stackstate.base.VariablesMap;
import com.neaterbits.compiler.util.parse.stackstate.setters.ClassMethodMemberSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.ClassModifierSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.ConstructorMemberSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.ExpressionSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.InterfaceMethodMemberSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.NestedExpressionSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.ParametersSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.StatementSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.VariableModifierSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.VariableReferenceSetter;
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

import statement.ASTMutability;

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
		
		CONDITION_BLOCK,
		IF_ELSE_IF_ELSE_STATEMENT extends STATEMENT,
		
		SWITCH_CASE_LABEL,
		CONSTANT_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
		ENUM_CONSTANT,
		ENUM_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
		DEFAULT_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
		SWITCH_CASE_GROUP,
		SWITCH_CASE_STATEMENT extends STATEMENT,
		
		BREAK_STATEMENT extends STATEMENT> {

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
			
			CONDITION_BLOCK,
			IF_ELSE_IF_ELSE_STATEMENT,

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
	protected BaseParserListener(ParseLogger logger,
			@SuppressWarnings("rawtypes") ParseTreeFactory parseTreeFactory) {

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

	public final void onCompilationUnitStart(Context context) {

		logEnter(context);

		if (!mainStack.isEmpty()) {
			throw new IllegalStateException("Expected empty stack");
		}

		push(new StackCompilationUnit<>(logger));

		logExit(context);
	}

	public COMPILATION_UNIT onCompilationUnitEnd(Context context) {

		logEnter(context);

		final StackCompilationUnit<COMPILATION_CODE, IMPORT> stackCompilationUnit = pop();

		final COMPILATION_UNIT compilationUnit = parseTreeFactory.createCompilationUnit(context,
				stackCompilationUnit.getImports(), stackCompilationUnit.getList());

		logExit(context);

		return compilationUnit;
	}

	public final void onImportStart(Context context, String importKeyword, Context importKeywordContext, String staticKeyword, Context staticKeywordContext) {
		
		Objects.requireNonNull(importKeyword);

		push(new StackImport<>(logger, importKeyword, importKeywordContext, staticKeyword, staticKeywordContext));
		
	}

	public final void onImportIdentifier(Context context, String identifier) {
		
		final StackImport<IDENTIFIER> stackImport = get();
		
		stackImport.addIdentifier(parseTreeFactory.createIdentifier(context, identifier));
	}
		
	
	public final void onImportEnd(Context context, boolean ondemand) {
		
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
	}

	public final void onNamespaceStart(Context context, String namespaceKeyword, Context namespaceKeywordContext,
			String name, Context nameContext, String[] parts) {

		logEnter(context);

		push(new StackNamespace<>(logger, namespaceKeyword, namespaceKeywordContext, name, nameContext, parts));

		logExit(context);
	}

	public final void onNameSpaceEnd(Context context) {

		logEnter(context);

		final StackNamespace<COMPILATION_CODE> stackNamespace = pop();

		final List<COMPILATION_CODE> namespaceCode = stackNamespace.getList();

		final NAMESPACE nameSpace = parseTreeFactory.createNamespace(context,
				parseTreeFactory.createKeyword(stackNamespace.getNamespaceKeywordContext(),
						stackNamespace.getNamespaceKeyword()),
				stackNamespace.getParts(), stackNamespace.getNameContext(), namespaceCode);

		mainStack.addElement(nameSpace);

		logExit(context);
	}

	public final void onClassStart(Context context, String classKeyword, Context classKeywordContext, String name,
			Context nameContext) {

		logEnter(context);

		push(new StackNamedClass<>(logger, classKeyword, classKeywordContext, name, nameContext));

		logExit(context);
	}

	private void addClassModifier(Context context, ClassModifier modifier) {

		logEnter(context);

		final ClassModifierSetter<CLASS_MODIFIER_HOLDER> stackClass = get();

		stackClass.addClassModifier(parseTreeFactory.createClassModifierHolder(context, modifier));

		logExit(context);
	}

	public final void onVisibilityClassModifier(Context context, ClassVisibility visibility) {

		logEnter(context);

		addClassModifier(context, visibility);

		logExit(context);
	}

	public final void onSubclassingModifier(Context context, Subclassing subclassing) {
		logEnter(context);

		addClassModifier(context, subclassing);

		logExit(context);
	}

	public final void onStaticClassModifier(Context context) {

		logEnter(context);

		addClassModifier(context, new ClassStatic());

		logExit(context);
	}

	public final void onStrictfpClassModifier(Context context) {

		logEnter(context);

		addClassModifier(context, new ClassStrictfp());

		logExit(context);
	}

	public final void onClassExtends(Context context, String extendsKeyword, Context extendsKeywordContext,
			ScopedName className) {

		logEnter(context);

		final StackNamedClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER, CLASS_MODIFIER_HOLDER, TYPE_REFERENCE> stackNamedClass = get();

		stackNamedClass.setExtendsKeyword(extendsKeyword, extendsKeywordContext);

		final TYPE_REFERENCE typeReference = parseTreeFactory.createResolveLaterTypeReference(context, className,
				ReferenceType.NAME);

		stackNamedClass.addExtendedClass(typeReference);

		logExit(context);
	}

	public final void onClassImplements(Context context, ScopedName interfaceName) {

		logEnter(context);

		final StackNamedClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER, CLASS_MODIFIER_HOLDER, TYPE_REFERENCE> stackNamedClass = get();

		final TYPE_REFERENCE typeReference = parseTreeFactory.createResolveLaterTypeReference(context, interfaceName,
				ReferenceType.NAME);

		stackNamedClass.addImplementedInterface(typeReference);

		logExit(context);
	}

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

	public final void onAnonymousClassStart(Context context) {

		logEnter(context);

		push(new StackAnonymousClass<>(logger));

		logEnter(context);
	}

	public final void onAnonymousClassEnd(Context context) {

		logEnter(context);

		final StackAnonymousClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> entry = pop();

		final List<COMPLEX_MEMBER_DEFINITION> classCode = entry.getList();

		final CLASS_DEFINITION classDefinition = parseTreeFactory.createClassDefinition(context, null, null, null, null,
				null, null, null, classCode);

		mainStack.addElement(classDefinition);

		logExit(context);
	}

	public final void onStaticInitializerStart(Context context) {

		logEnter(context);

		pushVariableScope();

		push(new StackStaticInitializer<>(logger));

		logExit(context);
	}

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

	public final void onConstructorVisibilityModifier(Context context, ConstructorVisibility visibility) {

		logEnter(context);

		addConstructorModifier(context, visibility);

		logExit(context);
	}

	public final void onConstructorName(Context context, String constructorName) {

		logEnter(context);

		final StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, CONSTRUCTOR_MODIFIER_HOLDER> constructor = get();

		constructor.setName(constructorName, context);

		logExit(context);
	}

	public final void onConstructorInvocationStart(Context context, ConstructorInvocation type) {

		logEnter(context);

		push(new StackConstructorInvocation<>(logger, type));

		logExit(context);
	}

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

	public final void onClassMethodStart(Context context) {

		logEnter(context);

		final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, CLASS_METHOD_MODIFIER_HOLDER> method
			= new StackClassMethod<>(logger);

		push(method);

		pushVariableScope();

		logExit(context);
	}

	public final void onMethodReturnTypeStart(Context context) {

		logEnter(context);

		push(new StackReturnType<>(logger));

		logExit(context);
	}

	public final void onMethodReturnTypeEnd(Context context) {

		logEnter(context);

		final StackReturnType<TYPE_REFERENCE> stackReturnType = pop();

		final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> stackMethod = get();

		stackMethod.setReturnType(stackReturnType.getType());

		logExit(context);
	}

	public final void onMethodName(Context context, String methodName) {

		logEnter(context);

		final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> method = get();

		method.setName(methodName, context);

		logExit(context);
	}

	public final void onMethodSignatureParametersStart(Context context) {

	}

	public final void onMethodSignatureParameterStart(Context context, boolean varArgs) {

		logEnter(context);

		push(new StackParameterSignature<>(logger, varArgs));

		logExit(context);
	}

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

	public final void onMethodSignatureParametersEnd(Context context) {

	}

	private void addClassMethodModifier(Context context, ClassMethodModifier modifier) {

		logEnter(context);

		final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, CLASS_METHOD_MODIFIER_HOLDER> stackMethod = get();

		stackMethod.addModifier(parseTreeFactory.createClassMethodModifierHolder(context, modifier));

		logExit(context);
	}

	public final void onVisibilityClassMethodModifier(Context context, ClassMethodVisibility visibility) {

		logEnter(context);

		addClassMethodModifier(context, visibility);

		logExit(context);
	}

	public final void onOverrideClassMethodModifier(Context context, ClassMethodOverride methodOverride) {

		logEnter(context);

		addClassMethodModifier(context, methodOverride);

		logExit(context);
	}

	public final void onStaticClassMethodModifier(Context context) {

		logEnter(context);

		addClassMethodModifier(context, new ClassMethodStatic());

		logExit(context);
	}

	public final void onStrictfpClassMethodModifier(Context context) {
		logEnter(context);

		addClassMethodModifier(context, new ClassMethodStrictfp());

		logExit(context);
	}

	public final void onSynchronizedClassMethodModifier(Context context) {

		logEnter(context);

		addClassMethodModifier(context, new ClassMethodSynchronized());

		logExit(context);
	}

	public final void onNativeClassMethodModifier(Context context) {

		logEnter(context);

		addClassMethodModifier(context, new ClassMethodNative());

		logExit(context);
	}

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

	public final void onFieldDeclarationStart(Context context) {

		logEnter(context);

		push(new StackFieldDeclarationList<>(logger));

		logExit(context);
	}

	private void addFieldModifier(Context context, FieldModifier modifier) {
		final StackFieldDeclarationList<TYPE_REFERENCE, EXPRESSION, FIELD_MODIFIER_HOLDER> stackFieldDeclarationList = get();

		stackFieldDeclarationList.addFieldModifier(parseTreeFactory.createFieldModifierHolder(context, modifier));
	}

	public final void onVisibilityFieldModifier(Context context, FieldVisibility visibility) {

		logEnter(context);

		addFieldModifier(context, visibility);

		logExit(context);
	}

	public final void onStaticFieldModifier(Context context) {

		logEnter(context);

		addFieldModifier(context, new FieldStatic());

		logExit(context);
	}

	public final void onMutabilityFieldModifier(Context context, ASTMutability mutability) {

		logEnter(context);

		addFieldModifier(context, mutability);

		logExit(context);
	}

	public final void onTransientFieldModifier(Context context) {

		logEnter(context);

		addFieldModifier(context, new FieldTransient());

		logExit(context);
	}

	public final void onVolatileFieldModifier(Context context) {

		logEnter(context);

		addFieldModifier(context, new FieldVolatile());

		logExit(context);
	}

	public final void onFieldDeclarationEnd(Context context) {

		logEnter(context);

		final StackFieldDeclarationList<TYPE_REFERENCE, EXPRESSION, FIELD_MODIFIER_HOLDER> stackFieldDeclarationList = pop();

		final StackClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> stackClass = get();

		for (StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION> element : stackFieldDeclarationList.getList()) {

			final EXPRESSION initializer = element.getInitializer();
			final TYPE_REFERENCE typeReference = element.getTypeReference();

			/*
			if (initializer != null) {
				initializer.take();
			}

			typeReference.take();
			*/

			final CLASS_FIELD_MEMBER dataFieldMember = parseTreeFactory.createClassFieldMember(
				context,
				stackFieldDeclarationList.getModifiers(),
				typeReference,
				element.getVarName(),
				element.getVarNameContext(),
				initializer
			);
			
			stackClass.add(dataFieldMember);
		}

		logExit(context);
	}

	public final void onInterfaceStart(Context context, String interfaceKeyword, Context interfaceKeywordContext,
			String name, Context nameContext) {

		logEnter(context);

		push(new StackInterface<>(logger, interfaceKeyword, interfaceKeywordContext, name, nameContext));

		logExit(context);
	}

	private void addInterfaceModifier(Context context, InterfaceModifier modifier) {

		logEnter(context);

		final StackInterface<COMPLEX_MEMBER_DEFINITION, INTERFACE_MODIFIER_HOLDER, TYPE_REFERENCE, INTERFACE_METHOD_MEMBER> stackInterface = get();

		stackInterface.addModifier(parseTreeFactory.createInterfaceModifierHolder(context, modifier));

		logExit(context);
	}

	public final void onVisibilityInterfaceModifier(Context context, InterfaceVisibility visibility) {

		logEnter(context);

		addInterfaceModifier(context, visibility);

		logExit(context);
	}

	public final void onAbstractInterfaceModifier(Context context) {

		logEnter(context);

		addInterfaceModifier(context, new InterfaceAbstract());

		logExit(context);
	}

	public final void onStaticInterfaceModifier(Context context) {

		logEnter(context);

		addInterfaceModifier(context, new InterfaceStatic());

		logExit(context);
	}

	public final void onStrictfpInterfaceModifier(Context context) {

		logEnter(context);

		addInterfaceModifier(context, new InterfaceStrictfp());

		logExit(context);
	}

	public final void onInterfaceExtends(Context context, ScopedName interfaceName) {

		logEnter(context);

		final StackInterface<COMPLEX_MEMBER_DEFINITION, INTERFACE_MODIFIER_HOLDER, TYPE_REFERENCE, INTERFACE_METHOD_MEMBER> entry = get();

		entry.addExtendedInterface(parseTreeFactory.createResolveLaterTypeReference(context, interfaceName, ReferenceType.NAME));

		logExit(context);
	}

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

	public final void onEnumStart(Context context, String enumKeyword, Context enumKeywordContext, String name,
			Context nameContext) {

		logEnter(context);

		push(new StackEnum<>(logger, enumKeyword, enumKeywordContext, name, nameContext));

		logExit(context);
	}

	public final void onEnumImplements(Context context, ScopedName interfaceName) {

		logEnter(context);

		final StackEnum<COMPLEX_MEMBER_DEFINITION, TYPE_REFERENCE, CLASS_MODIFIER_HOLDER, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER, ENUM_CONSTANT_DEFINITION> stackEnum = get();

		stackEnum.addImplementedInterface(parseTreeFactory.createResolveLaterTypeReference(context, interfaceName, ReferenceType.NAME));

		logExit(context);
	}

	public final void onEnumConstantStart(Context context, String name) {

		logEnter(context);

		push(new StackEnumConstant<>(logger, name));

		logExit(context);
	}

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

	public final void onVisibilityInterfaceMethodModifier(Context context, InterfaceMethodVisibility visibility) {

		logEnter(context);

		addInterfaceMethodModifier(context, visibility);

		logExit(context);
	}

	public final void onAbstractInterfaceMethodModifier(Context context) {

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStatic());

		logExit(context);
	}

	public final void onDefaultInterfaceMethodModifier(Context context) {

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodDefault());

		logExit(context);
	}

	public final void onStaticInterfaceMethodModifier(Context context) {

		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStatic());

		logExit(context);
	}

	public final void onStrictfpInterfaceMethodModifier(Context context) {
		logEnter(context);

		addInterfaceMethodModifier(context, new InterfaceMethodStrictfp());

		logExit(context);
	}

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
	public final void onEnterAssignmentExpression(Context context) {

		logEnter(context);

		push(new StackAssignmentExpression<>(logger));

		logExit(context);
	}

	public final void onEnterAssignmentLHS(Context context) {

		logEnter(context);

		push(new StackAssignmentLHS<>(logger));

		logExit(context);
	}

	public final void onExitAssignmentLHS(Context context) {

		logEnter(context);

		final StackAssignmentLHS<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION> assignmentLHS = pop();

		final StackAssignmentExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> assignmentExpression = get();

		assignmentExpression.setLHS(parseTreeFactory.makeVariableReference(context, assignmentLHS.getList()));

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

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

	public final void onNestedExpressionStart(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

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
	public final void onNameReference(Context context, String name) {

		logEnter(context);

		final VariableReferenceSetter<VARIABLE_REFERENCE> variableReferenceSetter = get();

		variableReferenceSetter.setVariableReference(parseTreeFactory.createNameReference(context, name));

		logExit(context);
	}

	// Resolved as variable
	public final void onVariableReference(Context context, String name) {

		logEnter(context);

		final VariableReferenceSetter<VARIABLE_REFERENCE> variableReferenceSetter = get();

		final VARIABLE_DECLARATION declaration = findVariableDeclaration(name);

		if (declaration == null) {
			throw new CompileException(context, "No variable declared for name " + name);
		}

		final SIMPLE_VARIABLE_REFERENCE variableReference = parseTreeFactory.createSimpleVariableReference(context, declaration);

		variableReferenceSetter.setVariableReference(variableReference);

		logExit(context);
	}

	// Field access

	public final void onPrimaryStart(Context context) {

		logEnter(context);

		// Start of any primary expression, like a literal or a linked list of
		// field accesses
		push(new StackPrimaryList<>(logger));

		logExit(context);
	}

	public final void onArrayAccessStart(Context context) {

		logEnter(context);

		push(new StackArrayAccess<>(logger));

		logExit(context);
	}

	public final void onArrayIndexStart(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	public final void onArrayIndexEnd(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackArrayAccess<EXPRESSION, EXPRESSION, PRIMARY> stackArrayAccess = get();

		stackArrayAccess.addExpression(makeExpression(context, stackExpressionList));

		logExit(context);
	}

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

	public final void onFieldAccess(Context context, FieldAccessType fieldAccessType, ScopedName typeName,
			ReferenceType referenceType, String fieldName, Context fieldNameContext) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		final FIELD_ACCESS fieldAccess = parseTreeFactory.createFieldAccess(
				context,
				fieldAccessType,
				typeName != null ? parseTreeFactory.createResolveLaterTypeReference(context, typeName, referenceType) : null,
				fieldName);

		primarySetter.addPrimary(fieldAccess);

		logExit(context);
	}

	public final void onCastExpressionStart(Context context) {

		logEnter(context);

		push(new StackCastExpression<>(logger));

		logExit(context);
	}

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

	public final void onPrimaryEnd(Context context) {

		logEnter(context);

		final StackPrimaryList<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION> stackPrimary = pop();

		final PRIMARY primary = makePrimary(context, stackPrimary.getList());

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(primary);

		logExit(context);
	}

	public final void onConditionalExpressionStart(Context context) {

		logEnter(context);

		push(new StackConditionalExpression<>(logger));

		logExit(context);
	}

	public final void onConditionalExpressionPart1Start(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	public final void onConditionalExpressionPart1End(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart1(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	public final void onConditionalExpressionPart2Start(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	public final void onConditionalExpressionPart2End(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart2(makeExpression(context, stackExpressionList));

		logExit(context);
	}

	public final void onConditionalExpressionPart3Start(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	public final void onConditionalExpressionPart3End(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackConditionalExpression<EXPRESSION> stackConditionalExpression = get();

		stackConditionalExpression.setPart3(makeExpression(context, stackExpressionList));

		logExit(context);
	}

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

	public final void onIntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createIntegerLiteral(context, value, base, signed, bits));

		logExit(context);
	}

	public final void onFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createFloatingPointLiteral(context, value, base, bits));

		logExit(context);
	}

	public final void onBooleanLiteral(Context context, boolean value) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createBooleanLiteral(context, value));

		logExit(context);
	}

	public final void onCharacterLiteral(Context context, char value) {

		logEnter(context);

		final PrimarySetter<PRIMARY> expressionSetter = get();

		expressionSetter.addPrimary(parseTreeFactory.createCharacterLiteral(context, value));

		logExit(context);
	}

	public final void onStringLiteral(Context context, String value) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createStringLiteral(context, value));

		logExit(context);
	}

	public final void onNullLiteral(Context context) {

		logEnter(context);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(parseTreeFactory.createNullLiteral(context));

		logExit(context);
	}

	public final void onClassInstanceCreationExpressionStart(Context context) {

		logEnter(context);

		push(new StackClassInstanceCreationExpression<>(logger));

		logExit(context);
	}

	public final void onClassInstanceCreationTypeAndConstructorName(Context context, ScopedName name) {

		logEnter(context);

		final StackClassInstanceCreationExpression<TYPE_REFERENCE, CONSTRUCTOR_NAME, EXPRESSION, CLASS_METHOD_MEMBER> stackClassInstanceCreationExpression = get();

		stackClassInstanceCreationExpression.setType(parseTreeFactory.createResolveLaterTypeReference(context, name, ReferenceType.NAME));
		stackClassInstanceCreationExpression.setConstructorName(parseTreeFactory.createConstructorName(context, name.getName()));

		logExit(context);
	}

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

	public final void onMethodInvocationStart(
			Context context,
			MethodInvocationType type,
			ScopedName classTypeName,
			Context classTypeNameContext,
			ReferenceType referenceType,
			String methodName,
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

		push(new StackMethodInvocation<>(logger, type, classType, methodName, methodNameContext));

		logExit(context);
	}

	public final void onParametersStart(Context context) {

		logEnter(context);

		push(new StackParameterList<>(logger));

		logExit(context);
	}

	public final void onParameterStart(Context context) {

		logEnter(context);

		push(new StackParameter<>(logger));

		logExit(context);
	}

	public final void onParameterEnd(Context context) {

		logEnter(context);

		final StackParameter<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackParameter = pop();

		final StackParameterList<EXPRESSION> stackParameterList = get();

		stackParameterList.addExpression(makeExpression(context, stackParameter));

		logExit(context);
	}

	public final void onParametersEnd(Context context) {

		logEnter(context);

		final StackParameterList<EXPRESSION> stackParameterList = pop();

		final ParametersSetter<EXPRESSION> parametersSetter = get();

		parametersSetter.setParameters(stackParameterList.getList());

		logExit(context);
	}

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

	public final void onArrayCreationExpressionStart(Context context, ScopedName typeName, ReferenceType referenceType, int numDims) {

		logEnter(context);

		final TYPE_REFERENCE typeReference = parseTreeFactory.createResolveLaterTypeReference(context, typeName, referenceType);
		
		push(new StackArrayCreationExpression<>(logger, typeReference, numDims));

		logExit(context);
	}

	public final void onDimExpressionStart(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	public final void onDimExpressionEnd(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpressionList = pop();

		final StackArrayCreationExpression<TYPE_REFERENCE, EXPRESSION> stackArrayCreationExpression = get();

		stackArrayCreationExpression.addDimExpression(makeExpression(context, stackExpressionList));

		logExit(context);
	}

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
	public final void onClassExpression(Context context, String className, int numArrayDims) {

		logEnter(context);

		final CLASS_EXPRESSION expression = parseTreeFactory.createClassExpression(context, className, numArrayDims);

		final PrimarySetter<PRIMARY> primarySetter = get();

		primarySetter.addPrimary(expression);

		logExit(context);
	}

	// Lambda expressions
	public final void onLambdaExpressionStart(Context context) {

		logEnter(context);

		push(new StackLambdaExpression<>(logger));

		pushVariableScope();

		logExit(context);
	}

	public final void onSingleLambdaParameter(Context context, String varName, Context varNameContext) {

		logEnter(context);

		final StackLambdaExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = get();

		stackLambdaExpression.setSingleParameter(varName, varNameContext);

		logExit(context);
	}

	public final void onFormalLambdaParameterListStart(Context context) {

		logEnter(context);

		push(new StackLambdaFormalParameters(logger));

		logExit(context);
	}

	public final void onFormalLambdaParameterListEnd(Context context) {

		logEnter(context);

		pop();

		logExit(context);
	}

	public final void onInferredLambdaParameterList(Context context, List<String> varNames, Context varNamesContext) {

		logEnter(context);

		final StackLambdaExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = get();

		stackLambdaExpression.setInferredParameterList(varNames, varNamesContext);

		logExit(context);
	}

	public final void onLambdaBodyStart(Context context) {

		logEnter(context);

		logExit(context);
	}

	public final void onLambdaBodyEnd(Context context) {

		logEnter(context);

		logExit(context);
	}

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

	public void onVariableDeclarationStatementStart(Context context) {

		logEnter(context);

		push(new StackVariableDeclarationList<>(logger));

		logExit(context);
	}

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
				elements);

		variableDeclaration.getList().forEach(e -> {
			
			final VARIABLE_DECLARATION var = parseTreeFactory.createVariableDeclaration(
					context,
					variableDeclaration.getModifiers(),
					e.getTypeReference(),
					e.getVarName(),
					e.getNumDims());
			
			variableScopes.get().add(e.getVarName(), var);
		});

		statementSetter.addStatement(statement);

		logExit(context);
	}

	public void onVariableDeclaratorStart(Context context) {

		logEnter(context);

		push(new StackVariableDeclaration<>(logger));

		logExit(context);
	}

	public void onVariableDeclaratorEnd(Context context) {

		logEnter(context);

		final StackVariableDeclaration<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackDeclaration = pop();

		final EXPRESSION initializer = makeExpressionOrNull(context, stackDeclaration);

		final BaseStackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION> declarationList = get();
		
		final StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION> variableDeclarationElement
			= new StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION>(
					context,
					declarationList.getTypeReference(),
					stackDeclaration.getName(),
					stackDeclaration.getNameContext(),
					stackDeclaration.getNumDims(),
					initializer);

		declarationList.add(variableDeclarationElement);

		logExit(context);
	}

	public final void onTypeReference(Context context, ScopedName name, ReferenceType referenceType) {

		logEnter(context);

		Objects.requireNonNull(name);

		final TypeReferenceSetter<TYPE_REFERENCE> typeReferenceSetter = get();

		typeReferenceSetter.setTypeReference(parseTreeFactory.createResolveLaterTypeReference(context, name, referenceType));

		logExit(context);
	}

	public final void onExpressionStatementStart(Context context) {

		logEnter(context);

		push(new StackExpressionStatement<>(logger));

		logExit(context);
	}

	public final void onExpressionStatementEnd(Context context) {

		logEnter(context);

		final StackExpressionStatement<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> statement = pop();

		final EXPRESSION_STATEMENT expressionStatement = parseTreeFactory.createExpressionStatement(context, makeExpression(context, statement));

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(expressionStatement);

		logExit(context);
	}

	public final void onForStatementStart(Context context, String keyword, Context keywordContext) {

		logEnter(context);

		push(new StackForStatement<>(logger, keyword, keywordContext));

		logExit(context);
	}

	public final void onForInitStart(Context context) {

		logEnter(context);

		push(new StackForInit<>(logger));

		logExit(context);
	}

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

	public final void onForUpdateStart(Context context) {

		logEnter(context);

		push(new StackForUpdate<>(logger));

		logExit(context);
	}

	public final void onForUpdateEnd(Context context) {

		logEnter(context);

		final StackForUpdate<EXPRESSION> stackForUpdate = pop();

		if (!stackForUpdate.getList().isEmpty()) {
			final StackForStatement<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT, FOR_INIT> stackForStatement = get();

			stackForStatement.setForUpdate(stackForUpdate.getList());
		}

		logExit(context);
	}

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

	public final void onIteratorForStatementStart(Context context) {

		logEnter(context);

		push(new StackIteratorForStatement<>(logger));

		pushVariableScope();

		logExit(context);
	}

	public final void onIteratorForTestEnd(Context context) {

		logEnter(context);

		final StackIteratorForStatement<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackIteratorForStatement = get();

		// Must add variable declarations to scope so that can be found further
		// down in parsing
		final VARIABLE_DECLARATION variableDeclaration = makeVariableDeclaration(context, stackIteratorForStatement);

		variableScopes.get().add(stackIteratorForStatement.getName(), variableDeclaration);

		logExit(context);
	}

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

	public final void onWhileStatementStart(Context context) {

		logEnter(context);

		push(new StackWhileStatement<>(logger));

		logExit(context);
	}

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

	public final void onDoWhileStatementStart(Context context) {

		logEnter(context);

		push(new StackDoWhileStatement<>(logger));

		logExit(context);
	}

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

	public final void onTryWithResourcesStatementStart(Context context) {

		logEnter(context);

		push(new StackTryWithResourcesStatement<>(logger));

		pushVariableScope(); // for the variables in resources

		logExit(context);
	}

	public final void onTryWithResourcesSpecificationStart(Context context) {

		logEnter(context);

		push(new StackResourceList<>(logger));

		logExit(context);
	}

	public final void onResourceStart(Context context) {

		logEnter(context);

		push(new StackResource<>(logger));

		logExit(context);
	}

	public final void onVariableName(Context context, String name, int numDims) {

		logEnter(context);

		final VariableNameSetter variableNameSetter = get();

		variableNameSetter.init(name, context, numDims);

		logExit(context);
	}

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

	public final void onTryWithResourcesSpecificationEnd(Context context) {

		logEnter(context);

		final StackResourceList<RESOURCE> stackResourceList = pop();

		final StackTryWithResourcesStatement<BLOCK, CATCH_BLOCK, RESOURCE> stackTryWithResourcesStatement = get();

		stackTryWithResourcesStatement.setResources(stackResourceList.getList());

		// Must push try-block to collect statements
		push(new StackTryBlock<>(logger));

		logExit(context);
	}

	public final void onTryStatementStart(Context context) {

		logEnter(context);

		push(new StackTryCatchFinallyStatement<>(logger));

		push(new StackTryBlock<>(logger));

		logExit(context);
	}

	public final void onTryBlockEnd(Context context) {

		logEnter(context);

		final StackTryBlock<STATEMENT> tryBlock = pop();

		final BaseStackTryCatchFinally<STATEMENT, TYPE_REFERENCE> baseStackTryCatchFinally = get();

		baseStackTryCatchFinally.setTryBlock(tryBlock);

		logExit(context);
	}
	
	

	public final void onCatchStart(Context context) {

		logEnter(context);

		push(new StackCatchBlock<>(logger));

		logExit(context);
	}

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

	public final void onFinallyStart(Context context) {

		logEnter(context);

		push(new StackFinallyBlock<>(logger));

		logExit(context);
	}

	public final void onFinallyEnd(Context context) {

		logEnter(context);

		final StackFinallyBlock<STATEMENT> stackFinallyBlock = pop();

		final BaseStackTryCatchFinally<STATEMENT, TYPE_REFERENCE> baseStackTryCatchFinally = get();

		baseStackTryCatchFinally.setFinallyBlock(stackFinallyBlock);

		logExit(context);
	}

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

	public final void onReturnStatementStart(Context context) {

		logEnter(context);

		push(new StackExpressionList<>(logger));

		logExit(context);
	}

	public final void onReturnStatementEnd(Context context) {

		logEnter(context);

		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackExpression = pop();

		final EXPRESSION expression = makeExpressionOrNull(context, stackExpression);

		final RETURN_STATEMENT returnStatement = parseTreeFactory.createReturnStatement(context, expression);

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(returnStatement);

		logExit(context);
	}

	public final void onThrowStatementStart(Context context) {

		logEnter(context);

		push(new StackThrowStatement<>(logger));

		logExit(context);
	}

	public final void onThrowStatementEnd(Context context) {

		logEnter(context);

		final StackThrowStatement<EXPRESSION, VARIABLE_REFERENCE, PRIMARY> stackThrowStatement = pop();

		final THROW_STATEMENT throwStatement = parseTreeFactory.createThrowStatement(context, stackThrowStatement.getExpression());

		final StatementSetter<STATEMENT> statementSetter = get();

		statementSetter.addStatement(throwStatement);

		logExit(context);
	}

	public final void onAnnotationStart(Context context) {

		logEnter(context);

		push(new StackAnnotation<>(logger));

		logExit(context);
	}

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
				e.getTypeReference(),
				e.getVarName(),
				e.getVarNameContext(),
				e.getNumDims(),
				e.getInitializer());
	}
}
