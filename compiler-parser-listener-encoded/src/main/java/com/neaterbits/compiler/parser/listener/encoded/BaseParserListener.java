package com.neaterbits.compiler.parser.listener.encoded;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.neaterbits.compiler.util.statement.ASTMutability;
import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;

abstract class BaseParserListener<COMPILATION_UNIT> implements ParserListener<COMPILATION_UNIT> {

    private final String file;
    private final StringASTBuffer astBuffer;

    private final ASTBuffer contextBuffer;
    
    private final IntKeyIntValueHash parseTreeRefToStartContextHash;
    private final IntKeyIntValueHash parseTreeRefToEndContextHash;
    
    private final Map<TypeName, Integer> typeNameToIndex;

    protected abstract COMPILATION_UNIT makeCompilationUnit(
            String file,
            ASTBufferRead astBuffer,
            ASTBufferRead contextBuffer,
            IntKeyIntValueHash parseTreeRefToStartContextHash,
            IntKeyIntValueHash parseTreeRefToEndContextHash,
            Map<TypeName, Integer> typeNameToIndex,
            MapStringStorageBuffer stringBuffer);
    
    BaseParserListener(String file, Tokenizer tokenizer) {

        this.file = file;
        this.astBuffer = new StringASTBuffer(tokenizer);

        this.contextBuffer = new ASTBufferImpl();
        
        this.parseTreeRefToStartContextHash = new IntKeyIntValueHash(100);
        this.parseTreeRefToEndContextHash = new IntKeyIntValueHash(100);
        
        this.typeNameToIndex = new HashMap<>();
    }
    
    private void writeStartElementContext(Context context) {
        
        final int parseTreeRef = astBuffer.getParseTreeRef();
        
        final int contextBufferPos = contextBuffer.getWritePos();
        
        AST.writeContext(contextBuffer, context);
        
        parseTreeRefToStartContextHash.put(parseTreeRef, contextBufferPos);
    }

    private void addEndElementContext(Context context) {
        
        // final int parseTreeRef = astBuffer.getParseTreeRef();
        
        // parseTreeRefToEndContextHash.put(parseTreeRef, contextBufferPos);
    }
    
    private int writeOtherContext(Context context) {
        
        return AST.writeContext(contextBuffer, context);
    }
    
    @Override
    public void onCompilationUnitStart(Context context) {

        astBuffer.writeElementStart(ParseTreeElement.COMPILATION_UNIT);
    }

    @Override
    public COMPILATION_UNIT onCompilationUnitEnd(Context context) {
        
        astBuffer.writeElementEnd(ParseTreeElement.COMPILATION_UNIT);

        return makeCompilationUnit(
                file,
                astBuffer.getASTReadBuffer(),
                contextBuffer,
                parseTreeRefToStartContextHash,
                parseTreeRefToEndContextHash,
                typeNameToIndex,
                astBuffer.getStringBuffer());
    }

    @Override
    public void onImportStart(
            Context context,
            long importKeyword,
            Context importKeywordContext,
            long staticKeyword,
            Context staticKeywordContext) {

        writeStartElementContext(importKeywordContext);

        final int importKeywordContextRef = writeOtherContext(importKeywordContext);
        
        final int staticKeywordContextRef = staticKeywordContext != null
                ? writeOtherContext(staticKeywordContext)
                : AST.NO_CONTEXTREF;
        
        AST.encodeImportStart(
                astBuffer,
                importKeyword,
                importKeywordContextRef,
                staticKeyword,
                staticKeywordContextRef);
    }

    @Override
    public void onImportIdentifier(Context context, long identifier) {
        
        writeStartElementContext(context);

        AST.encodeImportNamePart(astBuffer, identifier);
    }

    @Override
    public void onImportEnd(Context context, boolean ondemand) {
        
        addEndElementContext(context);
        
        AST.encodeImportEnd(astBuffer, ondemand);
    }

    @Override
    public void onNamespaceStart(Context context, long namespaceKeyword, Context namespaceKeywordContext) {

        if (context != null) {
            writeStartElementContext(context);
        }

        final int namespaceKeywordContextRef = writeOtherContext(namespaceKeywordContext);

        AST.encodeNamespaceStart(astBuffer, namespaceKeyword, namespaceKeywordContextRef);
    }
    

    @Override
    public void onNamespacePart(Context context, long part) {

        writeStartElementContext(context);
        
        AST.encodeNamespacePart(astBuffer, part);
    }

    @Override
    public void onNameSpaceEnd(Context context) {

        AST.encodeNamespaceEnd(astBuffer);
    }

    @Override
    public void onClassStart(Context context, long classKeyword, Context classKeywordContext, long name, Context nameContext) {

        writeStartElementContext(context);
        
        final int classKeywordContextRef = writeOtherContext(classKeywordContext);
        
        final int nameContextRef = writeOtherContext(nameContext);
        
        AST.encodeClassStart(astBuffer, classKeyword, classKeywordContextRef, name, nameContextRef);
    }

    @Override
    public final void onVisibilityClassModifier(Context context, ClassVisibility visibility) {
        
        writeStartElementContext(context);

        AST.encodeVisibilityClassModifier(astBuffer, visibility);
    }

    @Override
    public final void onSubclassingModifier(Context context, Subclassing subclassing) {

        writeStartElementContext(context);

        AST.encodeSubclassingModifier(astBuffer, subclassing);
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
    public final void onClassExtendsStart(Context context, long extendsKeyword, Context extendsKeywordContext) {

        writeStartElementContext(context);
        
        final int extendsKeywordContextRef = writeOtherContext(extendsKeywordContext);
        
        AST.encodeClassExtendsStart(astBuffer, extendsKeyword, extendsKeywordContextRef);
    }
    

    @Override
    public void onClassExtendsNamePart(Context context, long identifier) {
        
        writeStartElementContext(context);

        AST.encodeClassExtendsNamePart(astBuffer, identifier);
    }

    @Override
    public void onClassExtendsEnd(Context context) {

        AST.encodeClassExtendsEnd(astBuffer);
    }

    @Override
    public final void onClassImplementsStart(Context context, long implementsKeyword, Context implementsKeywordContext) {

        writeStartElementContext(context);
        
        final int implementsKeywordContextRef = writeOtherContext(implementsKeywordContext);
        
        AST.encodeClassImplementsStart(astBuffer, implementsKeyword, implementsKeywordContextRef);
    }
    
    @Override
    public void onClassImplementsTypeStart(Context context) {

        AST.encodeClassImplementsTypeStart(astBuffer);
    }

    @Override
    public void onClassImplementsNamePart(Context context, long identifier) {
        
        writeStartElementContext(context);

        AST.encodeClassImplementsNamePart(astBuffer, identifier);
    }

    @Override
    public void onClassImplementsTypeEnd(Context context) {

        AST.encodeClassImplementsTypeEnd(astBuffer);
    }

    @Override
    public void onClassImplementsEnd(Context context) {

        AST.encodeClassImplementsEnd(astBuffer);
    }

    @Override
    public final void onClassEnd(Context context) {

        AST.encodeClassEnd(astBuffer);
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

        AST.encodeClassMethodStart(astBuffer, context);
    }

    @Override
    public final void onMethodReturnTypeStart(Context context) {

        AST.encodeMethodReturnTypeStart(astBuffer, context);
    }

    @Override
    public final void onMethodReturnTypeEnd(Context context) {

        AST.encodeMethodReturnTypeEnd(astBuffer, context);
    }

    @Override
    public final void onMethodName(Context context, long methodName) {

        AST.encodeMethodName(astBuffer, context, methodName);
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

        AST.encodeClassMethodEnd(astBuffer, context);
    }

    @Override
    public final void onFieldDeclarationStart(Context context) {

        writeStartElementContext(context);

        AST.encodeFieldDeclarationStart(astBuffer);
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

        AST.encodeFieldDeclarationEnd(astBuffer);
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

        AST.encodeVariableDeclaratorStart(astBuffer);
    }

    @Override
    public final void onVariableDeclaratorEnd(Context context) {

        AST.encodeVariableDeclaratorEnd(astBuffer);
    }

    @Override
    public void onNonScopedTypeReference(Context context, long name, ReferenceType referenceType) {

        switch (referenceType) {
        case SCALAR:
            AST.encodeScalarTypeReference(astBuffer, name);
            break;
            
        case REFERENCE:
            AST.encodeIdentifierTypeReference(astBuffer, name);
            break;
            
        default:
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onScopedTypeReferenceStart(Context context, ReferenceType referenceType) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void onScopedTypeReferencePart(Context context, long part) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void onScopedTypeReferenceEnd(Context context) {

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

        AST.encodeVariableName(astBuffer, name, numDims);
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
