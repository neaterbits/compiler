package com.neaterbits.compiler.parser.listener.common;

import java.math.BigDecimal;
import java.util.List;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.block.ConstructorInvocation;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.statement.ASTMutability;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;

public interface ParserListener<COMPILATION_UNIT> extends ContextWriteAccess {

	void onCompilationUnitStart(int startContext);

	COMPILATION_UNIT onCompilationUnitEnd(int startContext, Context endContext);
	
	void onImportStart(
	        int startContext,
	        long importKeyword, int importKeywordContext,
	        long staticKeyword, int staticKeywordContext);
	
	void onImportName(int context, long name);
	
	void onImportEnd(int startContext, Context endContext, boolean ondemand);
	
	void onNamespaceStart(int startContext, long namespaceKeyword, int namespaceKeywordContext);
	
	void onNamespacePart(int context, long part);
	
	void onNameSpaceEnd(int startContext, Context endContext);
	
	void onTypeDefinitionStart(int startContext);
	
	void onTypeDefinitionEnd(int startContext, Context endContext);

	void onAnnotationStart(int startContext, Names typeName);
	
	void onAnnotationElementStart(int startContext, long name, int nameContext);
	
	void onAnnotationElementEnd(int startContext, Context endContext);
	
	void onAnnotationEnd(int startContext, Context endContext);
	
	void onClassStart(
	        int startContext,
	        long classKeyword, int classKeywordContext,
	        long name, int nameContext);

	void onVisibilityClassModifier(int context, ClassVisibility visibility);
	
	void onSubclassingModifier(int context, Subclassing subclassing);
	
	void onStaticClassModifier(int context);

	void onStrictfpClassModifier(int context);
	
	void onGenericClassDefinitionTypeListStart(int startContext);

	void onGenericNamedTypeStart(int startContext, long name, int nameContext);

	void onGenericNamedTypeEnd(int startContext, Context endContext);

	void onGenericWildcardTypeStart(int startContext);

    void onGenericWildcardTypeEnd(int startContext, Context endContext);

    void onTypeBoundStart(int context, TypeBoundType type);
    
    void onTypeBoundEnd(int startContext, Context endContext);
    
	void onGenericClassDefinitionTypeListEnd(int startContext, Context endContext);

	void onClassExtendsStart(int startContext, long extendsKeyword, int extendsKeywordContext);

	void onClassExtendsNamePart(int context, long identifier);
	
	void onClassExtendsEnd(int startContext, Context endContext);

	void onClassImplementsStart(int startContext, long implementsKeyword, int implementsKeywordContext);
	
	void onClassImplementsTypeStart(int context);

    void onClassImplementsNamePart(int context, long identifier);
    
    void onClassImplementsTypeEnd(int startContext, Context endContext);
    
    void onClassImplementsEnd(int startContext, Context endContext);
	
	void onClassEnd(int startContext, Context endContext);

	void onAnonymousClassStart(int startContext);
	
	void onAnonymousClassEnd(int startContext, Context endContext);
	
	void onStaticInitializerStart(int startContext);

	void onStaticInitializerEnd(int startContext, Context endContext);

	void onConstructorStart(int context);
	
	void onConstructorVisibilityModifier(int context, ConstructorVisibility visibility);
	
	void onConstructorName(int context, long constructorName);
	
	void onConstructorInvocationStart(int startContext, ConstructorInvocation type);
	
	void onConstructorInvocationEnd(int startContext, Context endContext);
	
	void onConstructorEnd(int startContext, Context endContext);
	
	void onClassMethodStart(int startContext);

	void onMethodReturnTypeStart(int startContext);
	
	void onMethodReturnTypeEnd(int startContext, Context endContext);
	
	void onMethodName(int leafContext, long methodName);

	void onMethodSignatureParametersStart(int startContext);

	void onMethodSignatureParameterStart(int startContext, boolean varArgs);

	void onMethodSignatureParameterEnd(int startContext, Context endContext);

	void onMethodSignatureParametersEnd(int startContext, Context endContext);

	void onVisibilityClassMethodModifier(int leafContext, ClassMethodVisibility visibility);
	
	void onOverrideClassMethodModifier(int leafContext, ClassMethodOverride methodOverride);
	
	void onStaticClassMethodModifier(int elontext);
	
	void onStrictfpClassMethodModifier(int leafContext);
	
	void onSynchronizedClassMethodModifier(int leafContext);
	
	void onNativeClassMethodModifier(int leafContext);
	
	void onClassMethodEnd(int startContext, Context endContext);
	
	void onFieldDeclarationStart(int startContext);
	
	void onVisibilityFieldModifier(int leafContext, FieldVisibility visibility);
	
	void onStaticFieldModifier(int leafContext);
	
	void onMutabilityFieldModifier(int leafContext, ASTMutability mutability);
	
	void onTransientFieldModifier(int leafContext);
	
	void onVolatileFieldModifier(int leafContext);
	
	void onFieldDeclarationEnd(int startContext, Context endContext);
	
	void onInterfaceStart(int startContext, long interfaceKeyword, int interfaceKeywordContext,
			long name, int nameContext);
	
	void onVisibilityInterfaceModifier(int leafContext, InterfaceVisibility visibility);
	
	void onAbstractInterfaceModifier(int leafContext);
	
	void onStaticInterfaceModifier(int leafContext);
	
	void onStrictfpInterfaceModifier(int leafContext);

	void onInterfaceExtends(int context, ScopedName interfaceName);
	
	void onInterfaceEnd(int startContext, Context endContext);
	
	void onEnumStart(int startContext, long enumKeyword, int enumKeywordContext, long name,
			int nameContext);
	
	void onEnumImplements(int context, ScopedName interfaceName);
	
	void onEnumConstantStart(int startContext, long name);
	
	void onEnumConstantEnd(int startContext, Context endContext);

	void onEnumEnd(int startContext, Context endContext);

	void onInterfaceMethodStart(int startContext);

	void onVisibilityInterfaceMethodModifier(int leafContext, InterfaceMethodVisibility visibility);

	void onAbstractInterfaceMethodModifier(int leafContext);
	
	void onDefaultInterfaceMethodModifier(int leafContext);

	void onStaticInterfaceMethodModifier(int leafContext);
	
	void onStrictfpInterfaceMethodModifier(int leafContext);
	
	void onInterfaceMethodEnd(int startContext, Context endContext);
	
	void onAssignmentStatementStart(int startContext);
	
	void onAssignmentStatementEnd(int startContext, Context endContext);
	
	void onEnterAssignmentExpression(int startContext);
	
	void onEnterAssignmentLHS(int startContext);
	
	void onExitAssignmentLHS(int startContext, Context endContext);
	
	void onExitAssignmentExpression(int startContext, Context endContext);

	void onNestedExpressionStart(int startContext);
	
	void onNestedExpressionEnd(int startContext, Context endContext);
	
	void onNameReference(int leafContext, long name);
	
	void onVariableReference(int leafContext, long name);
	
	void onPrimaryStart(int startContext);
	
	void onNamePrimary(int leafContext, long name);
	
	void onArrayAccessStart(int startContext);
	
	void onArrayIndexStart(int startContext);
	
	void onArrayIndexEnd(int startContext, Context endContext);
	
	void onArrayAccessEnd(int startContext, Context endContext);
	
	void onFieldAccess(int context, FieldAccessType fieldAccessType, ScopedName typeName,
			ReferenceType referenceType, long fieldName, int fieldNameContext);
	
	void onCastExpressionStart(int startContext);
	
	void onCastExpressionEnd(int startContext, Context endContext);
	
	void onThisPrimary(int leafContext);
	
	void onPrimaryEnd(int startContext, Context endContext);
	
	void onConditionalExpressionStart(int startContext);
	
	void onConditionalExpressionPart1Start(int startContext);
	
	void onConditionalExpressionPart1End(int startContext, Context endContext);
	
	void onConditionalExpressionPart2Start(int startContext);
	
	void onConditionalExpressionPart2End(int startContext, Context endContext);
	
	void onConditionalExpressionPart3Start(int startContext);
	
	void onConditionalExpressionPart3End(int startContext, Context endContext);
	
	void onConditionalExpressionEnd(int startContext, Context endContext);
	
	void onIntegerLiteral(int leafContext, long value, Base base, boolean signed, int bits);
	
	void onFloatingPointLiteral(int leafContext, BigDecimal value, Base base, int bits);
	
	void onBooleanLiteral(int leafContext, boolean value);
	
	void onCharacterLiteral(int leafContext, char value);
	
	void onStringLiteral(int leafContext, long value);
	
	void onNullLiteral(int leafContext);
	
	void onClassInstanceCreationExpressionStart(int startContext);
	
	void onClassInstanceCreationTypeAndConstructorName(int context, ScopedName name);
	
	void onClassInstanceCreationExpressionEnd(int startContext, Context endContext);
	
	void onMethodInvocationStart(
			int startContext,
			MethodInvocationType type,
			long methodName,
			int methodNameContext);
	
	void onParametersStart(int startContext);

	void onParameterStart(int startContext);
	
	void onParameterEnd(int startContext, Context endContext);
	
	void onParametersEnd(int startContext, Context endContext);
	
	void onMethodInvocationEnd(int startContext, Context endContext);
	
	void onArrayCreationExpressionStart(int startContext, ScopedName typeName, ReferenceType referenceType, int numDims);
	
	void onDimExpressionStart(int startContext);

	void onDimExpressionEnd(int startContext, Context endContext);

	void onArrayCreationExpressionEnd(int startContext, Context endContext);

	void onClassExpression(int context, long className, int numArrayDims);
	
	void onLambdaExpressionStart(int startContext);
	
	void onSingleLambdaParameter(int leafContext, long varName);
	
	void onFormalLambdaParameterListStart(int startContext);
	
	void onFormalLambdaParameterListEnd(int startContext, Context endContext);

	void onInferredLambdaParameterList(int context, List<String> varNames, int varNamesContext);

	void onLambdaBodyStart(int startContext);	
	
	void onLambdaBodyEnd(int startContext, Context endContext);
	
	void onLambdaExpressionEnd(int startContext, Context endContext);
	
	void onMutabilityVariableModifier(int leafContext, ASTMutability mutability);
	
	void onVariableDeclarationStatementStart(int startContext);
	
	void onVariableDeclarationStatementEnd(int startContext, Context endContext);
	
	void onVariableDeclaratorStart(int startContext);
	
	void onVariableDeclaratorEnd(int startContext, Context endContext);

	void onLeafTypeReference(int context, long name, ReferenceType referenceType);

	void onNonScopedTypeReferenceStart(int context, long name, ReferenceType referenceType);

	void onNonScopedTypeReferenceEnd(int context, Context endContext);

	void onScopedTypeReferenceStart(int startContext, ReferenceType referenceType);

	void onScopedTypeReferenceNameStart(int startContext);

	void onScopedTypeReferenceNamePart(int leafContext, long part);

	void onScopedTypeReferenceNameEnd(int startContext, Context endContext);

	void onGenericTypeParametersStart(int startContext);
	
	void onGenericTypeParameter(int context, long name);

	void onGenericTypeParametersEnd(int startContext, Context endContext);

	void onScopedTypeReferenceEnd(int startContext, Context endContext);

	void onExpressionStatementStart(int startContext);
	
	void onExpressionStatementEnd(int startContext, Context endContext);
	
	void onForStatementStart(int startContext, long keyword, int keywordContext);
	
	void onForInitStart(int startContext);
	
	void onForInitEnd(int startContext, Context endContext);
	
	void onForUpdateStart(int startContext);
	
	void onForUpdateEnd(int startContext, Context endContext);
	
	void onForStatementEnd(int startContext, Context endContext);
	
	void onIteratorForStatementStart(int startContext);

	void onIteratorForTestStart(int startContext);

	void onIteratorForTestEnd(int startContext, Context endContext);
	
	void onIteratorForStatementEnd(int startContext, Context endContext);
	
	void onWhileStatementStart(int startContext, long whileKeyword, int whileKeywordContext);
	
	void onWhileStatementEnd(int startContext, Context endContext);
	
	void onDoWhileStatementStart(int startContext);
	
	void onDoWhileStatementEnd(int startContext, Context endContext);
	
	void onTryWithResourcesStatementStart(int startContext);
	
	void onTryWithResourcesSpecificationStart(int startContext);
	
	void onResourceStart(int startContext);
	
	void onVariableName(int leafContext, long name, int numDims);
	
	void onResourceEnd(int startContext, Context endContext);
	
	void onTryWithResourcesSpecificationEnd(int startContext, Context endContext);
	
	void onTryStatementStart(int startContext);
	
	void onTryBlockEnd(int startContext, Context endContext);
	
	void onCatchStart(int startContext);
	
	void onCatchEnd(int startContext, Context endContext);
	
	void onFinallyStart(int startContext);
	
	void onFinallyEnd(int startContext, Context endContext);
	
	void onTryStatementEnd(int startContext, Context endContext);
	
	void onTryWithResourcesEnd(int startContext, Context endContext);
	
	void onReturnStatementStart(int startContext);
	
	void onReturnStatementEnd(int startContext, Context endContext);
	
	void onThrowStatementStart(int startContext);
	
	void onThrowStatementEnd(int startContext, Context endContext);

}
