package com.neaterbits.compiler.java.parser;

import java.math.BigInteger;
import java.util.List;

import org.antlr.v4.runtime.Token;

import com.neaterbits.compiler.antlr4.Antlr4;
import com.neaterbits.compiler.antlr4.ModelParserListener;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Import;
import com.neaterbits.compiler.ast.block.ConstructorInvocation;
import com.neaterbits.compiler.ast.operator.Arithmetic;
import com.neaterbits.compiler.ast.operator.Notation;
import com.neaterbits.compiler.ast.operator.Operator;
import com.neaterbits.compiler.ast.parser.FieldAccessType;
import com.neaterbits.compiler.ast.parser.MethodInvocationType;
import com.neaterbits.compiler.ast.parser.iterative.BaseIterativeOOParserListener;
import com.neaterbits.compiler.ast.statement.ASTMutability;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.type.primitive.IntegerType;
import com.neaterbits.compiler.ast.type.primitive.ScalarType;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.ast.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.ast.typedefinition.FieldVisibility;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.ast.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.java.JavaTypes;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TokenSequenceNoGenerator;
import com.neaterbits.compiler.util.parse.ParseLogger;

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

	private final ParseLogger logger;
	private final String file;
	private final TokenSequenceNoGenerator gen;
	
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
		logger.println("stack at " + statement + " " + statementsStack);
	}
	
	public JavaParserListener(ParseLogger logger, String file, TokenSequenceNoGenerator gen) {
		this.logger = logger;
		this.file = file;
		this.gen = gen;
		this.delegate = new JavaIterativeListener(logger);
		this.statementsStack = new StatementsStack();
	}

	@Override
	public CompilationUnit getResult() {
		return compilationUnit;
	}

	public void onPackageDeclaration(Context context, String packageKeyword, Context packageKeywordContext, String name, Context nameContext) {
		this.packageName = name;
		
		delegate.onNamespaceStart(context, packageKeyword, packageKeywordContext, name, nameContext, Strings.split(packageName, '.'));
	}

	public void onCompilationUnitStart(Context context) {
		delegate.onCompilationUnitStart(context);
	}
	
	public void onImport(Import importStatement) {
		delegate.onImport(importStatement);
	}
	
	public void onClassStart(Context context, String classKeyword, Context classKeywordContext, String name, Context nameContext) {
		delegate.onClassStart(context, classKeyword, classKeywordContext, name, nameContext);
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
	
	public void onClassExtends(Context context, String extendsKeyword, Context extendsKeywordContext, ScopedName className) {
		delegate.onClassExtends(context, extendsKeyword, extendsKeywordContext, className);
	}
	
	public void onClassImplements(Context context, ScopedName interfaceName) {
		delegate.onClassImplements(context, interfaceName);
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

	public void onStaticInitializerStart(Context context) {
		
		statementsStack.push();
		
		delegate.onStaticInitializerStart(context);
	}
	
	public void onStaticInitializerEnd(Context context) {
		delegate.onStaticInitializerEnd(context);
		
		statementsStack.pop();
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
	
	public void onConstructorInvocationStart(Context context, ConstructorInvocation type) {
		delegate.onConstructorInvocationStart(context, type);
	}
	
	public void onConstructorInvocationEnd(Context context) {
		delegate.onConstructorInvocationEnd(context);
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

	public void onMethodSignatureParameterStart(Context context, boolean varArgs) {
		delegate.onMethodSignatureParameterStart(context, varArgs);
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

	public void onInterfaceStart(Context context, String interfaceKeyword, Context interfaceKeywordContext, String name, Context nameContext) {
		delegate.onInterfaceStart(context, interfaceKeyword, interfaceKeywordContext, name, nameContext);
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
	
	public void onInterfaceExtends(Context context, ScopedName interfaceName) {
		delegate.onInterfaceExtends(context, interfaceName);
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
	
	public void onEnumStart(Context context, String enumKeyword, Context enumKeywordContext, String name, Context nameContext) {
		statementsStack.push();
		
		delegate.onEnumStart(context, enumKeyword, enumKeywordContext, name, nameContext);
	}

	public void onEnumImplements(Context context, ScopedName interfaceName) {
		delegate.onEnumImplements(context, interfaceName);
	}
	
	public void onEnumConstantStart(Context context, String name) {
		delegate.onEnumConstantStart(context, name);
	}
	
	public void onEnumConstantEnd(Context context) {
		delegate.onEnumConstantEnd(context);
	}
	
	public void onEnumEnd(Context context) {
		
		delegate.onEnumEnd(context);

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
	
	public void onMutabilityFieldModifier(Context context, ASTMutability mutability) {
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

	public void onArrayAccessStart(Context context) {
		delegate.onArrayAccessStart(context);
	}
	
	public void onArrayIndexStart(Context context) {
		delegate.onArrayIndexStart(context);
	}
	
	public void onArrayIndexEnd(Context context) {
		delegate.onArrayIndexEnd(context);
	}
	
	public void onArrayAccessEnd(Context context) {
		delegate.onArrayAccessEnd(context);
	}

	public void onFieldAccess(Context context, FieldAccessType fieldAccessType, ScopedName typeName, String fieldName, Context fieldNameContext) {
		delegate.onFieldAccess(context, fieldAccessType, typeName, fieldName, fieldNameContext);
	}

	public void onCastExpressionStart(Context context) {
		delegate.onCastExpressionStart(context);
	}
	
	public void onCastExpressionEnd(Context context) {
		delegate.onCastExpressionEnd(context);
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
		
		final int bits = javaInteger.getBits();
		
		final IntegerType type;
		
		switch (bits) {
		case 8:
			type = JavaTypes.BYTE_TYPE;
			break;
			
		case 16:
			type = JavaTypes.SHORT_TYPE;
			break;
			
		case 32:
			type = JavaTypes.INT_TYPE;
			break;
			
		case 64:
			type = JavaTypes.LONG_TYPE;
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
		
		
		delegate.onIntegerLiteral(
				context,
				BigInteger.valueOf(javaInteger.getValue()),
				javaInteger.getBase(),
				true,
				bits,
				type);
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

		delegate.onBooleanLiteral(context, value, JavaTypes.BOOLEAN_TYPE);
	}
	
	public void onJavaCharacterLiteral(Context context, String literal) {
		
		if (literal.length() < 3) {
			throw new IllegalStateException("Not a Character literal");
		}

		final String s = literal.substring(1, literal.length() - 1);
		
		delegate.onCharacterLiteral(context, s.charAt(0), JavaTypes.CHAR_TYPE);
	}
	
	public void onJavaStringLiteral(Context context, String literal) {
		if (literal.length() < 2) {
			throw new IllegalStateException("Not a String literal");
		}

		delegate.onStringLiteral(context, literal.substring(1, literal.length() - 1), JavaTypes.STRING_TYPE);
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

	public void onJavaClassInstanceCreationConstructorName(Context context, ScopedName name) {

		delegate.onClassInstanceCreationTypeAndConstructorName(
				context,
				new ResolveLaterTypeReference(context, name),
				name);
	}

	public void onJavaClassInstanceCreationExpressionEnd(Context context) {
		delegate.onClassInstanceCreationExpressionEnd(context);
	}
	
	public void onMethodInvocationStart(Context context, MethodInvocationType type, TypeReference classType, String methodName, Context methodNameContext) {
		delegate.onMethodInvocationStart(context, type, classType, methodName, methodNameContext);
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
	
	public void onArrayCreationExpressionStart(Context context, TypeReference typeReference, int numDims) {
		delegate.onArrayCreationExpressionStart(context, typeReference, numDims);
	}
	
	public void onDimExpressionStart(Context context) {
		delegate.onDimExpressionStart(context);
	}
	
	public void onDimExpressionEnd(Context context) {
		delegate.onDimExpressionEnd(context);
	}

	public void onArrayCreationExpressionEnd(Context context) {
		delegate.onArrayCreationExpressionEnd(context);
	}
	
	public void onClassExpression(Context context, String className, int numArrayDims) {
		delegate.onClassExpression(context, className, numArrayDims);
	}
	
	
	public void onLambdaExpressionStart(Context context) {
		delegate.onLambdaExpressionStart(context);
	}
	
	public void onSingleLambdaParameter(Context context, String varName, Context varNameContext) {
		delegate.onSingleLambdaParameter(context, varName, varNameContext);
	}
	
	public void onFormalLambdaParameterListStart(Context context) {
		delegate.onFormalLambdaParameterListStart(context);
	}

	public void onFormalLambdaParameterListEnd(Context context) {
		delegate.onFormalLambdaParameterListEnd(context);
	}
	
	public void onInferredLambdaParameterList(Context context, List<String> varNames, Context varNamesContext) {
		delegate.onInferredLambdaParameterList(context, varNames, varNamesContext);
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

	public void onMutabilityVariableModifier(Context context, ASTMutability mutability) {
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

	
	public void onJavaPrimitiveType(Context context, JavaPrimitiveType type) {
		
		final BuiltinType genericType;
		
		switch (type) {
		case BYTE:	genericType = JavaTypes.BYTE_TYPE; break;
		case SHORT:	genericType = JavaTypes.SHORT_TYPE; break;
		case INT:	genericType = JavaTypes.INT_TYPE; break;
		case LONG:	genericType = JavaTypes.LONG_TYPE; break;
		case CHAR:	genericType = JavaTypes.CHAR_TYPE; break;
		case FLOAT:	genericType = JavaTypes.FLOAT_TYPE; break;
		case DOUBLE:  genericType = JavaTypes.DOUBLE_TYPE; break;
		case BOOLEAN: genericType = JavaTypes.BOOLEAN_TYPE; break;
		case VOID:	genericType = JavaTypes.VOID_TYPE; break;
		
		default:
			throw new UnsupportedOperationException("Unknown type " + type);
		}

		delegate.onTypeReference(context, new BuiltinTypeReference(context, genericType));
	}
	
	public ScalarType parseJavaPrimitiveType(String typeString) {
		
		final ScalarType type;
		
		switch (typeString) {
		
		case "byte": 	type = JavaTypes.BYTE_TYPE; break;
		case "short": 	type = JavaTypes.SHORT_TYPE; break;
		case "int": 	type = JavaTypes.INT_TYPE; break;
		case "long": 	type = JavaTypes.LONG_TYPE; break;
		case "char": 	type = JavaTypes.CHAR_TYPE; break;
		case "float": 	type = JavaTypes.FLOAT_TYPE; break;
		case "double": 	type = JavaTypes.DOUBLE_TYPE; break;
		case "boolean": type = JavaTypes.BOOLEAN_TYPE; break;
		
		default:
			throw new UnsupportedOperationException("Unknown type " + typeString);
		}
		
		return type;
	}

	public void onJavaClassOrInterfaceReferenceType(Context context, ScopedName typeName) {

		delegate.onTypeReference(context, new ResolveLaterTypeReference(context, typeName));
	}
	
	public void onJavaTypeVariableReferenceType(Context context, ScopedName typeName) {

		delegate.onTypeReference(context, typeName);
	}
	
	private Context updateElseIfContext(Context context, Token elseToken, String file) {

		final Context elseContext = Antlr4.context(elseToken, file, gen.getNextTokenSequenceNo());
		
		final Context updatedContext;
		
		if (elseContext.getStartOffset() < context.getStartOffset()) {
			updatedContext = new Context(
					context.getFile(),
					elseContext.getStartLine(),
					elseContext.getStartPosInLine(),
					elseContext.getStartOffset(),
					context.getEndLine(),
					context.getEndPosInLine(),
					context.getEndOffset(),
					context.getText(),
					gen.getNextTokenSequenceNo());
		}
		else {
			updatedContext = context;
		}
		
		if (elseContext.getStartOffset() != updatedContext.getStartOffset()) {
			throw new IllegalStateException("elseContext " + elseContext.getStartOffset() + "/" + updatedContext.getStartOffset());
		}
		
		return updatedContext;
	}
	
	public void onJavaIfThenStatementStart(Context context, Token keywordToken) {
		
		if (logger != null) {
			printStack("javaIfThenStatementStart");
		}

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement != null && ifOrElseStatement.getStatement() == JavaStatement.ELSE) {
			
			statementsStack.pop();

			final String elseText = ifOrElseStatement.getKeywordToken(0).getText();
			final Token elseToken = ifOrElseStatement.getKeywordToken(0);

			delegate.onElseIfStatementStart(
					updateElseIfContext(context, elseToken, file),
					elseText, Antlr4.context(elseToken, file, gen.getNextTokenSequenceNo()),
					keywordToken.getText(), Antlr4.context(keywordToken, file, gen.getNextTokenSequenceNo()));
		}
		else {
			delegate.onIfStatementStart(context, keywordToken.getText(), Antlr4.context(keywordToken, file, gen.getNextTokenSequenceNo()));
		}

		statementsStack.add(JavaStatement.IF_THEN, keywordToken);
		statementsStack.push();

		if (logger != null) {
			printStack("javaIfThenStatementStart - exit");
		}
	}

	public void onJavaIfThenStatementEnd(Context context) {
		
		if (logger != null) {
			printStack("javaIfThenStatementEnd");
		}

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);
		
		if (ifOrElseStatement.getStatement() == JavaStatement.ELSE) {
			delegate.onElseIfStatementEnd(context);
		}
		else {
			delegate.onIfStatementInitialBlockEnd(context);
		}

		delegate.onEndIfStatement(context);
		statementsStack.pop();

		if (logger != null) {
			printStack("javaIfThenStatementEnd after pop");
		}
	}

	public void onJavaIfThenElseStatementStart(
			Context context,
			Token ifKeyword,
			Token elseKeyword) {

		if (logger != null) {
			printStack("javaIfThenElseStatementStart");
		}

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);
		
		if (ifOrElseStatement != null && ifOrElseStatement.getStatement() == JavaStatement.ELSE) {
			// Previous was if-statement so this is else-statement
			statementsStack.pop();

			final String elseText = ifOrElseStatement.getKeywordToken(0).getText();
			final Token elseToken = ifOrElseStatement.getKeywordToken(0);
			
			delegate.onElseIfStatementStart(
					updateElseIfContext(context, elseToken, file),
					elseText, Antlr4.context(elseToken, file, gen.getNextTokenSequenceNo()),
					ifKeyword.getText(), Antlr4.context(ifKeyword, file, gen.getNextTokenSequenceNo()));
		}
		else {
			delegate.onIfStatementStart(context, ifKeyword.getText(), Antlr4.context(ifKeyword, file, gen.getNextTokenSequenceNo()));
		}

		statementsStack.add(JavaStatement.IF_THEN_ELSE_START, ifKeyword, elseKeyword);
		statementsStack.push();
	}

	public void onJavaIfThenElseStatementEnd(Context context) {

		if (logger != null) {
			printStack("javaIfThenElseStatementEnd");
		}
	}
	
	final JavaStatementHolder getIfOrElseStatement(int i) {

		return 	   statementsStack.size() > 1
				&& statementsStack.getSizeOfFrame(1) > 0 + i

			? statementsStack.getLastFromFrame(1, i)
			: null;
	}

	private void onAnyNonIfStatementStart(Context context) {

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement != null && statementsStack.getSizeOfFrame(0) == 0) {
			
			switch (ifOrElseStatement.getStatement()) {
			case IF_THEN_ELSE_START:
				// We are at block1 of if <block1> else <block2>
				break;

			case ELSE:
				// We are at block2 of if <block1> else <block2>
				delegate.onElseStatementStart(
						context,
						ifOrElseStatement.getKeywordToken(0).getText(),
						Antlr4.context(ifOrElseStatement.getKeywordToken(0), file, gen.getNextTokenSequenceNo()));
				break;
				
			default:
				break;
			}
		}
	}
	
	private void onAnyNonIfStatementEnd(Context context) {

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement != null && statementsStack.getSizeOfFrame(0) == 1) {

			switch (ifOrElseStatement.getStatement()) {
			case IF_THEN_ELSE_START:
				
				final JavaStatementHolder prevStatement = getIfOrElseStatement(1);
				
				if (prevStatement != null && prevStatement.getStatement() == JavaStatement.ELSE) {
					delegate.onElseIfStatementEnd(context);
				}
				else {
					// Initial if-statement
					delegate.onIfStatementInitialBlockEnd(context);
				}
				
				// We are at block1 of if <block1> else <block2>
				
				final Token elseToken = ifOrElseStatement.getKeywordToken(1);
				
				statementsStack.pop();
				statementsStack.add(JavaStatement.ELSE, elseToken);
				
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
		
		if (logger != null) {
			printStack("javaBlockStart");
		}
		
		onAnyNonIfStatementStart(context);
		
		if (statementsStack.size() > 1) {
			statementsStack.add(JavaStatement.BLOCK);
		}
		
		if (statementsStack.size() > 1 && statementsStack.getSizeOfFrame(1) > 0) {
			
			switch (statementsStack.getLastFromFrame(1).getStatement()) {
				
			case ENHANCED_FOR:
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

		if (logger != null) {
			printStack("onJavaBlockStart - exit");
		}
	}
	
	public void onJavaBlockEnd(Context context) {

		if (logger != null) {
			printStack("onJavaBlockEnd");
		}

		if (statementsStack.size() > 2) {
			statementsStack.pop();
		}
		
		onAnyNonIfStatementEnd(context);
		
		
		if (statementsStack.size() > 1 && statementsStack.getSizeOfFrame(1) > 0) {

			switch (statementsStack.getLastFromFrame(1).getStatement()) {
				
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
		
		if (logger != null) {
			printStack("onJavaBlockEnd - exit");
		}
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

	public void onSwitchStatementStart(Context context, String keyword, Context keywordContext) {

		statementsStack.add(JavaStatement.SWITCH);

		delegate.onSwitchStatementStart(context, keyword, keywordContext);
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

	public void onConstantSwitchLabelStart(Context context, String keyword, Context keywordContext) {
		delegate.onConstantSwitchLabelStart(context, keyword, keywordContext);
	}
	
	public void onConstantSwitchLabelEnd(Context context) {
		delegate.onConstantSwitchLabelEnd(context);
	}
	
	public void onEnumSwitchLabel(Context context, String keyword, Context keywordContext, String constantName, Context constantNameContext) {
		delegate.onEnumSwitchLabel(context, keyword, keywordContext, constantName, constantNameContext);
	}
	
	public void onDefaultSwitchLabel(Context context, String keyword, Context keywordContext) {
		delegate.onDefaultSwitchLabel(context, keyword, keywordContext);
	}
	
	public void onJavaSwitchBlockEnd(Context context) {
		delegate.onJavaSwitchBlockEnd(context);
	}
	
	public void onSwitchStatementEnd(Context context) {
		delegate.onSwitchStatementEnd(context);
	}
	
	public void onBreakStatement(Context context, String keyword, Context keywordContext, String label) {
		delegate.onBreakStatement(context, keyword, keywordContext, label);
	}
	
	public void onWhileStatementStart(Context context) {
		delegate.onWhileStatementStart(context);
	}
	
	public void onWhileStatementEnd(Context context) {
		delegate.onWhileStatementEnd(context);
	}
	
	public void onForStatementStart(Context context, String keyword, Context keywordContext) {
		
		statementsStack.add(JavaStatement.FOR);
		
		delegate.onForStatementStart(context, keyword, keywordContext);
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

	public void onDoWhileStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.DOWHILE);
		
		statementsStack.push();
		
		delegate.onDoWhileStatementStart(context);
	}
	
	public void onDoWhileStatementEnd(Context context) {
		
		delegate.onDoWhileStatementEnd(context);
		
		statementsStack.pop();
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
