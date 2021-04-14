package com.neaterbits.compiler.parser.listener.common;

import java.math.BigDecimal;
import java.util.List;

import com.neaterbits.compiler.types.ReferenceType;
import com.neaterbits.compiler.types.block.ConstructorInvocation;
import com.neaterbits.compiler.types.statement.ASTMutability;
import com.neaterbits.compiler.types.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.types.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.types.typedefinition.ClassVisibility;
import com.neaterbits.compiler.types.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.types.typedefinition.FieldVisibility;
import com.neaterbits.compiler.types.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.types.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.types.typedefinition.Subclassing;
import com.neaterbits.compiler.types.typedefinition.TypeBoundType;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.language.common.types.ScopedName;
import com.neaterbits.util.parse.context.Context;

public class ParseTreeAdapter<COMPILATION_UNIT> implements ParseTreeListener<COMPILATION_UNIT> {

    
    @Override
    public int writeContext(Context context) {
        
        throw new UnsupportedOperationException();
    }

    @Override
    public int writeContext(int otherContext) {
        
        throw new UnsupportedOperationException();
    }

    @Override
    public void onCompilationUnitStart(int startContext) {
        
    }

    @Override
    public COMPILATION_UNIT onCompilationUnitEnd(int startContext, Context endContext) {

        return null;
    }

    @Override
    public void onImportStart(int startContext, long importKeyword, int importKeywordContext, long staticKeyword, int staticKeywordContext) {
        
    }

    @Override
    public void onImportName(int context, long name) {
        
    }

    @Override
    public void onImportEnd(int startContext, Context endContext, boolean ondemand) {
        
    }

    @Override
    public void onNamespaceStart(int startContext, long namespaceKeyword, int namespaceKeywordContext) {
        
    }

    @Override
    public void onNamespacePart(int context, long part) {
        
    }

    @Override
    public void onNameSpaceEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onTypeDefinitionStart(int startContext) {
        
    }

    @Override
    public void onTypeDefinitionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onAnnotationStart(int startContext, Names typeName) {
        
    }

    @Override
    public void onAnnotationElementStart(int startContext, long name, int nameContext) {
        
    }

    @Override
    public void onAnnotationElementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onAnnotationEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onClassStart(int startContext, long classKeyword, int classKeywordContext, long name, int nameContext) {
        
    }

    @Override
    public void onVisibilityClassModifier(int context, ClassVisibility visibility) {
        
    }

    @Override
    public void onSubclassingModifier(int context, Subclassing subclassing) {
        
    }

    @Override
    public void onStaticClassModifier(int context) {
        
    }

    @Override
    public void onStrictfpClassModifier(int context) {
        
    }

    @Override
    public void onGenericTypeArgumentsStart(int startContext) {
        
    }

    @Override
    public void onGenericReferenceTypeArgumentStart(int startContext) {
        
    }

    @Override
    public void onGenericReferenceTypeArgumentEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onGenericWildcardTypeArgumentStart(int startContext) {
        
    }

    @Override
    public void onGenericWildcardTypeArgumentEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onTypeBoundStart(int context, TypeBoundType type) {
        
    }

    @Override
    public void onTypeBoundEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onGenericTypeArgumentsEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onClassExtendsStart(int startContext, long extendsKeyword, int extendsKeywordContext) {
        
    }

    @Override
    public void onClassExtendsEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onImplementsStart(int startContext, long implementsKeyword, int implementsKeywordContext) {
        
    }

    @Override
    public void onImplementsTypeStart(int context) {
        
    }

    @Override
    public void onImplementsNamePart(int context, long identifier) {
        
    }

    @Override
    public void onImplementsTypeEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onImplementsEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onClassEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onAnonymousClassStart(int startContext) {
        
    }

    @Override
    public void onAnonymousClassEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onStaticInitializerStart(int startContext) {
        
    }

    @Override
    public void onStaticInitializerEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onConstructorStart(int context) {
        
    }

    @Override
    public void onConstructorVisibilityModifier(int context, ConstructorVisibility visibility) {
        
    }

    @Override
    public void onConstructorName(int context, long constructorName) {
        
    }

    @Override
    public void onConstructorInvocationStart(int startContext, ConstructorInvocation type) {
        
    }

    @Override
    public void onConstructorInvocationEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onConstructorEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onClassMethodStart(int startContext) {
        
    }

    @Override
    public void onMethodReturnTypeStart(int startContext) {
        
    }

    @Override
    public void onMethodReturnTypeEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onMethodName(int leafContext, long methodName) {
        
    }

    @Override
    public void onMethodSignatureParametersStart(int startContext) {
        
    }

    @Override
    public void onMethodSignatureParameterStart(int startContext, boolean varArgs) {
        
    }

    @Override
    public void onMethodSignatureParameterVarargs(int leafContext) {
        
    }

    @Override
    public void onMethodSignatureParameterEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onMethodSignatureParametersEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onThrowsStart(int startContext) {
        
    }

    @Override
    public void onThrowsEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onVisibilityClassMethodModifier(int leafContext, ClassMethodVisibility visibility) {
        
    }

    @Override
    public void onOverrideClassMethodModifier(int leafContext, ClassMethodOverride methodOverride) {
        
    }

    @Override
    public void onStaticClassMethodModifier(int leafContext) {
        
    }

    @Override
    public void onStrictfpClassMethodModifier(int leafContext) {
        
    }

    @Override
    public void onSynchronizedClassMethodModifier(int leafContext) {
        
    }

    @Override
    public void onNativeClassMethodModifier(int leafContext) {
        
    }

    @Override
    public void onClassMethodEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onFieldDeclarationStart(int startContext) {
        
    }

    @Override
    public void onVisibilityFieldModifier(int leafContext, FieldVisibility visibility) {
        
    }

    @Override
    public void onStaticFieldModifier(int leafContext) {
        
    }

    @Override
    public void onMutabilityFieldModifier(int leafContext, ASTMutability mutability) {
        
    }

    @Override
    public void onTransientFieldModifier(int leafContext) {
        
    }

    @Override
    public void onVolatileFieldModifier(int leafContext) {
        
    }

    @Override
    public void onFieldDeclarationEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onInterfaceStart(int startContext, long interfaceKeyword, int interfaceKeywordContext, long name, int nameContext) {
        
    }

    @Override
    public void onVisibilityInterfaceModifier(int leafContext, InterfaceVisibility visibility) {
        
    }

    @Override
    public void onAbstractInterfaceModifier(int leafContext) {
        
    }

    @Override
    public void onStaticInterfaceModifier(int leafContext) {
        
    }

    @Override
    public void onStrictfpInterfaceModifier(int leafContext) {
        
    }

    @Override
    public void onInterfaceExtends(int context, ScopedName interfaceName) {
        
    }

    @Override
    public void onInterfaceEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onEnumStart(int startContext, long enumKeyword, int enumKeywordContext, long name, int nameContext) {
        
    }

    @Override
    public void onEnumConstantStart(int startContext, long name) {
        
    }

    @Override
    public void onEnumConstantEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onEnumEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onInterfaceMethodStart(int startContext) {
        
    }

    @Override
    public void onVisibilityInterfaceMethodModifier(int leafContext, InterfaceMethodVisibility visibility) {
        
    }

    @Override
    public void onAbstractInterfaceMethodModifier(int leafContext) {
        
    }

    @Override
    public void onDefaultInterfaceMethodModifier(int leafContext) {
        
    }

    @Override
    public void onStaticInterfaceMethodModifier(int leafContext) {
        
    }

    @Override
    public void onStrictfpInterfaceMethodModifier(int leafContext) {
        
    }

    @Override
    public void onInterfaceMethodEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onAssignmentStatementStart(int startContext) {
        
    }

    @Override
    public void onAssignmentStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onEnterAssignmentExpression(int startContext) {
        
    }

    @Override
    public void onEnterAssignmentLHS(int startContext) {
        
    }

    @Override
    public void onExitAssignmentLHS(int startContext, Context endContext) {
        
    }

    @Override
    public void onExitAssignmentExpression(int startContext, Context endContext) {
        
    }

    @Override
    public void onNestedExpressionStart(int startContext) {
        
    }

    @Override
    public void onNestedExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onNameReference(int leafContext, long name) {
        
    }

    @Override
    public void onPrimaryStart(int startContext) {
        
    }

    @Override
    public void onUnresolvedNamePrimary(int leafContext, long name) {
        
    }

    @Override
    public void onArrayAccessStart(int startContext) {
        
    }

    @Override
    public void onArrayIndexStart(int startContext) {
        
    }

    @Override
    public void onArrayIndexEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onArrayAccessEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onFieldAccess(int context, FieldAccessType fieldAccessType, ScopedName typeName,
            ReferenceType referenceType, long fieldName, int fieldNameContext) {
        
    }

    @Override
    public void onCastExpressionStart(int startContext) {
        
    }

    @Override
    public void onCastExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onThisPrimary(int leafContext) {
        
    }

    @Override
    public void onPrimaryEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onConditionalExpressionStart(int startContext) {
        
    }

    @Override
    public void onConditionalExpressionPart1Start(int startContext) {
        
    }

    @Override
    public void onConditionalExpressionPart1End(int startContext, Context endContext) {
        
    }

    @Override
    public void onConditionalExpressionPart2Start(int startContext) {
        
    }

    @Override
    public void onConditionalExpressionPart2End(int startContext, Context endContext) {
        
    }

    @Override
    public void onConditionalExpressionPart3Start(int startContext) {
        
    }

    @Override
    public void onConditionalExpressionPart3End(int startContext, Context endContext) {
        
    }

    @Override
    public void onConditionalExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onExpressionListStart(int startContext) {
        
    }

    @Override
    public void onExpressionListEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onIntegerLiteral(int leafContext, long value, Base base, boolean signed, int bits) {
        
    }

    @Override
    public void onFloatingPointLiteral(int leafContext, BigDecimal value, Base base, int bits) {
        
    }

    @Override
    public void onBooleanLiteral(int leafContext, boolean value) {
        
    }

    @Override
    public void onCharacterLiteral(int leafContext, char value) {
        
    }

    @Override
    public void onStringLiteral(int leafContext, long value) {
        
    }

    @Override
    public void onNullLiteral(int leafContext) {
        
    }

    @Override
    public void onClassInstanceCreationExpressionStart(int startContext) {
        
    }

    @Override
    public void onClassInstanceCreationTypeAndConstructorName(int context, Names names) {
        
    }

    @Override
    public void onClassInstanceCreationTypeAndConstructorName(int context, long name) {
        
    }

    @Override
    public void onClassInstanceCreationExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onMethodInvocationStart(int startContext, long methodName, int methodNameContext) {
        
    }

    @Override
    public void onParametersStart(int startContext) {
        
    }

    @Override
    public void onParameterStart(int startContext) {
        
    }

    @Override
    public void onParameterEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onParametersEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onMethodInvocationEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onArrayCreationExpressionStart(int startContext, ScopedName typeName, ReferenceType referenceType, int numDims) {
        
    }

    @Override
    public void onDimExpressionStart(int startContext) {
        
    }

    @Override
    public void onDimExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onArrayCreationExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onClassExpression(int context, long className, int numArrayDims) {
        
    }

    @Override
    public void onLambdaExpressionStart(int startContext) {
        
    }

    @Override
    public void onSingleLambdaParameter(int leafContext, long varName) {
        
    }

    @Override
    public void onFormalLambdaParameterListStart(int startContext) {
        
    }

    @Override
    public void onFormalLambdaParameterListEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onInferredLambdaParameterList(int context, List<String> varNames, int varNamesContext) {
        
    }

    @Override
    public void onLambdaBodyStart(int startContext) {
        
    }

    @Override
    public void onLambdaBodyEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onLambdaExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onMutabilityVariableModifier(int leafContext, ASTMutability mutability) {
        
    }

    @Override
    public void onVariableDeclarationStatementStart(int startContext) {
        
    }

    @Override
    public void onVariableDeclarationStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onVariableDeclaratorStart(int startContext) {
        
    }

    @Override
    public void onVariableDeclaratorEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onLeafTypeReference(int context, long name, ReferenceType referenceType) {
        
    }

    @Override
    public void onNonScopedTypeReferenceStart(int context, long name, ReferenceType referenceType) {
        
    }

    @Override
    public void onNonScopedTypeReferenceEnd(int context, Context endContext) {
        
    }

    @Override
    public void onScopedTypeReferenceStart(int startContext, ReferenceType referenceType) {
        
    }

    @Override
    public void onScopedTypeReferenceNameStart(int startContext) {
        
    }

    @Override
    public void onScopedTypeReferenceNamePart(int leafContext, long part) {
        
    }

    @Override
    public void onScopedTypeReferenceNameEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onGenericTypeParametersStart(int startContext) {
        
    }

    @Override
    public void onNamedGenericTypeParameterStart(int startContext, long name, int nameContext) {
        
    }

    @Override
    public void onNamedGenericTypeParameterEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onGenericTypeParametersEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onScopedTypeReferenceEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onExpressionStatementStart(int startContext) {
        
    }

    @Override
    public void onExpressionStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onForStatementStart(int startContext, long forKeyword, int forKeywordContext) {
        
    }

    @Override
    public void onForInitStart(int startContext) {
        
    }

    @Override
    public void onForInitEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onForExpressionStart(int startContext) {
        
    }

    @Override
    public void onForExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onForUpdateStart(int startContext) {
        
    }

    @Override
    public void onForUpdateEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onForStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onIteratorForStatementStart(int startContext, long forKeyword, int forKeywordContext) {
        
    }

    @Override
    public void onIteratorForTestStart(int startContext) {
        
    }

    @Override
    public void onIteratorForTestEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onIteratorForStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onWhileStatementStart(int startContext, long whileKeyword, int whileKeywordContext) {
        
    }

    @Override
    public void onWhileStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onDoWhileStatementStart(int startContext) {
        
    }

    @Override
    public void onDoWhileStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onTryWithResourcesStatementStart(int startContext, long tryKeyword, int tryKeywordContext) {
        
    }

    @Override
    public void onTryWithResourcesSpecificationStart(int startContext) {
        
    }

    @Override
    public void onResourceStart(int startContext) {
        
    }

    @Override
    public void onVariableName(int leafContext, long name, int numDims) {
        
    }

    @Override
    public void onResourceEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onTryWithResourcesSpecificationEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onTryWithResourcesStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onTryStatementStart(int startContext, long tryKeyword, int tryKeywordContext) {
        
    }

    @Override
    public void onTryBlockEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onCatchStart(int startContext, long catchKeyword, int catchKeywordContext) {
        
    }

    @Override
    public void onCatchEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onFinallyStart(int startContext, long finallyKeyword, int finallyKeywordContext) {
        
    }

    @Override
    public void onFinallyEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onTryStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onTryWithResourcesEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onReturnStatementStart(int startContext, long returnKeyword, int returnKeywordContext) {
        
    }

    @Override
    public void onReturnStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onThrowStatementStart(int startContext, long throwKeyword, int throwKeywordContext) {
        
    }

    @Override
    public void onThrowStatementEnd(int startContext, Context endContext) {
        
    }
}
