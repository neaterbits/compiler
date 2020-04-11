package com.neaterbits.compiler.parser.listener.encoded;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.block.ConstructorInvocation;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.util.buffers.DuplicateDetectingStringStorageBuffer;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;

import statement.ASTMutability;

abstract class BaseParserListener<COMPILATION_UNIT> implements ParserListener<COMPILATION_UNIT> {

    private final Tokenizer tokenizer;

    private final ASTBuffer astBuffer;

    private final ASTBuffer contextBuffer;
    
    private final IntKeyIntValueHash parseTreeRefToContextHash;
    
    private final Map<TypeName, Integer> typeNameToIndex;

    private final DuplicateDetectingStringStorageBuffer stringBuffer;

    protected abstract COMPILATION_UNIT makeCompilationUnit(
            ASTBufferRead astBuffer,
            ASTBufferRead contextBuffer,
            IntKeyIntValueHash parseTreeRefToContextHash,
            Map<TypeName, Integer> typeNameToIndex,
            DuplicateDetectingStringStorageBuffer stringBuffer);
    
    BaseParserListener(Tokenizer tokenizer) {
        
        Objects.requireNonNull(tokenizer);
        
        this.tokenizer = tokenizer;

        this.astBuffer = new ASTBufferImpl();
        this.contextBuffer = new ASTBufferImpl();
        
        this.parseTreeRefToContextHash = new IntKeyIntValueHash(100);
        
        this.typeNameToIndex = new HashMap<>();
        
        this.stringBuffer = new DuplicateDetectingStringStorageBuffer();
    }
    
    private int getStringIndex(long stringRef) {
        
        // TODO fix allocation
        return tokenizer.addToBuffer(stringBuffer, stringRef);
    }
    
    private void writeStringRef(long stringRef) {
        
        astBuffer.writeInt(getStringIndex(stringRef));
    }
    
    private void writeContext(Context context) {
        
        Objects.requireNonNull(context);
        
        contextBuffer.writeInt(context.getStartLine());
        
        contextBuffer.writeInt(context.getStartPosInLine());
        
        contextBuffer.writeInt(context.getStartOffset());
        
        contextBuffer.writeInt(context.getEndLine());
        
        contextBuffer.writeInt(context.getEndPosInLine());
        
        contextBuffer.writeInt(context.getEndOffset());
        
        contextBuffer.writeInt(context.getLength());
    }
    
    @Override
    public void onCompilationUnitStart(Context context) {

        astBuffer.writeElementStart(ParseTreeElement.COMPILATION_UNIT);
    }

    @Override
    public COMPILATION_UNIT onCompilationUnitEnd(Context context) {
        
        astBuffer.writeElementEnd(ParseTreeElement.COMPILATION_UNIT);

        return makeCompilationUnit(
                astBuffer,
                contextBuffer,
                parseTreeRefToContextHash,
                typeNameToIndex,
                stringBuffer);
    }

    @Override
    public void onImportStart(
            Context context,
            long importKeyword,
            Context importKeywordContext,
            long staticKeyword,
            Context staticKeywordContext) {

        writeContext(importKeywordContext);

        if (staticKeywordContext != null) {
            writeContext(staticKeywordContext);
        }
        
        astBuffer.writeElementStart(ParseTreeElement.IMPORT);
        
        astBuffer.writeBoolean(staticKeyword != StringRef.STRING_NONE);
        
    }

    @Override
    public void onImportIdentifier(Context context, long identifier) {
        
        writeContext(context);
        
        astBuffer.writeLeafElement(ParseTreeElement.IMPORT_NAME);
        
        writeStringRef(identifier);
    }

    @Override
    public void onImportEnd(Context context, boolean ondemand) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPORT);

        astBuffer.writeBoolean(ondemand);
        
    }

    @Override
    public void onNamespaceStart(Context context, long namespaceKeyword, Context namespaceKeywordContext, long name,
            Context nameContext, String[] parts) {

        writeContext(namespaceKeywordContext);
        
        astBuffer.writeElementStart(ParseTreeElement.NAMESPACE);
        
        if (parts.length > Byte.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        
        astBuffer.writeByte((byte)parts.length);
        
        for (String part : parts) {
            astBuffer.writeInt(stringBuffer.add(part));
        }
    }

    @Override
    public void onNameSpaceEnd(Context context) {

        writeContext(context);
        
        astBuffer.writeElementEnd(ParseTreeElement.NAMESPACE);
    }

    @Override
    public void onClassStart(Context context, long classKeyword, Context classKeywordContext, long name,
            Context nameContext) {

        writeContext(context);
        
        writeContext(classKeywordContext);
        
        writeContext(nameContext);
        
        astBuffer.writeElementStart(ParseTreeElement.CLASS_DEFINITION);
    }

    @Override
    public final void onVisibilityClassModifier(Context context, ClassVisibility visibility) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSubclassingModifier(Context context, Subclassing subclassing) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticClassModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStrictfpClassModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassExtends(Context context, long extendsKeyword, Context extendsKeywordContext,
            ScopedName className) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassImplements(Context context, ScopedName interfaceName) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAnonymousClassStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAnonymousClassEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticInitializerStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticInitializerEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorVisibilityModifier(Context context, ConstructorVisibility visibility) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorName(Context context, long constructorName) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorInvocationStart(Context context, ConstructorInvocation type) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorInvocationEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassMethodStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodReturnTypeStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodReturnTypeEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodName(Context context, long methodName) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodSignatureParametersStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodSignatureParameterStart(Context context, boolean varArgs) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodSignatureParameterEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodSignatureParametersEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVisibilityClassMethodModifier(Context context, ClassMethodVisibility visibility) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onOverrideClassMethodModifier(Context context, ClassMethodOverride methodOverride) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticClassMethodModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStrictfpClassMethodModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSynchronizedClassMethodModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNativeClassMethodModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassMethodEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFieldDeclarationStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVisibilityFieldModifier(Context context, FieldVisibility visibility) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticFieldModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMutabilityFieldModifier(Context context, ASTMutability mutability) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTransientFieldModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVolatileFieldModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFieldDeclarationEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceStart(Context context, long interfaceKeyword, Context interfaceKeywordContext, long name,
            Context nameContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVisibilityInterfaceModifier(Context context, InterfaceVisibility visibility) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAbstractInterfaceModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticInterfaceModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStrictfpInterfaceModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceExtends(Context context, ScopedName interfaceName) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumStart(Context context, long enumKeyword, Context enumKeywordContext, long name,
            Context nameContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumImplements(Context context, ScopedName interfaceName) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumConstantStart(Context context, long name) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumConstantEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceMethodStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVisibilityInterfaceMethodModifier(Context context, InterfaceMethodVisibility visibility) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAbstractInterfaceMethodModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDefaultInterfaceMethodModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticInterfaceMethodModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStrictfpInterfaceMethodModifier(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceMethodEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnterAssignmentExpression(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnterAssignmentLHS(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onExitAssignmentLHS(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onExitAssignmentExpression(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNestedExpressionStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNestedExpressionEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNameReference(Context context, long name) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVariableReference(Context context, long name) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onPrimaryStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayAccessStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayIndexStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayIndexEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayAccessEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFieldAccess(Context context, FieldAccessType fieldAccessType, ScopedName typeName,
            ReferenceType referenceType, long fieldName, Context fieldNameContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCastExpressionStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCastExpressionEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onThisPrimary(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onPrimaryEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart1Start(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart1End(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart2Start(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart2End(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart3Start(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart3End(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onBooleanLiteral(Context context, boolean value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCharacterLiteral(Context context, char value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStringLiteral(Context context, long value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNullLiteral(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassInstanceCreationExpressionStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassInstanceCreationTypeAndConstructorName(Context context, ScopedName name) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassInstanceCreationExpressionEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodInvocationStart(Context context, MethodInvocationType type, ScopedName classTypeName,
            Context classTypeNameContext, ReferenceType referenceType, long methodName, Context methodNameContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onParametersStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onParameterStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onParameterEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onParametersEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodInvocationEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayCreationExpressionStart(Context context, ScopedName typeName, ReferenceType referenceType,
            int numDims) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDimExpressionStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDimExpressionEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayCreationExpressionEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassExpression(Context context, long className, int numArrayDims) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onLambdaExpressionStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSingleLambdaParameter(Context context, long varName, Context varNameContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFormalLambdaParameterListStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFormalLambdaParameterListEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInferredLambdaParameterList(Context context, List<String> varNames, Context varNamesContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onLambdaBodyStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onLambdaBodyEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onLambdaExpressionEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMutabilityVariableModifier(Context context, ASTMutability mutability) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVariableDeclarationStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVariableDeclarationStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVariableDeclaratorStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVariableDeclaratorEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTypeReference(Context context, ScopedName name, ReferenceType referenceType) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onExpressionStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onExpressionStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForStatementStart(Context context, long keyword, Context keywordContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForInitStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForInitEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForUpdateStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForUpdateEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIteratorForStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIteratorForTestEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIteratorForStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onWhileStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onWhileStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDoWhileStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDoWhileStatementEnd(Context context) {

        throw new UnsupportedOperationException();
   }

    @Override
    public final void onTryWithResourcesStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryWithResourcesSpecificationStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onResourceStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVariableName(Context context, long name, int numDims) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onResourceEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryWithResourcesSpecificationEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryBlockEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCatchStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCatchEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFinallyStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFinallyEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryWithResourcesEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onReturnStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onReturnStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onThrowStatementStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onThrowStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAnnotationStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAnnotationEnd(Context context) {

        throw new UnsupportedOperationException();
    }
}
