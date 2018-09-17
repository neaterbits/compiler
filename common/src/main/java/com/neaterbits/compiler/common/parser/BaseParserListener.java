package com.neaterbits.compiler.common.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
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
import com.neaterbits.compiler.common.ast.expression.AssignmentExpression;
import com.neaterbits.compiler.common.ast.expression.Base;
import com.neaterbits.compiler.common.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ParameterList;
import com.neaterbits.compiler.common.ast.expression.VariableExpression;
import com.neaterbits.compiler.common.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.common.ast.statement.ExpressionStatement;
import com.neaterbits.compiler.common.ast.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.common.ast.statement.VariableMutability;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifier;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassStatic;
import com.neaterbits.compiler.common.ast.typedefinition.ClassStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.common.ast.typedefinition.MethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifier;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.MethodNative;
import com.neaterbits.compiler.common.ast.typedefinition.MethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.MethodStatic;
import com.neaterbits.compiler.common.ast.typedefinition.MethodStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.MethodSynchronized;
import com.neaterbits.compiler.common.ast.typedefinition.MethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.ast.variables.SimpleVariableReference;
import com.neaterbits.compiler.common.ast.variables.VarName;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.ast.variables.VariableDeclarationElement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.stackstate.StackAnonymousClass;
import com.neaterbits.compiler.common.parser.stackstate.StackAssignmentExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackAssignmentLHS;
import com.neaterbits.compiler.common.parser.stackstate.StackClass;
import com.neaterbits.compiler.common.parser.stackstate.StackClassInstanceCreationExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackNamedClass;
import com.neaterbits.compiler.common.parser.stackstate.StackCompilationUnit;
import com.neaterbits.compiler.common.parser.stackstate.StackExpressionStatement;
import com.neaterbits.compiler.common.parser.stackstate.StackMethod;
import com.neaterbits.compiler.common.parser.stackstate.StackNamespace;
import com.neaterbits.compiler.common.parser.stackstate.StackVariableDeclaration;
import com.neaterbits.compiler.common.parser.stackstate.StackVariableDeclarationList;

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

	private void addClassModifier(ClassModifier modifier) {
		final StackNamedClass stackClass = (StackNamedClass)mainStack.get();
		
		stackClass.addModifier(modifier);
	}
	
	public final void onVisibilityClassModifier(ClassVisibility visibility) {
		addClassModifier(visibility);
	}
	
	public final void onSubclassingModifier(Subclassing subclassing) {
		addClassModifier(subclassing);
	}
	
	public final void onStaticClassModifier() {
		addClassModifier(new ClassStatic());
	}

	public final void onStrictfpClassModifier() {
		addClassModifier(new ClassStrictfp());
	}
	
	public final void onClassEnd(Context context) {
		
		final StackNamedClass entry = pop();
		
		final List<ComplexMemberDefinition> classCode = entry.getList();

		final ClassModifiers classModifiers = new ClassModifiers(entry.getModifiers());
		
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
	
	public final void onMethodName(Context context, String methodName) {
		
		
		final StackMethod method = get();
		
		System.out.println("Set methodname: " + methodName);
		
		method.setName(methodName);
	}

	private void addMethodModifier(MethodModifier modifier) {
		final StackMethod stackMethod = get();
		
		stackMethod.addModifier(modifier);
	}
	
	public final void onVisibilityMethodModifier(MethodVisibility visibility) {
		addMethodModifier(visibility);
	}
	
	public final void onOverrideModifier(MethodOverride methodOverride) {
		addMethodModifier(methodOverride);
	}
	
	public final void onStaticMethodModifier() {
		addMethodModifier(new MethodStatic());
	}

	public final void onStrictfpMethodModifier() {
		addMethodModifier(new MethodStrictfp());
	}

	public final void onSynchronizedMethodModifier() {
		addMethodModifier(new MethodSynchronized());
	}

	public final void onNativeMethodModifier() {
		addMethodModifier(new MethodNative());
	}
	
	public final void onMethodEnd(Context context) {
		
		popVariableScope();
		
		final StackMethod method = pop();

		final StackClass stackClass = get();
		
		stackClass.add(new MethodMember(context, new MethodModifiers(method.getModifiers()), method.makeMethod(context)));
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

		final ExpressionSetter expressionSetter = get();

		final VariableDeclaration declaration = findVariableDeclaration(name);

		if (declaration == null) {
			throw new CompileException(context, "No variable declared for name " + name);
		}

		final SimpleVariableReference variableReference = new SimpleVariableReference(context, declaration);
		final VariableExpression variableExpression = new VariableExpression(context, variableReference);

		expressionSetter.addExpression(variableExpression);
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
				new ParameterList(classInstanceCreationExpression.getList()));
		
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.addExpression(expression);
	}

	// Statements
	
	public final void onMutabilityVariableModifier(VariableMutability mutability) {
		addVariableModifier(mutability);
	}

	private void addVariableModifier(VariableModifier modifier) {
		Objects.requireNonNull(modifier);

		final VariableModifierSetter modifierSetter = get();

		modifierSetter.addModifier(modifier);
	}
	
	public void onVariableDeclarationStatementStart(Context context) {
		push(new StackVariableDeclarationList(logger));
	}
	
	public void onVariableDeclarationStatementEnd(Context context) {
		final StackVariableDeclarationList variableDeclaration = pop();
		
		final StatementSetter statementSetter = get();
		
		final VariableModifiers modifiers = new VariableModifiers(variableDeclaration.getModifiers());
		
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

	public void onVariableDeclaratorStart(Context context, String name, int numDims) {
		push(new StackVariableDeclaration(logger, name, numDims));
	}
	
	public void onVariableDeclaratorEnd(Context context) {
		final StackVariableDeclaration stackDeclaration = pop();
		
		final Expression initializer = stackDeclaration.makeExpressionOrNull(context);
		
		final StackVariableDeclarationList declarationList = get();
		
		final VariableDeclarationElement variableDeclarationElement = new VariableDeclarationElement(
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
