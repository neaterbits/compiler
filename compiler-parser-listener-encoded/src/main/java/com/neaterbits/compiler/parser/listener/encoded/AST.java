package com.neaterbits.compiler.parser.listener.encoded;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.ClassModifier;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
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

        case CLASS_DEFINITION:
            size = CLASS_START_SIZE;
            break;

        case CLASS_MODIFIER_HOLDER:
            size = CLASS_MODIFIER_SIZE;
            break;

        case CLASS_EXTENDS:
            size = CLASS_EXTENDS_SIZE;
            break;

        case CLASS_EXTENDS_NAME_PART:
            size = CLASS_EXTENDS_NAME_PART_SIZE;
            break;

        case CLASS_IMPLEMENTS:
            size = CLASS_IMPLEMENTS_SIZE;
            break;

        case CLASS_IMPLEMENTS_NAME_PART:
            size = CLASS_IMPLEMENTS_NAME_PART_SIZE;
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

    private static final int CLASS_START_SIZE = 2 * (STRING_REF_SIZE + CONTEXT_REF_SIZE);

    static void encodeClassStart(StringASTBuffer astBuffer, long classKeyword, int classKeywordContext, long name, int nameContext) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_DEFINITION);
        
        astBuffer.writeStringRef(classKeyword);
        astBuffer.writeContextRef(classKeywordContext);
        
        astBuffer.writeStringRef(name);
        astBuffer.writeContextRef(nameContext);
    }

    public static <COMPILATION_UNIT> void decodeClassStart(
            ASTBufferRead astBuffer,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final Context elementContext;
        final Context classKeywordContext;
        final Context nameContext;
        
        if (contextGetter != null) {
            
            elementContext = contextGetter.getElementContext(index);
            
            classKeywordContext = astBuffer.hasContextRef(index + 1 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 1 + 4))
                    : null;
                    
            nameContext = astBuffer.hasContextRef(index + 1 + 4 + 4 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 1 + 4 + 4 + 4))
                    : null;
        }
        else {
            elementContext = null;
            classKeywordContext = null;
            nameContext = null;
        }

        listener.onClassStart(
                elementContext,
                astBuffer.getStringRef(index + 1),
                classKeywordContext,
                astBuffer.getStringRef(index + 1 + 4 + 4),
                nameContext);
    }

    static void encodeClassEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.CLASS_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeClassEnd(ASTBufferRead astBuffer, Context context, ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassEnd(context);
    }
    
    private static final int CLASS_MODIFIER_SIZE = 1 + 1;

    static void encodeVisibilityClassModifier(StringASTBuffer astBuffer, ClassVisibility classVisibility) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassModifier.Type.VISIBILITY);
        astBuffer.writeEnumByte(classVisibility);
    }

    static void encodeSubclassingModifier(StringASTBuffer astBuffer, Subclassing subclassing) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassModifier.Type.SUBCLASSING);
        astBuffer.writeEnumByte(subclassing);
    }

    public static <COMPILATION_UNIT> void decodeClassModifierHolder(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final ClassModifier.Type type = astBuffer.getEnumByte(index + 1, ClassModifier.Type.class);
        
        switch (type) {
        case VISIBILITY:
            listener.onVisibilityClassModifier(context, astBuffer.getEnumByte(index + 2, ClassVisibility.class));
            break;
            
        case SUBCLASSING:
            listener.onSubclassingModifier(context, astBuffer.getEnumByte(index + 2, Subclassing.class));
            break;
            
         default:
             throw new UnsupportedOperationException();
        }
    }
    
    private static final int CLASS_EXTENDS_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    static void encodeClassExtendsStart(StringASTBuffer astBuffer, long extendsKeyword, int extendsKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_EXTENDS);
        astBuffer.writeStringRef(extendsKeyword);
        astBuffer.writeContextRef(extendsKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeClassExtendsStart(
            ASTBufferRead astBuffer,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final Context elementContext;
        final Context extendsKeywordContext;
        
        if (contextGetter != null) {
            
            elementContext = contextGetter.getElementContext(index);
            
            extendsKeywordContext = astBuffer.hasContextRef(index + 1 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 1 + 4))
                    : null;
        }
        else {
            elementContext = null;
            extendsKeywordContext = null;
        }

        listener.onClassExtendsStart(
                elementContext,
                astBuffer.getStringRef(index + 1),
                extendsKeywordContext);
    }
    
    private static final int CLASS_EXTENDS_NAME_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeClassExtendsNamePart(StringASTBuffer astBuffer, long identifier) {
        
        astBuffer.writeElementStart(ParseTreeElement.CLASS_EXTENDS_NAME_PART);
        
        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeClassExtendsNamePart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassExtendsNamePart(
                context,
                astBuffer.getStringRef(index + 1));
    }

    static void encodeClassExtendsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_EXTENDS);
    }
    
    public static <COMPILATION_UNIT> void decodeClassExtendsEnd(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassExtendsEnd(context);
    }

    private static final int CLASS_IMPLEMENTS_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    static void encodeClassImplementsStart(StringASTBuffer astBuffer, long implementsKeyword, int implementsKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_IMPLEMENTS);
        astBuffer.writeStringRef(implementsKeyword);
        astBuffer.writeContextRef(implementsKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeClassImplementsStart(
            ASTBufferRead astBuffer,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final Context elementContext;
        final Context implementsKeywordContext;
        
        if (contextGetter != null) {
            
            elementContext = contextGetter.getElementContext(index);
            
            implementsKeywordContext = astBuffer.hasContextRef(index + 1 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 1 + 4))
                    : null;
        }
        else {
            elementContext = null;
            implementsKeywordContext = null;
        }

        listener.onClassImplementsStart(
                elementContext,
                astBuffer.getStringRef(index + 1),
                implementsKeywordContext);
    }

    static void encodeClassImplementsTypeStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_IMPLEMENTS_TYPE);
    
    }

    static void encodeClassImplementsTypeEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_IMPLEMENTS_TYPE);
    
    }

    private static final int CLASS_IMPLEMENTS_NAME_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeClassImplementsNamePart(StringASTBuffer astBuffer, long identifier) {
        
        astBuffer.writeElementStart(ParseTreeElement.CLASS_IMPLEMENTS_NAME_PART);
        
        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeClassImplementsNamePart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassImplementsNamePart(
                context,
                astBuffer.getStringRef(index + 1));
    }

    static void encodeClassImplementsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_IMPLEMENTS);
    }
    
    public static <COMPILATION_UNIT> void decodeClassImplementsEnd(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassImplementsEnd(context);
    }

    public static <COMPILATION_UNIT> void decodeClassImplementsTypeStart(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassImplementsTypeStart(context);
    }

    public static <COMPILATION_UNIT> void decodeClassImplementsTypeEnd(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassImplementsTypeEnd(context);
    }
}
