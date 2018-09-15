package com.neaterbits.compiler.common.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationCodeLines;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.expression.AssignmentExpression;
import com.neaterbits.compiler.common.ast.expression.Base;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
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
import com.neaterbits.compiler.common.ast.variables.VarName;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.ast.variables.VariableDeclarationElement;
import com.neaterbits.compiler.common.parser.stackstate.StackAnonymousClass;
import com.neaterbits.compiler.common.parser.stackstate.StackAssignmentExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackAssignmentLHS;
import com.neaterbits.compiler.common.parser.stackstate.StackClass;
import com.neaterbits.compiler.common.parser.stackstate.StackNamedClass;
import com.neaterbits.compiler.common.parser.stackstate.StackCompilationUnit;
import com.neaterbits.compiler.common.parser.stackstate.StackMethod;
import com.neaterbits.compiler.common.parser.stackstate.StackNamespace;
import com.neaterbits.compiler.common.parser.stackstate.StackVariableDeclaration;
import com.neaterbits.compiler.common.parser.stackstate.StackVariableDeclarationList;

public abstract class BaseParserListener {
	
	// Stack for the main elements of a program
	private final ListStack mainStack;
	
	protected BaseParserListener() {
		this.mainStack = new ListStack();
	}
	
	public final void onCompilationUnitStart() {
		
		if (!mainStack.isEmpty()) {
			throw new IllegalStateException("Expected empty stack");
		}
		
		push(new StackCompilationUnit());
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
		push(new StackNamespace(name, parts));
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
		push(new StackNamedClass(name));
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
		push(new StackAnonymousClass());
	}

	public final void onAnonymousClassEnd(Context context) {
		final StackAnonymousClass entry = pop();
		
		final List<ComplexMemberDefinition> classCode = entry.getList();
		
		final ClassDefinition classDefinition = new ClassDefinition(context, null, null, classCode);
		
		mainStack.addElement(classDefinition);
	}

	public final void onMethodStart(Context context) {
		final StackMethod method = new StackMethod();

		push(method);
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
		final StackMethod method = pop();

		final StackClass stackClass = get();
		
		stackClass.add(new MethodMember(context, new MethodModifiers(method.getModifiers()), method.makeMethod(context)));
	}
	
	
	// Expressions
	public final void onEnterAssignmentExpression(Context context) {
		
		push(new StackAssignmentExpression());
	}
	
	public final void onEnterAssignmentLHS(Context context) {
		push(new StackAssignmentLHS());
	}

	public final void onExitAssignmentLHS(Context context) {
		final StackAssignmentLHS assignmentLHS = pop();
		
		final StackAssignmentExpression assignmentExpression = get();
		
		assignmentExpression.setLHS(assignmentLHS.getVariableReference());
	}
	
	public final void onExitAssignmentExpression(Context context) {
		final StackAssignmentExpression stackAssignmentExpression = pop();

		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.add(new AssignmentExpression(
				context,
				stackAssignmentExpression.getLHS(),
				stackAssignmentExpression.getRHS()));
		
	}
	
	public final void onIntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.add(new IntegerLiteral(context, value, base, signed, bits));
	}
	
	public final void onFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.add(new FloatingPointLiteral(context, value, base, bits));
	}
	
	public final void onBooleanLiteral(Context context, boolean value) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.add(new BooleanLiteral(context, value));
	}
	
	public final void onCharacterLiteral(Context context, char value) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.add(new CharacterLiteral(context, value));
	}
	
	public final void onStringLiteral(Context context, String value) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.add(new StringLiteral(context, value));
	}
	
	public final void onNullLiteral(Context context) {
		final ExpressionSetter expressionSetter = get();
		
		expressionSetter.add(new NullLiteral(context));
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
		push(new StackVariableDeclarationList());
	}
	
	public void onVariableDeclarationStatementEnd(Context context) {
		final StackVariableDeclarationList variableDeclaration = pop();
		
		final StatementSetter statementSetter = get();
		
		final VariableDeclarationStatement statement = new VariableDeclarationStatement(
				context,
				new VariableModifiers(variableDeclaration.getModifiers()), 
				variableDeclaration.getList());
		
		statementSetter.addStatement(statement);
	}

	public void onVariableDeclaratorStart(Context context, String name, int numDims) {
		push(new StackVariableDeclaration(name, numDims));
	}
	
	public void onVariableDeclaratorEnd(Context context) {
		final StackVariableDeclaration stackDeclaration = pop();
		
		final Expression initializer = stackDeclaration.getExpression();
		
		final StackVariableDeclarationList declarationList = get();
		
		final VariableDeclaration variableDeclaration = new VariableDeclaration(
				declarationList.getTypeReference(),
				new VarName(stackDeclaration.getName()),
				stackDeclaration.getNumDims());
		
		declarationList.add(new VariableDeclarationElement(context, variableDeclaration, initializer));
	}

	public final void onTypeReference(Context context, TypeReference typeReference) {
		
		Objects.requireNonNull(typeReference);
		
		final TypeReferenceSetter typeReferenceSetter = get();
		
		typeReferenceSetter.setTypeReference(typeReference);
	}
	
	
	protected final void push(StackEntry element) {
		
		Objects.requireNonNull(element);
		
		System.out.println("## push " + element.getClass());
		
		mainStack.push(element);
	}
	

	@SuppressWarnings("unchecked")
	protected final <T extends StackEntry> T pop() {
		final T result = (T)mainStack.pop();
		
		System.out.println("## pop " + result.getClass());
		
		return result;
	}

	@SuppressWarnings("unchecked")
	protected final <T extends StackEntry> T get() {
		return (T)mainStack.get();
	}

	private <T extends StackEntry> T get(Class<T> cl) {
		return mainStack.get(cl);
	}
	
	@SuppressWarnings("unchecked")
	private static <S, T extends S, C extends Collection<S>> List<T> cast(C collection) {
		return (List<T>)collection;
	}
}
