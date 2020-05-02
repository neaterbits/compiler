package com.neaterbits.compiler.java.parser.antlr4;

import com.neaterbits.compiler.java.Java8BaseListener;
import com.neaterbits.compiler.java.Java8Parser;
import com.neaterbits.compiler.java.Java8Parser.*;
import com.neaterbits.compiler.java.parser.JavaParserListener;
import com.neaterbits.compiler.java.parser.JavaPrimitiveType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.block.ConstructorInvocation;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.model.Visibility;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Bitwise;
import com.neaterbits.compiler.util.operator.Logical;
import com.neaterbits.compiler.util.operator.Notation;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.statement.ASTMutability;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.util.io.strings.OffsetLengthStringRef;
import com.neaterbits.util.io.strings.StringRef;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neaterbits.compiler.antlr4.Antlr4;

public class Java8AntlrParserListener extends Java8BaseListener {

	private final JavaParserListener delegate;
	
	private static final Boolean DEBUG = false;
	
	private final boolean debug;
	private final String file;
	private final ParseLogger logger;
	
	private int indent = 0;

	public Java8AntlrParserListener(JavaParserListener delegate, boolean debug, String file, ParseLogger logger) {

		this.delegate = delegate;
		this.debug = debug;
		this.file = file;
		this.logger = logger;
	}
	
	private boolean isDebugEnabled() {
		return debug && DEBUG;
	}
	
	private String ruleName(ParserRuleContext ctx) {
		return Java8Parser.ruleNames[ctx.getRuleIndex()];
	}
	
	private String indent() {
		return Strings.indent(indent);
	}
	
	private Context context(ParserRuleContext ctx) {
		return Antlr4.context(ctx, file);
	}

	private Context context(TerminalNode ctx) {
		return Antlr4.context(ctx.getSymbol(), file);
	}

	private Context context(List<TerminalNode> ctx) {
		
		return Antlr4.context(
				ctx.get(0).getSymbol(),
				ctx.get(ctx.size() - 1).getSymbol(),
				Strings.join(ctx, ' ', node -> node.getText()),
				file);
	}

	private Context context(Token ctx) {
		return Antlr4.context(ctx, file);
	}

	private static long stringRef(ParserRuleContext ctx) {
		return OffsetLengthStringRef.encode(ctx.start.getStartIndex(), ctx.getText().length());
	}

	private static long stringRef(Token ctx) {
		return OffsetLengthStringRef.encode(ctx.getStartIndex(), ctx.getText().length());
	}

	private static long stringRef(TerminalNode ctx) {
		return stringRef(ctx.getSymbol());
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		
		if (isDebugEnabled()) {
			System.out.println("## enter " + indent() + ruleName(ctx) + " " + ctx.getText());
		}
		
		++ indent;

		if (logger != null) {
			logger.onEnterAntlrRule(ruleName(ctx), ctx.getText());
		}
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		
		if (isDebugEnabled()) {
			System.out.println("## exit " + indent() + ruleName(ctx) + " " + ctx.getText());
		}
		
		-- indent;

		if (logger != null) {
			logger.onExitAntlrRule(ruleName(ctx), ctx.getText());
		}
	}

	@Override
	public void enterCompilationUnit(CompilationUnitContext ctx) {
		delegate.onCompilationUnitStart(context(ctx));
	}

	@Override
	public void exitCompilationUnit(CompilationUnitContext ctx) {
		delegate.onCompilationUnitEnd(context(ctx));
	}
	
	@Override
	public void exitPackageDeclaration(PackageDeclarationContext ctx) {

		delegate.onPackageDeclaration(
				context(ctx),
				stringRef(ctx.keyword), context(ctx.keyword),
				stringRef(ctx.packageName()), context(ctx.packageName()));
	}

	
	
	@Override
	public void enterSingleTypeImportDeclaration(SingleTypeImportDeclarationContext ctx) {
		
		delegate.onImportStart(
				context(ctx),
				stringRef(ctx.keyword),
				context(ctx.keyword),
				StringRef.STRING_NONE,
				null);
	}
	
	
	@Override
	public void exitSingleTypeImportDeclaration(SingleTypeImportDeclarationContext ctx) {
		delegate.onImportEnd(context(ctx), false);
	}

	@Override
	public void enterTypeImportOnDemandDeclaration(TypeImportOnDemandDeclarationContext ctx) {
		
		delegate.onImportStart(
				context(ctx),
				stringRef(ctx.keyword),
				context(ctx.keyword),
				StringRef.STRING_NONE,
				null);
	}

	@Override
	public void exitTypeImportOnDemandDeclaration(TypeImportOnDemandDeclarationContext ctx) {
		
		delegate.onImportEnd(context(ctx), true);
	}

	@Override
	public void enterSingleStaticImportDeclaration(SingleStaticImportDeclarationContext ctx) {
		
		delegate.onImportStart(
				context(ctx),
				stringRef(ctx.keyword),
				context(ctx.keyword),
				stringRef(ctx.modifier),
				context(ctx.modifier));
	}

	@Override
	public void exitSingleStaticImportDeclaration(SingleStaticImportDeclarationContext ctx) {

		delegate.onImportEnd(context(ctx), false);
	}

	@Override
	public void enterStaticImportOnDemandDeclaration(StaticImportOnDemandDeclarationContext ctx) {
		
		delegate.onImportStart(
				context(ctx),
				stringRef(ctx.keyword),
				context(ctx.keyword),
				stringRef(ctx.modifier),
				context(ctx.modifier));
	}

	@Override
	public void exitStaticImportOnDemandDeclaration(StaticImportOnDemandDeclarationContext ctx) {

		delegate.onImportEnd(context(ctx), true);
	}

	@Override
	public void enterNormalClassDeclaration(NormalClassDeclarationContext ctx) {
		delegate.onClassStart(
				context(ctx),
				stringRef(ctx.keyword), context(ctx.keyword),
				stringRef(ctx.Identifier()), context(ctx.Identifier()));
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
	public void enterSuperclass(SuperclassContext ctx) {
		delegate.onClassExtends(
				context(ctx),
				stringRef(ctx.keyword), context(ctx.keyword),
				parseName(ctx.classType().getText()));
	}

	@Override
	public void enterSuperinterfaces(com.neaterbits.compiler.java.Java8Parser.SuperinterfacesContext ctx) {
		for (InterfaceTypeContext interfaceCtx : ctx.interfaceTypeList().interfaceType()) {
			delegate.onClassImplements(context(ctx), parseName(interfaceCtx.getText()));
		}
	}

	@Override
	public void exitNormalClassDeclaration(NormalClassDeclarationContext ctx) {
		delegate.onClassEnd(context(ctx));
	}

	@Override
	public void enterMethodDeclaration(MethodDeclarationContext ctx) {
		
		delegate.onClassMethodStart(context(ctx));
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
	public void enterStaticInitializer(StaticInitializerContext ctx) {
		delegate.onStaticInitializerStart(context(ctx));
	}

	@Override
	public void exitStaticInitializer(StaticInitializerContext ctx) {
		delegate.onStaticInitializerEnd(context(ctx));
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
		delegate.onConstructorName(context(ctx.simpleTypeName()), stringRef(ctx.simpleTypeName()));
	}

	@Override
	public void enterThis_explicitConstructorInvocation(This_explicitConstructorInvocationContext ctx) {
		delegate.onConstructorInvocationStart(context(ctx), ConstructorInvocation.THIS);
	}

	@Override
	public void exitThis_explicitConstructorInvocation(This_explicitConstructorInvocationContext ctx) {
		delegate.onConstructorInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterSuper_explicitConstructorInvocation(Super_explicitConstructorInvocationContext ctx) {
		delegate.onConstructorInvocationStart(context(ctx), ConstructorInvocation.SUPER);
	}

	@Override
	public void exitSuper_explicitConstructorInvocation(Super_explicitConstructorInvocationContext ctx) {
		delegate.onConstructorInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterExpressionNameSuper_explicitConstructorInvocation(
			ExpressionNameSuper_explicitConstructorInvocationContext ctx) {

		delegate.onConstructorInvocationStart(context(ctx), ConstructorInvocation.EXPRESSIONNAME_SUPER);
	}

	@Override
	public void exitExpressionNameSuper_explicitConstructorInvocation(
			ExpressionNameSuper_explicitConstructorInvocationContext ctx) {

		delegate.onConstructorInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterPrimarySuper_explicitConstructorInvocation(PrimarySuper_explicitConstructorInvocationContext ctx) {
		delegate.onConstructorInvocationStart(context(ctx), ConstructorInvocation.PRIMARY_SUPER);
	}

	@Override
	public void exitPrimarySuper_explicitConstructorInvocation(PrimarySuper_explicitConstructorInvocationContext ctx) {
		delegate.onConstructorInvocationEnd(context(ctx));
	}

	@Override
	public void exitConstructorDeclaration(ConstructorDeclarationContext ctx) {
		delegate.onConstructorEnd(context(ctx));
	}

	@Override
	public void enterMethodDeclarator(MethodDeclaratorContext ctx) {
		delegate.onMethodName(context(ctx.Identifier()), stringRef(ctx.Identifier()));
	}
	
	@Override
	public void enterFormalParameterList(FormalParameterListContext ctx) {
		delegate.onMethodSignatureParametersStart(context(ctx));
	}

	@Override
	public void enterFormalParameter(FormalParameterContext ctx) {
		delegate.onMethodSignatureParameterStart(context(ctx), false);
	}

	@Override
	public void exitFormalParameter(FormalParameterContext ctx) {
		delegate.onMethodSignatureParameterEnd(context(ctx));
	}
	
	@Override
	public void enterLastFormalParameterVarArgs(LastFormalParameterVarArgsContext ctx) {
		delegate.onMethodSignatureParameterStart(context(ctx), true);
	}

	@Override
	public void exitLastFormalParameterVarArgs(LastFormalParameterVarArgsContext ctx) {
		delegate.onMethodSignatureParameterEnd(context(ctx));
	}

	@Override
	public void exitFormalParameterList(FormalParameterListContext ctx) {
		delegate.onMethodSignatureParametersEnd(context(ctx));
	}

	@Override
	public void exitPublicMethodModifier(PublicMethodModifierContext ctx) {
		delegate.onVisibilityClassMethodModifier(context(ctx), ClassMethodVisibility.PUBLIC);
	}

	@Override
	public void exitProtectedMethodModifier(ProtectedMethodModifierContext ctx) {
		delegate.onVisibilityClassMethodModifier(context(ctx), ClassMethodVisibility.NAMESPACE_AND_SUBCLASSES);
	}

	@Override
	public void exitPrivateMethodModifier(PrivateMethodModifierContext ctx) {
		delegate.onVisibilityClassMethodModifier(context(ctx), ClassMethodVisibility.PRIVATE);
	}
	
	@Override
	public void exitAbstractMethodModifier(AbstractMethodModifierContext ctx) {
		delegate.onOverrideClassMehodModifier(context(ctx), ClassMethodOverride.ABSTRACT);
	}

	@Override
	public void exitFinalMethodModifier(FinalMethodModifierContext ctx) {
		delegate.onOverrideClassMehodModifier(context(ctx), ClassMethodOverride.FINAL);
	}

	@Override
	public void exitStaticMethodModifier(StaticMethodModifierContext ctx) {
		delegate.onStaticClassMethodModifier(context(ctx));
	}

	@Override
	public void exitStrictfpMethodModifier(StrictfpMethodModifierContext ctx) {
		delegate.onStrictfpClassMethodModifier(context(ctx));
	}
	
	@Override
	public void exitSynchronizedMethodModifier(SynchronizedMethodModifierContext ctx) {
		delegate.onSynchronizedClassMethodModifier(context(ctx));
	}
	
	@Override
	public void exitNativeMethodModifier(NativeMethodModifierContext ctx) {
		delegate.onNativeClassMethodModifier(context(ctx));
	}

	@Override
	public void exitMethodDeclaration(MethodDeclarationContext ctx) {
		delegate.onClassMethodEnd(context(ctx));
	}
	
	@Override
	public void enterFieldDeclaration(FieldDeclarationContext ctx) {
		delegate.onFieldDeclarationStart(context(ctx));
	}

	@Override
	public void exitPublicFieldModifier(PublicFieldModifierContext ctx) {
		delegate.onVisibilityFieldModifier(context(ctx), new FieldVisibility(Visibility.PUBLIC));
	}

	@Override
	public void exitPrivateFieldModifier(PrivateFieldModifierContext ctx) {
		delegate.onVisibilityFieldModifier(context(ctx), new FieldVisibility(Visibility.PRIVATE));
	}

	@Override
	public void exitStaticFieldModifier(StaticFieldModifierContext ctx) {
		delegate.onStaticFieldModifier(context(ctx));
	}
	
	@Override
	public void exitFinalFieldModifier(FinalFieldModifierContext ctx) {
		delegate.onMutabilityFieldModifier(context(ctx), new ASTMutability(Mutability.VALUE_OR_REF_IMMUTABLE));
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


	// Interfaces
	
	@Override
	public void enterNormalInterfaceDeclaration(NormalInterfaceDeclarationContext ctx) {
		delegate.onInterfaceStart(
				context(ctx),
				stringRef(ctx.keyword), context(ctx.keyword),
				stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}
	
	@Override
	public void exitPublicInterfaceModifier(PublicInterfaceModifierContext ctx) {
		delegate.onVisibilityInterfaceModifier(context(ctx), InterfaceVisibility.PUBLIC);
	}

	@Override
	public void exitAbstractInterfaceModifier(AbstractInterfaceModifierContext ctx) {
		delegate.onAbstractInterfaceModifier(context(ctx));
	}

	@Override
	public void exitStaticInterfaceModifier(StaticInterfaceModifierContext ctx) {
		delegate.onStaticInterfaceModifier(context(ctx));
	}

	@Override
	public void exitStrictfpInterfaceModifier(StrictfpInterfaceModifierContext ctx) {
		delegate.onStrictfpInterfaceModifier(context(ctx));
	}

	@Override
	public void exitNormalInterfaceDeclaration(NormalInterfaceDeclarationContext ctx) {
		delegate.onInterfaceEnd(context(ctx));
	}

	@Override
	public void enterInterfaceMethodDeclaration(InterfaceMethodDeclarationContext ctx) {
		delegate.onInterfaceMethodStart(context(ctx));
	}

	@Override
	public void exitPublicInterfaceMethodModifier(PublicInterfaceMethodModifierContext ctx) {
		delegate.onVisibilityInterfaceMethodModifier(context(ctx), InterfaceMethodVisibility.PUBLIC);
	}

	@Override
	public void exitAbstractInterfaceMethodModifier(AbstractInterfaceMethodModifierContext ctx) {
		delegate.onAbstractInterfaceMethodModifier(context(ctx));
	}

	@Override
	public void exitStaticInterfaceMethodModifier(StaticInterfaceMethodModifierContext ctx) {
		delegate.onStaticInterfaceMethodModifier(context(ctx));
	}

	@Override
	public void exitStrictfpInterfaceMethodModifier(StrictfpInterfaceMethodModifierContext ctx) {
		delegate.onStrictfpInterfaceMethodModifier(context(ctx));
	}
	
	@Override
	public void enterExtendsInterfaces(ExtendsInterfacesContext ctx) {
		for (InterfaceTypeContext interfaceCtx : ctx.interfaceTypeList().interfaceType()) {
			delegate.onInterfaceExtends(context(ctx), parseName(interfaceCtx.getText()));
		}
	}

	@Override
	public void exitInterfaceMethodDeclaration(InterfaceMethodDeclarationContext ctx) {
		delegate.onInterfaceMethodEnd(context(ctx));
	}
	
	
	// Enums

	@Override
	public void enterEnumDeclaration(EnumDeclarationContext ctx) {
		delegate.onEnumStart(
				context(ctx),
				stringRef(ctx.keyword), context(ctx.keyword),
				stringRef(ctx.Identifier()), context(ctx.Identifier()));
		
		if (ctx.superinterfaces() != null) {
			for (InterfaceTypeContext interfaceCtx : ctx.superinterfaces().interfaceTypeList().interfaceType()) {
				delegate.onEnumImplements(context(ctx), parseName(interfaceCtx.getText()));
			}
		}
	}

	@Override
	public void enterEnumConstant(EnumConstantContext ctx) {
		delegate.onEnumConstantStart(context(ctx), stringRef(ctx.Identifier()));
	}

	@Override
	public void exitEnumConstant(EnumConstantContext ctx) {
		delegate.onEnumConstantEnd(context(ctx));
	}

	@Override
	public void exitEnumDeclaration(EnumDeclarationContext ctx) {
		delegate.onEnumEnd(context(ctx));
	}
	

	// Expressions
	@Override
	public void enterAssignment(AssignmentContext ctx) {
		delegate.onEnterAssignmentExpression(context(ctx));
	}

	@Override
	public void exitExpressionName(ExpressionNameContext ctx) {
		delegate.onNameReference(context(ctx), stringRef(ctx));
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
	public void exitBitwiseAndExpression(BitwiseAndExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Bitwise.AND);
	}

	@Override
	public void exitShiftLeftExpression(ShiftLeftExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Bitwise.LEFTSHIFT);
	}

	@Override
	public void exitShiftRightSignedExpression(ShiftRightSignedExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Bitwise.RIGHTSHIFT_SIGNED);
	}

	@Override
	public void exitShiftRightUnsignedExpression(ShiftRightUnsignedExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Bitwise.RIGHTSHIFT_UNSIGNED);
	}

	@Override
	public void exitNumericAddExpression(NumericAddExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Arithmetic.PLUS);
	}

	@Override
	public void exitNumericSubtractExpression(NumericSubtractExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Arithmetic.MINUS);
	}

	@Override
	public void exitMultiplicativeMultiplyExpression(MultiplicativeMultiplyExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Arithmetic.MULTIPLY);
	}

	@Override
	public void exitMultiplicativeDivideExpression(MultiplicativeDivideExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Arithmetic.DIVIDE);
	}

	@Override
	public void exitMultiplicativeModulusExpression(MultiplicativeModulusExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Arithmetic.MODULUS);
	}
	
	@Override
	public void exitLessThanOrEqualExpression(LessThanOrEqualExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Relational.LESS_THAN_OR_EQUALS);
	}

	@Override
	public void exitLessThanExpression(LessThanExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Relational.LESS_THAN);
	}

	@Override
	public void exitGreaterThanExpression(GreaterThanExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Relational.GREATER_THAN);
	}

	@Override
	public void exitGreaterThanOrEqualExpression(GreaterThanOrEqualExpressionContext ctx) {
		delegate.onExpressionBinaryOperator(context(ctx), Relational.GREATER_THAN_OR_EQUALS);
	}

	@Override
	public void enterPreIncrementExpression(PreIncrementExpressionContext ctx) {
		delegate.onIncrementDecrementExpressionStart(context(ctx), Arithmetic.INCREMENT, Notation.PREFIX);
	}

	@Override
	public void exitPreIncrementExpression(PreIncrementExpressionContext ctx) {
		delegate.onIncrementDecrementExpressionEnd(context(ctx));
	}

	@Override
	public void enterPreDecrementExpression(PreDecrementExpressionContext ctx) {
		delegate.onIncrementDecrementExpressionStart(context(ctx), Arithmetic.DECREMENT, Notation.PREFIX);
	}

	@Override
	public void exitPreDecrementExpression(PreDecrementExpressionContext ctx) {
		delegate.onIncrementDecrementExpressionEnd(context(ctx));
	}

	@Override
	public void enterPostIncrementExpression(PostIncrementExpressionContext ctx) {
		delegate.onIncrementDecrementExpressionStart(context(ctx), Arithmetic.INCREMENT, Notation.POSTFIX);
	}

	@Override
	public void exitPostIncrementExpression(PostIncrementExpressionContext ctx) {
		delegate.onIncrementDecrementExpressionEnd(context(ctx));
	}

	@Override
	public void enterPostDecrementExpression(PostDecrementExpressionContext ctx) {
		delegate.onIncrementDecrementExpressionStart(context(ctx), Arithmetic.DECREMENT, Notation.POSTFIX);
	}

	@Override
	public void exitPostDecrementExpression(PostDecrementExpressionContext ctx) {
		delegate.onIncrementDecrementExpressionEnd(context(ctx));
	}

	@Override
	public void enterJavaClassInstanceCreationExpression_primaryNoNewArray(
			JavaClassInstanceCreationExpression_primaryNoNewArrayContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionStart(context(ctx));
	}

	@Override
	public void enterJavaClassInstanceCreationExpression_primaryNoNewArray_lfno_arrayAccess(
			JavaClassInstanceCreationExpression_primaryNoNewArray_lfno_arrayAccessContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionStart(context(ctx));
	}

	@Override
	public void enterJavaClassInstanceCreationExpression_primaryNoNewArray_lfno_primary(
			JavaClassInstanceCreationExpression_primaryNoNewArray_lfno_primaryContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionStart(context(ctx));
	}

	@Override
	public void enterJavaClassInstanceCreateExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
			JavaClassInstanceCreateExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionStart(context(ctx));
	}
	
	@Override
	public void enterClassInstanceCreationExpressionSimple(ClassInstanceCreationExpressionSimpleContext ctx) {
		
		delegate.onJavaClassInstanceCreationConstructorName(context(ctx), new ScopedName(null, ctx.Identifier().getText()));
	}

	@Override
	public void enterClassInstanceCreationExpressionMultipleIdentifiers(
			ClassInstanceCreationExpressionMultipleIdentifiersContext ctx) {
		
		delegate.onJavaClassInstanceCreationConstructorName(
				context(ctx),
				makeScopedName(
					ctx.Identifier().stream()
					.map(identifier -> identifier.getText())
					.collect(Collectors.toList())));
	}

	@Override
	public void enterClassInstanceCreationExpressionWithExpressionName(
			ClassInstanceCreationExpressionWithExpressionNameContext ctx) {
		delegate.onJavaClassInstanceCreationConstructorName(context(ctx), new ScopedName(null, ctx.Identifier().getText()));
	}

	@Override
	public void exitJavaClassInstanceCreationExpression_primaryNoNewArray(
			JavaClassInstanceCreationExpression_primaryNoNewArrayContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionEnd(context(ctx));
	}

	@Override
	public void exitJavaClassInstanceCreationExpression_primaryNoNewArray_lfno_arrayAccess(
			JavaClassInstanceCreationExpression_primaryNoNewArray_lfno_arrayAccessContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionEnd(context(ctx));
	}

	@Override
	public void exitJavaClassInstanceCreationExpression_primaryNoNewArray_lfno_primary(
			JavaClassInstanceCreationExpression_primaryNoNewArray_lfno_primaryContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionEnd(context(ctx));
	}

	@Override
	public void exitJavaClassInstanceCreateExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
			JavaClassInstanceCreateExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
		delegate.onJavaClassInstanceCreationExpressionEnd(context(ctx));
	}


	@Override
	public void enterNoObjectMethodInvocation_lfno_primary(NoObjectMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.NO_OBJECT, null, null, null, stringRef(ctx.methodName()), context(ctx.methodName()));
	}

	@Override
	public void exitNoObjectMethodInvocation_lfno_primary(NoObjectMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterStaticMethodInvocation_lfno_primary(StaticMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(
				context(ctx),
				MethodInvocationType.NAMED_CLASS_STATIC_OR_STATIC_VAR,
				parseName(ctx.typeName().getText()),
				context((ParserRuleContext)ctx.typeName().getRuleContext()),
				ReferenceType.NAME,
				stringRef(ctx.Identifier()),
				context(ctx.Identifier()));
	}

	@Override
	public void exitStaticMethodInvocation_lfno_primary(StaticMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterObjectMethodInvocation_lfno_primary(ObjectMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.VARIABLE_REFERENCE, null, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitObjectMethodInvocation_lfno_primary(ObjectMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterSuperMethodInvocation_lfno_primary(SuperMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.SUPER, null, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
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
				parseName(ctx.typeName().getText()),
				context((ParserRuleContext)ctx.typeName().getRuleContext()),
				ReferenceType.NAME,
				stringRef(ctx.Identifier()),
				context(ctx.Identifier()));
	}

	@Override
	public void exitTypeSuperMethodInvocation_lfno_primary(TypeSuperMethodInvocation_lfno_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterNoObjectMethodInvocation(NoObjectMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.NO_OBJECT, null, null, null, stringRef(ctx.methodName()), context(ctx.methodName()));
	}

	@Override
	public void exitNoObjectMethodInvocation(NoObjectMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterStaticMethodInvocation(StaticMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(
				context(ctx),
				MethodInvocationType.NAMED_CLASS_STATIC_OR_STATIC_VAR,
				parseName(ctx.typeName().getText()),
				context((ParserRuleContext)ctx.typeName().getRuleContext()),
				ReferenceType.NAME,
				stringRef(ctx.Identifier()),
				context(ctx.Identifier()));
	}

	@Override
	public void exitStaticMethodInvocation(StaticMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterObjectMethodInvocation(ObjectMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.VARIABLE_REFERENCE, null, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitObjectMethodInvocation(ObjectMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterExpressionMethodInvocation(ExpressionMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.PRIMARY, null, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitExpressionMethodInvocation(ExpressionMethodInvocationContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}
	
	@Override
	public void enterSubMethodInvocation_lf_primary(SubMethodInvocation_lf_primaryContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.SUB, null, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitSubMethodInvocation_lf_primary(SubMethodInvocation_lf_primaryContext ctx) {
		delegate.onMethodInvocationEnd(context(ctx));
	}

	@Override
	public void enterSuperMethodInvocation(SuperMethodInvocationContext ctx) {
		delegate.onMethodInvocationStart(context(ctx), MethodInvocationType.SUPER, null, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
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
				parseName(ctx.typeName().getText()),
				context((ParserRuleContext)ctx.typeName().getRuleContext()),
				ReferenceType.NAME,
				stringRef(ctx.Identifier()),
				context(ctx.Identifier()));
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
	public void enterArgument(ArgumentContext ctx) {
		delegate.onParameterStart(context(ctx));
	}

	@Override
	public void exitArgument(ArgumentContext ctx) {
		delegate.onParameterEnd(context(ctx));
	}

	@Override
	public void exitArgumentList(ArgumentListContext ctx) {
		delegate.onParametersEnd(context(ctx));
	}

	@Override
	public void enterCastExpression_unaryExpression(CastExpression_unaryExpressionContext ctx) {
		delegate.onCastExpressionStart(context(ctx));
	}

	@Override
	public void exitCastExpression_unaryExpression(CastExpression_unaryExpressionContext ctx) {
		delegate.onCastExpressionEnd(context(ctx));
	}
	
	// Array creation

	
	@Override
	public void enterPrimitiveType_dimExprs_arrayCreationExpression(
			PrimitiveType_dimExprs_arrayCreationExpressionContext ctx) {

		delegate.onArrayCreationExpressionStart(
				context(ctx),
				JavaPrimitiveType.fromString(ctx.primitiveType().getText()).getScopedName(),
				ReferenceType.SCALAR,
				ctx.dims() != null ? countDims(ctx.dims().getText()) : 0);
	}

	@Override
	public void exitPrimitiveType_dimExprs_arrayCreationExpression(
			PrimitiveType_dimExprs_arrayCreationExpressionContext ctx) {

		delegate.onArrayCreationExpressionEnd(context(ctx));
	}

	@Override
	public void enterClassOrInterfaceType_dimExprs_arrayCreationExpression(
			ClassOrInterfaceType_dimExprs_arrayCreationExpressionContext ctx) {

		delegate.onArrayCreationExpressionStart(
				context(ctx),
				parseName(ctx.classOrInterfaceType().getText()),
				ReferenceType.NAME,
				ctx.dims() != null ? countDims(ctx.dims().getText()) : 0);
	}

	@Override
	public void exitClassOrInterfaceType_dimExprs_arrayCreationExpression(
			ClassOrInterfaceType_dimExprs_arrayCreationExpressionContext ctx) {

		delegate.onArrayCreationExpressionEnd(context(ctx));
	}

	
	@Override
	public void enterPrimitiveType_dims_arrayCreationExpression(PrimitiveType_dims_arrayCreationExpressionContext ctx) {
		delegate.onArrayCreationExpressionStart(
				context(ctx),
				JavaPrimitiveType.fromString(ctx.primitiveType().getText()).getScopedName(),
				ReferenceType.SCALAR,
				ctx.dims() != null ? countDims(ctx.dims().getText()) : 0);
	}

	@Override
	public void exitPrimitiveType_dims_arrayCreationExpression(PrimitiveType_dims_arrayCreationExpressionContext ctx) {
		delegate.onArrayCreationExpressionEnd(context(ctx));
	}

	@Override
	public void enterClassOrInterfaceType_dims_arrayCreationExpression(
			ClassOrInterfaceType_dims_arrayCreationExpressionContext ctx) {
		delegate.onArrayCreationExpressionStart(
				context(ctx),
				parseName(ctx.classOrInterfaceType().getText()),
				ReferenceType.NAME,
				ctx.dims() != null ? countDims(ctx.dims().getText()) : 0);
	}

	@Override
	public void exitClassOrInterfaceType_dims_arrayCreationExpression(
			ClassOrInterfaceType_dims_arrayCreationExpressionContext ctx) {
		delegate.onArrayCreationExpressionEnd(context(ctx));
	}

	
	@Override
	public void enterDimExpr(DimExprContext ctx) {
		delegate.onDimExpressionStart(context(ctx));
	}

	@Override
	public void exitDimExpr(DimExprContext ctx) {
		delegate.onDimExpressionEnd(context(ctx));
	}

	
	// .class literal expressions

	@Override
	public void enterTypeNameClassExpression_primaryNoNewArray(TypeNameClassExpression_primaryNoNewArrayContext ctx) {
		delegate.onClassExpression(context(ctx), stringRef(ctx.typeName()), countDims(ctx.typeNameArray().getText()));
	}


	@Override
	public void enterTypeNameClassExpression_primaryNoNewArray_lfno_arrayAccess(
			TypeNameClassExpression_primaryNoNewArray_lfno_arrayAccessContext ctx) {
		delegate.onClassExpression(context(ctx), stringRef(ctx.typeName()), countDims(ctx.typeNameArray().getText()));
	}
	
	@Override
	public void enterTypeNameClassExpression_primaryNoNewArray_lfno_primary(
			TypeNameClassExpression_primaryNoNewArray_lfno_primaryContext ctx) {
		delegate.onClassExpression(context(ctx), stringRef(ctx.typeName()), countDims(ctx.typeNameArray().getText()));
	}

	@Override
	public void enterPrimitiveTypeNameClassExpression_primaryNoNewArray_lfno_primary(
			PrimitiveTypeNameClassExpression_primaryNoNewArray_lfno_primaryContext ctx) {
		delegate.onClassExpression(context(ctx), stringRef(ctx.unannPrimitiveType()), countDims(ctx.typeNameArray().getText()));
	}
	
	
	@Override
	public void enterTypeNameClassExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
			TypeNameClassExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
		delegate.onClassExpression(context(ctx), stringRef(ctx.typeName()), countDims(ctx.typeNameArray().getText()));
	}

	@Override
	public void enterPrimitiveTypeNameClassExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
			PrimitiveTypeNameClassExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
		delegate.onClassExpression(context(ctx), stringRef(ctx.unannPrimitiveType()), countDims(ctx.typeNameArray().getText()));
	}

	
	
	
	// Lambda expressions
	

	@Override
	public void enterLambdaExpression(LambdaExpressionContext ctx) {
		delegate.onLambdaExpressionStart(context(ctx));
	}

	@Override
	public void exitLambdaSingleParameter(LambdaSingleParameterContext ctx) {

		delegate.onSingleLambdaParameter(context(ctx), stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitInferredFormalParameterList(InferredFormalParameterListContext ctx) {
		
		delegate.onInferredLambdaParameterList(
				context(ctx),
				ctx.Identifier().stream()
					.map(identifier -> identifier.getText())
					.collect(Collectors.toList()),
				context(ctx.Identifier()));
	}

	@Override
	public void enterLambdaBody(LambdaBodyContext ctx) {
		delegate.onLambdaBodyStart(context(ctx));
	}

	@Override
	public void exitLambdaBody(LambdaBodyContext ctx) {
		delegate.onLambdaBodyEnd(context(ctx));
	}

	@Override
	public void exitLambdaExpression(LambdaExpressionContext ctx) {
		delegate.onLambdaExpressionEnd(context(ctx));
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
	
	@Override
	public void enterNestedExpression_primaryNoNewArray(NestedExpression_primaryNoNewArrayContext ctx) {
		delegate.onNestedExpressionStart(context(ctx));
	}

	@Override
	public void enterNestedExpression_primaryNoNewArray_lfno_arrayAccess(
			NestedExpression_primaryNoNewArray_lfno_arrayAccessContext ctx) {
		delegate.onNestedExpressionStart(context(ctx));
	}

	@Override
	public void enterNestedExpression_primaryNoNewArray_lfno_primary(
			NestedExpression_primaryNoNewArray_lfno_primaryContext ctx) {
		delegate.onNestedExpressionStart(context(ctx));
	}

	@Override
	public void enterNestedExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
			NestedExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
		delegate.onNestedExpressionStart(context(ctx));
	}

	
	@Override
	public void exitNestedExpression_primaryNoNewArray(NestedExpression_primaryNoNewArrayContext ctx) {
		delegate.onNestedExpressionEnd(context(ctx));
	}

	@Override
	public void exitNestedExpression_primaryNoNewArray_lfno_arrayAccess(
			NestedExpression_primaryNoNewArray_lfno_arrayAccessContext ctx) {
		delegate.onNestedExpressionEnd(context(ctx));
	}

	@Override
	public void exitNestedExpression_primaryNoNewArray_lfno_primary(
			NestedExpression_primaryNoNewArray_lfno_primaryContext ctx) {
		delegate.onNestedExpressionEnd(context(ctx));
	}

	@Override
	public void exitNestedExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
			NestedExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {
		delegate.onNestedExpressionEnd(context(ctx));
	}

	// Primaries
	@Override
	public void enterPrimary(PrimaryContext ctx) {
		delegate.onPrimaryStart(context(ctx));
	}

	@Override
	public void enterArrayAccess(ArrayAccessContext ctx) {
		delegate.onArrayAccessStart(context(ctx));
	}

	@Override
	public void enterArrayIndex(ArrayIndexContext ctx) {
		delegate.onArrayIndexStart(context(ctx));
	}

	@Override
	public void exitArrayIndex(ArrayIndexContext ctx) {
		delegate.onArrayIndexEnd(context(ctx));
	}

	@Override
	public void exitArrayAccess(ArrayAccessContext ctx) {
		delegate.onArrayAccessEnd(context(ctx));
	}

	@Override
	public void enterArrayAccess_lf_primary(ArrayAccess_lf_primaryContext ctx) {
		delegate.onArrayAccessStart(context(ctx));
	}

	@Override
	public void exitArrayAccess_lf_primary(ArrayAccess_lf_primaryContext ctx) {
		delegate.onArrayAccessEnd(context(ctx));
	}

	@Override
	public void enterArrayAccess_lfno_primary(ArrayAccess_lfno_primaryContext ctx) {
		delegate.onArrayAccessStart(context(ctx));
	}

	@Override
	public void exitArrayAccess_lfno_primary(ArrayAccess_lfno_primaryContext ctx) {
		delegate.onArrayAccessEnd(context(ctx));
	}

	@Override
	public void exitPrimaryDotIdentifierFieldAccess(PrimaryDotIdentifierFieldAccessContext ctx) {
		delegate.onFieldAccess(context(ctx), FieldAccessType.FIELD, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitSuperDotIdentifierFieldAccess(SuperDotIdentifierFieldAccessContext ctx) {
		delegate.onFieldAccess(context(ctx), FieldAccessType.SUPER_FIELD, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitTypeNameDotSuperDotIdentifierFieldAccess(TypeNameDotSuperDotIdentifierFieldAccessContext ctx) {
		delegate.onFieldAccess(context(ctx), FieldAccessType.TYPE_SUPER_FIELD, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitDotIdentifierFieldAccess(DotIdentifierFieldAccessContext ctx) {
		delegate.onFieldAccess(context(ctx), FieldAccessType.FIELD, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitSuperDotIdentifierFieldAccess_fieldAccess_lfno_primary(
			SuperDotIdentifierFieldAccess_fieldAccess_lfno_primaryContext ctx) {
		delegate.onFieldAccess(context(ctx), FieldAccessType.SUPER_FIELD, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}
	
	@Override
	public void exitTypeNameDotSuperDotIdentifierFieldAccess_fieldAccess_lfno_primary(
			TypeNameDotSuperDotIdentifierFieldAccess_fieldAccess_lfno_primaryContext ctx) {
		delegate.onFieldAccess(context(ctx), FieldAccessType.TYPE_SUPER_FIELD, null, null, stringRef(ctx.Identifier()), context(ctx.Identifier()));
	}

	@Override
	public void exitThisExpression_primaryNoNewArray(ThisExpression_primaryNoNewArrayContext ctx) {

		delegate.onThisPrimary(context(ctx));
	}

	@Override
	public void exitThisExpression_primaryNoNewArray_lfno_arrayAccess(
			ThisExpression_primaryNoNewArray_lfno_arrayAccessContext ctx) {

		delegate.onThisPrimary(context(ctx));
	}

	@Override
	public void exitThisExpression_lfno_primary_primaryNoNewArray_lfno_primary(
			ThisExpression_lfno_primary_primaryNoNewArray_lfno_primaryContext ctx) {

		delegate.onThisPrimary(context(ctx));
	}

	@Override
	public void exitThisExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary(
			ThisExpression_primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primaryContext ctx) {

		delegate.onThisPrimary(context(ctx));
	}

	@Override
	public void exitPrimary(PrimaryContext ctx) {
		delegate.onPrimaryEnd(context(ctx));
	}
	
	@Override
	public void enterConditionalExpressionLine(ConditionalExpressionLineContext ctx) {
		delegate.onConditionalExpressionStart(context(ctx));
		
		delegate.onConditionalExpressionPart1Start(context(ctx));
	}
	
	public void exitCeQuestionMark(CeQuestionMarkContext ctx) {
		
		delegate.onConditionalExpressionPart1End(context(ctx));

		delegate.onConditionalExpressionPart2Start(context(ctx));
	}

	@Override
	public void exitCeColon(CeColonContext ctx) {

		delegate.onConditionalExpressionPart2End(context(ctx));
		
		delegate.onConditionalExpressionPart3Start(context(ctx));
	}

	@Override
	public void exitConditionalExpressionLine(ConditionalExpressionLineContext ctx) {
		
		delegate.onConditionalExpressionPart3End(context(ctx));
		
		delegate.onConditionalExpressionEnd(context(ctx));
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
		delegate.onJavaStringLiteral(context(ctx), stringRef(ctx));
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
	public void exitBooleanType_primitiveType(BooleanType_primitiveTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.BOOLEAN);
	}
	
	@Override
	public void exitBooleanType_unannPrimitiveType(BooleanType_unannPrimitiveTypeContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.BOOLEAN);
	}

	private static ScopedName parseName(String nameToken) {
		
		final String [] parts = Strings.split(nameToken, '.');
		
		final String [] typeParts = Strings.split(parts[parts.length - 1], '<');
		
		final String name = typeParts[0].trim();
		
		final ScopedName scopedName;
		
		if (parts.length == 1) {
			scopedName = new ScopedName(null, name);
		}
		else {
			final List<String> scope = new ArrayList<>(parts.length - 1);

			for (int i = 0; i < parts.length - 1; ++ i) {
				scope.add(parts[i].trim());
			}

			scopedName = new ScopedName(scope, name);
		}
		
		return scopedName;
	}
	
	private static ScopedName makeScopedName(List<String> names) {
		
		final ScopedName scopedName;
		
		switch (names.size()) {
		case 0:
			throw new IllegalArgumentException("No names given");

		case 1:
			scopedName = new ScopedName(null, names.get(0));
			break;
			
		default:
			final String name = names.get(names.size() - 1);
			
			final List<String> scope = names.subList(0, names.size());
			
			if (scope.size() == names.size()) {
				throw new IllegalStateException();
			}
			
			scopedName = new ScopedName(scope, name);
			break;
		}

		return scopedName;
	}
	
	@Override
	public void enterUnannClassType(UnannClassTypeContext ctx) {
		delegate.onJavaClassOrInterfaceReferenceType(context(ctx), parseName(ctx.getText()));
	}

	@Override
	public void enterUnannClassOrInterfaceType(UnannClassOrInterfaceTypeContext ctx) {
		delegate.onJavaClassOrInterfaceReferenceType(context(ctx), parseName(ctx.getText()));
	}

	@Override
	public void enterUnannTypeVariable(UnannTypeVariableContext ctx) {
		
		final ScopedName scopedName = new ScopedName(null, ctx.getText());
		
		delegate.onJavaClassOrInterfaceReferenceType(context(ctx), scopedName);
	}
	
	@Override
	public void enterVoidResult(VoidResultContext ctx) {
		delegate.onJavaPrimitiveType(context(ctx), JavaPrimitiveType.VOID);
	}

	void method() {
		
	}
	
	// Statements

	@Override
	public void exitFinalVariableModifier(FinalVariableModifierContext ctx) {
		delegate.onMutabilityVariableModifier(context(ctx), new ASTMutability(Mutability.VALUE_OR_REF_IMMUTABLE));
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
				stringRef(ctx.Identifier()),
				ctx.dims() != null
					? countDims(ctx.dims().getText())
					: 0);
	}

	// Hack to construct proper if-else if-else sequences
	@Override
	public void enterIfThenStatement(IfThenStatementContext ctx) {
		delegate.onJavaIfThenStatementStart(context(ctx), ctx.ifKeyword);
	}

	@Override
	public void exitIfThenStatement(IfThenStatementContext ctx) {
		delegate.onJavaIfThenStatementEnd(context(ctx));
	}

	@Override
	public void enterIfThenElseStatement(IfThenElseStatementContext ctx) {
		delegate.onJavaIfThenElseStatementStart(context(ctx), ctx.ifKeyword, ctx.elseKeyword);
	}

	@Override
	public void exitIfThenElseStatement(IfThenElseStatementContext ctx) {
		delegate.onJavaIfThenElseStatementEnd(context(ctx));
	}
	
	@Override
	public void enterIfThenElseStatementNoShortIf(IfThenElseStatementNoShortIfContext ctx) {
		delegate.onJavaIfThenElseStatementStart(context(ctx), ctx.ifKeyword, ctx.elseKeyword);
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
		delegate.onSwitchStatementStart(context(ctx), ctx.keyword.getText(), context(ctx.keyword));
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
		delegate.onConstantSwitchLabelStart(context(ctx), ctx.keyword.getText(), context(ctx.keyword));
	}

	@Override
	public void exitConstantExpressionSwitchLabel(ConstantExpressionSwitchLabelContext ctx) {
		delegate.onConstantSwitchLabelEnd(context(ctx));
	}
	
	@Override
	public void enterEnumConstantNameSwitchLabel(EnumConstantNameSwitchLabelContext ctx) {
		delegate.onEnumSwitchLabel(
				context(ctx),
				ctx.keyword.getText(), context(ctx.keyword),
				ctx.enumConstantName().getText(), context(ctx.enumConstantName()));
	}

	@Override
	public void exitDefaultSwitchLabel(DefaultSwitchLabelContext ctx) {
		delegate.onDefaultSwitchLabel(context(ctx), ctx.keyword.getText(), context(ctx.keyword));
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
		
		final String breakLabel = ctx.Identifier() != null
				? ctx.Identifier().getText()
				: null;

		delegate.onBreakStatement(context(ctx), ctx.keyword.getText(), context(ctx.keyword), breakLabel);
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
		delegate.onDoWhileStatementStart(context(ctx));
	}

	@Override
	public void exitDoStatement(DoStatementContext ctx) {
		delegate.onDoWhileStatementEnd(context(ctx));
	}

	@Override
	public void enterBasicForStatement(BasicForStatementContext ctx) {
		delegate.onForStatementStart(context(ctx), stringRef(ctx.keyword), context(ctx.keyword));
	}

	@Override
	public void exitBasicForStatement(BasicForStatementContext ctx) {
		delegate.onForStatementEnd(context(ctx));
	}

	@Override
	public void enterBasicForStatementNoShortIf(BasicForStatementNoShortIfContext ctx) {
		delegate.onForStatementStart(context(ctx), stringRef(ctx.keyword), context(ctx.keyword));
	}

	@Override
	public void exitBasicForStatementNoShortIf(BasicForStatementNoShortIfContext ctx) {
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

	@Override
	public void enterCatchClause(CatchClauseContext ctx) {
		delegate.onCatchStart(context(ctx));
	}

	@Override
	public void exitCatchClause(CatchClauseContext ctx) {
		delegate.onCatchEnd(context(ctx));
	}

	@Override
	public void enterThrowStatement(ThrowStatementContext ctx) {
		delegate.onThrowStatementStart(context(ctx));
	}

	@Override
	public void exitThrowStatement(ThrowStatementContext ctx) {
		delegate.onThrowStatementEnd(context(ctx));
	}

	@Override
	public void enterAnnotation(AnnotationContext ctx) {
		delegate.onAnnotationStart(context(ctx));
	}

	@Override
	public void exitAnnotation(AnnotationContext ctx) {
		delegate.onAnnotationEnd(context(ctx));
	}
}

