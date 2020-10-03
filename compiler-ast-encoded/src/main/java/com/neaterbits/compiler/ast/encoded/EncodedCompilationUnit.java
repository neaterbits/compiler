package com.neaterbits.compiler.ast.encoded;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.ast.encoded.ASTBufferRead.ParseTreeElementRef;
import com.neaterbits.compiler.parser.listener.common.ContextAccess;
import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.ImmutableContext;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.util.buffers.MapStringStorageBuffer;

public final class EncodedCompilationUnit {

    private final ASTBufferRead astBuffer;
    private final IntKeyIntValueHash parseTreeRefToStartContextHash;
    private final IntKeyIntValueHash parseTreeRefToEndContextHash;
    private final ASTBufferRead contexts;
    private final FullContextProvider fullContextProvider;

    private final TypeName [] indexToTypeName;

    private final MapStringStorageBuffer stringBuffer;

    private final boolean passContextToDecode;

    public EncodedCompilationUnit(
            ASTBufferRead astBuffer,
            ASTBufferRead contextBuffer,
            FullContextProvider fullContextProvider,
            IntKeyIntValueHash parseTreeRefToStartContextHash,
            IntKeyIntValueHash parseTreeRefToEndContextHash,
            Map<TypeName, Integer> typeNameToIndex,
            MapStringStorageBuffer stringBuffer,
            boolean passContextToDecode) {

        Objects.requireNonNull(astBuffer);
        Objects.requireNonNull(parseTreeRefToStartContextHash);
        Objects.requireNonNull(contextBuffer);
        Objects.requireNonNull(fullContextProvider);

        this.astBuffer = astBuffer;
        this.parseTreeRefToStartContextHash = parseTreeRefToStartContextHash;
        this.parseTreeRefToEndContextHash = parseTreeRefToEndContextHash;
        this.contexts = contextBuffer;
        this.fullContextProvider = fullContextProvider;
        this.stringBuffer = stringBuffer;
        this.passContextToDecode = passContextToDecode;

        this.indexToTypeName = new TypeName[typeNameToIndex.size()];

        for (Map.Entry<TypeName, Integer> entry : typeNameToIndex.entrySet()) {

            final int index = entry.getValue();

            if (indexToTypeName[index] != null) {
                throw new IllegalStateException();
            }

            indexToTypeName[index] = entry.getKey();
        }
    }

    public ASTBufferRead getBuffer() {
        return astBuffer;
    }

    public ParseTreeElement getParseTreeElement(int parseTreeRef) {
        return astBuffer.getParseTreeElement(parseTreeRef);
    }

    public String getStringFromASTBufferOffset(int astBufferOffset) {

        final int stringRef = astBuffer.getInt(astBufferOffset);

        return stringBuffer.getString(stringRef);
    }

    public String getStringFromRef(int stringRef) {

        return stringBuffer.getString(stringRef);
    }

    public TypeName getTypeName(int astBufferOffset) {

        final int typeIndex = astBuffer.getInt(astBufferOffset);

        return indexToTypeName[typeIndex];
    }

    public Context getContextByStartElementRef(int parseTreeRef) {

        final int index = parseTreeRefToStartContextHash.get(parseTreeRef);

        return getContextFromIndex(index);
    }

    public Context getContextByEndElementRef(int parseTreeRef) {

        final int index = parseTreeRefToEndContextHash.get(parseTreeRef);

        return getContextFromIndex(index);
    }

    public Context getContextFromIndex(int index) {

        final Context context;

        if (index >= 0) {
            context = new ImmutableContext(
                    contexts.getInt(index + 0),
                    contexts.getInt(index + 4));
        }
        else {
            context = null;
        }

        return context;
    }

    public FullContextProvider getFullContextProvider() {
        return fullContextProvider;
    }

    public String getTokenString(int parseTreeRef) {

        final int index = parseTreeRefToStartContextHash.get(parseTreeRef);

        return getTokenStringFromIndex(index);
    }

    private String getTokenStringFromIndex(int index) {

        return fullContextProvider.getText(getContextFromIndex(index));
    }

    public int getTokenOffset(int parseTreeRef) {

        final int index = parseTreeRefToStartContextHash.get(parseTreeRef);

        final Context context = getContextFromIndex(index);

        return context.getStartOffset();
    }

    public int getTokenLength(int parseTreeRef) {

        final int index = parseTreeRefToStartContextHash.get(parseTreeRef);

        final Context context = getContextFromIndex(index);

        return context.getLength();
    }

    private int getStartContext(int parseTreeRef) {
        return parseTreeRefToStartContextHash.get(parseTreeRef);
    }

    private Context getEndContext(int startContext) {

        return getContextFromIndex(startContext);
    }

    private int getLeafContext(int parseTreeRef) {
        return parseTreeRefToStartContextHash.get(parseTreeRef);
    }

    public ContextAccess getContextAccess() {
        return new ContextAccess() {

            @Override
            public Context getContext(int index) {
                return getContextFromIndex(index);
            }

            @Override
            public int writeContext(int otherContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int writeContext(Context context) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public <COMP_UNIT> COMP_UNIT iterate(IterativeParseTreeListener<COMP_UNIT> listener) {

        int parseTreeRef = 0;

        final ParseTreeElementRef ref = new ParseTreeElementRef();

        final ContextGetter contextGetter;

        if (passContextToDecode) {
            contextGetter = new ContextGetter() {

                @Override
                public Context getElementContext(int parseTreeRef) {
                    return EncodedCompilationUnit.this.getContextByStartElementRef(parseTreeRef);
                }

                @Override
                public Context getContextFromRef(int ref) {

                    return EncodedCompilationUnit.this.getContextFromIndex(ref);
                }
            };
        }
        else {
            contextGetter = null;
        }

        COMP_UNIT compUnit = null;

        do {
            astBuffer.getParseTreeElement(parseTreeRef, ref);

            switch (ref.element) {

            case COMPILATION_UNIT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    listener.onCompilationUnitStart(startContext);
                }
                else {
                    compUnit = listener.onCompilationUnitEnd(startContext, getEndContext(startContext));
                }
                break;
            }

            case NAMESPACE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeNamespaceStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeNamespaceEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case NAMESPACE_PART: {
                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeNamespacePart(astBuffer, leafContext, ref.index, listener);
                }
                break;
            }

            case IMPORT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeImportStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeImportEnd(astBuffer, startContext, getEndContext(startContext), ref.index, listener);
                }
                break;
            }

            case IMPORT_NAME_PART: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeImportNamePart(
                             astBuffer,
                            startContext,
                            ref.index,
                            listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
            }

            case TYPE_DEFINITION: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeTypeDefinitionStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeTypeDefinitionEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CLASS_DEFINITION: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeClassEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CLASS_MODIFIER_HOLDER: {
                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassModifierHolder(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
            }

            case CLASS_EXTENDS: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassExtendsStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeClassExtendsEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case IMPLEMENTS: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeImplementsStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeImplementsEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case IMPLEMENTS_TYPE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeImplementsTypeStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeImplementsTypeEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case IMPLEMENTS_NAME_PART: {
                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeImplementsNamePart(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
            }

            case CLASS_DATA_FIELD_MEMBER: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeFieldDeclarationStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeFieldDeclarationEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case SCALAR_TYPE_REFERENCE: {
                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeScalarTypeReference(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
            }

            case VARIABLE_DECLARATION_ELEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeVariableDeclaratorStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeVariableDeclaratorEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case VAR_NAME_DECLARATION: {
                final int leafContext = getLeafContext(parseTreeRef);

                AST.decodeVariableName(astBuffer, leafContext, ref.index, listener);
                break;
            }

            case UNRESOLVED_IDENTIFIER_TYPE_REFERENCE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeIdentifierTypeReferenceStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeIdentifierTypeReferenceEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CONSTRUCTOR_MEMBER: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeConstructorStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeConstructorEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CONSTRUCTOR_NAME: {

                final int leafContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeConstructorName(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
            }

            case CLASS_METHOD_MEMBER: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassMethodStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeClassMethodEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case METHOD_RETURN_TYPE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeMethodReturnTypeStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeMethodReturnTypeEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CLASS_METHOD_MODIFIER_HOLDER:
                if (ref.isStart) {
                    final int leafContext = getStartContext(parseTreeRef);

                    AST.decodeClassMethodModifierHolder(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;

            case METHOD_NAME: {
                final int leafContext = getLeafContext(parseTreeRef);

                AST.decodeMethodName(astBuffer, leafContext, ref.index, listener);
                break;
            }

            case SIGNATURE_PARAMETERS: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeSignatureParametersStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeSignatureParametersEnd(astBuffer, startContext, getEndContext(startContext), ref.index, listener);
                }
                break;
            }

            case SIGNATURE_PARAMETER: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeSignatureParameterStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeSignatureParameterEnd(astBuffer, startContext, getEndContext(startContext), ref.index, listener);
                }
                break;
            }

            case SIGNATURE_PARAMETER_VARARGS: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeSignatureParameterVarargs(astBuffer, startContext, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case THROWS: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeThrowsStart(startContext, listener);
                }
                else {
                    AST.decodeThrowsEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ENUM_DEFINITION: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeEnumStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeEnumEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ENUM_CONSTANT_DEFINITION: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeEnumConstantStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeEnumConstantEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case UNRESOLVED_SCOPED_TYPE_REFERENCE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeScopedTypeReferenceStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeScopedTypeReferenceEnd(astBuffer, startContext, getEndContext(startContext), ref.index, listener);
                }
                break;
            }

            case UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeScopedTypeReferenceNameStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeScopedTypeReferenceNameEnd(astBuffer, startContext, getEndContext(startContext), ref.index, listener);
                }
                break;
            }

            case UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME_PART: {

                final int leafContext = getLeafContext(parseTreeRef);

                AST.decodeScopedTypeReferencePart(astBuffer, leafContext, ref.index, listener);
                break;
            }

            case VARIABLE_DECLARATION_STATEMENT: {

                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeVariableDeclarationStatementStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeVariableDeclarationStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case IF_ELSE_IF_ELSE_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeIfElseIfElseStatementStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeIfElseIfElseStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case IF_CONDITION_BLOCK: {

                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeIfConditionBlockStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeIfConditionBlockEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ELSE_IF_CONDITION_BLOCK: {

                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeElseIfConditionBlockStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeElseIfConditionBlockEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ELSE_BLOCK: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeElseBlockStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeElseBlockEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case SIMPLE_VARIABLE_REFERENCE: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeVariableReference(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case EXPRESSION_BINARY_OPERATOR: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeExpressionBinaryOperator(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case EXPRESSION_LIST: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeExpressionListStart(startContext, listener);
                }
                else {
                    AST.decodeExpressionListEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case INTEGER_LITERAL: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeIntegerLiteral(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case STRING_LITERAL: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeStringLiteral(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case CHARACTER_LITERAL: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeCharacterLiteral(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case BOOLEAN_LITERAL: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeBooleanLiteral(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case WHILE_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeWhileStatementStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeWhileStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case FOR_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeForStatementStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeForStatementEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case FOR_INIT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeForInitStart(startContext, listener);
                }
                else {
                    AST.decodeForInitEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case FOR_EXPRESSION_LIST: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeForExpressionStart(startContext, listener);
                }
                else {
                    AST.decodeForExpressionEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case FOR_UPDATE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeForUpdateStart(startContext, listener);
                }
                else {
                    AST.decodeForUpdateEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ITERATOR_FOR_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeIteratorForStatementStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeIteratorForStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ITERATOR_FOR_TEST: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeIteratorForTestStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeIteratorForTestEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case TRY_CATCH_FINALLY_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeTryCatchFinallyStatementStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeTryCatchFinallyStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case TRY_BLOCK_END: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeTryBlockEnd(leafContext, getEndContext(leafContext), listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case CATCH: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeCatchStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeCatchEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case FINALLY: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeFinallyStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeFinallyEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case TRY_WITH_RESOURCES_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeTryWithResourcesStatementStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeTryWithResourcesStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case RESOURCES_LIST: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeTryWithResourcesSpecificationStart(startContext, listener);
                }
                else {
                    AST.decodeTryWithResourcesSpecificationEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case RESOURCE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeTryResourceStart(startContext, listener);
                }
                else {
                    AST.decodeTryResourceEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case RETURN_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeReturnStatementStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeReturnStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case THROW_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeThrowStatementStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeThrowStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ASSIGNMENT_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeAssignmentStatementStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeAssignmentStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ASSIGNMENT_EXPRESSION: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeAssignmentExpressionStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeAssignmentExpressionEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ASSIGNMENT_EXPRESSION_LHS: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeAssignmentLHSStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeAssignmentLHSEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case EXPRESSION_STATEMENT: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeExpressionStatementStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeExpressionStatementEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CLASS_INSTANCE_CREATION_EXPRESSION: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassInstanceCreationExpressionStart(startContext, listener);
                }
                else {
                    AST.decodeClassInstanceCreationExpressionEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CLASS_INSTANCE_CREATION_EXPRESSION_NAME: {
                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassInstanceCreationTypeAndConstructorName(
                            astBuffer,
                            leafContext,
                            ref.index,
                            listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
            }

            case METHOD_INVOCATION_EXPRESSION: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeMethodInvocationStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeMethodInvocationEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case PARAMETER_LIST:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeParametersStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeParametersEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case PARAMETER:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeParameterStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeParameterEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case NESTED_EXPRESSION:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeNestedExpressionStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeNestedExpressionEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case UNARY_EXPRESSION: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeUnaryExpressionStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeUnaryExpressionEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }


            case PRIMARY_LIST: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodePrimariesStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodePrimariesEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case NAME_PRIMARY: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeNamePrimary(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case FIELD_ACCESS: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeFieldAccess(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case ANNOTATION:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeAnnotationStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeAnnotationEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case ANNOTATION_ELEMENT:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeAnnotationElementStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeAnnotationElementEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case NAME_REFERENCE: {

                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeNameReference(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;
            }

            case TYPE_ARGUMENT_LIST:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeTypeArgumentListStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeTypeArgumentListEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case REFERENCE_GENERIC_TYPE_ARGUMENT:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeReferenceTypeArgumentStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeReferenceTypeArgumentEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case WILDCARD_GENERIC_TYPE_ARGUMENT:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeWildcardTypeArgumentStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeWildcardTypeArgumentEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case TYPE_BOUND:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeTypeBoundStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeTypeBoundEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case GENERIC_TYPE_PARAMETERS:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeGenericTypeParametersStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeGenericTypeParametersEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case NAMED_GENERIC_TYPE_PARAMETER:  {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeNamedGenericTypeParameterStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeNamedGenericTypeParameterEnd(startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case FIELD_MODIFIER_HOLDER:
                if (ref.isStart) {
                    final int leafContext = getStartContext(parseTreeRef);

                    AST.decodeFieldModifierHolder(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;

            case VARIABLE_MODIFIER_HOLDER:
                if (ref.isStart) {
                    final int leafContext = getStartContext(parseTreeRef);

                    AST.decodeVariableModifierHolder(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new IllegalStateException();
                }
                break;

            default:
                throw new UnsupportedOperationException("element " + ref.element);
            }

            parseTreeRef += ref.isStart
                    ? AST.sizeStart(ref.element, astBuffer, ref.index)
                    : AST.sizeEnd(ref.element);
        }
        while (compUnit == null);

        return compUnit;
    }
}
