package com.neaterbits.compiler.java.parser.antlr4;

import com.neaterbits.compiler.java.Java8BaseListener;
import com.neaterbits.compiler.java.Java8Parser;
import com.neaterbits.compiler.java.Java8Parser.*;
import com.neaterbits.compiler.java.parser.JavaParserListener;
import com.neaterbits.compiler.java.parser.JavaPrimitiveType;

import static com.neaterbits.compiler.common.antlr4.Antlr4.context;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;

import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.ast.NamespaceName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.operator.Bitwise;
import com.neaterbits.compiler.common.ast.operator.Logical;
import com.neaterbits.compiler.common.ast.operator.Relational;
import com.neaterbits.compiler.common.ast.statement.Mutability;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.FieldVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.MethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.MethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.MethodInvocationType;
import com.neaterbits.compiler.common.util.Strings;

public class Java8AntlrParserListener extends Java8BaseListener {

	private final JavaParserListener delegate;
	
	private final ParseLogger logger;

	public Java8AntlrParserListener(JavaParserListener delegate, ParseLogger logger) {
		this.delegate = delegate;
		
		this.logger = logger;
	}
	
	private String ruleName(ParserRuleContext ctx) {
		return Java8Parser.ruleNames[ctx.getRuleIndex()];
	}
	
	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		logger.onEnterAntlrRule(ruleName(ctx), ctx.getText());
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		logger.onExitAntlrRule(ruleName(ctx), ctx.getText());
	}

	@Override
	public void enterCompilationUnit(CompilationUnitContext ctx) {
		delegate.onCompilationUnitStart();
	}

	@Override
	public void exitCompilationUnit(CompilationUnitContext ctx) {
		delegate.onCompilationUnitEnd(context(ctx));
	}
	
	@Override
	public void exitPackageDeclaration(PackageDeclarationContext ctx) {
		delegate.onPackageDeclaration(context(ctx), ctx.packageName().getText());
	}

	private static NamespaceName namespace(String typeName) {
		final String [] names = Strings.split(typeName, '.');
		
		return new NamespaceName(Strings.join(names, '.', names.length - 1));
	}

	private static ClassName className(String typeName) {
		final String [] names = Strings.split(typeName, '.');
		
		return new ClassName(names[names.length - 1]);
	}
	
	@Override
	public void exitSingleTypeImportDeclaration(SingleTypeImportDeclarationContext ctx) {
		
		final String typeName = ctx.typeName().getText();
		
		final Import importStatement = new Import(
				context(ctx),
				namespace(typeName),
				className(typeName));
		
		delegate.onImport(importStatement);
	}

	@Override
	public void exitTypeImportOnDemandDeclaration(TypeImportOnDemandDeclarationContext ctx) {
		final String packageOrTypeName = ctx.packageOrTypeName().getText();
		
		final String [] s = Strings.split(packageOrTypeName, '.');
		
		final Import importStatement = new Import(context(ctx), s);
		
		delegate.onImport(importStatement);
	}

	@Override
	public void exitSingleStaticImportDeclaration(SingleStaticImportDeclarationContext ctx) {
		final String typeName = ctx.typeName().getText();
		
		final Import importStatement = new Import(
				context(ctx),
				namespace(typeName),
				className(typeName),
				new MethodName(ctx.Identifier().getText()));
		
		delegate.onImport(importStatement);
	}

	@Override
	public void exitStaticImportOnDemandDeclaration(StaticImportOnDemandDeclarationContext ctx) {
		final String typeName = ctx.typeName().getText();
		
		final Import importStatement = new Import(
				context(ctx),
				namespace(typeName),
				className(typeName),
				null);
		
		delegate.onImport(importStatement);
	}

	@Override
	public void enterNormalClassDeclaration(NormalClassDeclarationContext ctx) {
		delegate.onClassStart(ctx.Identifier().getText());
	}
	
	@Override
	public void exitPublicClassModifier(PublicClassModifierContext ctx) {
		delegate.onVisibilityClassModifier(context(ctx), ClassVisibility.PUBLIC);
	}

	@Override
	public void exitProtectedClassModifier(ProtectedClassModifierContext ctx) {
		delegate.onVisibilityClassModifier(context(ctx), ClassVisibility.NAMESPACE);
	}

	@Override
	public void exitPrivateClassModifier(PrivateClassModifierContext ctx) {
		delegate.onVisibilityClassModifier(context(ctx), ClassVisibility.PRIVATE);
	}

	@Override
	public void exitAbstractClassModifier(AbstractClassModifierContext ctx) {
		delegate.onSubclassingModifier(context(ctx), Subclassing.ABSTRACT);
	}
	
	@Override
	public void exitFinalClassModifier(FinalClassModifierContext ctx) {
		delegate.onSubclassingModifier(context(ctx), Subclassing.FINAL);
	}

	@Override
	public void exitStaticClassModifier(StaticClassModifierContext ctx) {
		delegate.onStaticClassModifier(context(ctx));
	}

	@Override
	public void exitStrictfpClassModifier(StrictfpClassModifierContext ctx) {
		delegate.onStrictfpClassModifier(context(ctx));
	}

	@Override
	public void exitNormalClassDeclaration(NormalClassDeclarationContext ctx) {
		delegate.onClassEnd(context(ctx));
	}

	@Override
	public void enterMethodDeclaration(MethodDeclarationContext ctx) {
		
		System.out.println("## enterMethodDeclaration: " + ctx.getText());
		
		delegate.onMethodStart(context(ctx));
	}

	@Override
	public void enterResult(ResultContext ctx) {
		delegate.onMethodReturnTypeStart(context(ctx));
	}

	@Override
	public void exitResult(ResultContext ctx) {
		delegate.onMethodReturnTypeEnd(context(ctx));
	}

	@Override
	public void enterConstructorDeclaration(ConstructorDeclarationContext ctx) {
		delegate.onConstructorStart(context(ctx));
	}
	
	@Override
	public void exitPublicConstructorModifier(PublicConstructorModifierContext ctx) {
		delegate.onConstructorVisibilityModifier(context(ctx), ConstructorVisibility.PUBLIC);
	}
	
	@Override
	public void exitProtectedConstructorModifier(ProtectedConstructorModifierContext ctx) {
		delegate.onConstructorVisibilityModifier(context(ctx), ConstructorVisibility.NAMESPACE_AND_SUBCLASSES);
	}
	
	@Override
	public void exitPrivateConstructorModifier(PrivateConstructorModifierContext ctx) {
		delegate.onConstructorVisibilityModifier(context(ctx), ConstructorVisibility.PRIVATE);
	}
	
	@Override
	public void enterConstructorDeclarator(ConstructorDeclaratorContext ctx) {
		delegate.onConstructorName(context(ctx), ctx.simpleTypeName().getText());
	}

	@Override
	public void exitConstructorDeclaration(ConstructorDeclarationContext ctx) {
		delegate.onConstructorEnd(context(ctx));
	}

	@Override
	public void enterMethodDeclarator(MethodDeclaratorContext ctx) {
		delegate.onMethodName(context(ctx), ctx.Identifier().getText());
	}
	
	@Override
	public void enterFormalParameterList(FormalParameterListContext ctx) {
		delegate.onMethodSignatureParametersStart(context(ctx));
	}

	@Override
	public void enterFormalParameter(FormalParameterContext ctx) {
		delegate.onMethodSignatureParameterStart(context(ctx));
	}

	@Override
	public void exitFormalParameter(FormalParameterContext ctx) {
		delegate.onMethodSignatureParameterEnd(context(ctx));
	}

	@Override
	public void exitFormalParameterList(FormalParameterListContext ctx) {
		delegate.onMethodSignatureParametersEnd(context(ctx));
	}

	@Override
	public void exitPublicMethodMofifier(PublicMethodMofifierContext ctx) {
		delegate.onVisibilityMethodModifier(context(ctx), MethodVisibility.PUBLIC);
	}

	@Override
	public void exitProtectedMethodModifier(ProtectedMethodModifierContext ctx) {
		delegate.onVisibilityMethodModifier(context(ctx), MethodVisibility.NAMESPACE_AND_SUBCLASSES);
	}

	@Override
	public void exitPrivateMethodModifier(PrivateMethodModifierContext ctx) {
		delegate.onVisibilityMethodModifier(context(ctx), MethodVisibility.PRIVATE);
	}
	
	@Override
	public void exitAbstractMethodModifier(AbstractMethodModifierContext ctx) {
		delegate.onOverrideModifier(context(ctx), MethodOverride.ABSTRACT);
	}

	@Override
	public void exitFinalMethodModifier(FinalMethodModifierContext ctx) {
		delegate.onOverrideModifier(context(ctx), MethodOverride.FINAL);
	}

	@Override
	public void exitStaticMethodModifier(StaticMethodModifierContext ctx) {
		delegate.onStaticMethodModifier(context(ctx));
	}

	@Override
	public void exitStrictfpMethodModifier(StrictfpMethodModifierContext ctx) {
		delegate.onStrictfpMethodModifier(context(ctx));
	}
	
	@Override
	public void exitSynchronizedMethodModifier(SynchronizedMethodModifierContext ctx) {
		delegate.onSynchronizedMethodModifier(context(ctx));
	}
	
	@Override
	public void exitNativeMethodModifier(NativeMethodModifierContext ctx) {
		delegate.onNativeMethodModifier(context(ctx));
	}

	@Override
	public void exitMethodDeclaration(MethodDeclarationContext ctx) {
		System.out.println("## exitMethodDeclaration: " + ctx.getText());

		delegate.onMethodEnd(context(ctx));
	}
	
	@Override
	public void enterFieldDeclaration(FieldDeclarationContext ctx) {
		delegate.onFieldDeclarationStart(context(ctx));
	}

	@Override
	public void exitPublicFieldModifier(PublicFieldModifierContext ctx) {
		delegate.onVisibilityFieldModifier(context(ctx), FieldVisibility.PUBLIC);
	}

	@Override
	public void exitProtectedFieldModifier(ProtectedFieldModifierContext ctx) {
		delegate.onVisibilityFieldModifier(context(ctx), FieldVisibility.NAMESPACE_AND_SUBCLASSES);
	}
	
	@Override
	public void exitPrivateFieldModifier(PrivateFieldModifierContext ctx) {
		delegate.onVisibilityFieldModifier(context(ctx), FieldVisibility.PRIVATE);
	}
	
	@Override
	public void exitStaticFieldModifier(StaticFieldModifierContext ctx) {
		delegate.onStaticFieldModifier(context(ctx));
	}
	
	@Override
	public void exitFinalFieldModifier(FinalFieldModifierContext ctx) {
		delegate.onMutabilityFieldModifier(context(ctx), Mutability.VALUE_OR_REF_IMMUTABLE);
	}
	
	@Override
	public void exitTransientFieldModifier(TransientFieldModifierContext ctx) {
		delegate.onTransientFieldModifier(context(ctx));
	}

	@Override
	public void exitVolatileFieldModifier(VolatileFieldModifierContext ctx) {
		delegate.onVolatileFieldModifier(context(ctx));
	}

	@Override
	public void exitFieldDeclaration(FieldDeclarationContext ctx) {
		delegate.onFieldDeclarationEnd(context(ctx));
	}

	// Expressions
	@Override
	public void enterAssignment(AssignmentContext ctx) {
		delegate.onEnterAssignmentExpression(context(ctx));
	}
	
	@Override
	public void exitExpressionName(ExpressionNameContext ctx) {
		delegate.onVariableReference(context(ctx), ctx.getText());
	}

	@Override
	public void enterLeftHandSide(LeftHandSideContext ctx) {
		delegate.onEnterAssignmentLHS(context(ctx));
	}

	@Override
	public void exitLeftHandSide(LeftHandSideContext ctx) {
		delegate.onExitAssignmentLHS(context(ctx));
	}

	@Override
	public void exitAssignment(AssignmentContext ctx) {
		delegate.onExitAssignmentExpression(context(ctx));
	}

	@Override
	public void exitBitwiseOrExpression(BitwiseOrExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Bitwise.OR);
	}

	@Override
	public void enterJavaClassInstanceCreationExpression(JavaClassInstanceCreationExpressionContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionStart(context(ctx));
	}
	
	@Override
	public void enterClassInstanceCreationExpressionSimple(ClassInstanceCreationExpressionSimpleContext ctx) {
		
		delegate.onJavaClassInstanceCreationConstructorName(context(ctx), Arrays.asList(ctx.Identifier().getText()));
	}

	@Override
	public void enterClassInstanceCreationExpressionMultipleIdentifiers(
			ClassInstanceCreationExpressionMultipleIdentifiersContext ctx) {
		
		delegate.onJavaClassInstanceCreationConstructorName(
				context(ctx),
				ctx.Identifier().stream()
				.map(identifier -> identifier.getText())
				.collect(Collectors.toList()));
	}

	@Override
	public void enterClassInstanceCreationExpressionWithExpressionName(
			ClassInstanceCreationExpressionWithExpressionNameContext ctx) {
		delegate.onJavaClassInstanceCreationConstructorName(context(ctx), Arrays.asList(ctx.Identifier().getText()));
	}

	@Override
	public void exitJavaClassInstanceCreationExpression(JavaClassInstanceCreationExpressionContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionEnd(context(ctx));
	}

	@Override
	public void enterNoObjectMethodInvocation_lfno_primary(NoObjectMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.NO_OBJECT, null, ctx.methodName().getText());
	}

	@Override
	public void exitNoObjectMethodInvocation_lfno_primary(NoObjectMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterStaticMethodInvocation_lfno_primary(StaticMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(
				context(ctx),
				MethodInvocationType.NAMED_CLASS_STATIC,
				new ResolveLaterTypeReference(context((ParserRuleContext)ctx.typeName().getRuleContext()), ctx.typeName().getText()),
				ctx.Identifier().getText());
	}

	@Override
	public void exitStaticMethodInvocation_lfno_primary(StaticMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterObjectMethodInvocation_lfno_primary(ObjectMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.VARIABLE_REFERENCE, null, ctx.Identifier().getText());
	}

	@Override
	public void exitObjectMethodInvocation_lfno_primary(ObjectMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterSuperMethodInvocation_lfno_primary(SuperMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.SUPER, null, ctx.Identifier().getText());
	}

	@Override
	public void exitSuperMethodInvocation_lfno_primary(SuperMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterTypeSuperMethodInvocation_lfno_primary(TypeSuperMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(
				context(ctx),
				MethodInvocationType.TYPED_SUPER,
				new ResolveLaterTypeReference(context((ParserRuleContext)ctx.typeName().getRuleContext()), ctx.typeName().getText()),
				ctx.Identifier().getText());
	}

	@Override
	public void exitTypeSuperMethodInvocation_lfno_primary(TypeSuperMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterNoObjectMethodInvocation(NoObjectMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.NO_OBJECT, null, ctx.methodName().getText());
	}

	@Override
	public void exitNoObjectMethodInvocation(NoObjectMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterStaticMethodInvocation(StaticMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(
				context(ctx),
				MethodInvocationType.NAMED_CLASS_STATIC,
				new ResolveLaterTypeReference(context((ParserRuleContext)ctx.typeName().getRuleContext()), ctx.typeName().getText()),
				ctx.Identifier().getText());
	}

	@Override
	public void exitStaticMethodInvocation(StaticMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterObjectMethodInvocation(ObjectMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.VARIABLE_REFERENCE, null, ctx.Identifier().getText());
	}

	@Override
	public void exitObjectMethodInvocation(ObjectMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterExpressionMethodInvocation(ExpressionMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.EXPRESSION, null, ctx.Identifier().getText());
	}

	@Override
	public void exitExpressionMethodInvocation(ExpressionMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterSuperMethodInvocation(SuperMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.SUPER, null, ctx.Identifier().getText());
	}

	@Override
	public void exitSuperMethodInvocation(SuperMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterTypeSuperMethodInvocation(TypeSuperMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(
				context(ctx),
				MethodInvocationType.TYPED_SUPER,
				new ResolveLaterTypeReference(context((ParserRuleContext)ctx.typeName().getRuleContext()), ctx.typeName().getText()),
				ctx.Identifier().getText());
	}

	@Override
	public void exitTypeSuperMethodInvocation(TypeSuperMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterArgumentList(ArgumentListContext ctx) {
		delegate.onParametersStart(context(ctx));
	}

	@Override
	public void exitArgumentList(ArgumentListContext ctx) {
		delegate.onParametersEnd(context(ctx));
	}
	
	// Conditions
	

	@Override
	public void exitLogicalOrExpression(LogicalOrExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Logical.OR);
	}

	@Override
	public void exitLogicalAndExpression(LogicalAndExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Logical.AND);
	}
	
	@Override
	public void exitRelationalEqualsExpression(RelationalEqualsExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Relational.EQUALS);
	}

	@Override
	public void exitRelationalNotEqualsExpression(RelationalNotEqualsExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Relational.NOT_EQUALS);
	}
	
	// Literals
	
	@Override
	public void exitIntegerLiteral(IntegerLiteralContext ctx) {
		delegate.onJavaIntegerLiteral(context(ctx), ctx.getText());
	}

	@Override
	public void exitFloatingPointLiteral(FloatingPointLiteralContext ctx) {
		delegate.onJavaFloatingPointLiteral(context(ctx), ctx.getText());
	}

	@Override
	public void exitBooleanLiteral(BooleanLiteralContext ctx) {
		delegate.onJavaBooleanLiteral(context(ctx), ctx.getText());
	}
	
	@Override
	public void exitCharacterLiteral(CharacterLiteralContext ctx) {
		delegate.onJavaCharacterLiteral(context(ctx), ctx.getText());
	}

	@Override
	public void exitStringLiteral(StringLiteralContext ctx) {
		delegate.onJavaStringLiteral(context(ctx), ctx.getText());
	}

	@Override
	public void exitNullLiteral(NullLiteralContext ctx) {
		delegate.onJavaNullLiteral(context(ctx), ctx.getText());
	}

	// Types

	@Override
	public void exitByteType(ByteTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.BYTE);
	}

	@Override
	public void exitShortType(ShortTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.SHORT);
	}

	@Override
	public void exitIntType(IntTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.INT);
	}

	@Override
	public void exitLongType(LongTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.LONG);
	}

	@Override
	public void exitCharType(CharTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.CHAR);
	}

	@Override
	public void exitFloatType(FloatTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.FLOAT);
	}

	@Override
	public void exitDoubleType(DoubleTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.DOUBLE);
	}

	@Override
	public void exitBooleanType(BooleanTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.BOOLEAN);
	}
	
	@Override
	public void exitClassOrInterfaceReferenceType(ClassOrInterfaceReferenceTypeContext ctx) {
		delegate.onJavaClassOrInterfaceReferenceType(context(ctx), ctx.getText());
	}

	@Override
	public void exitTypeVariableReferenceType(TypeVariableReferenceTypeContext ctx) {
		delegate.onJavaTypeVariableReferenceType(context(ctx), ctx.getText());
	}

	
	// Statements

	@Override
	public void exitFinalVariableModifier(FinalVariableModifierContext ctx) {
		delegate.onMutabilityVariableModifier(context(ctx), Mutability.VALUE_OR_REF_IMMUTABLE);
	}

	@Override
	public void enterLocalVariableDeclarationStatement(LocalVariableDeclarationStatementContext ctx) {
		delegate.onVariableDeclarationStatementStart(context(ctx));
	}

	@Override
	public void exitLocalVariableDeclarationStatement(LocalVariableDeclarationStatementContext ctx) {
		delegate.onVariableDeclarationStatementEnd(context(ctx));
	}

	private static final int countDims(String text) {
		return Strings.countOccurencesOf(text, "[]");
	}
	
	@Override
	public void enterVariableDeclarator(VariableDeclaratorContext ctx) {
		delegate.onVariableDeclaratorStart(context(ctx));
	}

	@Override
	public void exitVariableDeclarator(VariableDeclaratorContext ctx) {
		delegate.onVariableDeclaratorEnd(context(ctx));
	}
	
	@Override
	public void exitVariableDeclaratorId(VariableDeclaratorIdContext ctx) {
		delegate.onVariableName(
				context(ctx),
				ctx.Identifier().getText(),
				ctx.dims() != null
					? countDims(ctx.dims().getText())
					: 0);
	}

	// Hack to construct proper if-else if-else sequences
	@Override
	public void enterIfThenStatement(IfThenStatementContext ctx) {
		delegate.onJavaIfThenStatementStart(context(ctx));
	}

	@Override
	public void exitIfThenStatement(IfThenStatementContext ctx) {
		delegate.onJavaIfThenStatementEnd(context(ctx));
	}

	@Override
	public void enterIfThenElseStatement(IfThenElseStatementContext ctx) {
		delegate.onJavaIfThenElseStatementStart(context(ctx));
	}

	@Override
	public void exitIfThenElseStatement(IfThenElseStatementContext ctx) {
		delegate.onJavaIfThenElseStatementEnd(context(ctx));
	}
	
	@Override
	public void enterIfThenElseStatementNoShortIf(IfThenElseStatementNoShortIfContext ctx) {
		delegate.onJavaIfThenElseStatementStart(context(ctx));
	}

	@Override
	public void exitIfThenElseStatementNoShortIf(IfThenElseStatementNoShortIfContext ctx) {
		delegate.onJavaIfThenElseStatementEnd(context(ctx));
	}

	@Override
	public void enterBlock(BlockContext ctx) {
		delegate.onJavaBlockStart(context(ctx));
	}

	@Override
	public void exitBlock(BlockContext ctx) {
		delegate.onJavaBlockEnd(context(ctx));
	}

	@Override
	public void enterAssertStatement(AssertStatementContext ctx) {
		delegate.onJavaAssertStatementStart(context(ctx));
	}

	@Override
	public void exitAssertStatement(AssertStatementContext ctx) {
		delegate.onJavaAssertStatementEnd(context(ctx));
	}

	@Override
	public void enterExpressionStatement(ExpressionStatementContext ctx) {
		delegate.onJavaExpressionStatementStart(context(ctx));
	}

	@Override
	public void exitExpressionStatement(ExpressionStatementContext ctx) {
		delegate.onJavaExpressionStatementEnd(context(ctx));
	}

	@Override
	public void enterSwitchStatement(SwitchStatementContext ctx) {
		delegate.onSwitchStatementStart(context(ctx));
	}
	
	@Override
	public void enterSwitchBlock(SwitchBlockContext ctx) {
		delegate.onJavaSwitchBlockStart(context(ctx));
	}

	@Override
	public void enterSwitchBlockStatementGroup(SwitchBlockStatementGroupContext ctx) {
		delegate.onJavaSwitchBlockStatementGroupStart(context(ctx));
	}
	
	@Override
	public void enterSwitchLabels(SwitchLabelsContext ctx) {
		delegate.onSwitchLabelsStart(context(ctx));
	}

	@Override
	public void exitSwitchLabels(SwitchLabelsContext ctx) {
		delegate.onSwitchLabelsEnd(context(ctx));
	}

	@Override
	public void exitSwitchBlockStatementGroup(SwitchBlockStatementGroupContext ctx) {
		delegate.onJavaSwitchBlockStatementGroupEnd(context(ctx));
	}
	
	@Override
	public void enterConstantExpressionSwitchLabel(ConstantExpressionSwitchLabelContext ctx) {
		delegate.onConstantSwitchLabelStart(context(ctx));
	}

	@Override
	public void exitConstantExpressionSwitchLabel(ConstantExpressionSwitchLabelContext ctx) {
		delegate.onConstantSwitchLabelEnd(context(ctx));
	}
	
	@Override
	public void enterEnumConstantNameSwitchLabel(EnumConstantNameSwitchLabelContext ctx) {
		delegate.onEnumSwitchLabelStart(context(ctx));
	}

	@Override
	public void exitEnumConstantNameSwitchLabel(EnumConstantNameSwitchLabelContext ctx) {
		delegate.onEnumSwitchLabelEnd(context(ctx));
	}
	
	@Override
	public void exitDefaultSwitchLabel(DefaultSwitchLabelContext ctx) {
		delegate.onDefaultSwitchLabel(context(ctx));
	}

	@Override
	public void exitSwitchBlock(SwitchBlockContext ctx) {
		delegate.onJavaSwitchBlockEnd(context(ctx));
	}

	@Override
	public void exitSwitchStatement(SwitchStatementContext ctx) {
		delegate.onSwitchStatementEnd(context(ctx));
	}

	@Override
	public void exitBreakStatement(BreakStatementContext ctx) {
		delegate.onBreakStatement(context(ctx), ctx.Identifier().getText());
	}

	@Override
	public void enterWhileStatement(WhileStatementContext ctx) {
		delegate.onWhileStatementStart(context(ctx));
	}

	@Override
	public void exitWhileStatement(WhileStatementContext ctx) {
		delegate.onWhileStatementEnd(context(ctx));
	}

	@Override
	public void enterWhileStatementNoShortIf(WhileStatementNoShortIfContext ctx) {
		delegate.onWhileStatementStart(context(ctx));
	}

	@Override
	public void exitWhileStatementNoShortIf(WhileStatementNoShortIfContext ctx) {
		delegate.onWhileStatementEnd(context(ctx));
	}

	@Override
	public void enterDoStatement(DoStatementContext ctx) {
		delegate.onDoStatementStart(context(ctx));
	}

	@Override
	public void exitDoStatement(DoStatementContext ctx) {
		delegate.onDoStatementEnd(context(ctx));
	}

	@Override
	public void enterForStatement(ForStatementContext ctx) {
		delegate.onForStatementStart(context(ctx));
	}

	@Override
	public void exitForStatement(ForStatementContext ctx) {
		delegate.onForStatementEnd(context(ctx));
	}

	@Override
	public void enterForStatementNoShortIf(ForStatementNoShortIfContext ctx) {
		delegate.onForStatementStart(context(ctx));
	}

	@Override
	public void exitForStatementNoShortIf(ForStatementNoShortIfContext ctx) {
		delegate.onForStatementEnd(context(ctx));
	}
	
	@Override
	public void enterEnhancedForStatement(EnhancedForStatementContext ctx) {
		delegate.onIteratorForStatementStart(context(ctx));
	}

	@Override
	public void exitEnhancedForStatement(EnhancedForStatementContext ctx) {
		delegate.onIteratorForStatementEnd(context(ctx));
	}

	@Override
	public void enterEnhancedForStatementNoShortIf(EnhancedForStatementNoShortIfContext ctx) {
		delegate.onIteratorForStatementStart(context(ctx));
	}

	@Override
	public void exitEnhancedForStatementNoShortIf(EnhancedForStatementNoShortIfContext ctx) {
		delegate.onIteratorForStatementEnd(context(ctx));
	}

	@Override
	public void enterForInit(ForInitContext ctx) {
		delegate.onForInitStart(context(ctx));
	}

	@Override
	public void exitForInit(ForInitContext ctx) {
		delegate.onForInitEnd(context(ctx));
	}

	@Override
	public void enterForUpdate(ForUpdateContext ctx) {
		delegate.onForUpdateStart(context(ctx));
	}

	@Override
	public void exitForUpdate(ForUpdateContext ctx) {
		delegate.onForUpdateEnd(context(ctx));
	}

	@Override
	public void exitContinueStatement(ContinueStatementContext ctx) {
		delegate.onContinueStatement(context(ctx), ctx.Identifier().getText());
	}

	@Override
	public void enterReturnStatement(ReturnStatementContext ctx) {
		delegate.onReturnStatementStart(context(ctx));
	}

	@Override
	public void exitReturnStatement(ReturnStatementContext ctx) {
		delegate.onReturnStatementEnd(context(ctx));
	}

	@Override
	public void enterSynchronizedStatement(SynchronizedStatementContext ctx) {
		delegate.onJavaSynchronizedStatementStart(context(ctx));
	}

	@Override
	public void exitSynchronizedStatement(SynchronizedStatementContext ctx) {
		delegate.onJavaSynchronizedStatementEnd(context(ctx));
	}

	@Override
	public void enterTryCatch(TryCatchContext ctx) {
		delegate.onTryStatementStart(context(ctx));
	}

	@Override
	public void exitTryCatch(TryCatchContext ctx) {
		delegate.onTryStatementEnd(context(ctx));
	}

	@Override
	public void enterTryCatchFinally(TryCatchFinallyContext ctx) {
		delegate.onTryStatementStart(context(ctx));
	}

	@Override
	public void exitTryCatchFinally(TryCatchFinallyContext ctx) {
		delegate.onTryStatementEnd(context(ctx));
	}

	@Override
	public void enterTryWithResources(TryWithResourcesContext ctx) {
		delegate.onJavaTryWithResourcesStart(context(ctx));
	}

	@Override
	public void enterResourceSpecification(ResourceSpecificationContext ctx) {
		delegate.onJavaTryWithResourcesSpecificationStart(context(ctx));
	}
	
	@Override
	public void enterResource(ResourceContext ctx) {
		delegate.onJavaResourceStart(context(ctx));
	}

	@Override
	public void exitResource(ResourceContext ctx) {
		delegate.onJavaResourceEnd(context(ctx));
	}

	@Override
	public void exitResourceSpecification(ResourceSpecificationContext ctx) {
		delegate.onJavaTryWithResourcesSpecificationEnd(context(ctx));
	}

	@Override
	public void exitTryWithResources(TryWithResourcesContext ctx) {
		delegate.onJavaTryWithResourcesEnd(context(ctx));
	}
	
	
}

