package com.neaterbits.compiler.ast.encoded;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.ImmutableContext;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.util.buffers.MapStringStorageBuffer;import com.neaterbits.compiler.parser.listener.common.ParserListener;
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
    private static final int INDEX_TEXT           = 24;
    
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
                getTokenStringFromIndex(index));
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
        
        return stringBuffer.getString(stringRef);
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
    
    public <COMP_UNIT> COMP_UNIT iterate(ParserListener<COMP_UNIT> listener) {
     
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
            
            final Context context;
            
            if (passContextToDecode) {
                if (ref.isStart) {
                    context = getContextByStartElementRef(parseTreeRef);
                }
                else {
                    context = getContextByEndElementRef(parseTreeRef);
                }
            }
            else {
                context = null;
            }
            
            switch (ref.element) {
            
            case COMPILATION_UNIT:
                if (ref.isStart) {
                    listener.onCompilationUnitStart(context);
                }
                else {
                    compUnit = listener.onCompilationUnitEnd(context);
                }
                break;
            
            case NAMESPACE:
                if (ref.isStart) {
                    AST.decodeNamespaceStart(astBuffer, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeNamespaceEnd(context, listener);
                }
                break;
                
                
            case NAMESPACE_PART:
                if (ref.isStart) {
                    AST.decodeNamespacePart(astBuffer, context, ref.index, listener);
                }
                break;
                
            case IMPORT:
                if (ref.isStart) {
                    AST.decodeImportStart(astBuffer, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeImportEnd(astBuffer, context, ref.index, listener);
                }
                break;
                
            case IMPORT_NAME_PART:
                if (ref.isStart) {
                    AST.decodeImportNamePart(
                             astBuffer,
                            context,
                            ref.index,
                            listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
                
            case CLASS_DEFINITION:
                if (ref.isStart) {
                    AST.decodeClassStart(astBuffer, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeClassEnd(astBuffer, context, listener);
                }
                break;
                
            case CLASS_MODIFIER_HOLDER:
                if (ref.isStart) {
                    AST.decodeClassModifierHolder(astBuffer, context, ref.index, listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
            
            case CLASS_EXTENDS:
                if (ref.isStart) {
                    AST.decodeClassExtendsStart(astBuffer, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeClassExtendsEnd(astBuffer, context, listener);
                }
                break;
                
            case CLASS_EXTENDS_NAME_PART:
                if (ref.isStart) {
                    AST.decodeClassExtendsNamePart(astBuffer, context, ref.index, listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
                
            case CLASS_IMPLEMENTS:
                if (ref.isStart) {
                    AST.decodeClassImplementsStart(astBuffer, contextGetter, ref.index, listener);
                }
                else {
                    AST.decodeClassImplementsEnd(astBuffer, context, listener);
                }
                break;

            case CLASS_IMPLEMENTS_TYPE:
                if (ref.isStart) {
                    AST.decodeClassImplementsTypeStart(astBuffer, context, listener);
                }
                else {
                    AST.decodeClassImplementsTypeEnd(astBuffer, context, listener);
                }
                break;

            case CLASS_IMPLEMENTS_NAME_PART:
                if (ref.isStart) {
                    AST.decodeClassImplementsNamePart(astBuffer, context, ref.index, listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;

            case CLASS_DATA_FIELD_MEMBER:
                if (ref.isStart) {
                    AST.decodeFieldDeclarationStart(astBuffer, context, listener);
                }
                else {
                    AST.decodeFieldDeclarationEnd(astBuffer, context, listener);
                }
                break;
                
            case SCALAR_TYPE_REFERENCE:
                if (ref.isStart) {
                    AST.decodeScalarTypeReference(astBuffer, context, ref.index, listener);
                }
                else {
                    throw new UnsupportedOperationException();
                }
                break;
                
            case VARIABLE_DECLARATION_ELEMENT:
                if (ref.isStart) {
                    AST.decodeVariableDeclaratorStart(astBuffer, context, listener);
                }
                else {
                    AST.decodeVariableDeclaratorEnd(astBuffer, context, listener);
                }
                break;
          
            case VAR_NAME_DECLARATION:
                AST.decodeVariableName(astBuffer, context, ref.index, listener);
                break;

            case RESOLVE_LATER_IDENTIFIER_TYPE_REFERENCE:
                AST.decodeIdentifierTypeReference(astBuffer, context, ref.index, listener);
                break;
                
            case CLASS_METHOD_MEMBER:
                if (ref.isStart) {
                    AST.decodeClassMethodStart(astBuffer, context, listener);
                }
                else {
                    AST.decodeClassMethodEnd(astBuffer, context, listener);
                }
                break;
                
            case METHOD_RETURN_TYPE:
                if (ref.isStart) {
                    AST.decodeMethodReturnTypeStart(astBuffer, context, listener);
                }
                else {
                    AST.decodeMethodReturnTypeEnd(astBuffer, context, listener);
                }
                break;
                
            case METHOD_NAME:
                AST.decodeMethodName(astBuffer, context, ref.index, listener);
                break;

            case SIGNATURE_PARAMETER:
                if (ref.isStart) {
                    AST.decodeSignatureParameterStart(astBuffer, context, ref.index, listener);
                }
                else {
                    AST.decodeSignatureParameterEnd(astBuffer, context, ref.index, listener);
                }
                break;
                
            default:
                throw new UnsupportedOperationException("element " + ref.element);
            }
            
            parseTreeRef += ref.isStart
                    ? AST.sizeStart(ref.element)
                    : AST.sizeEnd(ref.element);
        }
        while (compUnit == null);

        return compUnit;
    }
}
