package com.neaterbits.compiler.ast.encoded;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.ImmutableContext;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.compiler.parser.listener.common.ContextAccess;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.listener.encoded.AST;
import com.neaterbits.compiler.parser.listener.encoded.ASTBufferRead;
import com.neaterbits.compiler.parser.listener.encoded.ContextGetter;
import com.neaterbits.compiler.parser.listener.encoded.ASTBufferRead.ParseTreeElementRef;

public final class EncodedCompilationUnit {

    private final String file;
    private final ASTBufferRead astBuffer;
    private final IntKeyIntValueHash parseTreeRefToStartContextHash;
    private final IntKeyIntValueHash parseTreeRefToEndContextHash;
    private final ASTBufferRead contexts;

    private final TypeName [] indexToTypeName;
    
    private final MapStringStorageBuffer stringBuffer;
    
    private final boolean passContextToDecode;

    public EncodedCompilationUnit(
            String file,
            ASTBufferRead astBuffer,
            ASTBufferRead contextBuffer,
            IntKeyIntValueHash parseTreeRefToStartContextHash,
            IntKeyIntValueHash parseTreeRefToEndContextHash,
            Map<TypeName, Integer> typeNameToIndex,
            MapStringStorageBuffer stringBuffer,
            boolean passContextToDecode) {

        Objects.requireNonNull(file);
        Objects.requireNonNull(astBuffer);
        Objects.requireNonNull(parseTreeRefToStartContextHash);
        Objects.requireNonNull(contextBuffer);

        this.file = file;
        this.astBuffer = astBuffer;
        this.parseTreeRefToStartContextHash = parseTreeRefToStartContextHash;
        this.parseTreeRefToEndContextHash = parseTreeRefToEndContextHash;
        this.contexts = contextBuffer;
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
    
    private static final int INDEX_START_LINE       = 0;
    private static final int INDEX_START_POS_IN_LINE = 4;
    private static final int INDEX_START_OFFSET     = 8;
    private static final int INDEX_END_LINE         = 12;
    private static final int INDEX_END_POS_IN_LINE  = 16;
    private static final int INDEX_END_OFFSET       = 20;
    private static final int INDEX_TEXT             = 24;
    
    public Context getContextByStartElementRef(int parseTreeRef) {
        
        final int index = parseTreeRefToStartContextHash.get(parseTreeRef);
     
        return getContextFromIndex(index);
    }

    public Context getContextByEndElementRef(int parseTreeRef) {
        
        final int index = parseTreeRefToEndContextHash.get(parseTreeRef);
     
        return getContextFromIndex(index);
    }

    public Context getContextFromIndex(int index) {
        final ImmutableContext context;
        
        if (index >= 0) {
            context = new ImmutableContext(
                file,
                contexts.getInt(index + INDEX_START_LINE),
                contexts.getInt(index + INDEX_START_POS_IN_LINE),
                contexts.getInt(index + INDEX_START_OFFSET),
                contexts.getInt(index + INDEX_END_LINE),
                contexts.getInt(index + INDEX_END_POS_IN_LINE),
                contexts.getInt(index + INDEX_END_OFFSET),
                null);
        }
        else {
            context = null;
        }
        
        return context;
    }
    
    public String getTokenString(int parseTreeRef) {
        
        final int index = parseTreeRefToStartContextHash.get(parseTreeRef);
        
        return getTokenStringFromIndex(index);
    }
    
    private String getTokenStringFromIndex(int index) {
        
        final int stringRef = contexts.getInt(index + INDEX_TEXT);
        
        return stringRef != StringRef.STRING_NONE
                ? stringBuffer.getString(stringRef)
                : null;
    }
    
    public int getTokenOffset(int parseTreeRef) {
        
        final int index = parseTreeRefToStartContextHash.get(parseTreeRef);

        return contexts.getInt(index + INDEX_START_OFFSET);
    }
    
    public int getTokenLength(int parseTreeRef) {

        final int index = parseTreeRefToStartContextHash.get(parseTreeRef);

        return    contexts.getInt(index + INDEX_END_OFFSET)
                - contexts.getInt(index + INDEX_START_OFFSET)
                + 1;
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

    public <COMP_UNIT> COMP_UNIT iterate(IterativeParserListener<COMP_UNIT> listener) {
     
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
                
            case CLASS_EXTENDS_NAME_PART: {
                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassExtendsNamePart(astBuffer, leafContext, ref.index, listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
            }
                
            case CLASS_IMPLEMENTS: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassImplementsStart(astBuffer, startContext, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeClassImplementsEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CLASS_IMPLEMENTS_TYPE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassImplementsTypeStart(astBuffer, startContext, listener);
                }
                else {
                    AST.decodeClassImplementsTypeEnd(astBuffer, startContext, getEndContext(startContext), listener);
                }
                break;
            }

            case CLASS_IMPLEMENTS_NAME_PART: {
                final int leafContext = getLeafContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeClassImplementsNamePart(astBuffer, leafContext, ref.index, listener);
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

            case RESOLVE_LATER_IDENTIFIER_TYPE_REFERENCE: {
                final int leafContext = getLeafContext(parseTreeRef);

                AST.decodeIdentifierTypeReference(astBuffer, leafContext, ref.index, listener);
                break;
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
                
            case RESOLVE_LATER_SCOPED_TYPE_REFERENCE: {
                final int startContext = getStartContext(parseTreeRef);

                if (ref.isStart) {
                    AST.decodeScopedTypeReferenceStart(astBuffer, startContext, ref.index, listener);
                }
                else {
                    AST.decodeScopedTypeReferenceEnd(astBuffer, startContext, getEndContext(startContext), ref.index, listener);
                }
                break;
            }
                
            case RESOLVE_LATER_SCOPED_TYPE_REFERENCE_PART: {
             
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
