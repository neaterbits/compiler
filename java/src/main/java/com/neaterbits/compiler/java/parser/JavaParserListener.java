package com.neaterbits.compiler.java.parser;

import java.math.BigInteger;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ResolvedTypeReference;
import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.antlr4.ModelParserListener;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.ast.statement.VariableMutability;
import com.neaterbits.compiler.common.ast.type.BaseType;
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
import com.neaterbits.compiler.common.ast.typedefinition.MethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.MethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.parser.iterative.BaseIterativeOOParserListener;
import com.neaterbits.compiler.common.parser.stackstate.StackStatements;
import com.neaterbits.compiler.common.util.Strings;

/**
 * Listener for the Java grammars
 */

public class JavaParserListener implements ModelParserListener<CompilationUnit> {

	// Delegate to make sure make all special handling here
	private static class JavaIterativeListener extends BaseIterativeOOParserListener {
		
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
	private final Stack<StackStatements> statementsStack; 

	public JavaParserListener() {
		this.delegate = new JavaIterativeListener();
		this.statementsStack = new Stack<>();
	}

	@Override
	public CompilationUnit getResult() {
		return compilationUnit;
	}

	public void onPackageDeclaration(Context context, String name) {
		this.packageName = name;
		
		delegate.onNamespaceStart(context, name, Strings.split(packageName, '.'));
	}

	public void onCompilationUnitStart() {
		delegate.onCompilationUnitStart();
	}
	
	public void onImport(Import importStatement) {
		delegate.onImport(importStatement);
	}
	
	public void onClassStart(String name) {
		delegate.onClassStart(name);
	}
	
	public void onVisibilityClassModifier(ClassVisibility visibility) {
		delegate.onVisibilityClassModifier(visibility);
	}
	
	public void onSubclassingModifier(Subclassing subclassing) {
		delegate.onSubclassingModifier(subclassing);
	}
	
	public void onStaticClassModifier() {
		delegate.onStaticClassModifier();
	}
	
	public void onStrictfpClassModifier() {
		delegate.onStrictfpClassModifier();
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
	
	public void onMethodStart(Context context) {
		delegate.onMethodStart(context);
	}
	
	public void onMethodName(Context context, String methodName) {
		delegate.onMethodName(context, methodName);
	}
	
	public void onVisibilityMethodModifier(MethodVisibility visibility) {
		delegate.onVisibilityMethodModifier(visibility);
	}
	
	public void onOverrideModifier(MethodOverride methodOverride) {
		delegate.onOverrideModifier(methodOverride);
	}
	
	public void onStaticMethodModifier() {
		delegate.onStaticMethodModifier();
	}

	public void onStrictfpMethodModifier() {
		delegate.onStrictfpMethodModifier();
	}
	
	public void onSynchronizedMethodModifier() {
		delegate.onSynchronizedMethodModifier();
	}

	public void onNativeMethodModifier() {
		delegate.onNativeMethodModifier();
	}
	
	public void onMethodEnd(Context context) {
		delegate.onMethodEnd(context);
	}
	
	public void onEnterAssignmentExpression(Context context) {
		delegate.onEnterAssignmentExpression(context);
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

	// Literals
	public void onJavaIntegerLiteral(Context context, String literal) {

		final JavaInteger javaInteger = JavaParserUtil.parseIntegerLiteral(literal);
		
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
	
	public CompilationUnit onCompilationUnitEnd(Context context) {
		
		// Trigger namespace end here since namespace contains code
		// to suppert eg. C# namespace { }, namespace { }
		delegate.onNameSpaceEnd(context);
		
		final CompilationUnit compilationUnit = delegate.onCompilationUnitEnd(context);
		
		this.compilationUnit = compilationUnit;
		
		return compilationUnit;
	}

	public void onMutabilityVariableModifier(VariableMutability mutability) {
		delegate.onMutabilityVariableModifier(mutability);
	}
	
	public void onVariableDeclarationStatementStart(Context context) {
		delegate.onVariableDeclarationStatementStart(context);
	}
	
	public void onVariableDeclarationStatementEnd(Context context) {
		delegate.onVariableDeclarationStatementEnd(context);
	}
	
	public void onVariableDeclaratorStart(Context context, String name, int numDims) {
		delegate.onVariableDeclaratorStart(context, name, numDims);
	}
	
	public void onVariableDeclaratorEnd(Context context) {
		delegate.onVariableDeclaratorEnd(context);
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

		delegate.onTypeReference(context, new ResolvedTypeReference(genericType));
	}
	
	public void onJavaIfStatementStart(Context context) {
		
	}

	public void onJavaIfStatementEnd(Context context) {
		
	}

	public void onJavaIfThenElseStatementStart(Context context) {
		
	}

	public void onJavaIfThenElseStatementEnd(Context context) {
		
	}
	
	public void onJavaBlockStart(Context context) {
		
	}
	
	public void onJavaBlockEnd(Context context) {
		
	}

	public void onJavaAssertStatementStart(Context context) {
		
	}

	public void onJavaAssertStatementEnd(Context context) {
		
	}
	
	public void onJavaExpressionStatementStart(Context context) {
		
	}
	
	public void onJavaExpressionStatementEnd(Context context) {
		
	}
	
	public void onSwitchStatementStart(Context context) {
		
	}
	
	public void onJavaSwitchBlockStart(Context context) {
		
	}
	
	public void onJavaSwitchBlockStatementGroupStart(Context context) {
		
	}

	public void onSwitchLabelsStart(Context context) {
		
	}
	
	public void onSwitchLabelsEnd(Context context) {
		
	}

	public void onJavaSwitchBlockStatementGroupEnd(Context context) {
		
	}

	public void onConstantSwitchLabelStart(Context context) {
		
	}
	
	public void onConstantSwitchLabelEnd(Context context) {
		
	}
	
	public void onEnumSwitchLabelStart(Context context) {
		
	}
	
	public void onEnumSwitchLabelEnd(Context context) {
		
	}
	
	public void onDefaultSwitchLabel(Context context) {
		
	}
	
	public void onJavaSwitchBlockEnd(Context context) {
		
	}
	
	public void onSwitchStatementEnd(Context context) {
		
	}
	
	public void onBreakStatement(Context context, String label) {
		
	}
	
	public void onWhileStatementStart(Context context) {
		
	}
	
	public void onWhileStatementEnd(Context context) {
		
	}
	
	public void onDoStatementStart(Context context) {
		
	}
	
	public void onDoStatementEnd(Context context) {
		
	}
	
	public void onForStatementStart(Context context) {
		
	}
	
	public void onForInitStart(Context context) {
		
	}
	
	public void onForInitEnd(Context context) {
		
	}
	
	public void onForUpdateStart(Context context) {
		
	}
	
	public void onForUpdateEnd(Context context) {
		
	}
	
	public void onForStatementEnd(Context context) {
		
	}
	
	public void onContinueStatement(Context context, String label) {
		
	}
	
	public void onReturnStatementStart(Context context) {
		
	}
	
	public void onReturnStatementEnd(Context context) {
		
	}
	
	public void onJavaSynchronizedStatementStart(Context context) {
		
	}
	
	public void onJavaSynchronizedStatementEnd(Context context) {
		
	}
	
	public void onThrowStatementStart(Context context) {
		
	}
	
	public void onThrowStatementEnd(Context context) {
		
	}

	public void onTryStatementStart(Context context) {
		
	}

	public void onTryStatementEnd(Context context) {
		
	}
	
	public void onJavaTryWithResourcesStart(Context context) {
		
	}
	
	public void onJavaTryWithResourcesEnd(Context context) {
		
	}
	
	public void onCatchStart(Context context) {
		
	}

	public void onCatchEnd(Context context) {
		
	}

	public void onFinallyStart(Context context) {
		
	}

	public void onFinallyEnd(Context context) {
		
	}
}

