package com.neaterbits.compiler.parser.listener.encoded;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.ReferenceType;
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

        case SCALAR_TYPE_REFERENCE:
            size = SCALAR_TYPE_REFERENCE_SIZE;
            break;
        
        case VAR_NAME_DECLARATION:
            size = VARIABLE_NAME_SIZE;
            break;
            
        case RESOLVE_LATER_IDENTIFIER_TYPE_REFERENCE:
            size = IDENTIFIER_TYPE_REFERENCE_SIZE;
            break;

        case METHOD_NAME:
            size = METHOD_NAME_SIZE;
            break;
            
        case SIGNATURE_PARAMETER:
            size = SIGNATURE_PARAMETER_SIZE;
            break;
            
        case RESOLVE_LATER_SCOPED_TYPE_REFERENCE_PART:
            size = SCOPED_TYPE_REFERENCE_PART_SIZE;
            break;
            
        default:
            size = 0; // ParseTreeElement
            break;
        }
        
        return size + (element.isLeaf()
                ? 1
                : 1 + 1); // enum + start/end boolean
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
        
        return size + (element.isLeaf()
                ? 1
                : 1 + 1); // enum + start/end boolean
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
            
            importKeywordContext = astBuffer.hasContextRef(index + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 4))
                    : null;

            staticKeywordContext = astBuffer.hasContextRef(index + 4 + 4 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 4 + 4 + 4))
                    : null;
        }
        else {
            elementContext = null;
            importKeywordContext = null;
            staticKeywordContext = null;
        }
        
        listener.onImportStart(
                elementContext,
                astBuffer.hasStringRef(index) ? astBuffer.getInt(index) : StringRef.STRING_NONE,
                importKeywordContext,
                astBuffer.hasStringRef(index + 4 + 4) ? astBuffer.getInt(index + 4 + 4) : StringRef.STRING_NONE,
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
                astBuffer.getStringRef(index));
    }

    static void encodeImportEnd(StringASTBuffer astBuffer, boolean onDemand) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPORT);

        astBuffer.writeBoolean(onDemand);
    }
    
    public static <COMPILATION_UNIT> void decodeImportEnd(ASTBufferRead astBuffer, Context context, int index, ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onImportEnd(context, astBuffer.getBoolean(index));

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
            
            namespaceKeywordContext = astBuffer.hasContextRef(index + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 4))
                    : null;
        }
        else {
            elementContext = null;
            namespaceKeywordContext = null;
        }
        
        listener.onNamespaceStart(
                elementContext,
                astBuffer.hasStringRef(index) ? astBuffer.getInt(index) : StringRef.STRING_NONE,
                namespaceKeywordContext);
    }

    private static final int NAMESPACE_START_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    private static final int NAMESPACE_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeNamespacePart(StringASTBuffer astBuffer, long part) {
        
        astBuffer.writeLeafElement(ParseTreeElement.NAMESPACE_PART);
        
        astBuffer.writeStringRef(part);
    }

    public static <COMPILATION_UNIT> void decodeNamespacePart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onNamespacePart(
                context,
                astBuffer.getStringRef(index));
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
            
            classKeywordContext = astBuffer.hasContextRef(index + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 4))
                    : null;
                    
            nameContext = astBuffer.hasContextRef(index + 4 + 4 + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 4 + 4 + 4))
                    : null;
        }
        else {
            elementContext = null;
            classKeywordContext = null;
            nameContext = null;
        }

        listener.onClassStart(
                elementContext,
                astBuffer.getStringRef(index),
                classKeywordContext,
                astBuffer.getStringRef(index + 4 + 4),
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

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassModifier.Type.VISIBILITY);
        astBuffer.writeEnumByte(classVisibility);
    }

    static void encodeSubclassingModifier(StringASTBuffer astBuffer, Subclassing subclassing) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassModifier.Type.SUBCLASSING);
        astBuffer.writeEnumByte(subclassing);
    }

    public static <COMPILATION_UNIT> void decodeClassModifierHolder(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final ClassModifier.Type type = astBuffer.getEnumByte(index, ClassModifier.Type.class);
        
        switch (type) {
        case VISIBILITY:
            listener.onVisibilityClassModifier(context, astBuffer.getEnumByte(index + 1, ClassVisibility.class));
            break;
            
        case SUBCLASSING:
            listener.onSubclassingModifier(context, astBuffer.getEnumByte(index + 1, Subclassing.class));
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
            
            extendsKeywordContext = astBuffer.hasContextRef(index + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 4))
                    : null;
        }
        else {
            elementContext = null;
            extendsKeywordContext = null;
        }

        listener.onClassExtendsStart(
                elementContext,
                astBuffer.getStringRef(index),
                extendsKeywordContext);
    }
    
    private static final int CLASS_EXTENDS_NAME_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeClassExtendsNamePart(StringASTBuffer astBuffer, long identifier) {
        
        astBuffer.writeLeafElement(ParseTreeElement.CLASS_EXTENDS_NAME_PART);
        
        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeClassExtendsNamePart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassExtendsNamePart(
                context,
                astBuffer.getStringRef(index));
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
            
            implementsKeywordContext = astBuffer.hasContextRef(index + 4)
                    ? contextGetter.getContextFromRef(astBuffer.getContextRef(index + 4))
                    : null;
        }
        else {
            elementContext = null;
            implementsKeywordContext = null;
        }

        listener.onClassImplementsStart(
                elementContext,
                astBuffer.getStringRef(index),
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
        
        astBuffer.writeLeafElement(ParseTreeElement.CLASS_IMPLEMENTS_NAME_PART);
        
        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeClassImplementsNamePart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassImplementsNamePart(
                context,
                astBuffer.getStringRef(index));
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

    static void encodeFieldDeclarationStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.CLASS_DATA_FIELD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeFieldDeclarationStart(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onFieldDeclarationStart(context);
    }

    static void encodeFieldDeclarationEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.CLASS_DATA_FIELD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeFieldDeclarationEnd(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onFieldDeclarationEnd(context);
    }

    private static final int SCALAR_TYPE_REFERENCE_SIZE = STRING_REF_SIZE;
    
    static void encodeScalarTypeReference(StringASTBuffer astBuffer, long typeName) {
        
        astBuffer.writeElementStart(ParseTreeElement.SCALAR_TYPE_REFERENCE);

        astBuffer.writeStringRef(typeName);
    }

    public static <COMPILATION_UNIT> void decodeScalarTypeReference(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNonScopedTypeReference(context, astBuffer.getStringRef(index), ReferenceType.SCALAR);
    }

    private static final int IDENTIFIER_TYPE_REFERENCE_SIZE = STRING_REF_SIZE;
    
    static void encodeIdentifierTypeReference(StringASTBuffer astBuffer, long typeName) {
        
        astBuffer.writeElementStart(ParseTreeElement.RESOLVE_LATER_IDENTIFIER_TYPE_REFERENCE);

        astBuffer.writeStringRef(typeName);
    }

    public static <COMPILATION_UNIT> void decodeIdentifierTypeReference(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNonScopedTypeReference(context, astBuffer.getStringRef(index), ReferenceType.REFERENCE);
    }

    static void encodeVariableDeclaratorStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.VARIABLE_DECLARATION_ELEMENT);
    }

    public static <COMPILATION_UNIT> void decodeVariableDeclaratorStart(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onVariableDeclaratorStart(context);
    }

    static void encodeVariableDeclaratorEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.VARIABLE_DECLARATION_ELEMENT);
    }
    
    public static <COMPILATION_UNIT> void decodeVariableDeclaratorEnd(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onVariableDeclaratorEnd(context);
    }

    private static final int VARIABLE_NAME_SIZE = STRING_REF_SIZE + 4;

    static void encodeVariableName(StringASTBuffer astBuffer, long name, int numDims) {
        
        astBuffer.writeLeafElement(ParseTreeElement.VAR_NAME_DECLARATION);
        
        astBuffer.writeStringRef(name);
        astBuffer.writeInt(numDims);
    }

    public static <COMPILATION_UNIT> void decodeVariableName(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onVariableName(
                context,
                astBuffer.getStringRef(index),
                astBuffer.getInt(index + 4));
    }

    static void encodeClassMethodStart(StringASTBuffer astBuffer, Context context) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_METHOD_MEMBER);
        
    }

    public static <COMPILATION_UNIT> void decodeClassMethodStart(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassMethodStart(context);
    }

    static void encodeMethodReturnTypeStart(StringASTBuffer astBuffer, Context context) {

        astBuffer.writeElementStart(ParseTreeElement.METHOD_RETURN_TYPE);
        
    }

    public static <COMPILATION_UNIT> void decodeMethodReturnTypeStart(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodReturnTypeStart(context);
    }

    static void encodeMethodReturnTypeEnd(StringASTBuffer astBuffer, Context context) {

        astBuffer.writeElementEnd(ParseTreeElement.METHOD_RETURN_TYPE);
    }

    public static <COMPILATION_UNIT> void decodeMethodReturnTypeEnd(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodReturnTypeEnd(context);
    }
    
    private static final int METHOD_NAME_SIZE = STRING_REF_SIZE;

    static void encodeMethodName(StringASTBuffer astBuffer, Context context, long methodName) {

        astBuffer.writeLeafElement(ParseTreeElement.METHOD_NAME);
        
        astBuffer.writeStringRef(methodName);
    }

    public static <COMPILATION_UNIT> void decodeMethodName(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodName(context, astBuffer.getStringRef(index));
    }

    static void encodeClassMethodEnd(StringASTBuffer astBuffer, Context context) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_METHOD_MEMBER);
        
    }

    public static <COMPILATION_UNIT> void decodeClassMethodEnd(
            ASTBufferRead astBuffer,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassMethodEnd(context);
    }
    
    private static final int SIGNATURE_PARAMETER_SIZE = 1;
    
    static void encodeSignatureParameterStart(StringASTBuffer astBuffer, Context context, boolean varArgs) {
        
        astBuffer.writeElementStart(ParseTreeElement.SIGNATURE_PARAMETER);
        
        astBuffer.writeBoolean(varArgs);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParameterStart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodSignatureParameterStart(context, astBuffer.getBoolean(index));
    }

    static void encodeSignatureParameterEnd(StringASTBuffer astBuffer, Context context) {
        
        astBuffer.writeElementEnd(ParseTreeElement.SIGNATURE_PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParameterEnd(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodSignatureParameterEnd(context);
    }

    static void encodeScopedTypeReferenceStart(StringASTBuffer astBuffer, Context context) {
        
        astBuffer.writeElementStart(ParseTreeElement.RESOLVE_LATER_SCOPED_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceStart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceStart(context, ReferenceType.REFERENCE);
    }

    private static final int SCOPED_TYPE_REFERENCE_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeScopedTypeReferencePart(StringASTBuffer astBuffer, Context context, long part) {
        
        astBuffer.writeLeafElement(ParseTreeElement.RESOLVE_LATER_SCOPED_TYPE_REFERENCE_PART);
        
        astBuffer.writeStringRef(part);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferencePart(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferencePart(context, astBuffer.getStringRef(index));
    }

    static void encodeScopedTypeReferenceEnd(StringASTBuffer astBuffer, Context context) {
        
        astBuffer.writeElementEnd(ParseTreeElement.RESOLVE_LATER_SCOPED_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceEnd(
            ASTBufferRead astBuffer,
            Context context,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceEnd(context);
    }
}
