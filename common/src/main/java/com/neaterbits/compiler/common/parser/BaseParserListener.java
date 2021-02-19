package com.neaterbits.compiler.common.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.TypeReferenceType;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationCodeLines;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.block.Constructor;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.block.ParameterName;
import com.neaterbits.compiler.common.ast.expression.AssignmentExpression;
import com.neaterbits.compiler.common.ast.expression.Base;
import com.neaterbits.compiler.common.ast.expression.BlockLambdaExpression;
import com.neaterbits.compiler.common.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.common.ast.expression.ConditionalExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.FieldAccess;
import com.neaterbits.compiler.common.ast.expression.LambdaExpression;
import com.neaterbits.compiler.common.ast.expression.LambdaExpressionParameters;
import com.neaterbits.compiler.common.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.common.ast.expression.NestedExpression;
import com.neaterbits.compiler.common.ast.expression.ParameterList;
import com.neaterbits.compiler.common.ast.expression.Resource;
import com.neaterbits.compiler.common.ast.expression.SingleLambdaExpression;
import com.neaterbits.compiler.common.ast.expression.ThisPrimary;
import com.neaterbits.compiler.common.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.common.ast.statement.CatchBlock;
import com.neaterbits.compiler.common.ast.statement.ExpressionStatement;
import com.neaterbits.compiler.common.ast.statement.FieldTransient;
import com.neaterbits.compiler.common.ast.statement.FieldVolatile;
import com.neaterbits.compiler.common.ast.statement.ForExpressionList;
import com.neaterbits.compiler.common.ast.statement.ForInit;
import com.neaterbits.compiler.common.ast.statement.ForStatement;
import com.neaterbits.compiler.common.ast.statement.IteratorForStatement;
import com.neaterbits.compiler.common.ast.statement.ReturnStatement;
import com.neaterbits.compiler.common.ast.statement.ThrowStatement;
import com.neaterbits.compiler.common.ast.statement.TryCatchFinallyStatement;
import com.neaterbits.compiler.common.ast.statement.TryWithResourcesStatement;
import com.neaterbits.compiler.common.ast.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.common.ast.statement.Mutability;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifier;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassStatic;
import com.neaterbits.compiler.common.ast.typedefinition.ClassStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorMember;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorModifier;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.FieldModifier;
import com.neaterbits.compiler.common.ast.typedefinition.FieldName;
import com.neaterbits.compiler.common.ast.typedefinition.FieldStatic;
import com.neaterbits.compiler.common.ast.typedefinition.FieldVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceAbstract;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodDefault;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodModifier;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodStatic;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceModifier;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceName;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceStatic;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodModifier;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodNative;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodSynchronized;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.ast.variables.SimpleVariableReference;
import com.neaterbits.compiler.common.ast.variables.VarName;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.common.ast.variables.ModifiersVariableDeclarationElement;
import com.neaterbits.compiler.common.ast.variables.NameReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.stackstate.BaseStackTryCatchFinally;
import com.neaterbits.compiler.common.parser.stackstate.BaseStackVariableDeclarationList;
import com.neaterbits.compiler.common.parser.stackstate.CallableStackEntry;
import com.neaterbits.compiler.common.parser.stackstate.StackAnnotation;
import com.neaterbits.compiler.common.parser.stackstate.StackAnonymousClass;
import com.neaterbits.compiler.common.parser.stackstate.StackAssignmentExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackAssignmentLHS;
import com.neaterbits.compiler.common.parser.stackstate.StackCatchBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackClass;
import com.neaterbits.compiler.common.parser.stackstate.StackClassInstanceCreationExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackNamedClass;
import com.neaterbits.compiler.common.parser.stackstate.StackCompilationUnit;
import com.neaterbits.compiler.common.parser.stackstate.StackConditionalExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackConstructor;
import com.neaterbits.compiler.common.parser.stackstate.StackExpressionList;
import com.neaterbits.compiler.common.parser.stackstate.StackExpressionStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackFieldDeclarationList;
import com.neaterbits.compiler.common.parser.stackstate.StackFinallyBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackForInit;
import com.neaterbits.compiler.common.parser.stackstate.StackForStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackForUpdate;
import com.neaterbits.compiler.common.parser.stackstate.StackInterface;
import com.neaterbits.compiler.common.parser.stackstate.StackInterfaceMethod;
import com.neaterbits.compiler.common.parser.stackstate.StackIteratorForStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackLambdaExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackLambdaFormalParameters;
import com.neaterbits.compiler.common.parser.stackstate.StackClassMethod;
import com.neaterbits.compiler.common.parser.stackstate.StackMethodInvocation;
import com.neaterbits.compiler.common.parser.stackstate.StackNamespace;
import com.neaterbits.compiler.common.parser.stackstate.StackParameter;
import com.neaterbits.compiler.common.parser.stackstate.StackParameterList;
import com.neaterbits.compiler.common.parser.stackstate.StackParameterSignature;
import com.neaterbits.compiler.common.parser.stackstate.StackPrimaryList;
import com.neaterbits.compiler.common.parser.stackstate.StackResource;
import com.neaterbits.compiler.common.parser.stackstate.StackReturnType;
import com.neaterbits.compiler.common.parser.stackstate.StackThrowStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackTryBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackTryCatchFinallyStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackTryWithResourcesStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackVariableDeclaration;
import com.neaterbits.compiler.common.parser.stackstate.StackVariableDeclarationList;
import com.neaterbits.compiler.common.parser.stackstate.VariableNameSetter;

public abstract class BaseParserListener {

	private final ParseLogger logger;
	
	// Stack for the main elements of a program
	private final ListStack mainStack;

	// Scope for variables
	private final Stack<VariablesMap> variableScopes;

	protected BaseParserListener(ParseLogger logger) {
		
		this.logger = logger;
		
		this.mainStack = new ListStack();
		
		this.variableScopes = new Stack<>();
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
		logger.onEnterListenerFunction(getMethodName(), context);
	}
	
	protected final void logExit(Context context) {
		logger.onExitListenerFunction(getMethodName(), context);
	}
	
	public final void onCompilationUnitStart() {
		
		if (!mainStack.isEmpty()) {
			throw new IllegalStateException("Expected empty stack");
		}
		
		push(new StackCompilationUnit(logger));
	}
	
	public CompilationUnit onCompilationUnitEnd(Context context) {
		
		final StackCompilationUnit stackCompilationUnit = pop();
		
		return new CompilationUnit(context, stackCompilationUnit.getImports(), stackCompilationUnit.getList());
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
		
		final StackNamespace stackNamespace = (StackNamespace)mainStack.pop();
		
		final List<CompilationCode> namespaceCode = stackNamespace.getList();

		final Namespace nameSpace = new Namespace(
				context,
				stackNamespace.getName(),
				stackNamespace.getParts(),
				new CompilationCodeLines(context, namespaceCode));
		
		mainStack.addElement(nameSpace);
		
		logExit(context);
	}
	
	public final void onClassStart(Context context, String name) {
		
		logEnter(context);
		
		push(new StackNamedClass(logger, name));
		
		logExit(context);
	}

	private void addClassModifier(Context context, ClassModifier modifier) {

		logEnter(context);

		final StackNamedClass stackClass = (StackNamedClass)mainStack.get();
		
		stackClass.addModifier(new ClassModifierHolder(context, modifier));
		
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
	
	public final void onClassEnd(Context context) {
		
		logEnter(context);
		
		final StackNamedClass entry = pop();
		
		final List<ComplexMemberDefinition> classCode = entry.getList();

		final ClassModifiers classModifiers = new ClassModifiers(context, entry.getModifiers());
		
		final ClassDefinition classDefinition = new ClassDefinition(context, classModifiers, new ClassName(entry.getName()), classCode);
		
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
		
		final ClassDefinition classDefinition = new ClassDefinition(context, null, null, classCode);
		
		mainStack.addElement(classDefinition);
		
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
		
		System.out.println("Set constructorname: " + constructorName);
		
		constructor.setName(constructorName);
		
		logExit(context);
	}

	public final void onConstructorEnd(Context context) {
		
		logEnter(context);

		popVariableScope();
		
		final StackConstructor stackConstructor = pop();
		
		final StackClass stackClass = get();
		
		final Constructor constructor = new Constructor(
				context,
				new ConstructorName(stackConstructor.getName()),
				stackConstructor.getParameters(),
				new Block(context, stackConstructor.getList()));

		
		final ConstructorMember constructorMember = new ConstructorMember(
				context,
				new ConstructorModifiers(context, stackConstructor.getModifiers()),
				constructor);
		
		stackClass.add(constructorMember);
		
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
		
		System.out.println("Set methodname: " + methodName);
		
		method.setName(methodName);
		
		logExit(context);
	}
	
	public final void onMethodSignatureParametersStart(Context context) {
		
	}

	public final void onMethodSignatureParameterStart(Context context) {
		
		logEnter(context);
		
		push(new StackParameterSignature(logger));
		
		logExit(context);
	}

	public final void onMethodSignatureParameterEnd(Context context) {
		
		logEnter(context);
		
		final StackParameterSignature stackParameterSignature = pop();
		
		final CallableStackEntry stackCallable = get();
		
		final Parameter parameter = new Parameter(
				context,
				stackParameterSignature.getTypeReference(),
				new ParameterName(stackParameterSignature.getName()));

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
				new ClassMethodModifiers(context, method.getModifiers()), method.makeMethod(context));

		methodMemberSetter.addMethod(methodMember);
		
		logExit(context);
	}
	
	public final void onFieldDeclarationStart(Context context) {
		
		logEnter(context);
		
		push(new StackFieldDeclarationList(logger));
		
		logExit(context);
	}
	
	private void addFieldModifier(FieldModifier modifier) {
		final StackFieldDeclarationList stackFieldDeclarationList = get();

		stackFieldDeclarationList.addFieldModifier(modifier);
	}
	
	public final void onVisibilityFieldModifier(Context context, FieldVisibility visibility) {
		
		logEnter(context);
		
		addFieldModifier(visibility);
		
		logExit(context);
	}
	
	public final void onStaticFieldModifier(Context context) {
		
		logEnter(context);
		
		addFieldModifier(new FieldStatic());
		
		logExit(context);
	}
	
	public final void onMutabilityFieldModifier(Context context, Mutability mutability) {
		
		logEnter(context);
		
		addFieldModifier(mutability);
		
		logExit(context);
	}
	
	public final void onTransientFieldModifier(Context context) {
		
		logEnter(context);
		
		addFieldModifier(new FieldTransient());
		
		logExit(context);
	}
	
	public final void onVolatileFieldModifier(Context context) {
		
		logEnter(context);
		
		addFieldModifier(new FieldVolatile());
		
		logExit(context);
	}
	
	public final void onFieldDeclarationEnd(Context context) {
		
		logEnter(context);
		
		final StackFieldDeclarationList stackFieldDeclarationList = pop();

		logExit(context);
	}

	public final void onInterfaceStart(Context context, String name) {
		
		logEnter(context);
		
		push(new StackInterface(logger, name));
		
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
	
	public final void onInterfaceEnd(Context context) {
		
		logEnter(context);
		
		final StackInterface entry = pop();
		
		final List<ComplexMemberDefinition> interfaceCode = entry.getList();

		final InterfaceModifiers interfaceModifiers = new InterfaceModifiers(context, entry.getModifiers());
		
		final InterfaceDefinition classDefinition = new InterfaceDefinition(context, interfaceModifiers, new InterfaceName(entry.getName()), interfaceCode);
		
		mainStack.addElement(classDefinition);
		
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
				new InterfaceMethodModifiers(context, method.getModifiers()), method.makeMethod(context));

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
	
	
	public final void onFieldAccess(Context context, FieldAccessType fieldAccessType, String typeName, String fieldName) {
		
		logEnter(context);
		
		final StackPrimaryList stackPrimaryList = get();
		
		final FieldAccess fieldAccess = new FieldAccess(
				context,
				fieldAccessType,
				typeName != null ? new ResolveLaterTypeReference(context, typeName) : null,
				new FieldName(fieldName));
		
		stackPrimaryList.add(fieldAccess);
		
		logExit(context);
	}
	
	public final void onThisPrimary(Context context) {
		
		logEnter(context);
		
		final StackPrimaryList stackPrimaryList = get();
		
		stackPrimaryList.add(new ThisPrimary(context));
		
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
	
	public final void onIntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new IntegerLiteral(context, value, base, signed, bits));
		
		logExit(context);
	}
	
	public final void onFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new FloatingPointLiteral(context, value, base, bits));
		
		logExit(context);
	}
	
	public final void onBooleanLiteral(Context context, boolean value) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new BooleanLiteral(context, value));
		
		logExit(context);
	}
	
	public final void onCharacterLiteral(Context context, char value) {
		
		logEnter(context);
		
		final PrimarySetter expressionSetter = get();
		
		expressionSetter.addPrimary(new CharacterLiteral(context, value));
		
		logExit(context);
	}
	
	public final void onStringLiteral(Context context, String value) {
		
		logEnter(context);
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(new StringLiteral(context, value));
		
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
	
	public final void onClassInstanceCreationTypeAndConstructorName(Context context, TypeReference type, List<String> name) {
		
		logEnter(context);
		
		final StackClassInstanceCreationExpression stackClassInstanceCreationExpression = get();
		
		stackClassInstanceCreationExpression.setType(type);
		stackClassInstanceCreationExpression.setConstructorName(new ConstructorName(name));
		
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
						context,
						classInstanceCreationExpression.getParameters() != null
							? classInstanceCreationExpression.getParameters()
							: Collections.emptyList()));
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(primary);
		
		logExit(context);
	}
	
	public final void onMethodInvocationStart(Context context, MethodInvocationType type, TypeReference classType, String methodName) {
		
		logEnter(context);
		
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
						context,
						stackMethodInvocation.getParameters() != null
							? stackMethodInvocation.getParameters()
							: Collections.emptyList()));
		
		final PrimarySetter primarySetter = get();
		
		primarySetter.addPrimary(methodInvocation);
		
		logExit(context);
	}
	
	public final void onLambdaExpressionStart(Context context) {
		
		logEnter(context);
		
		push(new StackLambdaExpression(logger));
		
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
			
			System.out.println("-- adding variable name " + e.getName().getName());
			
			
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

	public final void onTypeReference(Context context, TypeReferenceType typeReferenceType, String name) {

		logEnter(context);
		
		Objects.requireNonNull(typeReferenceType);
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
		
		System.out.println("-- adding resource name " + resource.getName().getName());
		
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
		
		logger.onStackPush(element.getClass().getSimpleName());
		
		mainStack.push(element);
	}
	

	@SuppressWarnings("unchecked")
	protected final <T extends StackEntry> T pop() {
		final T result = (T)mainStack.pop();

		logger.onStackPop(result.getClass().getSimpleName());

		return result;
	}

	@SuppressWarnings("unchecked")
	protected final <T extends StackEntry> T get() {
		return (T)mainStack.get();
	}

	private <T extends StackEntry> T get(Class<T> cl) {
		return mainStack.get(cl);
	}

	protected final void pushVariableScope() {
		variableScopes.push(new VariablesMap());
	}

	protected final void popVariableScope() {
		variableScopes.pop();
	}

	@SuppressWarnings("unchecked")
	private static <S, T extends S, C extends Collection<S>> List<T> cast(C collection) {
		return (List<T>)collection;
	}
}
