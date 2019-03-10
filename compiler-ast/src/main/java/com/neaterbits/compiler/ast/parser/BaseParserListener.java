package com.neaterbits.compiler.ast.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.CompilationCodeLines;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Import;
import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.block.Constructor;
import com.neaterbits.compiler.ast.block.ConstructorInvocation;
import com.neaterbits.compiler.ast.block.ConstructorInvocationStatement;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.block.ParameterName;
import com.neaterbits.compiler.ast.block.StaticInitializer;
import com.neaterbits.compiler.ast.expression.ArrayAccessExpression;
import com.neaterbits.compiler.ast.expression.ArrayCreationExpression;
import com.neaterbits.compiler.ast.expression.AssignmentExpression;
import com.neaterbits.compiler.ast.expression.Base;
import com.neaterbits.compiler.ast.expression.BlockLambdaExpression;
import com.neaterbits.compiler.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.ast.expression.ConditionalExpression;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.FieldAccess;
import com.neaterbits.compiler.ast.expression.LambdaExpression;
import com.neaterbits.compiler.ast.expression.LambdaExpressionParameters;
import com.neaterbits.compiler.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.expression.NestedExpression;
import com.neaterbits.compiler.ast.expression.ParameterList;
import com.neaterbits.compiler.ast.expression.Resource;
import com.neaterbits.compiler.ast.expression.SingleLambdaExpression;
import com.neaterbits.compiler.ast.expression.ThisPrimary;
import com.neaterbits.compiler.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.ast.expression.literal.ClassExpression;
import com.neaterbits.compiler.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.ast.parser.stackstate.BaseStackTryCatchFinally;
import com.neaterbits.compiler.ast.parser.stackstate.BaseStackVariableDeclarationList;
import com.neaterbits.compiler.ast.parser.stackstate.CallableStackEntry;
import com.neaterbits.compiler.ast.parser.stackstate.StackAnnotation;
import com.neaterbits.compiler.ast.parser.stackstate.StackAnonymousClass;
import com.neaterbits.compiler.ast.parser.stackstate.StackArrayAccess;
import com.neaterbits.compiler.ast.parser.stackstate.StackArrayCreationExpression;
import com.neaterbits.compiler.ast.parser.stackstate.StackAssignmentExpression;
import com.neaterbits.compiler.ast.parser.stackstate.StackAssignmentLHS;
import com.neaterbits.compiler.ast.parser.stackstate.StackCatchBlock;
import com.neaterbits.compiler.ast.parser.stackstate.StackClass;
import com.neaterbits.compiler.ast.parser.stackstate.StackClassInstanceCreationExpression;
import com.neaterbits.compiler.ast.parser.stackstate.StackClassMethod;
import com.neaterbits.compiler.ast.parser.stackstate.StackCompilationUnit;
import com.neaterbits.compiler.ast.parser.stackstate.StackConditionalExpression;
import com.neaterbits.compiler.ast.parser.stackstate.StackConstructor;
import com.neaterbits.compiler.ast.parser.stackstate.StackConstructorInvocation;
import com.neaterbits.compiler.ast.parser.stackstate.StackDoWhileStatement;
import com.neaterbits.compiler.ast.parser.stackstate.StackEnum;
import com.neaterbits.compiler.ast.parser.stackstate.StackEnumConstant;
import com.neaterbits.compiler.ast.parser.stackstate.StackExpressionList;
import com.neaterbits.compiler.ast.parser.stackstate.StackExpressionStatement;
import com.neaterbits.compiler.ast.parser.stackstate.StackFieldDeclarationList;
import com.neaterbits.compiler.ast.parser.stackstate.StackFinallyBlock;
import com.neaterbits.compiler.ast.parser.stackstate.StackForInit;
import com.neaterbits.compiler.ast.parser.stackstate.StackForStatement;
import com.neaterbits.compiler.ast.parser.stackstate.StackForUpdate;
import com.neaterbits.compiler.ast.parser.stackstate.StackInterface;
import com.neaterbits.compiler.ast.parser.stackstate.StackInterfaceMethod;
import com.neaterbits.compiler.ast.parser.stackstate.StackIteratorForStatement;
import com.neaterbits.compiler.ast.parser.stackstate.StackLambdaExpression;
import com.neaterbits.compiler.ast.parser.stackstate.StackLambdaFormalParameters;
import com.neaterbits.compiler.ast.parser.stackstate.StackMethodInvocation;
import com.neaterbits.compiler.ast.parser.stackstate.StackNamedClass;
import com.neaterbits.compiler.ast.parser.stackstate.StackNamespace;
import com.neaterbits.compiler.ast.parser.stackstate.StackParameter;
import com.neaterbits.compiler.ast.parser.stackstate.StackParameterList;
import com.neaterbits.compiler.ast.parser.stackstate.StackParameterSignature;
import com.neaterbits.compiler.ast.parser.stackstate.StackPrimaryList;
import com.neaterbits.compiler.ast.parser.stackstate.StackResource;
import com.neaterbits.compiler.ast.parser.stackstate.StackReturnType;
import com.neaterbits.compiler.ast.parser.stackstate.StackStaticInitializer;
import com.neaterbits.compiler.ast.parser.stackstate.StackThrowStatement;
import com.neaterbits.compiler.ast.parser.stackstate.StackTryBlock;
import com.neaterbits.compiler.ast.parser.stackstate.StackTryCatchFinallyStatement;
import com.neaterbits.compiler.ast.parser.stackstate.StackTryWithResourcesStatement;
import com.neaterbits.compiler.ast.parser.stackstate.StackVariableDeclaration;
import com.neaterbits.compiler.ast.parser.stackstate.StackVariableDeclarationList;
import com.neaterbits.compiler.ast.parser.stackstate.StackWhileStatement;
import com.neaterbits.compiler.ast.parser.stackstate.VariableNameSetter;
import com.neaterbits.compiler.ast.statement.CatchBlock;
import com.neaterbits.compiler.ast.statement.DoWhileStatement;
import com.neaterbits.compiler.ast.statement.ExpressionStatement;
import com.neaterbits.compiler.ast.statement.FieldTransient;
import com.neaterbits.compiler.ast.statement.FieldVolatile;
import com.neaterbits.compiler.ast.statement.ForExpressionList;
import com.neaterbits.compiler.ast.statement.ForInit;
import com.neaterbits.compiler.ast.statement.ForStatement;
import com.neaterbits.compiler.ast.statement.IteratorForStatement;
import com.neaterbits.compiler.ast.statement.Mutability;
import com.neaterbits.compiler.ast.statement.ReturnStatement;
import com.neaterbits.compiler.ast.statement.ThrowStatement;
import com.neaterbits.compiler.ast.statement.TryCatchFinallyStatement;
import com.neaterbits.compiler.ast.statement.TryWithResourcesStatement;
import com.neaterbits.compiler.ast.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.ast.statement.WhileStatement;
import com.neaterbits.compiler.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.ast.type.primitive.CharacterType;
import com.neaterbits.compiler.ast.type.primitive.FloatingPointType;
import com.neaterbits.compiler.ast.type.primitive.IntegerType;
import com.neaterbits.compiler.ast.type.primitive.StringType;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifier;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodNative;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodStrictfp;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodSynchronized;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.ast.typedefinition.ClassModifier;
import com.neaterbits.compiler.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassName;
import com.neaterbits.compiler.ast.typedefinition.ClassOrInterfaceName;
import com.neaterbits.compiler.ast.typedefinition.ClassStatic;
import com.neaterbits.compiler.ast.typedefinition.ClassStrictfp;
import com.neaterbits.compiler.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.ConstructorMember;
import com.neaterbits.compiler.ast.typedefinition.ConstructorModifier;
import com.neaterbits.compiler.ast.typedefinition.ConstructorModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ConstructorModifiers;
import com.neaterbits.compiler.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.ast.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.ast.typedefinition.EnumConstantDefinition;
import com.neaterbits.compiler.ast.typedefinition.EnumConstantName;
import com.neaterbits.compiler.ast.typedefinition.EnumDefinition;
import com.neaterbits.compiler.ast.typedefinition.FieldModifier;
import com.neaterbits.compiler.ast.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.FieldModifiers;
import com.neaterbits.compiler.ast.typedefinition.FieldName;
import com.neaterbits.compiler.ast.typedefinition.FieldStatic;
import com.neaterbits.compiler.ast.typedefinition.FieldVisibility;
import com.neaterbits.compiler.ast.typedefinition.InterfaceAbstract;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodDefault;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodMember;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodModifier;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodModifiers;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodStatic;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodStrictfp;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifier;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifiers;
import com.neaterbits.compiler.ast.typedefinition.InterfaceName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceStatic;
import com.neaterbits.compiler.ast.typedefinition.InterfaceStrictfp;
import com.neaterbits.compiler.ast.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.ast.typedefinition.VariableModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.variables.ModifiersVariableDeclarationElement;
import com.neaterbits.compiler.ast.variables.NameReference;
import com.neaterbits.compiler.ast.variables.SimpleVariableReference;
import com.neaterbits.compiler.ast.variables.VarName;
import com.neaterbits.compiler.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class BaseParserListener {

	private final ParseLogger logger;
	
	// Stack for the main elements of a program
	private final ListStack mainStack;

	// Scope for variables
	private final ArrayStack<VariablesMap> variableScopes;

	private boolean variableScopesContain(String name) {
		
		boolean contains = false;
		
		for (int i = variableScopes.size() - 1; i >= 0; -- i) {
			if (variableScopes.get(i).hasVariable(name)) {
				contains = true;
				break;
			}
		}

		return contains;
	}
	
	protected BaseParserListener(ParseLogger logger) {
		
		this.logger = logger;
		
		this.mainStack = new ListStack();
		
		this.variableScopes = new ArrayStack<>();
	}
	
	protected final ParseLogger getLogger() {
		return logger;
	}
	
	protected final VariableDeclaration findVariableDeclaration(String name) {
		Objects.requireNonNull(name);

		for (int i = variableScopes.size() - 1; i >=0; -- i) {
			final VariableDeclaration variableDeclaration = variableScopes.get(i).findVariable(name);

			if (variableDeclaration != null) {
				return variableDeclaration;
			}
		}

		return null;
	}
	
	private static String getMethodName() {
		final StackTraceElement stackTraceElement [] = Thread.currentThread().getStackTrace();
		
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
		
		push(new StackCompilationUnit(logger));
		
		logExit(context);
	}
	
	public CompilationUnit onCompilationUnitEnd(Context context) {
		
		logEnter(context);
		
		final StackCompilationUnit stackCompilationUnit = pop();
		
		final CompilationUnit compilationUnit = new CompilationUnit(
				context,
				stackCompilationUnit.getImports(),
				stackCompilationUnit.getList());
		
		logExit(context);
		
		return compilationUnit;
	}

	public final void onImport(Import importStatement) {
		Objects.requireNonNull(importStatement);
		
		final StackCompilationUnit stackCompilationUnit = get(StackCompilationUnit.class);
		
		stackCompilationUnit.addImport(importStatement);
	}
	
	public final void onNamespaceStart(Context context, String name, String [] parts) {
		
		logEnter(context);
		
		push(new StackNamespace(logger, name, parts));
		
		logExit(context);
	}
	
	public final void onNameSpaceEnd(Context context) {

		logEnter(context);
		
		final StackNamespace stackNamespace = pop();
		
		final List<CompilationCode> namespaceCode = stackNamespace.getList();

		final Namespace nameSpace = new Namespace(
				context,
				stackNamespace.getParts(),
				new CompilationCodeLines(context, namespaceCode));
		
		mainStack.addElement(nameSpace);
		
		logExit(context);
	}
	
	public final void onClassStart(Context context, String name, Context nameContext) {
		
		logEnter(context);
		
		push(new StackNamedClass(logger, name, nameContext));
		
		logExit(context);
	}

	private void addClassModifier(Context context, ClassModifier modifier) {

		logEnter(context);

		final ClassModifierSetter stackClass = get();
		
		stackClass.addClassModifier(new ClassModifierHolder(context, modifier));
		
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
	
	public final void onClassExtends(Context context, ScopedName className) {
		
		logEnter(context);

		final StackNamedClass stackNamedClass = get();
		
		stackNamedClass.addExtendedClass(new ResolveLaterTypeReference(context, className));
		
		logExit(context);
	}
	
	public final void onClassImplements(Context context, ScopedName interfaceName) {
		
		logEnter(context);
		
		final StackNamedClass stackNamedClass = get();
		
		stackNamedClass.addImplementedInterface(new ResolveLaterTypeReference(context, interfaceName));
		
		logExit(context);
	}
	
	public final void onClassEnd(Context context) {
		
		logEnter(context);
		
		final StackNamedClass entry = pop();
		
		final List<ComplexMemberDefinition> classCode = entry.getList();

		final ClassModifiers classModifiers = new ClassModifiers(entry.getModifiers());
		
		final ClassDefinition classDefinition = new ClassDefinition(
				context,
				classModifiers,
				new ClassDeclarationName(entry.getNameContext(), new ClassName(entry.getName())),
				entry.getExtendedClasses(),
				entry.getImplementedInterfaces(),
				classCode);
		
		mainStack.addElement(classDefinition);
		
		logExit(context);
	}

	public final void onAnonymousClassStart(Context context) {
		
		logEnter(context);
		
		push(new StackAnonymousClass(logger));
		
		logEnter(context);
	}

	public final void onAnonymousClassEnd(Context context) {
		
		logEnter(context);
		
		final StackAnonymousClass entry = pop();
		
		final List<ComplexMemberDefinition> classCode = entry.getList();
		
		final ClassDefinition classDefinition = new ClassDefinition(context, null, null, null, null, classCode);
		
		mainStack.addElement(classDefinition);
		
		logExit(context);
	}
	
	public final void onStaticInitializerStart(Context context) {
		
		logEnter(context);
		
		pushVariableScope();
		
		push(new StackStaticInitializer(logger));
		
		logExit(context);
	}
	
	public final void onStaticInitializerEnd(Context context) {
		
		logEnter(context);
		
		final StackStaticInitializer stackStaticInitializer = pop();
		
		final StaticInitializer initializer = new StaticInitializer(context, new Block(context, stackStaticInitializer.getList()));
		
		final StackClass stackClass = get();
		
		stackClass.add(initializer);
		
		popVariableScope();
		
		logExit(context);
	}

	public final void onConstructorStart(Context context) {
		
		logEnter(context);
		
		push(new StackConstructor(logger));
		
		pushVariableScope();
		
		logExit(context);
	}
	
	private void addConstructorModifier(Context context, ConstructorModifier modifier) {
		
		logEnter(context);
		
		final StackConstructor stackConstructor = get();
		
		stackConstructor.addModifier(new ConstructorModifierHolder(context, modifier));
		
		logExit(context);
	}
	
	public final void onConstructorVisibilityModifier(Context context, ConstructorVisibility visibility) {
		
		logEnter(context);
		
		addConstructorModifier(context, visibility);
		
		logExit(context);
	}

	public final void onConstructorName(Context context, String constructorName) {
		
		logEnter(context);
		
		final StackConstructor constructor = get();
		
		constructor.setName(constructorName);
		
		logExit(context);
	}

	public final void onConstructorInvocationStart(Context context, ConstructorInvocation type) {
		
		logEnter(context);
		
		push(new StackConstructorInvocation(logger, type));
		
		logExit(context);
	}
	
	public final void onConstructorInvocationEnd(Context context) {
		
		logEnter(context);
		
		final StackConstructorInvocation stackConstructorInvocation = pop();
		
		final ParameterList parameterList = new ParameterList(
				stackConstructorInvocation.getParameters() != null
					? stackConstructorInvocation.getParameters()
					: Collections.emptyList());
		
		final ConstructorInvocationStatement statement = new ConstructorInvocationStatement(
				context,
				stackConstructorInvocation.getType(),
				stackConstructorInvocation.makeExpressionOrNull(context),
				parameterList);
		
		final StatementSetter statementSetter = get();

		statementSetter.addStatement(statement);

		logExit(context);
	}
	
	public final void onConstructorEnd(Context context) {
		
		logEnter(context);

		popVariableScope();
		
		final StackConstructor stackConstructor = pop();
		
		final Constructor constructor = new Constructor(
				context,
				new ConstructorName(stackConstructor.getName()),
				stackConstructor.getParameters(),
				new Block(context, stackConstructor.getList()));
		
		final ConstructorMember constructorMember = new ConstructorMember(
				context,
				new ConstructorModifiers(stackConstructor.getModifiers()),
				constructor);

		final ConstructorMemberSetter constructorMemberSetter = get();

		constructorMemberSetter.addConstructorMember(constructorMember);
		
		logExit(context);
	}

	public final void onClassMethodStart(Context context) {
		
		logEnter(context);
		
		final StackClassMethod method = new StackClassMethod(logger);

		push(method);
		
		pushVariableScope();
		
		logExit(context);
	}
	
	public final void onMethodReturnTypeStart(Context context) {

		logEnter(context);
		
		push(new StackReturnType(logger));
		
		logExit(context);
	}
	
	public final void onMethodReturnTypeEnd(Context context) {
		
		logEnter(context);
		
		final StackReturnType stackReturnType = pop();
		
		final CallableStackEntry stackMethod = get();
		
		stackMethod.setReturnType(stackReturnType.getType());
		
		logExit(context);
	}
	
	public final void onMethodName(Context context, String methodName) {
		
		logEnter(context);
		
		final CallableStackEntry method = get();
		
		method.setName(methodName);
		
		logExit(context);
	}
	
	public final void onMethodSignatureParametersStart(Context context) {
		
	}

	public final void onMethodSignatureParameterStart(Context context, boolean varArgs) {
		
		logEnter(context);
		
		push(new StackParameterSignature(logger, varArgs));
		
		logExit(context);
	}

	public final void onMethodSignatureParameterEnd(Context context) {
		
		logEnter(context);
		
		final StackParameterSignature stackParameterSignature = pop();
		
		final CallableStackEntry stackCallable = get();
		
		final Parameter parameter = new Parameter(
				context,
				stackParameterSignature.getTypeReference(),
				new ParameterName(stackParameterSignature.getName()),
				stackParameterSignature.isVarArgs());

		stackCallable.addParameter(parameter);
		
		final VariableDeclaration variableDeclaration = stackParameterSignature.makeVariableDeclaration(context);

		variableScopes.get().add(stackParameterSignature.getName(), variableDeclaration);
		
		logExit(context);
	}
	
	public final void onMethodSignatureParametersEnd(Context context) {
		
	}

	private void addClassMethodModifier(Context context, ClassMethodModifier modifier) {
		
		logEnter(context);
		
		final StackClassMethod stackMethod = get();
		
		stackMethod.addModifier(new ClassMethodModifierHolder(context, modifier));
		
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
		
		final StackClassMethod method = pop();

		final ClassMethodMemberSetter methodMemberSetter = get();
		
		final ClassMethodMember methodMember = new ClassMethodMember(
				context,
				new ClassMethodModifiers(method.getModifiers()), method.makeMethod(context));

		methodMemberSetter.addMethod(methodMember);
		
		logExit(context);
	}
	
	public final void onFieldDeclarationStart(Context context) {
		
		logEnter(context);
		
		push(new StackFieldDeclarationList(logger));
		
		logExit(context);
	}
	
	private void addFieldModifier(Context context, FieldModifier modifier) {
		final StackFieldDeclarationList stackFieldDeclarationList = get();

		stackFieldDeclarationList.addFieldModifier(new FieldModifierHolder(context, modifier));
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
	
	public final void onMutabilityFieldModifier(Context context, Mutability mutability) {
		
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
		
		final StackFieldDeclarationList stackFieldDeclarationList = pop();
		
		final StackClass stackClass = get();

		for (InitializerVariableDeclarationElement element : stackFieldDeclarationList.getList()) {
			
			final Expression initializer = element.getInitializer();
			final TypeReference typeReference = element.getTypeReference();

			if (initializer != null) {
				initializer.take();
			}

			typeReference.take();
			
			final ClassDataFieldMember dataFieldMember = new ClassDataFieldMember(
					context,
					new FieldModifiers(stackFieldDeclarationList.getModifiers()),
					typeReference,
					new FieldName(element.getName().getName()),
					initializer);
			
			stackClass.add(dataFieldMember);
		}
		
		logExit(context);
	}

	public final void onInterfaceStart(Context context, String name, Context nameContext) {
		
		logEnter(context);
		
		push(new StackInterface(logger, name, nameContext));
		
		logExit(context);
	}

	private void addInterfaceModifier(Context context, InterfaceModifier modifier) {

		logEnter(context);

		final StackInterface stackInterface = (StackInterface)mainStack.get();
		
		stackInterface.addModifier(new InterfaceModifierHolder(context, modifier));
		
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
		
		final StackInterface entry = get();
		
		entry.addExtendedInterface(new ResolveLaterTypeReference(context, interfaceName));
		
		logExit(context);
	}
	
	public final void onInterfaceEnd(Context context) {
		
		logEnter(context);
		
		final StackInterface entry = pop();
		
		final List<ComplexMemberDefinition> interfaceCode = entry.getList();

		final InterfaceModifiers interfaceModifiers = new InterfaceModifiers(entry.getModifiers());
		
		final InterfaceDefinition classDefinition = new InterfaceDefinition(
				context,
				interfaceModifiers,
				new InterfaceDeclarationName(entry.getNameContext(), new InterfaceName(entry.getName())),
				entry.getExtendedInterfaces(),
				interfaceCode);
		
		mainStack.addElement(classDefinition);
		
		logExit(context);
	}

	public final void onEnumStart(Context context, String name, Context nameContext) {

		logEnter(context);

		push(new StackEnum(logger, name, nameContext));
		
		logExit(context);
	}

	public final void onEnumImplements(Context context, ScopedName interfaceName) {
		
		logEnter(context);

		final StackEnum stackEnum = get();
		
		stackEnum.addImplementedInterface(new ResolveLaterTypeReference(context, interfaceName));
		
		logExit(context);
	}
	
	public final void onEnumConstantStart(Context context, String name) {
		
		logEnter(context);

		push(new StackEnumConstant(logger, name));
		
		logExit(context);
	}
	
	public final void onEnumConstantEnd(Context context) {
	
		logEnter(context);

		final StackEnumConstant stackEnumConstant = pop();

		final EnumConstantDefinition enumConstant = new EnumConstantDefinition(
				context,
				new EnumConstantName(stackEnumConstant.getName()),
				stackEnumConstant.getParameters() != null
					? new ParameterList(stackEnumConstant.getParameters())
					: null,
				stackEnumConstant.getList());
		
		final StackEnum stackEnum = get();
		
		stackEnum.addConstant(enumConstant);
		
		logExit(context);
	}
	
	
	public final void onEnumEnd(Context context) {

		logEnter(context);

		final StackEnum stackEnum = pop();
		
		final EnumDefinition enumDefinition = new EnumDefinition(
				context,
				new ClassModifiers(stackEnum.getModifiers()),
				new ClassDeclarationName(stackEnum.getNameContext(), new ClassName(stackEnum.getName())),
				stackEnum.getImplementedInterfaces(),
				stackEnum.getConstants(),
				stackEnum.getList());
		
		mainStack.addElement(enumDefinition);
		
		logExit(context);
	}
	
	public final void onInterfaceMethodStart(Context context) {
		
		logEnter(context);
		
		final StackInterfaceMethod method = new StackInterfaceMethod(logger);

		push(method);
		
		pushVariableScope();
		
		logExit(context);
	}

	private void addInterfaceMethodModifier(Context context, InterfaceMethodModifier modifier) {
		
		logEnter(context);
		
		final StackInterfaceMethod stackMethod = get();
		
		stackMethod.addModifier(new InterfaceMethodModifierHolder(context, modifier));
		
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
		
		final StackInterfaceMethod method = pop();

		final InterfaceMethodMemberSetter methodMemberSetter = get();
		
		final InterfaceMethodMember methodMember = new InterfaceMethodMember(
				context,
				new InterfaceMethodModifiers(method.getModifiers()), method.makeMethod(context));

		methodMemberSetter.addMethod(methodMember);
		
		logExit(context);
	}
	
	// Expressions
	public final void onEnterAssignmentExpression(Context context) {

		logEnter(context);
		
		push(new StackAssignmentExpression(logger));
		
		logExit(context);
	}
	
	public final void onEnterAssignmentLHS(Context context) {
		
		logEnter(context);
		
		push(new StackAssignmentLHS(logger));
		
		logExit(context);
	}

	public final void onExitAssignmentLHS(Context context) {
		
		logEnter(context);
		
		final StackAssignmentLHS assignmentLHS = pop();
		
		final StackAssignmentExpression assignmentExpression = get();
		
		assignmentExpression.setLHS(assignmentLHS.getVariableReference(context));
		
		push(new StackExpressionList(logger));
		
		logExit(context);
	}
	
	public final void onExitAssignmentExpression(Context context) {

		logEnter(context);
		
		final StackExpressionList stackExpressionList = pop();
		
		final StackAssignmentExpression stackAssignmentExpression = pop();
		
		stackAssignmentExpression.setRHS(stackExpressionList.makeExpression(context));

		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(new AssignmentExpression(
				context,
				stackAssignmentExpression.getLHS(),
				stackAssignmentExpression.getRHS()));
		
		logExit(context);
	}
	
	public final void onNestedExpressionStart(Context context) {
		
		logEnter(context);
		
		push(new StackExpressionList(logger));
		
		logExit(context);
	}
	
	public final void onNestedExpressionEnd(Context context) {

		logEnter(context);
		
		final StackExpressionList stackExpressionList = pop();
		
		final NestedExpression nestedExpression = new NestedExpression(
				context,
				stackExpressionList.makeExpression(context));
		
		final NestedExpressionSetter nestedExpressionSetter = get();
		
		nestedExpressionSetter.addNestedExpression(nestedExpression);
		
		logExit(context);
	}

	// Variables
	
	// Variable or class member
	public final void onNameReference(Context context, String name) {
		
		logEnter(context);
		
		final VariableReferenceSetter variableReferenceSetter = get();
		
		variableReferenceSetter.setVariableReference(new NameReference(context, name));
		
		logExit(context);
	}
	
	
	// Resolved as variable
	public final void onVariableReference(Context context, String name) {

		logEnter(context);
		
		final VariableReferenceSetter variableReferenceSetter = get();

		final VariableDeclaration declaration = findVariableDeclaration(name);

		if (declaration == null) {
			throw new CompileException(context, "No variable declared for name " + name);
		}

		final SimpleVariableReference variableReference = new SimpleVariableReference(context, declaration);

		variableReferenceSetter.setVariableReference(variableReference);
		
		logExit(context);
	}
	
	// Field access
	
	public final void onPrimaryStart(Context context) {
		
		logEnter(context);
		
		// Start of any primary expression, like a literal or a linked list of field accesses
		push(new StackPrimaryList(logger));
		
		logExit(context);
	}
	
	public final void onArrayAccessStart(Context context) {
		
		logEnter(context);

		push(new StackArrayAccess(logger));
		
		logExit(context);
	}
	
	public final void onArrayIndexStart(Context context) {
		
		logEnter(context);
		
		push(new StackExpressionList(logger));
		
		logExit(context);
	}
	
	public final void onArrayIndexEnd(Context context) {
		
		logEnter(context);
		
		final StackExpressionList stackExpressionList = pop();
		
		final StackArrayAccess stackArrayAccess = get();
		
		stackArrayAccess.addExpression(stackExpressionList.makeExpression(context));
		
		logExit(context);
	}
	
	public final void onArrayAccessEnd(Context context) {
		
		logEnter(context);
		
		final StackArrayAccess stackArrayAccess = pop();

		final ArrayAccessExpression expression = new ArrayAccessExpression(
				context,
				stackArrayAccess.getArray(context),
				stackArrayAccess.getIndex());
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(expression);
		
		logExit(context);
	}
	
	
	public final void onFieldAccess(Context context, FieldAccessType fieldAccessType, ScopedName typeName, String fieldName) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		final FieldAccess fieldAccess = new FieldAccess(
				context,
				fieldAccessType,
				typeName != null ? new ResolveLaterTypeReference(context, typeName) : null,
				new FieldName(fieldName));
		
		primarySetter.addPrimary(fieldAccess);
		
		logExit(context);
	}
	
	public final void onThisPrimary(Context context) {
		
		logEnter(context);
		
		final StackPrimaryList stackPrimaryList = get();
		
		final StackNamedClass stackClass = mainStack.getFromTop(StackNamedClass.class);
		
		stackPrimaryList.add(new ThisPrimary(context, new ResolveLaterTypeReference(context, new ScopedName(null, stackClass.getName()))));
		
		logExit(context);
	}
	
	public final void onPrimaryEnd(Context context) {
		
		logEnter(context);
		
		final StackPrimaryList stackPrimary = pop();
		
		final Primary primary = stackPrimary.makePrimary(context);

		final PrimarySetter primarySetter = get();

		primarySetter.addPrimary(primary);
		
		logExit(context);
	}
	
	public final void onConditionalExpressionStart(Context context) {

		logEnter(context);
		
		push(new StackConditionalExpression(logger));

		logExit(context);
	}
	
	public final void onConditionalExpressionPart1Start(Context context) {

		logEnter(context);
		
		push(new StackExpressionList(logger));

		logExit(context);
	}
	
	public final void onConditionalExpressionPart1End(Context context) {
		
		logEnter(context);
		
		final StackExpressionList stackExpressionList = pop();
		
		final StackConditionalExpression stackConditionalExpression = get();
		
		stackConditionalExpression.setPart1(stackExpressionList.makeExpression(context));
		
		logExit(context);
	}

	public final void onConditionalExpressionPart2Start(Context context) {
		
		logEnter(context);
		
		push(new StackExpressionList(logger));

		logExit(context);
	}
	
	public final void onConditionalExpressionPart2End(Context context) {
		
		logEnter(context);
		
		final StackExpressionList stackExpressionList = pop();
		
		final StackConditionalExpression stackConditionalExpression = get();
		
		stackConditionalExpression.setPart2(stackExpressionList.makeExpression(context));

		logExit(context);
	}

	public final void onConditionalExpressionPart3Start(Context context) {
		
		logEnter(context);
		
		push(new StackExpressionList(logger));

		logExit(context);
	}
	
	public final void onConditionalExpressionPart3End(Context context) {
		
		logEnter(context);
		
		final StackExpressionList stackExpressionList = pop();
		
		final StackConditionalExpression stackConditionalExpression = get();
		
		stackConditionalExpression.setPart3(stackExpressionList.makeExpression(context));

		logExit(context);
	}
	
	public final void onConditionalExpressionEnd(Context context) {

		logEnter(context);
		
		final StackConditionalExpression stackConditionalExpression = pop();
		
		final ConditionalExpression conditionalExpression = new ConditionalExpression(
				context,
				stackConditionalExpression.getPart1(),
				stackConditionalExpression.getPart2(),
				stackConditionalExpression.getPart3());
		
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(conditionalExpression);
		
		logExit(context);
	}
	
	// Literals
	
	public final void onIntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits, IntegerType type) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new IntegerLiteral(context, value, base, signed, bits, type));
		
		logExit(context);
	}
	
	public final void onFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits, FloatingPointType type) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new FloatingPointLiteral(context, value, base, bits, type));
		
		logExit(context);
	}
	
	public final void onBooleanLiteral(Context context, boolean value, BooleanType type) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new BooleanLiteral(context, value, type));
		
		logExit(context);
	}
	
	public final void onCharacterLiteral(Context context, char value, CharacterType type) {
		
		logEnter(context);
		
		final PrimarySetter expressionSetter = get();
		
		expressionSetter.addPrimary(new CharacterLiteral(context, value, type));
		
		logExit(context);
	}
	
	public final void onStringLiteral(Context context, String value, StringType type) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new StringLiteral(context, value, type));
		
		logExit(context);
	}
	
	public final void onNullLiteral(Context context) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new NullLiteral(context));
		
		logExit(context);
	}
	
	public final void onClassInstanceCreationExpressionStart(Context context) {
		
		logEnter(context);
		
		push(new StackClassInstanceCreationExpression(logger));
		
		logExit(context);
	}
	
	public final void onClassInstanceCreationTypeAndConstructorName(Context context, TypeReference type, ScopedName name) {
		
		logEnter(context);
		
		final StackClassInstanceCreationExpression stackClassInstanceCreationExpression = get();
		
		stackClassInstanceCreationExpression.setType(type);
		stackClassInstanceCreationExpression.setConstructorName(new ConstructorName(name.getName()));
		
		logExit(context);
	}
	
	public final void onClassInstanceCreationExpressionEnd(Context context) {
		
		logEnter(context);
		
		final StackClassInstanceCreationExpression classInstanceCreationExpression = pop();
		
		final ClassInstanceCreationExpression primary = new ClassInstanceCreationExpression(
				context,
				classInstanceCreationExpression.getType(),
				classInstanceCreationExpression.getConstructorName(),
				new ParameterList(
						classInstanceCreationExpression.getParameters() != null
							? classInstanceCreationExpression.getParameters()
							: Collections.emptyList()));
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(primary);
		
		logExit(context);
	}
	
	public final void onMethodInvocationStart(Context context, MethodInvocationType type, TypeReference classType, String methodName) {
		
		logEnter(context);
		
		if (classType != null && classType instanceof ResolveLaterTypeReference) {
			final ResolveLaterTypeReference resolveLaterTypeReference = (ResolveLaterTypeReference)classType;

			if (variableScopesContain(resolveLaterTypeReference.getTypeName().getName())) {
				// Likely a scoped variable, eg. variable.invokeMethod() instead of Class.invokeStaticMethod()
				
				if (type == MethodInvocationType.NAMED_CLASS_STATIC) {
					type = MethodInvocationType.VARIABLE_REFERENCE;
					classType = null;
				}
			}
		}
		
		push(new StackMethodInvocation(logger, type, classType, methodName));
		
		logExit(context);
	}
	
	public final void onParametersStart(Context context) {
		
		logEnter(context);
		
		push(new StackParameterList(logger));
		
		logExit(context);
	}
	
	public final void onParameterStart(Context context) {
		
		logEnter(context);
		
		push(new StackParameter(logger));
		
		logExit(context);
	}
	
	public final void onParameterEnd(Context context) {

		logEnter(context);
		
		final StackParameter stackParameter = pop();
		
		final StackParameterList stackParameterList = get();
		
		stackParameterList.addExpression(stackParameter.makeExpression(context));
		
		logExit(context);
	}

	public final void onParametersEnd(Context context) {

		logEnter(context);
		
		final StackParameterList stackParameterList = pop();

		final ParametersSetter parametersSetter = get();

		parametersSetter.setParameters(stackParameterList.getList());
		
		logExit(context);
	}
	
	public final void onMethodInvocationEnd(Context context) {

		logEnter(context);
		
		final StackMethodInvocation stackMethodInvocation = pop();

		final MethodInvocationExpression methodInvocation = new MethodInvocationExpression(
				context,
				stackMethodInvocation.getType(),
				stackMethodInvocation.getClassType(),
				stackMethodInvocation.getObject(),
				stackMethodInvocation.getName(),
				new ParameterList(
						stackMethodInvocation.getParameters() != null
							? stackMethodInvocation.getParameters()
							: Collections.emptyList()));
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(methodInvocation);
		
		logExit(context);
	}
	
	public final void onArrayCreationExpressionStart(Context context, TypeReference type, int numDims) {
		
		logEnter(context);
		
		push(new StackArrayCreationExpression(logger, type, numDims));
		
		logExit(context);
	}
	
	public final void onDimExpressionStart(Context context) {
		
		logEnter(context);
		
		push(new StackExpressionList(logger));
		
		logExit(context);
	}
	
	
	public final void onDimExpressionEnd(Context context) {
		
		logEnter(context);
		
		final StackExpressionList stackExpressionList = pop();
		
		final StackArrayCreationExpression stackArrayCreationExpression = get();
		
		stackArrayCreationExpression.addDimExpression(stackExpressionList.makeExpression(context));
		
		logExit(context);
	}
	
	public final void onArrayCreationExpressionEnd(Context context) {
		
		logEnter(context);
		
		final StackArrayCreationExpression stackArrayCreationExpression = pop();
		
		final ArrayCreationExpression expression = new ArrayCreationExpression(
				context,
				stackArrayCreationExpression.getType(),
				stackArrayCreationExpression.getDimExpressions(),
				stackArrayCreationExpression.getNumDims());
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(expression);

		logExit(context);
	}

	
	// Class expressions
	public final void onClassExpression(Context context, String className, int numArrayDims) {
		
		logEnter(context);
		
		final ClassExpression expression = new ClassExpression(context, new ClassOrInterfaceName(className), numArrayDims);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(expression);
		
		logExit(context);
	}
	
	// Lambda expressions
	public final void onLambdaExpressionStart(Context context) {
		
		logEnter(context);
		
		push(new StackLambdaExpression(logger));
		
		pushVariableScope();
		
		logExit(context);
	}
	
	public final void onSingleLambdaParameter(Context context, String varName) {
	
		logEnter(context);
		
		final StackLambdaExpression stackLambdaExpression = get();
		
		stackLambdaExpression.setSingleParameter(varName);
		
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
	
	public final void onInferredLambdaParameterList(Context context, List<String> varNames) {
		
		logEnter(context);
		
		final StackLambdaExpression stackLambdaExpression = get();
		
		stackLambdaExpression.setInferredParameterList(varNames);
		
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
		
		final StackLambdaExpression stackLambdaExpression = pop();

		final LambdaExpressionParameters parameters;

		if (stackLambdaExpression.getSingleParameter() != null) {
			parameters = new LambdaExpressionParameters(context, stackLambdaExpression.getSingleParameter());
		}
		else if (stackLambdaExpression.getInferredParameterList() != null) {
			parameters = new LambdaExpressionParameters(context, stackLambdaExpression.getInferredParameterList());
		}
		else {
			throw new UnsupportedOperationException();
		}

		final LambdaExpression lambdaExpression;
		
		final Expression expression = stackLambdaExpression.makeExpressionOrNull(context);
		
		if (expression != null) {
			lambdaExpression = new SingleLambdaExpression(context, parameters, expression);
		}
		else {
			lambdaExpression = new BlockLambdaExpression(
					context,
					parameters,
					new Block(context, stackLambdaExpression.getStatements()));
		}

		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(lambdaExpression);
		
		popVariableScope();
		
		logExit(context);
	}

	// Statements
	
	public final void onMutabilityVariableModifier(Context context, Mutability mutability) {
		
		logEnter(context);
		
		addVariableModifier(context, mutability);
		
		logExit(context);
	}

	private void addVariableModifier(Context context, VariableModifier modifier) {
		
		logEnter(context);
		
		Objects.requireNonNull(modifier);

		final VariableModifierSetter modifierSetter = get();

		modifierSetter.addModifier(new VariableModifierHolder(context, modifier));
		
		logExit(context);
	}
	
	public void onVariableDeclarationStatementStart(Context context) {
		
		logEnter(context);
		
		push(new StackVariableDeclarationList(logger));
		
		logExit(context);
	}
	
	public void onVariableDeclarationStatementEnd(Context context) {
		
		logEnter(context);
		
		final StackVariableDeclarationList variableDeclaration = pop();
		
		final StatementSetter statementSetter = get();
		
		final VariableModifiers modifiers = new VariableModifiers(context, variableDeclaration.getModifiers());
		
		final VariableDeclarationStatement statement = new VariableDeclarationStatement(
				context,
				modifiers, 
				variableDeclaration.getList());

		variableDeclaration.getList().forEach(e -> {
			variableScopes.get().add(e.getName().getName(), e.makeVariableDeclaration(modifiers));
		});
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}

	public void onVariableDeclaratorStart(Context context) {
		
		logEnter(context);
		
		push(new StackVariableDeclaration(logger));
		
		logExit(context);
	}
	
	public void onVariableDeclaratorEnd(Context context) {
		
		logEnter(context);
		
		final StackVariableDeclaration stackDeclaration = pop();
		
		final Expression initializer = stackDeclaration.makeExpressionOrNull(context);
		
		final BaseStackVariableDeclarationList declarationList = get();
		
		final InitializerVariableDeclarationElement variableDeclarationElement = new InitializerVariableDeclarationElement(
				context,
				declarationList.getTypeReference(),
				new VarName(stackDeclaration.getName()),
				stackDeclaration.getNumDims(),
				initializer);
		
		declarationList.add(variableDeclarationElement);
		
		logExit(context);
	}

	public final void onTypeReference(Context context, TypeReference typeReference) {

		logEnter(context);
		
		Objects.requireNonNull(typeReference);

		final TypeReferenceSetter typeReferenceSetter = get();

		typeReferenceSetter.setTypeReference(typeReference);
		
		logExit(context);
	}

	public final void onTypeReference(Context context, ScopedName name) {

		logEnter(context);
		
		Objects.requireNonNull(name);
	
		final TypeReferenceSetter typeReferenceSetter = get();

		typeReferenceSetter.setTypeReference(new ResolveLaterTypeReference(context, name));
		
		logExit(context);
	}

	public final void onExpressionStatementStart(Context context) {

		logEnter(context);
		
		push(new StackExpressionStatement(logger));
		
		logExit(context);
	}

	public final void onExpressionStatementEnd(Context context) {
		
		logEnter(context);
		
		final StackExpressionStatement statement = pop();
		
		final ExpressionStatement expressionStatement = new ExpressionStatement(context, statement.makeExpression(context));
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(expressionStatement);
		
		logExit(context);
	}
	
	public final void onForStatementStart(Context context) {
		
		logEnter(context);

		push(new StackForStatement(logger));
		
		logExit(context);
	}
	
	public final void onForInitStart(Context context) {
		
		logEnter(context);
		
		push(new StackForInit(logger));
		
		logExit(context);
	}
	
	public final void onForInitEnd(Context context) {
		
		logEnter(context);
		
		final StackForInit stackForInit = pop();
		
		final ForInit forInit;
		
		if (!stackForInit.getList().isEmpty()) {
			
			final InitializerVariableDeclarationElement element = stackForInit.getList().get(0);
			
			forInit = new ForInit(context, element);
		}
		else if (!stackForInit.getExpressions().isEmpty()) {
			forInit = new ForInit(context, new ForExpressionList(context, stackForInit.getExpressions()));
		}
		else {
			forInit = null;
		}
		
		if (forInit != null) {
			final StackForStatement forStatement = get();
			
			forStatement.setForInit(forInit);
		}
		
		
		logExit(context);
	}
	
	public final void onForUpdateStart(Context context) {

		logEnter(context);

		push(new StackForUpdate(logger));
		
		logExit(context);
	}
	
	public final void onForUpdateEnd(Context context) {

		logEnter(context);
		
		final StackForUpdate stackForUpdate = pop();
		
		if (!stackForUpdate.getList().isEmpty()) {
			final StackForStatement stackForStatement = get();
			
			final ForExpressionList forExpressionList = new ForExpressionList(
					context,
					stackForUpdate.getList());
			
			stackForStatement.setForUpdate(forExpressionList);
		}
		
		logExit(context);
	}
	
	public final void onForStatementEnd(Context context) {

		logEnter(context);
		
		final StackForStatement stackForStatement = pop();
		
		final ForStatement statement = new ForStatement(
				context,
				stackForStatement.getForInit(),
				stackForStatement.makeExpressionOrNull(context),
				stackForStatement.getForUpdate(),
				new Block(context, stackForStatement.getStatements()));
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}
	
	public final void onIteratorForStatementStart(Context context) {
		
		logEnter(context);

		push(new StackIteratorForStatement(logger));
		
		pushVariableScope();
		
		logExit(context);
	}
	
	public final void onIteratorForTestEnd(Context context) {

		logEnter(context);
		
		final StackIteratorForStatement stackIteratorForStatement = get();
		
		final VarName varName = new VarName(stackIteratorForStatement.getName());
		
		// Must add variable declarations to scope so that can be found further down in parsing
		final VariableDeclaration variableDeclaration = stackIteratorForStatement.makeVariableDeclaration(context);

		variableScopes.get().add(varName.getName(), variableDeclaration);
		
		logExit(context);
	}

	
	public final void onIteratorForStatementEnd(Context context) {

		logEnter(context);
		
		popVariableScope();
		
		final StackIteratorForStatement stackIteratorForStatement = pop();
		
		final ModifiersVariableDeclarationElement variableDeclarationElement = new ModifiersVariableDeclarationElement(
				context,
				new VariableModifiers(context, stackIteratorForStatement.getModifiers()),
				stackIteratorForStatement.getTypeReference(),
				new VarName(stackIteratorForStatement.getName()),
				stackIteratorForStatement.getNumDims()); 

		final IteratorForStatement statement = new IteratorForStatement(
				context,
				variableDeclarationElement,
				stackIteratorForStatement.getExpression(),
				new Block(context, stackIteratorForStatement.getStatements()));
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}
	
	public final void onWhileStatementStart(Context context) {
		
		logEnter(context);
		
		push(new StackWhileStatement(logger));
		
		logExit(context);
	}
	
	public final void onWhileStatementEnd(Context context) {
		
		logEnter(context);
		
		final StackWhileStatement stackWhileStatement = pop();
		
		final WhileStatement whileStatement = new WhileStatement(
				context,
				stackWhileStatement.getWhileCondition(context),
				new Block(context, stackWhileStatement.getStatements()));
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(whileStatement);
		
		logExit(context);
	}

	
	public final void onDoWhileStatementStart(Context context) {
		
		logEnter(context);
		
		push(new StackDoWhileStatement(logger));
		
		logExit(context);
	}
	
	public final void onDoWhileStatementEnd(Context context) {
		
		logEnter(context);
		
		final StackDoWhileStatement stackDoWhileStatement = pop();
		
		final DoWhileStatement doWhileStatement = new DoWhileStatement(
				context,
				stackDoWhileStatement.getWhileCondition(context),
				new Block(context, stackDoWhileStatement.getStatements()));
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(doWhileStatement);
		
		logExit(context);
	}
	
	public final void onTryWithResourcesStatementStart(Context context) {
		
		logEnter(context);
		
		push(new StackTryWithResourcesStatement(logger));
		
		pushVariableScope(); // for the variables in resources
		
		logExit(context);
	}
	
	public final void onTryWithResourcesSpecificationStart(Context context) {
		
		logEnter(context);
		
		push(new StackResourceList(logger));
		
		logExit(context);
	}
	
	public final void onResourceStart(Context context) {
		
		logEnter(context);
		
		push(new StackResource(logger));
		
		logExit(context);
	}

	public final void onVariableName(Context context, String name, int numDims) {
		
		logEnter(context);
		
		final VariableNameSetter variableNameSetter = get();

		variableNameSetter.init(name, numDims);
		
		logExit(context);
	}
	
	public final void onResourceEnd(Context context) {
		
		logEnter(context);
		
		final StackResource stackResource = pop();
		
		final Resource resource = new Resource(
				context,
				new VariableModifiers(context, stackResource.getModifiers()),
				stackResource.getTypeReference(),
				new VarName(stackResource.getName()),
				stackResource.getNumDims(),
				stackResource.getInitializer());
		
		variableScopes.get().add(resource.getName().getName(), resource.makeVariableDeclaration());
	
		final StackResourceList stackResourceList = get();
		
		stackResourceList.add(resource);
		
		logExit(context);
	}
	
	public final void onTryWithResourcesSpecificationEnd(Context context) {

		logEnter(context);
		
		final StackResourceList stackResourceList = pop();
		
		final StackTryWithResourcesStatement stackTryWithResourcesStatement = get();
		
		stackTryWithResourcesStatement.setResources(stackResourceList.getList());
		
		// Must push try-block to collect statements
		push(new StackTryBlock(logger));
		
		logExit(context);
	}
	
	public final void onTryStatementStart(Context context) {
		
		logEnter(context);
		
		push(new StackTryCatchFinallyStatement(logger));
		
		push(new StackTryBlock(logger));
		
		logExit(context);
	}
	
	public final void onTryBlockEnd(Context context) {
		
		logEnter(context);
		
		final StackTryBlock tryBlock = pop();
		
		final BaseStackTryCatchFinally baseStackTryCatchFinally = get();
		
		baseStackTryCatchFinally.setTryBlock(new Block(context, tryBlock.getList()));
		
		logExit(context);
	}

	public final void onCatchStart(Context context) {
		
		logEnter(context);
		
		push(new StackCatchBlock(logger));
		
		logExit(context);
	}
	
	public final void onCatchEnd(Context context) {
		
		logEnter(context);
		
		final StackCatchBlock stackCatchBlock = pop();
		
		final BaseStackTryCatchFinally baseStackTryCatchFinally = get();
		
		final CatchBlock catchBlock = new CatchBlock(
				context,
				stackCatchBlock.getExceptionTypes(),
				stackCatchBlock.getExceptionVarName(),
				new Block(context, stackCatchBlock.getList()));
		
		baseStackTryCatchFinally.addCatchBlock(catchBlock);
		
		logExit(context);
	}
	
	public final void onFinallyStart(Context context) {
		
		logEnter(context);
		
		push(new StackFinallyBlock(logger));
		
		logExit(context);
	}
	
	public final void onFinallyEnd(Context context) {
		
		logEnter(context);
		
		final StackFinallyBlock stackFinallyBlock = pop();
		
		final BaseStackTryCatchFinally baseStackTryCatchFinally = get();
		
		final Block finallyBlock = new Block(context, stackFinallyBlock.getList());
		
		baseStackTryCatchFinally.setFinallyBlock(finallyBlock);
		
		logExit(context);
	}
	
	public final void onTryStatementEnd(Context context) {
	
		logEnter(context);
		
		final StackTryCatchFinallyStatement stackTryCatchFinallyStatement = pop();
		
		final TryCatchFinallyStatement statement = new TryCatchFinallyStatement(
				context,
				stackTryCatchFinallyStatement.getTryBlock(),
				stackTryCatchFinallyStatement.getCatchBlocks(),
				stackTryCatchFinallyStatement.getFinallyBlock());
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}
	
	public final void onTryWithResourcesEnd(Context context) {
		
		logEnter(context);
		
		popVariableScope();
		
		final StackTryWithResourcesStatement stackTryWithResourcesStatement = pop();
		
		final TryWithResourcesStatement statement = new TryWithResourcesStatement(
				context,
				stackTryWithResourcesStatement.getResources(),
				stackTryWithResourcesStatement.getTryBlock(),
				stackTryWithResourcesStatement.getCatchBlocks(),
				stackTryWithResourcesStatement.getFinallyBlock());
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}
	
	public final void onReturnStatementStart(Context context) {
		
		logEnter(context);
		
		push(new StackExpressionList(logger));
		
		logExit(context);
	}

	public final void onReturnStatementEnd(Context context) {

		logEnter(context);
		
		final StackExpressionList stackExpression = pop();
		
		final Expression expression = stackExpression.makeExpressionOrNull(context);

		final ReturnStatement returnStatement = new ReturnStatement(context, expression);

		final StatementSetter statementSetter = get();

		statementSetter.addStatement(returnStatement);
		
		logExit(context);
	}

	public final void onThrowStatementStart(Context context) {
		
		logEnter(context);
		
		push(new StackThrowStatement(logger));
		
		logExit(context);
	}
	
	public final void onThrowStatementEnd(Context context) {
		
		logEnter(context);
		
		final StackThrowStatement stackThrowStatement = pop();
		
		final ThrowStatement throwStatement = new ThrowStatement(context, stackThrowStatement.getExpression());
	
		final StatementSetter statementSetter = get();

		statementSetter.addStatement(throwStatement);

		logExit(context);
	}
	
	public final void onAnnotationStart(Context context) {
		
		logEnter(context);

		push(new StackAnnotation(logger));
		
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
		
		final List<String> stack = mainStack.stream()
				.map(o -> o.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		if (logger != null) {
			logger.onStackPush(element.getClass().getSimpleName(), stack);
		}
		
		mainStack.push(element);
	}
	

	@SuppressWarnings("unchecked")
	protected final <T extends StackEntry> T pop() {
		final T result = (T)mainStack.pop();

		if (logger != null) {
			logger.onStackPop(result.getClass().getSimpleName());
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	protected final <T extends StackEntry> T get() {
		return (T)mainStack.get();
	}

	private <T extends StackEntry> T get(Class<T> cl) {
		return mainStack.getFromTop(cl);
	}

	protected final void pushVariableScope() {
		variableScopes.push(new VariablesMap());
	}

	protected final void popVariableScope() {
		variableScopes.pop();
	}
}
