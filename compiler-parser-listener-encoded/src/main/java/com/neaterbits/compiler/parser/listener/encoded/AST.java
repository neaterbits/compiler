package com.neaterbits.compiler.parser.listener.encoded;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.util.io.strings.StringRef;

public class AST {
    
    private static final int STRING_REF_SIZE = 4;
    private static final int CONTEXT_REF_SIZE = 4;

    static final int NO_STRINGREF = -1;
    static final int NO_CONTEXTREF = -1;
    
    static int writeContext(ASTBuffer contextBuffer, Context context) {
        
        Objects.requireNonNull(context);
        
        final int writePos = contextBuffer.getWritePos();
        
        contextBuffer.writeInt(context.getStartLine());
        
        contextBuffer.writeInt(context.getStartPosInLine());
        
        contextBuffer.writeInt(context.getStartOffset());
        
        contextBuffer.writeInt(context.getEndLine());
        
        contextBuffer.writeInt(context.getEndPosInLine());
        
        contextBuffer.writeInt(context.getEndOffset());
        
        contextBuffer.writeInt(context.getLength());
        
        return writePos;
    }

    public static int sizeStart(ParseTreeElement element) {
        
        final int size;
        
        switch (element) {

        case IMPORT:
            size = IMPORT_START_SIZE;
            break;
            
        case IMPORT_NAME_PART:
            size = IMPORT_PART_SIZE;
            break;
            
        case NAMESPACE:
            size = NAMESPACE_START_SIZE;
            break;
            
        case NAMESPACE_PART:
            size = NAMESPACE_PART_SIZE;
            break;
        
        default:
            size = 0; // ParseTreeElement
            break;
        }
        
        return size + 1;
    }

    public static int sizeEnd(ParseTreeElement element) {
        
        final int size;
        
        switch (element) {

        case IMPORT:
            size = IMPORT_END_SIZE;
            break;
            
        default:
            size = 0; // ParseTreeElement
            break;
        }
        
        return size + 1;
    }
    

    private static final int IMPORT_START_SIZE = 2 * (STRING_REF_SIZE + CONTEXT_REF_SIZE);
    private static final int IMPORT_END_SIZE = 1;
    private static final int IMPORT_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeImportStart(
            StringASTBuffer astBuffer,
            long importKeyword, int importKeywordContext,
            long staticKeyword, int staticKeywordContext) {
        
        astBuffer.writeElementStart(ParseTreeElement.IMPORT);
        
        astBuffer.writeStringRef(importKeyword);
        astBuffer.writeContextRef(importKeywordContext);
        
        if (staticKeyword != StringRef.STRING_NONE) {
            astBuffer.writeStringRef(staticKeyword);
            astBuffer.writeContextRef(staticKeywordContext);
        }
        else {
            astBuffer.writeNoStringRef();
            astBuffer.writeNoContextRef();
        }
    }

    public static <COMPILATION_UNIT> void decodeImportStart(
            ASTBufferRead astBuffer,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final Context elementContext;
        final Context importKeywordContext;
        final Context staticKeywordContext;
        
        if (contextGetter != null) {
            elementContext = contextGetter.getElementContext(index);
            
            importKeywordContext = astBuffer.hasContextRef(index + 1 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 1 + 4))
                    : null;

            staticKeywordContext = astBuffer.hasContextRef(index + 1 + 4 + 4 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 1 + 4 + 4 + 4))
                    : null;
        }
        else {
            elementContext = null;
            importKeywordContext = null;
            staticKeywordContext = null;
        }
        
        listener.onImportStart(
                elementContext,
                astBuffer.hasStringRef(index + 1) ? astBuffer.getInt(index + 1) : StringRef.STRING_NONE,
                importKeywordContext,
                astBuffer.hasStringRef(index + 1 + 4 + 4) ? astBuffer.getInt(index + 1 + 4 + 4) : StringRef.STRING_NONE,
                staticKeywordContext);
    }

    static void encodeImportNamePart(StringASTBuffer astBuffer, long identifier) {
        
        astBuffer.writeLeafElement(ParseTreeElement.IMPORT_NAME_PART);
        
        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeImportNamePart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onImportIdentifier(
                context,
                astBuffer.getStringRef(index + 1));
    }

    static void encodeImportEnd(StringASTBuffer astBuffer, boolean onDemand) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPORT);

        astBuffer.writeBoolean(onDemand);
    }
    
    public static <COMPILATION_UNIT> void decodeImportEnd(ASTBufferRead astBuffer, Context context, int index, ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onImportEnd(context, astBuffer.getBoolean(index + 1));

    }

    static void encodeNamespaceStart(StringASTBuffer astBuffer, long namespaceKeyword, int namespaceKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.NAMESPACE);
        
        astBuffer.writeStringRef(namespaceKeyword);
        astBuffer.writeContextRef(namespaceKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeNamespaceStart(ASTBufferRead astBuffer, ContextGetter contextGetter, int index, ParserListener<COMPILATION_UNIT> listener) {
        
        final Context elementContext;
        final Context namespaceKeywordContext;
        
        if (contextGetter != null) {
            elementContext = contextGetter.getElementContext(index);
            
            namespaceKeywordContext = astBuffer.hasContextRef(index + 1 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 1 + 4))
                    : null;
        }
        else {
            elementContext = null;
            namespaceKeywordContext = null;
        }
        
        listener.onNamespaceStart(
                elementContext,
                astBuffer.hasStringRef(index + 1) ? astBuffer.getInt(index + 1) : StringRef.STRING_NONE,
                namespaceKeywordContext);
    }

    private static final int NAMESPACE_START_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    private static final int NAMESPACE_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeNamespacePart(StringASTBuffer astBuffer, long part) {
        
        astBuffer.writeElementStart(ParseTreeElement.NAMESPACE_PART);
        
        astBuffer.writeStringRef(part);
    }

    public static <COMPILATION_UNIT> void decodeNamespacePart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onNamespacePart(
                context,
                astBuffer.getStringRef(index + 1));
    }

    static void encodeNamespaceEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.NAMESPACE);
    }

    public static <COMPILATION_UNIT> void decodeNamespaceEnd(Context context, ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onNameSpaceEnd(context);
    }

    static void encodeClassStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_DEFINITION);
    }
}
