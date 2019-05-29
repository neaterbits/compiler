package com.neaterbits.compiler.util.parse.parserlistener;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.block.ConstructorInvocation;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;

import statement.ASTMutability;

public interface ParserListener<COMPILATION_UNIT> {

	void onCompilationUnitStart(Context context);

	COMPILATION_UNIT onCompilationUnitEnd(Context context);
	
	void onImportStart(Context context, String importKeyword, Context importKeywordContext, String staticKeyword, Context staticKeywordContext);
	
	void onImportIdentifier(Context context, String identifier);
	
	void onImportEnd(Context context, boolean ondemand);
	
	void onNamespaceStart(Context context, String namespaceKeyword, Context namespaceKeywordContext,
			String name, Context nameContext, String [] parts);
	
	void onNameSpaceEnd(Context context);
	
	void onClassStart(Context context, String classKeyword, Context classKeywordContext, String name,
			Context nameContext);

	void onVisibilityClassModifier(Context context, ClassVisibility visibility);
	
	void onSubclassingModifier(Context context, Subclassing subclassing);
	
	void onStaticClassModifier(Context context);

	void onStrictfpClassModifier(Context context);
	
	void onClassExtends(Context context, String extendsKeyword, Context extendsKeywordContext, ScopedName className);

	void onClassImplements(Context context, ScopedName interfaceName);

	void onClassEnd(Context context);

	void onAnonymousClassStart(Context context);
	
	void onAnonymousClassEnd(Context context);
	
	void onStaticInitializerStart(Context context);

	void onStaticInitializerEnd(Context context);

	void onConstructorStart(Context context);
	
	void onConstructorVisibilityModifier(Context context, ConstructorVisibility visibility);
	
	void onConstructorName(Context context, String constructorName);
	
	void onConstructorInvocationStart(Context context, ConstructorInvocation type);
	
	void onConstructorInvocationEnd(Context context);
	
	void onConstructorEnd(Context context);
	
	void onClassMethodStart(Context context);

	void onMethodReturnTypeStart(Context context);
	
	void onMethodReturnTypeEnd(Context context);
	
	void onMethodName(Context context, String methodName);

	void onMethodSignatureParametersStart(Context context);

	void onMethodSignatureParameterStart(Context context, boolean varArgs);

	void onMethodSignatureParameterEnd(Context context);

	void onMethodSignatureParametersEnd(Context context);

	void onVisibilityClassMethodModifier(Context context, ClassMethodVisibility visibility);
	
	void onOverrideClassMethodModifier(Context context, ClassMethodOverride methodOverride);
	
	void onStaticClassMethodModifier(Context context);
	
	void onStrictfpClassMethodModifier(Context context);
	
	void onSynchronizedClassMethodModifier(Context context);
	
	void onNativeClassMethodModifier(Context context);
	
	void onClassMethodEnd(Context context);
	
	void onFieldDeclarationStart(Context context);
	
	void onVisibilityFieldModifier(Context context, FieldVisibility visibility);
	
	void onStaticFieldModifier(Context context);
	
	void onMutabilityFieldModifier(Context context, ASTMutability mutability);
	
	void onTransientFieldModifier(Context context);
	
	void onVolatileFieldModifier(Context context);
	
	void onFieldDeclarationEnd(Context context);
	
	void onInterfaceStart(Context context, String interfaceKeyword, Context interfaceKeywordContext,
			String name, Context nameContext);
	
	void onVisibilityInterfaceModifier(Context context, InterfaceVisibility visibility);
	
	void onAbstractInterfaceModifier(Context context);
	
	void onStaticInterfaceModifier(Context context);
	
	void onStrictfpInterfaceModifier(Context context);

	void onInterfaceExtends(Context context, ScopedName interfaceName);
	
	void onInterfaceEnd(Context context);
	
	void onEnumStart(Context context, String enumKeyword, Context enumKeywordContext, String name,
			Context nameContext);
	
	void onEnumImplements(Context context, ScopedName interfaceName);
	
	void onEnumConstantStart(Context context, String name);
	
	void onEnumConstantEnd(Context context);

	void onEnumEnd(Context context);

	void onInterfaceMethodStart(Context context);

	void onVisibilityInterfaceMethodModifier(Context context, InterfaceMethodVisibility visibility);

	void onAbstractInterfaceMethodModifier(Context context);
	
	void onDefaultInterfaceMethodModifier(Context context);

	void onStaticInterfaceMethodModifier(Context context);
	
	void onStrictfpInterfaceMethodModifier(Context context);
	
	void onInterfaceMethodEnd(Context context);
	
	void onEnterAssignmentExpression(Context context);
	
	void onEnterAssignmentLHS(Context context);
	
	void onExitAssignmentLHS(Context context);
	
	void onExitAssignmentExpression(Context context);

	void onNestedExpressionStart(Context context);
	
	void onNestedExpressionEnd(Context context);
	
	void onNameReference(Context context, String name);
	
	void onVariableReference(Context context, String name);
	
	void onPrimaryStart(Context context);
	
	void onArrayAccessStart(Context context);
	
	void onArrayIndexStart(Context context);
	
	void onArrayIndexEnd(Context context);
	
	void onArrayAccessEnd(Context context);
	
	void onFieldAccess(Context context, FieldAccessType fieldAccessType, ScopedName typeName,
			ReferenceType referenceType, String fieldName, Context fieldNameContext);
	
	void onCastExpressionStart(Context context);
	
	void onCastExpressionEnd(Context context);
	
	void onThisPrimary(Context context);
	
	void onPrimaryEnd(Context context);
	
	void onConditionalExpressionStart(Context context);
	
	void onConditionalExpressionPart1Start(Context context);
	
	void onConditionalExpressionPart1End(Context context);
	
	void onConditionalExpressionPart2Start(Context context);
	
	void onConditionalExpressionPart2End(Context context);
	
	void onConditionalExpressionPart3Start(Context context);
	
	void onConditionalExpressionPart3End(Context context);
	
	void onConditionalExpressionEnd(Context context);
	
	void onIntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits);
	
	void onFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits);
	
	void onBooleanLiteral(Context context, boolean value);
	
	void onCharacterLiteral(Context context, char value);
	
	void onStringLiteral(Context context, String value);
	
	void onNullLiteral(Context context);
	
	void onClassInstanceCreationExpressionStart(Context context);
	
	void onClassInstanceCreationTypeAndConstructorName(Context context, ScopedName name);
	
	void onClassInstanceCreationExpressionEnd(Context context);
	
	void onMethodInvocationStart(
			Context context,
			MethodInvocationType type,
			ScopedName classTypeName,
			Context classTypeNameContext,
			ReferenceType referenceType,
			String methodName,
			Context methodNameContext);
	
	void onParametersStart(Context context);

	void onParameterStart(Context context);
	
	void onParameterEnd(Context context);
	
	void onParametersEnd(Context context);
	
	void onMethodInvocationEnd(Context context);
	
	void onArrayCreationExpressionStart(Context context, ScopedName typeName, ReferenceType referenceType, int numDims);
	
	void onDimExpressionStart(Context context);

	void onDimExpressionEnd(Context context);

	void onArrayCreationExpressionEnd(Context context);

	void onClassExpression(Context context, String className, int numArrayDims);
	
	void onLambdaExpressionStart(Context context);
	
	void onSingleLambdaParameter(Context context, String varName, Context varNameContext);
	
	void onFormalLambdaParameterListStart(Context context);
	
	void onFormalLambdaParameterListEnd(Context context);

	void onInferredLambdaParameterList(Context context, List<String> varNames, Context varNamesContext);

	void onLambdaBodyStart(Context context);	
	
	void onLambdaBodyEnd(Context context);
	
	void onLambdaExpressionEnd(Context context);
	
	void onMutabilityVariableModifier(Context context, ASTMutability mutability);
	
	void onVariableDeclarationStatementStart(Context context);
	
	void onVariableDeclarationStatementEnd(Context context);
	
	void onVariableDeclaratorStart(Context context);
	
	void onVariableDeclaratorEnd(Context context);
	
	void onTypeReference(Context context, ScopedName name, ReferenceType referenceType);
	
	void onExpressionStatementStart(Context context);
	
	void onExpressionStatementEnd(Context context);
	
	void onForStatementStart(Context context, String keyword, Context keywordContext);
	
	void onForInitStart(Context context);
	
	void onForInitEnd(Context context);
	
	void onForUpdateStart(Context context);
	
	void onForUpdateEnd(Context context);
	
	void onForStatementEnd(Context context);
	
	void onIteratorForStatementStart(Context context);
	
	void onIteratorForTestEnd(Context context);
	
	void onIteratorForStatementEnd(Context context);
	
	void onWhileStatementStart(Context context);
	
	void onWhileStatementEnd(Context context);
	
	void onDoWhileStatementStart(Context context);
	
	void onDoWhileStatementEnd(Context context);
	
	void onTryWithResourcesStatementStart(Context context);
	
	void onTryWithResourcesSpecificationStart(Context context);
	
	void onResourceStart(Context context);
	
	void onVariableName(Context context, String name, int numDims);
	
	void onResourceEnd(Context context);
	
	void onTryWithResourcesSpecificationEnd(Context context);
	
	void onTryStatementStart(Context context);
	
	void onTryBlockEnd(Context context);
	
	void onCatchStart(Context context);
	
	void onCatchEnd(Context context);
	
	void onFinallyStart(Context context);
	
	void onFinallyEnd(Context context);
	
	void onTryStatementEnd(Context context);
	
	void onTryWithResourcesEnd(Context context);
	
	void onReturnStatementStart(Context context);
	
	void onReturnStatementEnd(Context context);
	
	void onThrowStatementStart(Context context);
	
	void onThrowStatementEnd(Context context);
	
	void onAnnotationStart(Context context);

	void onAnnotationEnd(Context context);

}
