package com.neaterbits.compiler.common.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationCodeLines;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.block.ParameterName;
import com.neaterbits.compiler.common.ast.expression.AssignmentExpression;
import com.neaterbits.compiler.common.ast.expression.Base;
import com.neaterbits.compiler.common.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.common.ast.expression.ParameterList;
import com.neaterbits.compiler.common.ast.expression.Resource;
import com.neaterbits.compiler.common.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.common.ast.statement.CatchBlock;
import com.neaterbits.compiler.common.ast.statement.ExpressionStatement;
import com.neaterbits.compiler.common.ast.statement.IteratorForStatement;
import com.neaterbits.compiler.common.ast.statement.ReturnStatement;
import com.neaterbits.compiler.common.ast.statement.TryWithResourcesStatement;
import com.neaterbits.compiler.common.ast.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.common.ast.statement.VariableMutability;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifier;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassStatic;
import com.neaterbits.compiler.common.ast.typedefinition.ClassStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.common.ast.typedefinition.MethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifier;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.MethodNative;
import com.neaterbits.compiler.common.ast.typedefinition.MethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.MethodStatic;
import com.neaterbits.compiler.common.ast.typedefinition.MethodStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.MethodSynchronized;
import com.neaterbits.compiler.common.ast.typedefinition.MethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.ast.variables.SimpleVariableReference;
import com.neaterbits.compiler.common.ast.variables.VarName;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.common.ast.variables.ModifiersVariableDeclarationElement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.stackstate.BaseStackTryCatchFinally;
import com.neaterbits.compiler.common.parser.stackstate.CallableStackEntry;
import com.neaterbits.compiler.common.parser.stackstate.StackAnonymousClass;
import com.neaterbits.compiler.common.parser.stackstate.StackAssignmentExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackAssignmentLHS;
import com.neaterbits.compiler.common.parser.stackstate.StackCatchBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackClassInstanceCreationExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackNamedClass;
import com.neaterbits.compiler.common.parser.stackstate.StackCompilationUnit;
import com.neaterbits.compiler.common.parser.stackstate.StackExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackExpressionStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackFinallyBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackIteratorForStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackMethod;
import com.neaterbits.compiler.common.parser.stackstate.StackMethodInvocation;
import com.neaterbits.compiler.common.parser.stackstate.StackNamespace;
import com.neaterbits.compiler.common.parser.stackstate.StackParameterList;
import com.neaterbits.compiler.common.parser.stackstate.StackParameterSignature;
import com.neaterbits.compiler.common.parser.stackstate.StackResource;
import com.neaterbits.compiler.common.parser.stackstate.StackReturnType;
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
		push(new StackNamespace(logger, name, parts));
	}
	
	public final void onNameSpaceEnd(Context context) {
		
		final StackNamespace stackNamespace = (StackNamespace)mainStack.pop();
		
		final List<CompilationCode> namespaceCode = stackNamespace.getList();

		final Namespace nameSpace = new Namespace(
				context,
				stackNamespace.getName(),
				stackNamespace.getParts(),
				new CompilationCodeLines(context, namespaceCode));
		
		mainStack.addElement(nameSpace);
	}
	
	public final void onClassStart(String name) {
		push(new StackNamedClass(logger, name));
	}

	private void addClassModifier(Context context, ClassModifier modifier) {
		final StackNamedClass stackClass = (StackNamedClass)mainStack.get();
		
		stackClass.addModifier(new ClassModifierHolder(context, modifier));
	}
	
	public final void onVisibilityClassModifier(Context context, ClassVisibility visibility) {
		addClassModifier(context, visibility);
	}
	
	public final void onSubclassingModifier(Context context, Subclassing subclassing) {
		addClassModifier(context, subclassing);
	}
	
	public final void onStaticClassModifier(Context context) {
		addClassModifier(context, new ClassStatic());
	}

	public final void onStrictfpClassModifier(Context context) {
		addClassModifier(context, new ClassStrictfp());
	}
	
	public final void onClassEnd(Context context) {
		
		final StackNamedClass entry = pop();
		
		final List<ComplexMemberDefinition> classCode = entry.getList();

		final ClassModifiers classModifiers = new ClassModifiers(context, entry.getModifiers());
		
		final ClassDefinition classDefinition = new ClassDefinition(context, classModifiers, new ClassName(entry.getName()), classCode);
		
		mainStack.addElement(classDefinition);
	}

	public final void onAnonymousClassStart(Context context) {
		push(new StackAnonymousClass(logger));
	}

	public final void onAnonymousClassEnd(Context context) {
		final StackAnonymousClass entry = pop();
		
		final List<ComplexMemberDefinition> classCode = entry.getList();
		
		final ClassDefinition classDefinition = new ClassDefinition(context, null, null, classCode);
		
		mainStack.addElement(classDefinition);
	}

	public final void onMethodStart(Context context) {
		final StackMethod method = new StackMethod(logger);

		push(method);
		
		pushVariableScope();
	}
	
	public final void onMethodReturnTypeStart(Context context) {
		push(new StackReturnType(logger));
	}
	
	public final void onMethodReturnTypeEnd(Context context) {
		final StackReturnType stackReturnType = pop();
		
		final StackMethod stackMethod = get();
		
		stackMethod.setReturnType(stackReturnType.getType());
	}
	
	public final void onMethodName(Context context, String methodName) {
		
		
		final StackMethod method = get();
		
		System.out.println("Set methodname: " + methodName);
		
		method.setName(methodName);
	}
	
	public final void onMethodSignatureParametersStart(Context context) {
		
	}

	public final void onMethodSignatureParameterStart(Context context) {
		push(new StackParameterSignature(logger));
	}

	public final void onMethodSignatureParameterEnd(Context context) {
		final StackParameterSignature stackParameterSignature = pop();
		
		final CallableStackEntry stackCallable = get();
		
		final Parameter parameter = new Parameter(
				context,
				stackParameterSignature.getTypeReference(),
				new ParameterName(stackParameterSignature.getName()));

		stackCallable.addParameter(parameter);
		
		final VariableDeclaration variableDeclaration = stackParameterSignature.makeVariableDeclaration(context);

		variableScopes.get().add(stackParameterSignature.getName(), variableDeclaration);
	}
	
	public final void onMethodSignatureParametersEnd(Context context) {
		
	}

	private void addMethodModifier(Context context, MethodModifier modifier) {
		final StackMethod stackMethod = get();
		
		
		stackMethod.addModifier(new MethodModifierHolder(context, modifier));
	}
	
	public final void onVisibilityMethodModifier(Context context, MethodVisibility visibility) {
		addMethodModifier(context, visibility);
	}
	
	public final void onOverrideModifier(Context context, MethodOverride methodOverride) {
		addMethodModifier(context, methodOverride);
	}
	
	public final void onStaticMethodModifier(Context context) {
		addMethodModifier(context, new MethodStatic());
	}

	public final void onStrictfpMethodModifier(Context context) {
		addMethodModifier(context, new MethodStrictfp());
	}

	public final void onSynchronizedMethodModifier(Context context) {
		addMethodModifier(context, new MethodSynchronized());
	}

	public final void onNativeMethodModifier(Context context) {
		addMethodModifier(context, new MethodNative());
	}
	
	public final void onMethodEnd(Context context) {
		
		popVariableScope();
		
		final StackMethod method = pop();

		final MethodMemberSetter methodMemberSetter = get();
		
		final MethodMember methodMember = new MethodMember(
				context,
				new MethodModifiers(context, method.getModifiers()), method.makeMethod(context));

		methodMemberSetter.addMethod(methodMember);
	}
	
	
	// Expressions
	public final void onEnterAssignmentExpression(Context context) {
		
		push(new StackAssignmentExpression(logger));
	}
	
	public final void onEnterAssignmentLHS(Context context) {
		push(new StackAssignmentLHS(logger));
	}

	public final void onExitAssignmentLHS(Context context) {
		final StackAssignmentLHS assignmentLHS = pop();
		
		final StackAssignmentExpression assignmentExpression = get();
		
		assignmentExpression.setLHS(assignmentLHS.getVariableReference());
	}
	
	public final void onExitAssignmentExpression(Context context) {
		final StackAssignmentExpression stackAssignmentExpression = pop();

		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(new AssignmentExpression(
				context,
				stackAssignmentExpression.getLHS(),
				stackAssignmentExpression.getRHS()));
	}

	public void onVariableReference(Context context, String name) {

		final VariableReferenceSetter variableReferenceSetter = get();

		final VariableDeclaration declaration = findVariableDeclaration(name);

		if (declaration == null) {
			throw new CompileException(context, "No variable declared for name " + name);
		}

		final SimpleVariableReference variableReference = new SimpleVariableReference(context, declaration);

		variableReferenceSetter.setVariableReference(variableReference);
	}
	
	public final void onIntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(new IntegerLiteral(context, value, base, signed, bits));
	}
	
	public final void onFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(new FloatingPointLiteral(context, value, base, bits));
	}
	
	public final void onBooleanLiteral(Context context, boolean value) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(new BooleanLiteral(context, value));
	}
	
	public final void onCharacterLiteral(Context context, char value) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(new CharacterLiteral(context, value));
	}
	
	public final void onStringLiteral(Context context, String value) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(new StringLiteral(context, value));
	}
	
	public final void onNullLiteral(Context context) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(new NullLiteral(context));
	}
	
	public final void onClassInstanceCreationExpressionStart(Context context) {
		push(new StackClassInstanceCreationExpression(logger));
	}
	
	public final void onClassInstanceCreationTypeAndConstructorName(Context context, TypeReference type, List<String> name) {
		
		final StackClassInstanceCreationExpression stackClassInstanceCreationExpression = get();
		
		stackClassInstanceCreationExpression.setType(type);
		stackClassInstanceCreationExpression.setConstructorName(new ConstructorName(name));
	}
	
	public final void onClassInstanceCreationExpressionEnd(Context context) {
		final StackClassInstanceCreationExpression classInstanceCreationExpression = pop();
		
		final ClassInstanceCreationExpression expression = new ClassInstanceCreationExpression(
				context,
				classInstanceCreationExpression.getType(),
				classInstanceCreationExpression.getConstructorName(),
				new ParameterList(
						context,
						classInstanceCreationExpression.getParameters() != null
							? classInstanceCreationExpression.getParameters()
							: Collections.emptyList()));
		
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(expression);
	}
	
	public final void onMethodInvocationStart(Context context, MethodInvocationType type, TypeReference classType, String methodName) {
		push(new StackMethodInvocation(logger, type, classType, methodName));
	}
	
	public final void onParametersStart(Context context) {
		push(new StackParameterList(logger));
	}
	
	public final void onParametersEnd(Context context) {

		final StackParameterList stackParameterList = pop();

		final ParametersSetter parametersSetter = get();

		parametersSetter.setParameters(stackParameterList.getList());
	}
	
	public final void onMethodInvocationEnd(Context context) {

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
		
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(methodInvocation);
	}

	// Statements
	
	public final void onMutabilityVariableModifier(Context context, VariableMutability mutability) {
		addVariableModifier(context, mutability);
	}

	private void addVariableModifier(Context context, VariableModifier modifier) {
		Objects.requireNonNull(modifier);

		final VariableModifierSetter modifierSetter = get();

		modifierSetter.addModifier(new VariableModifierHolder(context, modifier));
	}
	
	public void onVariableDeclarationStatementStart(Context context) {
		push(new StackVariableDeclarationList(logger));
	}
	
	public void onVariableDeclarationStatementEnd(Context context) {
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
	}

	public void onVariableDeclaratorStart(Context contex) {
		push(new StackVariableDeclaration(logger));
	}
	
	public void onVariableDeclaratorEnd(Context context) {
		final StackVariableDeclaration stackDeclaration = pop();
		
		final Expression initializer = stackDeclaration.makeExpressionOrNull(context);
		
		final StackVariableDeclarationList declarationList = get();
		
		final InitializerVariableDeclarationElement variableDeclarationElement = new InitializerVariableDeclarationElement(
				context,
				declarationList.getTypeReference(),
				new VarName(stackDeclaration.getName()),
				stackDeclaration.getNumDims(),
				initializer);
		
		declarationList.add(variableDeclarationElement);
	}

	public final void onTypeReference(Context context, TypeReference typeReference) {

		Objects.requireNonNull(typeReference);

		final TypeReferenceSetter typeReferenceSetter = get();

		typeReferenceSetter.setTypeReference(typeReference);
	}

	public final void onExpressionStatementStart(Context context) {

		push(new StackExpressionStatement(logger));
	}

	public final void onExpressionStatementEnd(Context context) {
		
		final StackExpressionStatement statement = pop();
		
		final ExpressionStatement expressionStatement = new ExpressionStatement(context, statement.makeExpression(context));
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(expressionStatement);
	}
	
	public final void onIteratorForStatementStart(Context context) {

		push(new StackIteratorForStatement(logger));
		
		pushVariableScope();
	}
	
	public final void onIteratorForTestEnd(Context context) {
		
		final StackIteratorForStatement stackIteratorForStatement = get();
		
		final VarName varName = new VarName(stackIteratorForStatement.getName());
		
		// Must add variable declarations to scope so that can be found further down in parsing
		final VariableDeclaration variableDeclaration = stackIteratorForStatement.makeVariableDeclaration(context);

		System.out.println("## add to scope: " + varName);
		
		variableScopes.get().add(varName.getName(), variableDeclaration);
	}

	
	public final void onIteratorForStatementEnd(Context context) {
		
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
	}
	
	public final void onTryWithResourcesStatementStart(Context context) {
		
		push(new StackTryWithResourcesStatement(logger));
		
		pushVariableScope(); // for the variables in resources
	}
	
	public final void onTryWithResourcesSpecificationStart(Context context) {
		push(new StackResourceList(logger));
	}
	
	public final void onResourceStart(Context context) {
		push(new StackResource(logger));
	}

	public final void onVariableName(Context context, String name, int numDims) {
		final VariableNameSetter variableNameSetter = get();

		variableNameSetter.init(name, numDims);
	}
	
	public final void onResourceEnd(Context context) {
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
	}
	
	public final void onTryWithResourcesSpecificationEnd(Context context) {

		final StackResourceList stackResourceList = pop();
		
		final StackTryWithResourcesStatement stackTryWithResourcesStatement = get();
		
		stackTryWithResourcesStatement.setResources(stackResourceList.getList());
		
		// Must push try-block to collect statements
		push(new StackTryBlock(logger));
	}
	
	public final void onTryStatementStart(Context context) {
		push(new StackTryCatchFinallyStatement(logger));
	}
	
	public final void onTryBlockEnd(Context context) {
		final StackTryBlock tryBlock = pop();
		
		final BaseStackTryCatchFinally baseStackTryCatchFinally = get();
		
		baseStackTryCatchFinally.setTryBlock(new Block(context, tryBlock.getList()));
	}

	public final void onCatchStart(Context context) {
		push(new StackCatchBlock(logger));
	}
	
	public final void onCatchEnd(Context context) {
		final StackCatchBlock stackCatchBlock = pop();
		
		final BaseStackTryCatchFinally baseStackTryCatchFinally = get();
		
		final CatchBlock catchBlock = new CatchBlock(
				context,
				stackCatchBlock.getExceptionTypes(),
				stackCatchBlock.getExceptionVarName(),
				new Block(context, stackCatchBlock.getList()));
		
		baseStackTryCatchFinally.addCatchBlock(catchBlock);
	}
	
	public final void onFinallyStart(Context context) {
		push(new StackFinallyBlock(logger));
	}
	
	public final void onFinallyEnd(Context context) {
		final StackFinallyBlock stackFinallyBlock = pop();
		
		final BaseStackTryCatchFinally baseStackTryCatchFinally = get();
		
		final Block finallyBlock = new Block(context, stackFinallyBlock.getList());
		
		baseStackTryCatchFinally.setFinallyBlock(finallyBlock);
	}
	
	public final void onTryWithResourcesEnd(Context context) {
		
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
	}
	
	public final void onReturnStatementStart(Context context) {
		push(new StackExpression(logger));
	}

	public final void onReturnStatementEnd(Context context) {

		final StackExpression stackExpression = pop();

		final ReturnStatement returnStatement = new ReturnStatement(context, stackExpression.getExpression());

		final StatementSetter statementSetter = get();

		statementSetter.addStatement(returnStatement);
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
