package com.neaterbits.compiler.java.parser;

import java.math.BigInteger;
import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.ResolvedTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.TypeReferenceType;
import com.neaterbits.compiler.common.antlr4.ModelParserListener;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.ast.operator.Arithmetic;
import com.neaterbits.compiler.common.ast.operator.Notation;
import com.neaterbits.compiler.common.ast.operator.Operator;
import com.neaterbits.compiler.common.ast.statement.Mutability;
import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.common.ast.type.primitive.ByteType;
import com.neaterbits.compiler.common.ast.type.primitive.Char16Type;
import com.neaterbits.compiler.common.ast.type.primitive.DoubleType;
import com.neaterbits.compiler.common.ast.type.primitive.FloatType;
import com.neaterbits.compiler.common.ast.type.primitive.IntType;
import com.neaterbits.compiler.common.ast.type.primitive.LongType;
import com.neaterbits.compiler.common.ast.type.primitive.ScalarType;
import com.neaterbits.compiler.common.ast.type.primitive.ShortType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.FieldVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.FieldAccessType;
import com.neaterbits.compiler.common.parser.MethodInvocationType;
import com.neaterbits.compiler.common.parser.iterative.BaseIterativeOOParserListener;
import com.neaterbits.compiler.common.util.Strings;

/**
 * Listener for the Java grammars
 */

public class JavaParserListener implements ModelParserListener<CompilationUnit> {

	// Delegate to make sure make all special handling here
	private static class JavaIterativeListener extends BaseIterativeOOParserListener {

		JavaIterativeListener(ParseLogger logger) {
			super(logger);
		}
	}

	// Cache packagename
	private String packageName;

	private CompilationUnit compilationUnit;

	// Workaround for Java grammar oddities where 'else if' is specified like
	// ifThenElseStatement [ ifThenElseStatement ]
	// so we need to detect whether an if directly follows an else and merge those into
	// the if-then-else structures made by this framework
	// before we pass them to the delegate

	// We use delegation instead of subclassing to make sure that we handle all methods in this class

	private final JavaIterativeListener delegate;
	private final StatementsStack statementsStack; 

	private void printStack(String statement) {
		System.out.println("stack at " + statement + " " + statementsStack);
	}
	
	public JavaParserListener(ParseLogger logger) {
		this.delegate = new JavaIterativeListener(logger);
		this.statementsStack = new StatementsStack();
	}

	@Override
	public CompilationUnit getResult() {
		return compilationUnit;
	}

	public void onPackageDeclaration(Context context, String name) {
		this.packageName = name;
		
		delegate.onNamespaceStart(context, name, Strings.split(packageName, '.'));
	}

	public void onCompilationUnitStart(Context context) {
		delegate.onCompilationUnitStart(context);
	}
	
	public void onImport(Import importStatement) {
		delegate.onImport(importStatement);
	}
	
	public void onClassStart(Context context, String name) {
		delegate.onClassStart(context, name);
	}
	
	public void onVisibilityClassModifier(Context context, ClassVisibility visibility) {
		delegate.onVisibilityClassModifier(context, visibility);
	}
	
	public void onSubclassingModifier(Context context, Subclassing subclassing) {
		delegate.onSubclassingModifier(context, subclassing);
	}
	
	public void onStaticClassModifier(Context context) {
		delegate.onStaticClassModifier(context);
	}
	
	public void onStrictfpClassModifier(Context context) {
		delegate.onStrictfpClassModifier(context);
	}
	
	public void onClassEnd(Context context) {
		delegate.onClassEnd(context);
	}
	
	public void onAnonymousClassStart(Context context) {
		delegate.onAnonymousClassStart(context);
	}
	
	public void onAnonymousClassEnd(Context context) {
		delegate.onAnonymousClassEnd(context);
	}
	
	public void onConstructorStart(Context context) {
		
		statementsStack.push();
		
		delegate.onConstructorStart(context);
	}

	public void onConstructorVisibilityModifier(Context context, ConstructorVisibility visibility) {
		delegate.onConstructorVisibilityModifier(context, visibility);
	}
	
	public void onConstructorName(Context context, String constructorName) {
		delegate.onConstructorName(context, constructorName);
	}

	public void onConstructorEnd(Context context) {
		delegate.onConstructorEnd(context);
		
		statementsStack.pop();
	}

	public void onClassMethodStart(Context context) {
		
		statementsStack.push();
		
		delegate.onClassMethodStart(context);
	}
	
	public void onMethodReturnTypeStart(Context context) {
		delegate.onMethodReturnTypeStart(context);
	}
	
	public void onMethodReturnTypeEnd(Context context) {
		delegate.onMethodReturnTypeEnd(context);
	}

	public void onMethodName(Context context, String methodName) {
		delegate.onMethodName(context, methodName);
	}

	public void onMethodSignatureParametersStart(Context context) {
		delegate.onMethodSignatureParametersStart(context);
	}

	public void onMethodSignatureParameterStart(Context context) {
		delegate.onMethodSignatureParameterStart(context);
	}

	public void onMethodSignatureParameterEnd(Context context) {
		delegate.onMethodSignatureParameterEnd(context);
	}

	public void onMethodSignatureParametersEnd(Context context) {
		delegate.onMethodSignatureParametersEnd(context);
	}

	public void onVisibilityClassMethodModifier(Context context, ClassMethodVisibility visibility) {
		delegate.onVisibilityClassMethodModifier(context, visibility);
	}
	
	public void onOverrideClassMehodModifier(Context context, ClassMethodOverride methodOverride) {
		delegate.onOverrideClassMethodModifier(context, methodOverride);
	}
	
	public void onStaticClassMethodModifier(Context context) {
		delegate.onStaticClassMethodModifier(context);
	}

	public void onStrictfpClassMethodModifier(Context context) {
		delegate.onStrictfpClassMethodModifier(context);
	}
	
	public void onSynchronizedClassMethodModifier(Context context) {
		delegate.onSynchronizedClassMethodModifier(context);
	}

	public void onNativeClassMethodModifier(Context context) {
		delegate.onNativeClassMethodModifier(context);
	}
	
	public void onClassMethodEnd(Context context) {
		delegate.onClassMethodEnd(context);
		
		statementsStack.pop();
	}

	public void onInterfaceStart(Context context, String name) {
		delegate.onInterfaceStart(context, name);
	}
	
	public void onVisibilityInterfaceModifier(Context context, InterfaceVisibility visibility) {
		delegate.onVisibilityInterfaceModifier(context, visibility);
	}

	public void onAbstractInterfaceModifier(Context context) {
		delegate.onStaticInterfaceModifier(context);
	}
	
	public void onStaticInterfaceModifier(Context context) {
		delegate.onStaticInterfaceModifier(context);
	}
	
	public void onStrictfpInterfaceModifier(Context context) {
		delegate.onStrictfpInterfaceModifier(context);
	}
	
	public void onInterfaceEnd(Context context) {
		delegate.onInterfaceEnd(context);
	}

	public void onInterfaceMethodStart(Context context) {
		
		statementsStack.push();
		
		delegate.onInterfaceMethodStart(context);
	}

	public void onVisibilityInterfaceMethodModifier(Context context, InterfaceMethodVisibility visibility) {
		delegate.onVisibilityInterfaceMethodModifier(context, visibility);
	}
	
	public void onAbstractInterfaceMethodModifier(Context context) {
		delegate.onAbstractInterfaceMethodModifier(context);
	}
	
	public void onDefaultInterfaceMethodModifier(Context context) {
		delegate.onDefaultInterfaceMethodModifier(context);
	}
	
	public void onStaticInterfaceMethodModifier(Context context) {
		delegate.onStaticInterfaceMethodModifier(context);
	}

	public void onStrictfpInterfaceMethodModifier(Context context) {
		delegate.onStrictfpInterfaceMethodModifier(context);
	}
	
	public void onInterfaceMethodEnd(Context context) {
		delegate.onInterfaceMethodEnd(context);
		
		statementsStack.pop();
	}
	
	public final void onFieldDeclarationStart(Context context) {
		delegate.onFieldDeclarationStart(context);
	}

	public void onVisibilityFieldModifier(Context context, FieldVisibility visibility) {
		delegate.onVisibilityFieldModifier(context, visibility);
	}
	
	public void onStaticFieldModifier(Context context) {
		delegate.onStaticFieldModifier(context);
	}
	
	public void onMutabilityFieldModifier(Context context, Mutability mutability) {
		delegate.onMutabilityFieldModifier(context, mutability);
	}
	
	public void onTransientFieldModifier(Context context) {
		delegate.onTransientFieldModifier(context);
	}
	
	public void onVolatileFieldModifier(Context context) {
		delegate.onVolatileFieldModifier(context);
	}

	public final void onFieldDeclarationEnd(Context context) {
		delegate.onFieldDeclarationEnd(context);
	}

	
	public void onNestedExpressionStart(Context context) {
		delegate.onNestedExpressionStart(context);
	}
	
	public void onNestedExpressionEnd(Context context) {
		delegate.onNestedExpressionEnd(context);
	}

	public void onEnterAssignmentExpression(Context context) {
		delegate.onEnterAssignmentExpression(context);
	}
	
	public void onNameReference(Context context, String name) {
		delegate.onNameReference(context, name);
	}

	public void onVariableReference(Context context, String name) {
		delegate.onVariableReference(context, name);
	}

	public void onEnterAssignmentLHS(Context context) {
		delegate.onEnterAssignmentLHS(context);
	}
	
	public void onExitAssignmentLHS(Context context) {
		delegate.onExitAssignmentLHS(context);
	}
	
	public void onExitAssignmentExpression(Context context) {
		delegate.onExitAssignmentExpression(context);
	}

	public void onExpressionBinaryOperator(Context context, Operator operator) {
		delegate.onExpressionBinaryOperator(context, operator);
	}

	public void onIncrementDecrementExpressionStart(Context context, Arithmetic operator, Notation notation) {
		delegate.onIncrementDecrementExpressionStart(context, operator, notation);
	}

	public void onIncrementDecrementExpressionEnd(Context context) {
		delegate.onIncrementDecrementExpressionEnd(context);
	}
	
	// Primary
	public void onPrimaryStart(Context context) {
		delegate.onPrimaryStart(context);
	}

	public void onFieldAccess(Context context, FieldAccessType fieldAccessType, String typeName, String fieldName) {
		delegate.onFieldAccess(context, fieldAccessType, typeName, fieldName);
	}

	public void onThisPrimary(Context context) {
		delegate.onThisPrimary(context);
	}

	public void onPrimaryEnd(Context context) {
		delegate.onPrimaryEnd(context);
	}

	public void onConditionalExpressionStart(Context context) {
		delegate.onConditionalExpressionStart(context);
	}
	
	public void onConditionalExpressionPart1Start(Context context) {
		delegate.onConditionalExpressionPart1Start(context);
	}
	
	public void onConditionalExpressionPart1End(Context context) {
		delegate.onConditionalExpressionPart1End(context);
	}

	public void onConditionalExpressionPart2Start(Context context) {
		delegate.onConditionalExpressionPart2Start(context);
	}
	
	public void onConditionalExpressionPart2End(Context context) {
		delegate.onConditionalExpressionPart2End(context);
	}

	public void onConditionalExpressionPart3Start(Context context) {
		delegate.onConditionalExpressionPart3Start(context);
	}
	
	public void onConditionalExpressionPart3End(Context context) {
		delegate.onConditionalExpressionPart3End(context);
	}
	
	public void onConditionalExpressionEnd(Context context) {
		delegate.onConditionalExpressionEnd(context);
	}
	
	// Literals
	public void onJavaIntegerLiteral(Context context, String literal) {

		final JavaInteger javaInteger = JavaParserUtil.parseIntegerLiteral(literal);
		
		System.out.println("--- integer literal " + javaInteger);

		delegate.onIntegerLiteral(context, BigInteger.valueOf(javaInteger.getValue()), javaInteger.getBase(), true, javaInteger.getBits());
	}

	public void onJavaFloatingPointLiteral(Context context, String literal) {
		throw new UnsupportedOperationException();
	}

	public void onJavaBooleanLiteral(Context context, String literal) {

		final boolean value;

		switch (literal) {
		case "true":
			value = true;
			break;

		case "false":
			value = false;
			break;

		default:
			throw new IllegalStateException("Not a boolean literal: " + literal);
		}

		delegate.onBooleanLiteral(context, value);
	}
	
	public void onJavaCharacterLiteral(Context context, String literal) {
		
		if (literal.length() < 3) {
			throw new IllegalStateException("Not a Character literal");
		}

		final String s = literal.substring(1, literal.length() - 1);
		
		delegate.onCharacterLiteral(context, s.charAt(0));
	}
	
	public void onJavaStringLiteral(Context context, String literal) {
		if (literal.length() < 2) {
			throw new IllegalStateException("Not a String literal");
		}

		delegate.onStringLiteral(context, literal.substring(1, literal.length() - 1));
	}
	
	public void onJavaNullLiteral(Context context, String literal) {
		
		if (!literal.equals("null")) {
			throw new IllegalStateException("not value null");
		}
		
		delegate.onNullLiteral(context);
	}
	
	public void onJavaClassInstanceCreationExpressionStart(Context context) {
		delegate.onClassInstanceCreationExpressionStart(context);
	}

	public void onJavaClassInstanceCreationConstructorName(Context context, List<String> name) {

		delegate.onClassInstanceCreationTypeAndConstructorName(
				context,
				new ResolveLaterTypeReference(context, name),
				name);
	}

	public void onJavaClassInstanceCreationExpressionEnd(Context context) {
		delegate.onClassInstanceCreationExpressionEnd(context);
	}
	
	public void onMethodInvocationStart(Context context, MethodInvocationType type, TypeReference classType, String methodName) {
		delegate.onMethodInvocationStart(context, type, classType, methodName);
	}
	
	public void onMethodInvocationEnd(Context context) {
		delegate.onMethodInvocationEnd(context);
	}
	
	public void onParametersStart(Context context) {
		delegate.onParametersStart(context);
	}
	
	public void onParameterStart(Context context) {
		delegate.onParameterStart(context);
	}
	
	public void onParameterEnd(Context context) {
		delegate.onParameterEnd(context);
	}
	
	public void onParametersEnd(Context context) {
		delegate.onParametersEnd(context);
	}
	
	public void onLambdaExpressionStart(Context context) {
		delegate.onLambdaExpressionStart(context);
	}
	
	public void onSingleLambdaParameter(Context context, String varName) {
		delegate.onSingleLambdaParameter(context, varName);
	}
	
	public void onFormalLambdaParameterListStart(Context context) {
		delegate.onFormalLambdaParameterListStart(context);
	}

	public void onFormalLambdaParameterListEnd(Context context) {
		delegate.onFormalLambdaParameterListEnd(context);
	}
	
	public void onInferredLambdaParameterList(Context context, List<String> varNames) {
		delegate.onInferredLambdaParameterList(context, varNames);
	}
	
	public void onLambdaBodyStart(Context context) {
		delegate.onLambdaBodyStart(context);
	}
	
	public void onLambdaBodyEnd(Context context) {
		delegate.onLambdaBodyEnd(context);
	}
	
	public void onLambdaExpressionEnd(Context context) {
		delegate.onLambdaExpressionEnd(context);
	}
	
	public CompilationUnit onCompilationUnitEnd(Context context) {
		
		// Trigger namespace end here since namespace contains code
		// to suppert eg. C# namespace { }, namespace { }
		delegate.onNameSpaceEnd(context);
		
		final CompilationUnit compilationUnit = delegate.onCompilationUnitEnd(context);
		
		this.compilationUnit = compilationUnit;
		
		return compilationUnit;
	}

	public void onMutabilityVariableModifier(Context context, Mutability mutability) {
		delegate.onMutabilityVariableModifier(context, mutability);
	}
	
	public void onVariableDeclarationStatementStart(Context context) {
		delegate.onVariableDeclarationStatementStart(context);
	}
	
	public void onVariableDeclarationStatementEnd(Context context) {
		delegate.onVariableDeclarationStatementEnd(context);
	}
	
	public void onVariableDeclaratorStart(Context context) {
		delegate.onVariableDeclaratorStart(context);
	}
	
	public void onVariableDeclaratorEnd(Context context) {
		delegate.onVariableDeclaratorEnd(context);
	}

	public void onVariableName(Context context, String name, int numDims) {
		delegate.onVariableName(context, name, numDims);
	}

	private static final ByteType 	BYTE_TYPE 	= new ByteType	(new TypeName("byte"), false);
	private static final ShortType 	SHORT_TYPE 	= new ShortType	(new TypeName("short"), false);
	private static final IntType 	INT_TYPE 	= new IntType	(new TypeName("int"), false);
	private static final LongType 	LONG_TYPE 	= new LongType	(new TypeName("long"), false);
	private static final Char16Type CHAR_TYPE 	= new Char16Type(new TypeName("char"), false);
	private static final FloatType 	FLOAT_TYPE 	= new FloatType	(new TypeName("float"), false);
	private static final DoubleType DOUBLE_TYPE = new DoubleType(new TypeName("double"), false);
	private static final BooleanType BOOLEAN_TYPE = new BooleanType(new TypeName("boolean"), false);
	
	public void onJavaPrimitiveType(Context context, JavaPrimitiveType type) {
		
		final ScalarType genericType;
		
		switch (type) {
		case BYTE:	genericType = BYTE_TYPE; break;
		case SHORT:	genericType = SHORT_TYPE; break;
		case INT:	genericType = INT_TYPE; break;
		case LONG:	genericType = LONG_TYPE; break;
		case CHAR:	genericType = CHAR_TYPE; break;
		case FLOAT:	genericType = FLOAT_TYPE; break;
		case DOUBLE:  genericType = DOUBLE_TYPE; break;
		case BOOLEAN: genericType = BOOLEAN_TYPE; break;
		
		default:
			throw new UnsupportedOperationException("Unknown type " + type);
		}

		delegate.onTypeReference(context, new ResolvedTypeReference(context, genericType));
	}

	public void onJavaClassOrInterfaceReferenceType(Context context, TypeReferenceType typeReferenceType, String typeName) {
System.out.println("## onJavaClassOrInterfaceReferenceType");		
		
		delegate.onTypeReference(context, typeReferenceType, typeName);
	}
	
	public void onJavaTypeVariableReferenceType(Context context, TypeReferenceType typeReferenceType, String typeName) {

System.out.println("## onJavaTypeVariableReferenceType");
		
		delegate.onTypeReference(context, typeReferenceType, typeName);
	}
	
	public void onJavaIfThenStatementStart(Context context) {
		
		printStack("javaIfThenStatementStart");

		final JavaStatement ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement == JavaStatement.ELSE) {
			
			statementsStack.pop();
			
			delegate.onElseIfStatementStart(context);
		}
		else {
			delegate.onIfStatementStart(context);
		}

		statementsStack.add(JavaStatement.IF_THEN);

		statementsStack.push();

		printStack("javaIfThenStatementStart - exit");
	}

	public void onJavaIfThenStatementEnd(Context context) {
		printStack("javaIfThenStatementEnd");

		final JavaStatement ifOrElseStatement = getIfOrElseStatement(0);
		
		if (ifOrElseStatement == JavaStatement.ELSE) {
			delegate.onElseIfStatementEnd(context);
		}
		else {
			delegate.onIfStatementInitialBlockEnd(context);
		}

		delegate.onEndIfStatement(context);
		
		statementsStack.pop();

		printStack("javaIfThenStatementEnd after pop");
	}

	public void onJavaIfThenElseStatementStart(Context context) {

		printStack("javaIfThenElseStatementStart");


		final JavaStatement ifOrElseStatement = getIfOrElseStatement(0);
		
		if (ifOrElseStatement == JavaStatement.ELSE) {
			// Previous was if-statement so this is else-statement
			
			statementsStack.pop();

			delegate.onElseIfStatementStart(context);
		}
		else {
			delegate.onIfStatementStart(context);
		}

		statementsStack.add(JavaStatement.IF_THEN_ELSE_START);

		statementsStack.push();
	}

	public void onJavaIfThenElseStatementEnd(Context context) {

		printStack("javaIfThenElseStatementEnd");

	}
	
	final JavaStatement getIfOrElseStatement(int i) {

		return 	   statementsStack.size() > 1
				&& statementsStack.getSizeOfFrame(1) > 0 + i

			? statementsStack.getLastFromFrame(1, i)
			: null;
	}

	private void onAnyNonIfStatementStart(Context context) {

		final JavaStatement ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement != null && statementsStack.getSizeOfFrame(0) == 0) {
			
			switch (ifOrElseStatement) {
			case IF_THEN_ELSE_START:
				// We are at block1 of if <block1> else <block2>
				break;

			case ELSE:
				// We are at block2 of if <block1> else <block2>
				delegate.onElseStatementStart(context);
				break;
				
			default:
				break;
			}
		}
	}
	
	private void onAnyNonIfStatementEnd(Context context) {

		final JavaStatement ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement != null && statementsStack.getSizeOfFrame(0) == 1) {

			switch (ifOrElseStatement) {
			case IF_THEN_ELSE_START:
				
				final JavaStatement prevStatement = getIfOrElseStatement(1);
				
				if (prevStatement == JavaStatement.ELSE) {
					delegate.onElseIfStatementEnd(context);
				}
				else {
					// Initial if-statement
					delegate.onIfStatementInitialBlockEnd(context);
				}
				
				// We are at block1 of if <block1> else <block2>
				statementsStack.pop();
				
				statementsStack.add(JavaStatement.ELSE);
				
				// else-block
				statementsStack.push();
				break;
				
			case ELSE:
				// We are at block2 of if <block1> else <block2>
				delegate.onElseStatementEnd(context);
				
				delegate.onEndIfStatement(context);
				
				statementsStack.pop();
				break;
				
			default:
				break;
			}
		}
	}
	
	
	public void onJavaBlockStart(Context context) {
		
		printStack("javaBlockStart");
		
		onAnyNonIfStatementStart(context);
		
		if (statementsStack.size() > 1) {
			statementsStack.add(JavaStatement.BLOCK);
		}
		
		if (statementsStack.size() > 1 && statementsStack.getSizeOfFrame(1) > 0) {
			
			System.out.println(" --- last statement in stack " + statementsStack.getLastFromFrame(1));
			
			switch (statementsStack.getLastFromFrame(1)) {
				
			case ENHANCED_FOR:
System.out.println("-- matched enhanced for");
				delegate.onIteratorForTestEnd(context);
				break;
				
			case TRY_WITH_RESOURCES:
				break;

			default:
				break;
			}
		}
		else {
			// Do nothing, blocks are handled separately in parser
		}

		if (statementsStack.size() > 1) {
			statementsStack.push();
		}

		printStack("onJavaBlockStart - exit");
	}
	
	public void onJavaBlockEnd(Context context) {

		printStack("onJavaBlockEnd");

		if (statementsStack.size() > 2) {
			statementsStack.pop();
		}
		
		onAnyNonIfStatementEnd(context);
		
		
		if (statementsStack.size() > 1 && statementsStack.getSizeOfFrame(1) > 0) {

			switch (statementsStack.getLastFromFrame(1)) {
				
			case TRY_CATCH:
			case TRY_CATCH_FINALLY:
			case TRY_WITH_RESOURCES:
				delegate.onTryBlockEnd(context);
				
				statementsStack.pop();
				break;
				
			default:
				break;
			}
		}
		else {
			// Do nothing, blocks are handled separately in parser
		}
		
		printStack("onJavaBlockEnd - exit");

	}

	public void onJavaAssertStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.ASSERT);
		
	}

	public void onJavaAssertStatementEnd(Context context) {
		
	}

	public void onJavaExpressionStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.EXPRESSION);
		
		delegate.onExpressionStatementStart(context);
	}

	public void onJavaExpressionStatementEnd(Context context) {
		delegate.onExpressionStatementEnd(context);
	}

	public void onSwitchStatementStart(Context context) {

		statementsStack.add(JavaStatement.SWITCH);

		delegate.onSwitchStatementStart(context);
	}

	public void onJavaSwitchBlockStart(Context context) {
		delegate.onJavaSwitchBlockStart(context);
	}
	
	public void onJavaSwitchBlockStatementGroupStart(Context context) {
		delegate.onJavaSwitchBlockStatementGroupStart(context);
	}

	public void onSwitchLabelsStart(Context context) {
		delegate.onSwitchLabelsStart(context);
	}
	
	public void onSwitchLabelsEnd(Context context) {
		delegate.onSwitchLabelsEnd(context);
	}

	public void onJavaSwitchBlockStatementGroupEnd(Context context) {
		delegate.onJavaSwitchBlockStatementGroupEnd(context);
	}

	public void onConstantSwitchLabelStart(Context context) {
		delegate.onConstantSwitchLabelStart(context);
	}
	
	public void onConstantSwitchLabelEnd(Context context) {
		delegate.onConstantSwitchLabelEnd(context);
	}
	
	public void onEnumSwitchLabel(Context context, String constantName) {
		delegate.onEnumSwitchLabel(context, constantName);
	}
	
	public void onDefaultSwitchLabel(Context context) {
		delegate.onDefaultSwitchLabel(context);
	}
	
	public void onJavaSwitchBlockEnd(Context context) {
		delegate.onJavaSwitchBlockEnd(context);
	}
	
	public void onSwitchStatementEnd(Context context) {
		delegate.onSwitchStatementEnd(context);
	}
	
	public void onBreakStatement(Context context, String label) {
		delegate.onBreakStatement(context, label);
	}
	
	public void onWhileStatementStart(Context context) {
		
	}
	
	public void onWhileStatementEnd(Context context) {
		
	}
	
	public void onDoStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.DO);
		
	}
	
	public void onDoStatementEnd(Context context) {
		
	}
	
	public void onForStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.FOR);
		
		delegate.onForStatementStart(context);
	}
	
	public void onForInitStart(Context context) {
		
		delegate.onForInitStart(context);
	}
	
	public void onForInitEnd(Context context) {
		
		delegate.onForInitEnd(context);
	}
	
	public void onForUpdateStart(Context context) {
		
		delegate.onForUpdateStart(context);
	}
	
	public void onForUpdateEnd(Context context) {
		
		delegate.onForUpdateEnd(context);
	}
	
	public void onForStatementEnd(Context context) {
		
		delegate.onForStatementEnd(context);
	}
	
	public void onIteratorForStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.ENHANCED_FOR);
		
		statementsStack.push();
		
		delegate.onIteratorForStatementStart(context);
	}
	
	public void onIteratorForStatementEnd(Context context) {
		
		statementsStack.pop();
		
		delegate.onIteratorForStatementEnd(context);
	}

	public void onContinueStatement(Context context, String label) {
		
	}
	
	public void onReturnStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.RETURN);
		
		delegate.onReturnStatementStart(context);
	}
	
	public void onReturnStatementEnd(Context context) {
		delegate.onReturnStatementEnd(context);
	}
	
	public void onJavaSynchronizedStatementStart(Context context) {
		
	}
	
	public void onJavaSynchronizedStatementEnd(Context context) {
		
	}
	
	public void onThrowStatementStart(Context context) {
		delegate.onThrowStatementStart(context);
	}
	
	public void onThrowStatementEnd(Context context) {
		delegate.onThrowStatementEnd(context);
	}

	public void onAnnotationStart(Context context) {
		delegate.onAnnotationStart(context);
	}

	public void onAnnotationEnd(Context context) {
		delegate.onAnnotationEnd(context);
	}

	public void onTryStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.TRY_CATCH);
		
		statementsStack.push();
		
		delegate.onTryStatementStart(context);
	}

	public void onTryStatementEnd(Context context) {
		
		delegate.onTryStatementEnd(context);
	}
	
	public void onJavaTryWithResourcesStart(Context context) {
		
		statementsStack.add(JavaStatement.TRY_WITH_RESOURCES);

		statementsStack.push();

		delegate.onTryWithResourcesStatementStart(context);
	}
	
	public void onJavaTryWithResourcesSpecificationStart(Context context) {
		delegate.onTryWithResourcesSpecificationStart(context);
	}
	
	public void onJavaResourceStart(Context context) {
		delegate.onResourceStart(context);
	}
	
	public void onJavaResourceEnd(Context context) {
		delegate.onResourceEnd(context);
	}

	public void onJavaTryWithResourcesSpecificationEnd(Context context) {
		delegate.onTryWithResourcesSpecificationEnd(context);
	}
	
	public void onJavaTryWithResourcesEnd(Context context) {
		
		statementsStack.pop();
		
		delegate.onTryWithResourcesEnd(context);
	}
	
	public void onCatchStart(Context context) {
		
		statementsStack.add(JavaStatement.CATCH);
		
		statementsStack.push();
		
		delegate.onCatchStart(context);
	}

	public void onCatchEnd(Context context) {
		
		statementsStack.pop();
		
		delegate.onCatchEnd(context);
	}

	public void onFinallyStart(Context context) {
		
		statementsStack.add(JavaStatement.FINALLY);
		
		statementsStack.push();

		delegate.onFinallyStart(context);
	}

	public void onFinallyEnd(Context context) {

		statementsStack.pop();
		
		delegate.onFinallyEnd(context);
	}
}
