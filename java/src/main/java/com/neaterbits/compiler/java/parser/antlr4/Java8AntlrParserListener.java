package com.neaterbits.compiler.java.parser.antlr4;

import com.neaterbits.compiler.java.Java8BaseListener;
import com.neaterbits.compiler.java.Java8Parser;
import com.neaterbits.compiler.java.Java8Parser.AbstractClassModifierContext;
import com.neaterbits.compiler.java.Java8Parser.AbstractMethodModifierContext;
import com.neaterbits.compiler.java.Java8Parser.AssertStatementContext;
import com.neaterbits.compiler.java.Java8Parser.AssignmentContext;
import com.neaterbits.compiler.java.Java8Parser.BlockContext;
import com.neaterbits.compiler.java.Java8Parser.BooleanLiteralContext;
import com.neaterbits.compiler.java.Java8Parser.BooleanTypeContext;
import com.neaterbits.compiler.java.Java8Parser.BreakStatementContext;
import com.neaterbits.compiler.java.Java8Parser.ByteTypeContext;
import com.neaterbits.compiler.java.Java8Parser.CharTypeContext;
import com.neaterbits.compiler.java.Java8Parser.CharacterLiteralContext;
import com.neaterbits.compiler.java.Java8Parser.ClassInstanceCreationExpression_lfno_primaryContext;
import com.neaterbits.compiler.java.Java8Parser.CompilationUnitContext;
import com.neaterbits.compiler.java.Java8Parser.ConditionalAndExpressionContext;
import com.neaterbits.compiler.java.Java8Parser.ConditionalOrExpressionContext;
import com.neaterbits.compiler.java.Java8Parser.ConstantExpressionSwitchLabelContext;
import com.neaterbits.compiler.java.Java8Parser.ContinueStatementContext;
import com.neaterbits.compiler.java.Java8Parser.DefaultSwitchLabelContext;
import com.neaterbits.compiler.java.Java8Parser.DoStatementContext;
import com.neaterbits.compiler.java.Java8Parser.DoubleTypeContext;
import com.neaterbits.compiler.java.Java8Parser.EnumConstantNameSwitchLabelContext;
import com.neaterbits.compiler.java.Java8Parser.ExpressionStatementContext;
import com.neaterbits.compiler.java.Java8Parser.FinalClassModifierContext;
import com.neaterbits.compiler.java.Java8Parser.FinalMethodModifierContext;
import com.neaterbits.compiler.java.Java8Parser.FinalVariableModifierContext;
import com.neaterbits.compiler.java.Java8Parser.FloatTypeContext;
import com.neaterbits.compiler.java.Java8Parser.FloatingPointLiteralContext;
import com.neaterbits.compiler.java.Java8Parser.ForInitContext;
import com.neaterbits.compiler.java.Java8Parser.ForStatementContext;
import com.neaterbits.compiler.java.Java8Parser.ForStatementNoShortIfContext;
import com.neaterbits.compiler.java.Java8Parser.ForUpdateContext;
import com.neaterbits.compiler.java.Java8Parser.IfThenElseStatementContext;
import com.neaterbits.compiler.java.Java8Parser.IfThenElseStatementNoShortIfContext;
import com.neaterbits.compiler.java.Java8Parser.IfThenStatementContext;
import com.neaterbits.compiler.java.Java8Parser.IntTypeContext;
import com.neaterbits.compiler.java.Java8Parser.IntegerLiteralContext;
import com.neaterbits.compiler.java.Java8Parser.LeftHandSideContext;
import com.neaterbits.compiler.java.Java8Parser.LocalVariableDeclarationStatementContext;
import com.neaterbits.compiler.java.Java8Parser.LongTypeContext;
import com.neaterbits.compiler.java.Java8Parser.MethodDeclarationContext;
import com.neaterbits.compiler.java.Java8Parser.MethodDeclaratorContext;
import com.neaterbits.compiler.java.Java8Parser.NativeMethodModifierContext;
import com.neaterbits.compiler.java.Java8Parser.NormalClassDeclarationContext;
import com.neaterbits.compiler.java.Java8Parser.NullLiteralContext;
import com.neaterbits.compiler.java.Java8Parser.PackageDeclarationContext;
import com.neaterbits.compiler.java.Java8Parser.PrivateClassModifierContext;
import com.neaterbits.compiler.java.Java8Parser.PrivateMethodModifierContext;
import com.neaterbits.compiler.java.Java8Parser.ProtectedClassModifierContext;
import com.neaterbits.compiler.java.Java8Parser.ProtectedMethodModifierContext;
import com.neaterbits.compiler.java.Java8Parser.PublicClassModifierContext;
import com.neaterbits.compiler.java.Java8Parser.PublicMethodMofifierContext;
import com.neaterbits.compiler.java.Java8Parser.ReturnStatementContext;
import com.neaterbits.compiler.java.Java8Parser.ShortTypeContext;
import com.neaterbits.compiler.java.Java8Parser.SingleStaticImportDeclarationContext;
import com.neaterbits.compiler.java.Java8Parser.SingleTypeImportDeclarationContext;
import com.neaterbits.compiler.java.Java8Parser.StaticClassModifierContext;
import com.neaterbits.compiler.java.Java8Parser.StaticImportOnDemandDeclarationContext;
import com.neaterbits.compiler.java.Java8Parser.StaticMethodModifierContext;
import com.neaterbits.compiler.java.Java8Parser.StrictfpClassModifierContext;
import com.neaterbits.compiler.java.Java8Parser.StrictfpMethodModifierContext;
import com.neaterbits.compiler.java.Java8Parser.StringLiteralContext;
import com.neaterbits.compiler.java.Java8Parser.SwitchBlockContext;
import com.neaterbits.compiler.java.Java8Parser.SwitchBlockStatementGroupContext;
import com.neaterbits.compiler.java.Java8Parser.SwitchLabelsContext;
import com.neaterbits.compiler.java.Java8Parser.SwitchStatementContext;
import com.neaterbits.compiler.java.Java8Parser.SynchronizedMethodModifierContext;
import com.neaterbits.compiler.java.Java8Parser.SynchronizedStatementContext;
import com.neaterbits.compiler.java.Java8Parser.TryCatchContext;
import com.neaterbits.compiler.java.Java8Parser.TryCatchFinallyContext;
import com.neaterbits.compiler.java.Java8Parser.TryWithResourcesContext;
import com.neaterbits.compiler.java.Java8Parser.TypeImportOnDemandDeclarationContext;
import com.neaterbits.compiler.java.Java8Parser.VariableDeclaratorContext;
import com.neaterbits.compiler.java.Java8Parser.WhileStatementContext;
import com.neaterbits.compiler.java.Java8Parser.WhileStatementNoShortIfContext;
import com.neaterbits.compiler.java.parser.JavaParserListener;
import com.neaterbits.compiler.java.parser.JavaPrimitiveType;

import static com.neaterbits.compiler.common.antlr4.Antlr4.context;

import org.antlr.v4.runtime.ParserRuleContext;

import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.ast.NamespaceName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.statement.VariableMutability;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.MethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.MethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.util.Strings;

public class Java8AntlrParserListener extends Java8BaseListener {

	private final JavaParserListener delegate;

	public Java8AntlrParserListener(JavaParserListener delegate) {
		this.delegate = delegate;
	}

	
	private String ruleName(ParserRuleContext ctx) {
		return Java8Parser.ruleNames[ctx.getRuleIndex()];
	}
	
	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		System.out.println("## enter " + ruleName(ctx) + ": " + ctx.getText());
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		System.out.println("## exit " + ruleName(ctx) + ": " + ctx.getText());
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
		delegate.onVisibilityClassModifier(ClassVisibility.PUBLIC);
	}

	@Override
	public void exitProtectedClassModifier(ProtectedClassModifierContext ctx) {
		delegate.onVisibilityClassModifier(ClassVisibility.NAMESPACE);
	}

	@Override
	public void exitPrivateClassModifier(PrivateClassModifierContext ctx) {
		delegate.onVisibilityClassModifier(ClassVisibility.PRIVATE);
	}

	@Override
	public void exitAbstractClassModifier(AbstractClassModifierContext ctx) {
		delegate.onSubclassingModifier(Subclassing.ABSTRACT);
	}
	
	@Override
	public void exitFinalClassModifier(FinalClassModifierContext ctx) {
		delegate.onSubclassingModifier(Subclassing.FINAL);
	}

	@Override
	public void exitStaticClassModifier(StaticClassModifierContext ctx) {
		delegate.onStaticClassModifier();
	}

	@Override
	public void exitStrictfpClassModifier(StrictfpClassModifierContext ctx) {
		delegate.onStrictfpClassModifier();
	}

	@Override
	public void exitNormalClassDeclaration(NormalClassDeclarationContext ctx) {
		delegate.onClassEnd(context(ctx));
	}
	

	@Override
	public void enterClassInstanceCreationExpression_lfno_primary(ClassInstanceCreationExpression_lfno_primaryContext ctx) {
		
		if (ctx.classBody() != null && !ctx.classBody().isEmpty()) {
			delegate.onAnonymousClassStart(context(ctx));
		}
	}

	@Override
	public void exitClassInstanceCreationExpression_lfno_primary(ClassInstanceCreationExpression_lfno_primaryContext ctx) {
		if (ctx.classBody() != null && !ctx.classBody().isEmpty()) {
			delegate.onAnonymousClassEnd(context(ctx));
		}
	}

	@Override
	public void enterMethodDeclaration(MethodDeclarationContext ctx) {
		
		System.out.println("## enterMethodDeclaration: " + ctx.getText());
		
		delegate.onMethodStart(context(ctx));
	}
	
	@Override
	public void enterMethodDeclarator(MethodDeclaratorContext ctx) {
		delegate.onMethodName(context(ctx), ctx.Identifier().getText());
	}


	@Override
	public void exitPublicMethodMofifier(PublicMethodMofifierContext ctx) {
		delegate.onVisibilityMethodModifier(MethodVisibility.PUBLIC);
	}

	@Override
	public void exitProtectedMethodModifier(ProtectedMethodModifierContext ctx) {
		delegate.onVisibilityMethodModifier(MethodVisibility.NAMESPACE_AND_SUBCLASSES);
	}

	@Override
	public void exitPrivateMethodModifier(PrivateMethodModifierContext ctx) {
		delegate.onVisibilityMethodModifier(MethodVisibility.PRIVATE);
	}
	
	@Override
	public void exitAbstractMethodModifier(AbstractMethodModifierContext ctx) {
		delegate.onOverrideModifier(MethodOverride.ABSTRACT);
	}

	@Override
	public void exitFinalMethodModifier(FinalMethodModifierContext ctx) {
		delegate.onOverrideModifier(MethodOverride.FINAL);
	}

	@Override
	public void exitStaticMethodModifier(StaticMethodModifierContext ctx) {
		delegate.onStaticMethodModifier();
	}

	@Override
	public void exitStrictfpMethodModifier(StrictfpMethodModifierContext ctx) {
		delegate.onStrictfpMethodModifier();
	}
	
	@Override
	public void exitSynchronizedMethodModifier(SynchronizedMethodModifierContext ctx) {
		delegate.onSynchronizedMethodModifier();
	}
	
	@Override
	public void exitNativeMethodModifier(NativeMethodModifierContext ctx) {
		delegate.onNativeMethodModifier();
	}

	@Override
	public void exitMethodDeclaration(MethodDeclarationContext ctx) {
		System.out.println("## exitMethodDeclaration: " + ctx.getText());

		delegate.onMethodEnd(context(ctx));
	}

	// Expressions
	@Override
	public void enterAssignment(AssignmentContext ctx) {
		delegate.onEnterAssignmentExpression(context(ctx));
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

	// Conditions
	@Override
	public void enterConditionalOrExpression(ConditionalOrExpressionContext ctx) {
	}

	@Override
	public void exitConditionalOrExpression(ConditionalOrExpressionContext ctx) {
		// TODO Auto-generated method stub
		super.enterConditionalOrExpression(ctx);
	}


	@Override
	public void enterConditionalAndExpression(ConditionalAndExpressionContext ctx) {
		// TODO Auto-generated method stub
		super.enterConditionalAndExpression(ctx);
	}

	@Override
	public void exitConditionalAndExpression(ConditionalAndExpressionContext ctx) {
		// TODO Auto-generated method stub
		super.enterConditionalAndExpression(ctx);
	}
	
	
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

	
	// Statements

	@Override
	public void exitFinalVariableModifier(FinalVariableModifierContext ctx) {
		delegate.onMutabilityVariableModifier(VariableMutability.VALUE_OR_REF_IMMUTABLE);
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
		delegate.onVariableDeclaratorStart(
				context(ctx),
				ctx.variableDeclaratorId().Identifier().getText(),
				ctx.variableDeclaratorId().dims() != null
					? countDims(ctx.variableDeclaratorId().dims().getText())
					: 0);
	}

	@Override
	public void exitVariableDeclarator(VariableDeclaratorContext ctx) {
		delegate.onVariableDeclaratorEnd(context(ctx));
	}
	
	
	// Hack to construct proper if-else if-else sequences


	@Override
	public void enterIfThenStatement(IfThenStatementContext ctx) {
		delegate.onJavaIfStatementStart(context(ctx));
	}


	@Override
	public void exitIfThenStatement(IfThenStatementContext ctx) {
		delegate.onJavaIfStatementEnd(context(ctx));
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
	public void exitTryWithResources(TryWithResourcesContext ctx) {
		delegate.onJavaTryWithResourcesEnd(context(ctx));
	}
}

