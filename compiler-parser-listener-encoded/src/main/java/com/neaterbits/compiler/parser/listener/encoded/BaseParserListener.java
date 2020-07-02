package com.neaterbits.compiler.parser.listener.encoded;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.FullContext;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.ImmutableFullContext;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.block.ConstructorInvocation;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.name.Names;
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
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;

abstract class BaseParserListener<COMPILATION_UNIT> implements ParserListener<COMPILATION_UNIT> {

    final StringASTBuffer astBuffer;

    private final ASTBuffer contextBuffer;
    private final FullContextProvider fullContextProvider;
    
    private final IntKeyIntValueHash parseTreeRefToStartContextHash;
    private final IntKeyIntValueHash parseTreeRefToEndContextHash;
    
    private final Map<TypeName, Integer> typeNameToIndex;

    protected abstract COMPILATION_UNIT makeCompilationUnit(
            ASTBufferRead astBuffer,
            ASTBufferRead contextBuffer,
            FullContextProvider fullContextProvider,
            IntKeyIntValueHash parseTreeRefToStartContextHash,
            IntKeyIntValueHash parseTreeRefToEndContextHash,
            Map<TypeName, Integer> typeNameToIndex,
            MapStringStorageBuffer stringBuffer);
    
    BaseParserListener(String file, Tokenizer tokenizer) {

        this.astBuffer = new StringASTBuffer(tokenizer);

        this.contextBuffer = new ASTBufferImpl();
        
        this.fullContextProvider = new FullContextProvider() {
            
            @Override
            public FullContext makeFullContext(Context context) {
                return new ImmutableFullContext(file, -1, -1, context.getStartOffset(), -1, -1, context.getEndOffset(), getText(context));
            }

            @Override
            public String getText(Context context) {
                return tokenizer.asStringFromOffset(context.getStartOffset(), context.getEndOffset());
            }
        };
        
        this.parseTreeRefToStartContextHash = new IntKeyIntValueHash(100);
        this.parseTreeRefToEndContextHash = new IntKeyIntValueHash(100);
        
        this.typeNameToIndex = new HashMap<>();
    }

    final void writeStartElementContextRef(int startElementContextRef) {
        
        final int parseTreeRef = astBuffer.getParseTreeRef();
        
        parseTreeRefToStartContextHash.put(parseTreeRef, startElementContextRef);
    }
    
    final void verifyNotSameContext(int context1, int context2) {
        if (context1 == context2) {
            throw new IllegalArgumentException();
        }
    }

    final void verifyNotSameContext(int context1, int context2, int context3) {

        verifyNotSameContext(context1, context2);
        verifyNotSameContext(context1, context3);
        verifyNotSameContext(context2, context3);
    }

    final void writeLeafElementContextRef(int leafContextRef) {

        final int parseTreeRef = astBuffer.getParseTreeRef();
        
        parseTreeRefToStartContextHash.put(parseTreeRef, leafContextRef);

        // writeElementContext(context);
    }

    final void writeEndElementContext(int startElementContextRef, Context endContext) {
        
        // TODO update start context with end context information
        // to merge start pos with end pos
    }

    @Override
    public int writeContext(Context context) {

        return AST.writeContext(contextBuffer, context);
    }

    @Override
    public int writeContext(int otherContext) {
        
        return AST.writeContext(contextBuffer, otherContext);
    }

    @Override
    public void onCompilationUnitStart(int compilationUnitStartContext) {
        
        writeStartElementContextRef(compilationUnitStartContext);

        astBuffer.writeElementStart(ParseTreeElement.COMPILATION_UNIT);
    }

    @Override
    public COMPILATION_UNIT onCompilationUnitEnd(int compilationUnitStartContext, Context endContext) {
        
        writeEndElementContext(compilationUnitStartContext, endContext);
        
        astBuffer.writeElementEnd(ParseTreeElement.COMPILATION_UNIT);

        return makeCompilationUnit(
                astBuffer.getASTReadBuffer(),
                contextBuffer,
                fullContextProvider,
                parseTreeRefToStartContextHash,
                parseTreeRefToEndContextHash,
                typeNameToIndex,
                astBuffer.getStringBuffer());
    }

    @Override
    public void onImportStart(
            int importStartContext,
            long importKeyword,
            int importKeywordContext,
            long staticKeyword,
            int staticKeywordContext) {
        
        verifyNotSameContext(importStartContext, importKeywordContext, staticKeywordContext);

        writeStartElementContextRef(importStartContext);

        AST.encodeImportStart(
                astBuffer,
                importKeyword,
                importKeywordContext,
                staticKeyword,
                staticKeywordContext);
    }

    @Override
    public void onImportName(int identifierContext, long identifier) {
        
        writeLeafElementContextRef(identifierContext);

        AST.encodeImportNamePart(astBuffer, identifier);
    }

    @Override
    public void onImportEnd(int importStartContext, Context endContext, boolean ondemand) {
        
        writeEndElementContext(importStartContext, endContext);
        
        AST.encodeImportEnd(astBuffer, ondemand);
    }

    @Override
    public void onNamespaceStart(int namespaceStartContext, long namespaceKeyword, int namespaceKeywordContext) {

        verifyNotSameContext(namespaceStartContext, namespaceKeywordContext);
        
        writeStartElementContextRef(namespaceStartContext);

        AST.encodeNamespaceStart(astBuffer, namespaceKeyword, namespaceKeywordContext);
    }
    

    @Override
    public void onNamespacePart(int namespacePartContext, long part) {

        writeLeafElementContextRef(namespacePartContext);
        
        AST.encodeNamespacePart(astBuffer, part);
    }

    @Override
    public void onNameSpaceEnd(int namespaceEndContext, Context endContext) {
        
        writeEndElementContext(namespaceEndContext, endContext);

        AST.encodeNamespaceEnd(astBuffer);
    }

    @Override
    public void onTypeDefinitionStart(int startContext) {
        
        writeStartElementContextRef(startContext);
        
        AST.encodeTypeDefinitionStart(astBuffer);
    }

    @Override
    public void onTypeDefinitionEnd(int startContext, Context endContext) {

        writeEndElementContext(startContext, endContext);

        AST.encodeTypeDefinitionEnd(astBuffer);
    }

    @Override
    public void onClassStart(int classStartContext, long classKeyword, int classKeywordContext, long name, int nameContext) {

        writeStartElementContextRef(classStartContext);
        
        AST.encodeClassStart(astBuffer, classKeyword, classKeywordContext, name, nameContext);
    }

    @Override
    public final void onVisibilityClassModifier(int context, ClassVisibility visibility) {
        
        writeLeafElementContextRef(context);

        AST.encodeVisibilityClassModifier(astBuffer, visibility);
    }

    @Override
    public final void onSubclassingModifier(int context, Subclassing subclassing) {

        writeLeafElementContextRef(context);

        AST.encodeSubclassingModifier(astBuffer, subclassing);
    }

    @Override
    public final void onStaticClassModifier(int context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStrictfpClassModifier(int context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onGenericClassDefinitionTypeListStart(int startContext) {
        
        writeStartElementContextRef(startContext);

        AST.encodeTypeArgumentListStart(astBuffer);
    }

    @Override
    public final void onGenericNamedTypeStart(int startContext, long name, int nameContext) {
        
        writeStartElementContextRef(startContext);
        
        AST.encodeNamedTypeArgumentStart(astBuffer, name, nameContext);
    }

    @Override
    public final void onGenericNamedTypeEnd(int startContext, Context endContext) {

        writeEndElementContext(startContext, endContext);

        AST.encodeNamedTypeArgumentEnd(astBuffer);
    }

    @Override
    public final void onGenericWildcardTypeStart(int startContext) {
        
        writeStartElementContextRef(startContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onGenericWildcardTypeEnd(int startContext, Context endContext) {
        
        writeEndElementContext(startContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTypeBoundStart(int startContext, TypeBoundType type) {
        
        writeStartElementContextRef(startContext);

        AST.encodeTypeBoundStart(astBuffer, type);
    }

    @Override
    public final void onTypeBoundEnd(int startContext, Context endContext) {
        
        writeEndElementContext(startContext, endContext);

        AST.encodeTypeBoundEnd(astBuffer);
    }

    @Override
    public final void onGenericClassDefinitionTypeListEnd(int startContext, Context endContext) {
        
        writeEndElementContext(startContext, endContext);

        AST.encodeTypeArgumentListEnd(astBuffer);
    }

    @Override
    public final void onClassExtendsStart(int classExtendsStartContext, long extendsKeyword, int extendsKeywordContext) {
        
        verifyNotSameContext(classExtendsStartContext, extendsKeywordContext);

        writeStartElementContextRef(classExtendsStartContext);
        
        AST.encodeClassExtendsStart(astBuffer, extendsKeyword, extendsKeywordContext);
    }
    

    @Override
    public void onClassExtendsNamePart(int leafContext, long identifier) {
        
        writeLeafElementContextRef(leafContext);

        AST.encodeClassExtendsNamePart(astBuffer, identifier);
    }

    @Override
    public void onClassExtendsEnd(int classExtendsStartContext, Context endContext) {

        writeEndElementContext(classExtendsStartContext, endContext);
        
        AST.encodeClassExtendsEnd(astBuffer);
    }

    @Override
    public final void onClassImplementsStart(int classImplementsStartContext, long implementsKeyword, int implementsKeywordContext) {

        verifyNotSameContext(classImplementsStartContext, implementsKeywordContext);
        
        writeStartElementContextRef(classImplementsStartContext);
        
        AST.encodeClassImplementsStart(astBuffer, implementsKeyword, implementsKeywordContext);
    }
    
    @Override
    public void onClassImplementsTypeStart(int classImplementsTypeStartContext) {

        writeStartElementContextRef(classImplementsTypeStartContext);
        
        AST.encodeClassImplementsTypeStart(astBuffer);
    }

    @Override
    public void onClassImplementsNamePart(int leafContext, long identifier) {
        
        writeLeafElementContextRef(leafContext);

        AST.encodeClassImplementsNamePart(astBuffer, identifier);
    }

    @Override
    public void onClassImplementsTypeEnd(int classImplementsTypeEndContext, Context endContext) {

        writeEndElementContext(classImplementsTypeEndContext, endContext);
        
        AST.encodeClassImplementsTypeEnd(astBuffer);
    }

    @Override
    public void onClassImplementsEnd(int classImplementsStartContext, Context endContext) {

        writeEndElementContext(classImplementsStartContext, endContext);
        
        AST.encodeClassImplementsEnd(astBuffer);
    }

    @Override
    public final void onClassEnd(int classStartContext, Context endContext) {

        writeEndElementContext(classStartContext, endContext);
        
        AST.encodeClassEnd(astBuffer);
    }

    @Override
    public final void onAnonymousClassStart(int anonymousClassStartContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAnonymousClassEnd(int anonymousClassStartContext, Context endContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticInitializerStart(int staticInitializerStartContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticInitializerEnd(int staticInitializerStartContext, Context endContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorStart(int constructorStartContext) {

        writeStartElementContextRef(constructorStartContext);
        
        AST.encodeConstructorStart(astBuffer);
    }

    @Override
    public final void onConstructorVisibilityModifier(int context, ConstructorVisibility visibility) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorName(int context, long constructorName) {

        writeLeafElementContextRef(context);
        
        AST.encodeConstructorName(astBuffer, constructorName);
    }

    @Override
    public final void onConstructorInvocationStart(int constructorInvoicationStartContext, ConstructorInvocation type) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorInvocationEnd(int constructorInvoicationStartContext, Context endContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstructorEnd(int constructorStartContext, Context endContext) {

        writeEndElementContext(constructorStartContext, endContext);

        AST.encodeConstructorEnd(astBuffer);
    }

    @Override
    public final void onClassMethodStart(int classMethodStartContext) {

        writeStartElementContextRef(classMethodStartContext);
        
        AST.encodeClassMethodStart(astBuffer);
    }

    @Override
    public final void onMethodReturnTypeStart(int methodReturnTypeStartContext) {

        writeStartElementContextRef(methodReturnTypeStartContext);
        
        AST.encodeMethodReturnTypeStart(astBuffer);
    }

    @Override
    public final void onMethodReturnTypeEnd(int methodReturnTypeStartContext, Context endContext) {

        writeEndElementContext(methodReturnTypeStartContext, endContext);
        
        AST.encodeMethodReturnTypeEnd(astBuffer);
    }

    @Override
    public final void onMethodName(int leafContext, long methodName) {
        
        writeLeafElementContextRef(leafContext);

        AST.encodeMethodName(astBuffer, methodName);
    }

    @Override
    public final void onMethodSignatureParametersStart(int methodSignatureParametersStartContext) {

        writeStartElementContextRef(methodSignatureParametersStartContext);
        
        AST.encodeSignatureParametersStart(astBuffer);
    }

    @Override
    public final void onMethodSignatureParameterStart(int methodSignatureParameterStartContext, boolean varArgs) {
        
        writeStartElementContextRef(methodSignatureParameterStartContext);

        AST.encodeSignatureParameterStart(astBuffer, varArgs);
    }

    @Override
    public final void onMethodSignatureParameterEnd(int methodSignatureParameterStartContext, Context endContext) {
        
        writeEndElementContext(methodSignatureParameterStartContext, endContext);

        AST.encodeSignatureParameterEnd(astBuffer);
    }

    @Override
    public final void onMethodSignatureParametersEnd(int methodSignatureParametersStartContext, Context endContext) {

        writeEndElementContext(methodSignatureParametersStartContext, endContext);
        
        AST.encodeSignatureParametersEnd(astBuffer);
    }

    @Override
    public final void onVisibilityClassMethodModifier(int leafContext, ClassMethodVisibility visibility) {

        writeLeafElementContextRef(leafContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onOverrideClassMethodModifier(int leafContext, ClassMethodOverride methodOverride) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticClassMethodModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStrictfpClassMethodModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSynchronizedClassMethodModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNativeClassMethodModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassMethodEnd(int classMethodStartContext, Context endContext) {

        writeEndElementContext(classMethodStartContext, endContext);

        AST.encodeClassMethodEnd(astBuffer);
    }

    @Override
    public final void onFieldDeclarationStart(int fieldDeclarationStartContext) {

        writeStartElementContextRef(fieldDeclarationStartContext);

        AST.encodeFieldDeclarationStart(astBuffer);
    }

    @Override
    public final void onVisibilityFieldModifier(int leafContext, FieldVisibility visibility) {

        writeLeafElementContextRef(leafContext);

        AST.encodeVisibilityFieldModifier(astBuffer, visibility);
    }

    @Override
    public final void onStaticFieldModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMutabilityFieldModifier(int leafContext, ASTMutability mutability) {

        writeLeafElementContextRef(leafContext);

        AST.encodeMutabilityModifier(astBuffer, mutability);
    }

    @Override
    public final void onTransientFieldModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVolatileFieldModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFieldDeclarationEnd(int fieldDeclaratrionStartContext, Context endContext) {

        writeEndElementContext(fieldDeclaratrionStartContext, endContext);

        AST.encodeFieldDeclarationEnd(astBuffer);
    }

    @Override
    public final void onInterfaceStart(int interfaceStartContext, long interfaceKeyword, int interfaceKeywordContext, long name,
            int nameContext) {
        
        writeStartElementContextRef(interfaceStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVisibilityInterfaceModifier(int leafContext, InterfaceVisibility visibility) {
        
        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAbstractInterfaceModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticInterfaceModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStrictfpInterfaceModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceExtends(int interfaceExtendsStartContext, ScopedName interfaceName) {

        writeStartElementContextRef(interfaceExtendsStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceEnd(int interfaceExtendsStartContext, Context endContext) {

        writeEndElementContext(interfaceExtendsStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumStart(int enumStartContext, long enumKeyword, int enumKeywordContext, long name,
            int nameContext) {

        writeStartElementContextRef(enumStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumImplements(int context, ScopedName interfaceName) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumConstantStart(int enumConstantStartContext, long name) {
        
        writeStartElementContextRef(enumConstantStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumConstantEnd(int enumConstantStartContext, Context endContext) {

        writeEndElementContext(enumConstantStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumEnd(int enumStartContext, Context endContext) {

        writeEndElementContext(enumStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceMethodStart(int interfaceMethodStartContext) {

        writeStartElementContextRef(interfaceMethodStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVisibilityInterfaceMethodModifier(int leafContext, InterfaceMethodVisibility visibility) {
        
        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAbstractInterfaceMethodModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDefaultInterfaceMethodModifier(int leafContext) {
        
        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStaticInterfaceMethodModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStrictfpInterfaceMethodModifier(int leafContext) {

        writeLeafElementContextRef(leafContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInterfaceMethodEnd(int interfaceMethodStartContext, Context endContext) {

        writeEndElementContext(interfaceMethodStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnterAssignmentExpression(int assignmentExpressionStartContext) {

        writeStartElementContextRef(assignmentExpressionStartContext);
        
        AST.encodeAssignmentExpressionStart(astBuffer);
    }

    @Override
    public final void onEnterAssignmentLHS(int assignmentLHSStartContext) {

        writeStartElementContextRef(assignmentLHSStartContext);

        AST.encodeAssignmentLHSStart(astBuffer);
    }

    @Override
    public final void onExitAssignmentLHS(int assignmentLHSStartContext, Context endContext) {

        writeEndElementContext(assignmentLHSStartContext, endContext);
        
        AST.encodeAssignmentLHSEnd(astBuffer);
    }

    @Override
    public final void onExitAssignmentExpression(int assignmenExpressionContext, Context endContext) {

        writeEndElementContext(assignmenExpressionContext, endContext);

        AST.encodeAssignmentExpressionEnd(astBuffer);
    }

    @Override
    public final void onNestedExpressionStart(int nestedExpressionStartContext) {

        writeStartElementContextRef(nestedExpressionStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNestedExpressionEnd(int nestedExpressionStartContext, Context endContext) {

        writeEndElementContext(nestedExpressionStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNameReference(int leafContext, long name) {
        
        writeLeafElementContextRef(leafContext);

        AST.encodeNameReference(astBuffer, name, leafContext);
    }

    @Override
    public final void onVariableReference(int leafContext, long name) {

        writeLeafElementContextRef(leafContext);
        
        AST.encodeVariableReference(astBuffer, name);
    }

    @Override
    public final void onPrimaryStart(int primaryStartContext) {

        writeStartElementContextRef(primaryStartContext);

        AST.encodePrimariesStart(astBuffer);
    }

    @Override
    public final void onArrayAccessStart(int arrayAccessStartContext) {

        writeStartElementContextRef(arrayAccessStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayIndexStart(int arrayIndexStartContext) {

        writeStartElementContextRef(arrayIndexStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayIndexEnd(int arrayIndexStartContext, Context endContext) {

        writeEndElementContext(arrayIndexStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayAccessEnd(int arrayAccesStartContext, Context endContext) {

        writeEndElementContext(arrayAccesStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFieldAccess(int context, FieldAccessType fieldAccessType, ScopedName typeName,
            ReferenceType referenceType, long fieldName, int fieldNameContext) {

        writeLeafElementContextRef(context);
        
        AST.encodeFieldAccess(astBuffer, fieldName);
    }

    @Override
    public final void onCastExpressionStart(int castExpressionStartContext) {

        writeStartElementContextRef(castExpressionStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCastExpressionEnd(int castExpressionStartContext, Context endContext) {

        writeEndElementContext(castExpressionStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onThisPrimary(int leafContext) {
        
        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onPrimaryEnd(int primaryStartContext, Context endContext) {

        writeEndElementContext(primaryStartContext, endContext);

        AST.encodePrimariesEnd(astBuffer);
    }

    @Override
    public final void onConditionalExpressionStart(int conditionalExpressionStartContext) {

        writeStartElementContextRef(conditionalExpressionStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart1Start(int startContext) {
        
        writeStartElementContextRef(startContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart1End(int startContext, Context endContext) {

        writeEndElementContext(startContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart2Start(int startContext) {

        writeStartElementContextRef(startContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart2End(int startContext, Context endContext) {
        
        writeEndElementContext(startContext, endContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart3Start(int startContext) {
        
        writeStartElementContextRef(startContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionPart3End(int startContext, Context endContext) {
        
        writeEndElementContext(startContext, endContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConditionalExpressionEnd(int conditionalExpressionStartContext, Context endContext) {

        writeEndElementContext(conditionalExpressionStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIntegerLiteral(int leafContext, long value, Base base, boolean signed, int bits) {

        writeLeafElementContextRef(leafContext);
        
        AST.encodeIntegerLiteral(astBuffer, value, base, signed, bits);
    }

    @Override
    public final void onFloatingPointLiteral(int leafContext, BigDecimal value, Base base, int bits) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onBooleanLiteral(int leafContext, boolean value) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCharacterLiteral(int leafContext, char value) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onStringLiteral(int leafContext, long value) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onNullLiteral(int leafContext) {

        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassInstanceCreationExpressionStart(int classInstanceCreationExpressionStartContext) {

        writeStartElementContextRef(classInstanceCreationExpressionStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassInstanceCreationTypeAndConstructorName(int context, ScopedName name) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassInstanceCreationExpressionEnd(int classInstanceCreationExpressionStartContext, Context endContext) {

        writeEndElementContext(classInstanceCreationExpressionStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMethodInvocationStart(
            int context,
            MethodInvocationType type,
            Names names,
            long methodName,
            int methodNameContext) {

        if (names != null) {
            AST.encodeUnresolvedMethodInvocationStart(astBuffer, type, names, methodName, methodNameContext);
        }
        else {
            AST.encodeMethodInvocationStart(astBuffer, type, methodName, methodNameContext);
        }
    }

    @Override
    public final void onParametersStart(int parametersStartContext) {

        writeStartElementContextRef(parametersStartContext);
        
        AST.encodeParametersStart(astBuffer);
    }

    @Override
    public final void onParameterStart(int parameterContext) {

        writeStartElementContextRef(parameterContext);
        
        AST.encodeParameterStart(astBuffer);
    }

    @Override
    public final void onParameterEnd(int parameterStartContext, Context endContext) {

        writeEndElementContext(parameterStartContext, endContext);

        AST.encodeParameterEnd(astBuffer);
    }

    @Override
    public final void onParametersEnd(int parametersStartContext, Context endContext) {

        writeEndElementContext(parametersStartContext, endContext);

        AST.encodeParametersEnd(astBuffer);
    }

    @Override
    public final void onMethodInvocationEnd(int methodInvocationStartContext, boolean resolved, Context endContext) {
        
        writeEndElementContext(methodInvocationStartContext, endContext);

        if (resolved) {
            AST.encodeMethodInvocationEnd(astBuffer);
        }
        else {
            AST.encodeUnresolvedMethodInvocationEnd(astBuffer);
        }
    }

    @Override
    public final void onArrayCreationExpressionStart(int arrayCreationExpressionStartContext, ScopedName typeName, ReferenceType referenceType,
            int numDims) {
        
        writeStartElementContextRef(arrayCreationExpressionStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDimExpressionStart(int dimExpressionStartContext) {

        writeStartElementContextRef(dimExpressionStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDimExpressionEnd(int dimExpressionStartContext, Context endContext) {

        writeEndElementContext(dimExpressionStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onArrayCreationExpressionEnd(int arrayCreationExpressionStartContext, Context endContext) {

        writeEndElementContext(arrayCreationExpressionStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onClassExpression(int leafContext, long className, int numArrayDims) {

        writeLeafElementContextRef(leafContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onLambdaExpressionStart(int lambdaExpressionStartContext) {
        
        writeStartElementContextRef(lambdaExpressionStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSingleLambdaParameter(int leafContext, long varName) {

        writeLeafElementContextRef(leafContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFormalLambdaParameterListStart(int formalLambdaParameterListStartContext) {
        
        writeStartElementContextRef(formalLambdaParameterListStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFormalLambdaParameterListEnd(int formalLambdaParameterListStartContext, Context endContext) {

        writeEndElementContext(formalLambdaParameterListStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onInferredLambdaParameterList(int context, List<String> varNames, int varNamesContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onLambdaBodyStart(int lambdaBodyStartContext) {

        writeStartElementContextRef(lambdaBodyStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onLambdaBodyEnd(int lambdaBodyStartContext, Context endContext) {

        writeEndElementContext(lambdaBodyStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onLambdaExpressionEnd(int lambdaExpressionStartContext, Context endContext) {
        
        writeEndElementContext(lambdaExpressionStartContext, endContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onMutabilityVariableModifier(int leafContext, ASTMutability mutability) {
        
        writeLeafElementContextRef(leafContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVariableDeclarationStatementStart(int variableDeclarationStatementStartContext) {
        
        writeStartElementContextRef(variableDeclarationStatementStartContext);

        AST.encodeVariableDeclarationStatementStart(astBuffer);
    }

    @Override
    public final void onVariableDeclarationStatementEnd(int variableDeclarationStatementStartContext, Context endContext) {

        writeEndElementContext(variableDeclarationStatementStartContext, endContext);
        
        AST.encodeVariableDeclarationStatementEnd(astBuffer);
    }

    @Override
    public final void onVariableDeclaratorStart(int variableDeclaratorStartContext) {

        writeStartElementContextRef(variableDeclaratorStartContext);
        
        AST.encodeVariableDeclaratorStart(astBuffer);
    }

    @Override
    public final void onVariableDeclaratorEnd(int variableDeclaratorStartContext, Context endContext) {
        
        writeEndElementContext(variableDeclaratorStartContext, endContext);

        AST.encodeVariableDeclaratorEnd(astBuffer);
    }

    @Override
    public void onLeafTypeReference(int leafContext, long name, ReferenceType referenceType) {
        
        writeLeafElementContextRef(leafContext);

        switch (referenceType) {
        case SCALAR:
            AST.encodeScalarTypeReference(astBuffer, name);
            break;
            
        default:
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onNonScopedTypeReferenceStart(int leafContext, long name, ReferenceType referenceType) {
        
        writeStartElementContextRef(leafContext);

        switch (referenceType) {
            
        case REFERENCE:
            AST.encodeIdentifierTypeReferenceStart(astBuffer, name);
            break;
            
        default:
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onNonScopedTypeReferenceEnd(int context, Context endContext) {

        writeEndElementContext(context, endContext);
        
        AST.encodeIdentifierTypeReferenceEnd(astBuffer);
    }

    @Override
    public void onScopedTypeReferenceStart(int scopedTypeReferenceStartContext, ReferenceType referenceType) {

        writeStartElementContextRef(scopedTypeReferenceStartContext);
        
        AST.encodeScopedTypeReferenceStart(astBuffer);
    }

    @Override
    public void onScopedTypeReferencePart(int leafContext, long part) {

        writeLeafElementContextRef(leafContext);
        
        AST.encodeScopedTypeReferencePart(astBuffer, part);
    }

    @Override
    public void onScopedTypeReferenceEnd(int scopedTypeReferenceStartContext, Context endContext) {

        writeEndElementContext(scopedTypeReferenceStartContext, endContext);
        
        AST.encodeScopedTypeReferenceEnd(astBuffer);
    }

    
    @Override
    public void onGenericTypeParametersStart(int startContext) {

        writeStartElementContextRef(startContext);

        AST.encodeGenericTypeParametersStart(astBuffer);
    }

    @Override
    public void onGenericTypeParameter(int context, long name) {

        writeLeafElementContextRef(context);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGenericTypeParametersEnd(int startContext, Context endContext) {

        writeEndElementContext(startContext, endContext);

        AST.encodeGenericTypeParametersEnd(astBuffer);
    }

    @Override
    public void onAssignmentStatementStart(int startContext) {

        writeStartElementContextRef(startContext);

        AST.encodeAssignmentStatementStart(astBuffer);
    }

    @Override
    public void onAssignmentStatementEnd(int startContext, Context endContext) {

        writeEndElementContext(startContext, endContext);

        AST.encodeAssignmentStatementEnd(astBuffer);
    }

    @Override
    public final void onExpressionStatementStart(int expressionStatementStartContext) {

        writeStartElementContextRef(expressionStatementStartContext);

        AST.encodeExpressionStatementStart(astBuffer);
    }

    @Override
    public final void onExpressionStatementEnd(int expressionStatementStartContext, Context endContext) {

        writeEndElementContext(expressionStatementStartContext, endContext);

        AST.encodeExpressionStatementEnd(astBuffer);
    }

    @Override
    public final void onForStatementStart(int forStatementStartContext, long keyword, int keywordContext) {
        
        verifyNotSameContext(forStatementStartContext, keywordContext);
        
        writeStartElementContextRef(forStatementStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForInitStart(int forInitStartContext) {

        writeStartElementContextRef(forInitStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForInitEnd(int forInitStartContext, Context endContext) {
        
        writeEndElementContext(forInitStartContext, endContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForUpdateStart(int forUpdateStartContext) {

        writeStartElementContextRef(forUpdateStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForUpdateEnd(int forUpdateStartContext, Context endContext) {

        writeEndElementContext(forUpdateStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onForStatementEnd(int forStatementStartContext, Context endContext) {
        
        writeEndElementContext(forStatementStartContext, endContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIteratorForStatementStart(int iteratorForStatementStartContext) {

        writeStartElementContextRef(iteratorForStatementStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public void onIteratorForTestStart(int iteratorForTestStartContext) {

        writeStartElementContextRef(iteratorForTestStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIteratorForTestEnd(int iteratorForTestStartContext, Context endContext) {

        writeEndElementContext(iteratorForTestStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIteratorForStatementEnd(int iteratorForStatementStartContext, Context endContext) {

        writeEndElementContext(iteratorForStatementStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onWhileStatementStart(int whileStatementStartContext, long whileKeyword, int whileKeywordContext) {
        
        writeStartElementContextRef(whileStatementStartContext);
        
        AST.encodeWhileStatementStart(astBuffer, whileKeyword, whileKeywordContext);
    }

    @Override
    public final void onWhileStatementEnd(int whieStatementStartContext, Context endContext) {

        writeEndElementContext(whieStatementStartContext, endContext);
        
        AST.encodeWhileStatementEnd(astBuffer);
    }

    @Override
    public final void onDoWhileStatementStart(int doWhileStatementStartContext) {

        writeStartElementContextRef(doWhileStatementStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDoWhileStatementEnd(int doWhileStatemetStartContext, Context endContext) {

        writeEndElementContext(doWhileStatemetStartContext, endContext);
        
        throw new UnsupportedOperationException();
   }

    @Override
    public final void onTryWithResourcesStatementStart(int tryWithResourcesStatementStartContext) {

        writeStartElementContextRef(tryWithResourcesStatementStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryWithResourcesSpecificationStart(int tryWithResourcesSpecificationStartContext) {
        
        writeStartElementContextRef(tryWithResourcesSpecificationStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onResourceStart(int resourceStartContext) {
        
        writeStartElementContextRef(resourceStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onVariableName(int leafContext, long name, int numDims) {
        
        writeLeafElementContextRef(leafContext);

        AST.encodeVariableName(astBuffer, name, numDims);
    }

    @Override
    public final void onResourceEnd(int resourceStartContext, Context endContext) {
        
        writeEndElementContext(resourceStartContext, endContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryWithResourcesSpecificationEnd(int tryWithResourcesSpecificationStartContext, Context endContext) {

        writeEndElementContext(tryWithResourcesSpecificationStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryStatementStart(int tryStatementStartContext) {

        writeStartElementContextRef(tryStatementStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryBlockEnd(int tryStartContext, Context endContext) {
        
        writeEndElementContext(tryStartContext, endContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCatchStart(int catchStartContext) {
        
        writeStartElementContextRef(catchStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onCatchEnd(int catchStartContext, Context endContext) {

        writeEndElementContext(catchStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFinallyStart(int finallyStartContext) {

        writeStartElementContextRef(finallyStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onFinallyEnd(int finallyEndContext, Context endContext) {

        writeEndElementContext(finallyEndContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryStatementEnd(int tryStatementStartContext, Context endContext) {

        writeEndElementContext(tryStatementStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onTryWithResourcesEnd(int tryWithResourcesStartContext, Context endContext) {

        writeEndElementContext(tryWithResourcesStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onReturnStatementStart(int returnStatementStartContext) {

        writeStartElementContextRef(returnStatementStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onReturnStatementEnd(int returnStatementStartContext, Context endContext) {

        writeEndElementContext(returnStatementStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onThrowStatementStart(int throwStatementStartContext) {
        
        writeStartElementContextRef(throwStatementStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onThrowStatementEnd(int throwStatementStartContext, Context endContext) {

        writeEndElementContext(throwStatementStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onAnnotationStart(int annotationStartContext, Names typeName) {
        
        writeStartElementContextRef(annotationStartContext);

        AST.encodeAnnotationStart(astBuffer, typeName);
    }

    @Override
    public final void onAnnotationElementStart(int startContext, long name, int nameContext) {

        writeStartElementContextRef(startContext);

        AST.encodeAnnotationElementStart(astBuffer, name, nameContext);
    }

    @Override
    public final void onAnnotationElementEnd(int startContext, Context endContext) {

        writeEndElementContext(startContext, endContext);

        AST.encodeAnnotationElementEnd(astBuffer);
    }

    @Override
    public final void onAnnotationEnd(int annotationStartContext, Context endContext) {

        writeEndElementContext(annotationStartContext, endContext);

        AST.encodeAnnotationEnd(astBuffer);
    }
}
