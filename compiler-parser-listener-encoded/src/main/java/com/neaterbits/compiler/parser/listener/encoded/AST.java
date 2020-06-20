package com.neaterbits.compiler.parser.listener.encoded;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Bitwise;
import com.neaterbits.compiler.util.operator.Logical;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.OperatorType;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.typedefinition.ClassModifier;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.util.io.strings.StringRef;

public class AST {
    
    private static final int STRING_REF_SIZE = 4;
    private static final int CONTEXT_REF_SIZE = 4;

    static final int NO_STRINGREF = -1;
    static final int NO_CONTEXTREF = -1;
    
    private static final int CONTEXT_SIZE = 8;
    
    static int writeContext(ASTBuffer contextBuffer, Context context) {
        
        if (context.getStartOffset() > 100000) {
            throw new IllegalArgumentException();
        }
        
        Objects.requireNonNull(context);
        
        final int writePos = contextBuffer.getWritePos();

        contextBuffer.writeInt(context.getStartOffset());
        contextBuffer.writeInt(context.getEndOffset());
        
        return writePos;
    }

    static int writeContext(ASTBuffer contextBuffer, int otherContext) {
        
        final int writePos = contextBuffer.getWritePos();
        
        contextBuffer.appendFrom(otherContext, CONTEXT_SIZE);
        
        return writePos;
    }

    public static int sizeStart(ParseTreeElement element, ASTBufferRead astBuffer, int index) {
        
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
            
        case IF_ELSE_IF_ELSE_STATEMENT:
            size = IF_STATEMENT_SIZE;
            break;
            
        case ELSE_IF_CONDITION_BLOCK:
            size = ELSE_IF_CONDITION_BLOCK_SIZE;
            break;
            
        case ELSE_BLOCK:
            size = ELSE_BLOCK_SIZE;
            break;
         
        case SIMPLE_VARIABLE_REFERENCE:
            size = VARIABLE_REFERENCE_SIZE;
            break;
            
        case INTEGER_LITERAL:
            // Size of literal storage in bytes stored as initial byte
            size = 1 + astBuffer.getByte(index) + 1 + 1 + 1;
            break;
            
        case EXPRESSION_BINARY_OPERATOR:
            size = EXPRESSION_BINARY_OPERATOR_SIZE;
            break;
            
        case WHILE_STATEMENT:
            size = WHILE_STATEMENT_SIZE;
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
            int importStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final int importKeywordContext;
        final int staticKeywordContext;
        
        if (contextGetter != null) {
            importKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);

            staticKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE + STRING_REF_SIZE);
        }
        else {
            importKeywordContext = ContextRef.NONE;
            staticKeywordContext = ContextRef.NONE;
        }
        
        listener.onImportStart(
                importStartContext,
                astBuffer.hasStringRef(index) ? astBuffer.getInt(index) : StringRef.STRING_NONE,
                importKeywordContext,
                astBuffer.hasStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE)
                    ? astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE)
                    : StringRef.STRING_NONE,
                staticKeywordContext);
    }

    static void encodeImportNamePart(StringASTBuffer astBuffer, long identifier) {
        
        astBuffer.writeLeafElement(ParseTreeElement.IMPORT_NAME_PART);
        
        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeImportNamePart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onImportIdentifier(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeImportEnd(StringASTBuffer astBuffer, boolean onDemand) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPORT);

        astBuffer.writeBoolean(onDemand);
    }
    
    public static <COMPILATION_UNIT> void decodeImportEnd(
            ASTBufferRead astBuffer,
            int importStartContext,
            Context endContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onImportEnd(importStartContext, endContext, astBuffer.getBoolean(index));
    }

    static void encodeNamespaceStart(StringASTBuffer astBuffer, long namespaceKeyword, int namespaceKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.NAMESPACE);
        
        astBuffer.writeStringRef(namespaceKeyword);
        astBuffer.writeContextRef(namespaceKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeNamespaceStart(
            ASTBufferRead astBuffer,
            int namespaceStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final int namespaceKeywordContext;
        
        if (contextGetter != null) {
            namespaceKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            namespaceKeywordContext = ContextRef.NONE;
        }
        
        listener.onNamespaceStart(
                namespaceStartContext,
                astBuffer.hasStringRef(index) ? astBuffer.getStringRef(index) : StringRef.STRING_NONE,
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
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onNamespacePart(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeNamespaceEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.NAMESPACE);
    }

    public static <COMPILATION_UNIT> void decodeNamespaceEnd(
            int namespaceStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onNameSpaceEnd(namespaceStartContext, endContext);
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
            int classStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int classKeywordContext;
        final int nameContext;
        
        if (contextGetter != null) {
            
            classKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
            nameContext = astBuffer.getContextRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE + STRING_REF_SIZE);
        }
        else {
            classKeywordContext = ContextRef.NONE;
            nameContext = ContextRef.NONE;
        }

        listener.onClassStart(
                classStartContext,
                astBuffer.getStringRef(index),
                classKeywordContext,
                astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE),
                nameContext);
    }

    static void encodeClassEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.CLASS_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeClassEnd(
            ASTBufferRead astBuffer,
            int classStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassEnd(classStartContext, endContext);
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
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final ClassModifier.Type type = astBuffer.getEnumByte(index, ClassModifier.Type.class);
        
        switch (type) {
        case VISIBILITY:
            listener.onVisibilityClassModifier(leafContext, astBuffer.getEnumByte(index + 1, ClassVisibility.class));
            break;
            
        case SUBCLASSING:
            listener.onSubclassingModifier(leafContext, astBuffer.getEnumByte(index + 1, Subclassing.class));
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
            int classExtendsStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int extendsKeywordContext;
        
        if (contextGetter != null) {
            extendsKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            extendsKeywordContext = ContextRef.NONE;
        }

        listener.onClassExtendsStart(
                classExtendsStartContext,
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
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassExtendsNamePart(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeClassExtendsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_EXTENDS);
    }
    
    public static <COMPILATION_UNIT> void decodeClassExtendsEnd(
            ASTBufferRead astBuffer,
            int classExtendsStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassExtendsEnd(classExtendsStartContext, endContext);
    }

    private static final int CLASS_IMPLEMENTS_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    static void encodeClassImplementsStart(StringASTBuffer astBuffer, long implementsKeyword, int implementsKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_IMPLEMENTS);
        astBuffer.writeStringRef(implementsKeyword);
        astBuffer.writeContextRef(implementsKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeClassImplementsStart(
            ASTBufferRead astBuffer,
            int classImplementsStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int implementsKeywordContext;
        
        if (contextGetter != null) {
            implementsKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            implementsKeywordContext = ContextRef.NONE;
        }

        listener.onClassImplementsStart(
                classImplementsStartContext,
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
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassImplementsNamePart(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeClassImplementsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_IMPLEMENTS);
    }
    
    public static <COMPILATION_UNIT> void decodeClassImplementsEnd(
            ASTBufferRead astBuffer,
            int classImplementsStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassImplementsEnd(classImplementsStartContext, endContext);
    }

    public static <COMPILATION_UNIT> void decodeClassImplementsTypeStart(
            ASTBufferRead astBuffer,
            int classImplementsTypeStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassImplementsTypeStart(classImplementsTypeStartContext);
    }

    public static <COMPILATION_UNIT> void decodeClassImplementsTypeEnd(
            ASTBufferRead astBuffer,
            int classImplementsTypeStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassImplementsTypeEnd(classImplementsTypeStartContext, endContext);
    }

    static void encodeFieldDeclarationStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.CLASS_DATA_FIELD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeFieldDeclarationStart(
            ASTBufferRead astBuffer,
            int fieldDeclarationStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onFieldDeclarationStart(fieldDeclarationStartContext);
    }

    static void encodeFieldDeclarationEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.CLASS_DATA_FIELD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeFieldDeclarationEnd(
            ASTBufferRead astBuffer,
            int fieldDeclarationStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onFieldDeclarationEnd(fieldDeclarationStartContext, endContext);
    }

    private static final int SCALAR_TYPE_REFERENCE_SIZE = STRING_REF_SIZE;
    
    static void encodeScalarTypeReference(StringASTBuffer astBuffer, long typeName) {
        
        astBuffer.writeElementStart(ParseTreeElement.SCALAR_TYPE_REFERENCE);

        astBuffer.writeStringRef(typeName);
    }

    public static <COMPILATION_UNIT> void decodeScalarTypeReference(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNonScopedTypeReference(leafContext, astBuffer.getStringRef(index), ReferenceType.SCALAR);
    }

    private static final int IDENTIFIER_TYPE_REFERENCE_SIZE = STRING_REF_SIZE;
    
    static void encodeIdentifierTypeReference(StringASTBuffer astBuffer, long typeName) {
        
        astBuffer.writeElementStart(ParseTreeElement.RESOLVE_LATER_IDENTIFIER_TYPE_REFERENCE);

        astBuffer.writeStringRef(typeName);
    }

    public static <COMPILATION_UNIT> void decodeIdentifierTypeReference(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNonScopedTypeReference(leafContext, astBuffer.getStringRef(index), ReferenceType.REFERENCE);
    }

    static void encodeVariableDeclaratorStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.VARIABLE_DECLARATION_ELEMENT);
    }

    public static <COMPILATION_UNIT> void decodeVariableDeclaratorStart(
            ASTBufferRead astBuffer,
            int variableDeclaratorStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onVariableDeclaratorStart(variableDeclaratorStartContext);
    }

    static void encodeVariableDeclaratorEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.VARIABLE_DECLARATION_ELEMENT);
    }
    
    public static <COMPILATION_UNIT> void decodeVariableDeclaratorEnd(
            ASTBufferRead astBuffer,
            int variableDeclaratorStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onVariableDeclaratorEnd(variableDeclaratorStartContext, endContext);
    }

    private static final int VARIABLE_NAME_SIZE = STRING_REF_SIZE + 4;

    static void encodeVariableName(StringASTBuffer astBuffer, long name, int numDims) {
        
        astBuffer.writeLeafElement(ParseTreeElement.VAR_NAME_DECLARATION);
        
        astBuffer.writeStringRef(name);
        astBuffer.writeInt(numDims);
    }

    public static <COMPILATION_UNIT> void decodeVariableName(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onVariableName(
                leafContext,
                astBuffer.getStringRef(index),
                astBuffer.getInt(index + 4));
    }

    static void encodeClassMethodStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_METHOD_MEMBER);
        
    }

    public static <COMPILATION_UNIT> void decodeClassMethodStart(
            ASTBufferRead astBuffer,
            int classMethodStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassMethodStart(classMethodStartContext);
    }

    static void encodeMethodReturnTypeStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.METHOD_RETURN_TYPE);
        
    }

    public static <COMPILATION_UNIT> void decodeMethodReturnTypeStart(
            ASTBufferRead astBuffer,
            int methodReturnTypeContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodReturnTypeStart(methodReturnTypeContext);
    }

    static void encodeMethodReturnTypeEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.METHOD_RETURN_TYPE);
    }

    public static <COMPILATION_UNIT> void decodeMethodReturnTypeEnd(
            ASTBufferRead astBuffer,
            int methodReturnTypeStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodReturnTypeEnd(methodReturnTypeStartContext, endContext);
    }
    
    private static final int METHOD_NAME_SIZE = STRING_REF_SIZE;

    static void encodeMethodName(StringASTBuffer astBuffer, long methodName) {

        astBuffer.writeLeafElement(ParseTreeElement.METHOD_NAME);
        
        astBuffer.writeStringRef(methodName);
    }

    public static <COMPILATION_UNIT> void decodeMethodName(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodName(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeClassMethodEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_METHOD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeClassMethodEnd(
            ASTBufferRead astBuffer,
            int classMethodStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onClassMethodEnd(classMethodStartContext, endContext);
    }

    static void encodeSignatureParametersStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.SIGNATURE_PARAMETERS);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParametersStart(
            ASTBufferRead astBuffer,
            int signatureParametersStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodSignatureParametersStart(signatureParametersStartContext);
    }

    static void encodeSignatureParametersEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.SIGNATURE_PARAMETERS);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParametersEnd(
            ASTBufferRead astBuffer,
            int signatureParametersStartContext,
            Context endContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodSignatureParametersEnd(signatureParametersStartContext, endContext);
    }

    private static final int SIGNATURE_PARAMETER_SIZE = 1;
    
    static void encodeSignatureParameterStart(StringASTBuffer astBuffer, boolean varArgs) {
        
        astBuffer.writeElementStart(ParseTreeElement.SIGNATURE_PARAMETER);
        
        astBuffer.writeBoolean(varArgs);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParameterStart(
            ASTBufferRead astBuffer,
            int signatureParameterStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodSignatureParameterStart(signatureParameterStartContext, astBuffer.getBoolean(index));
    }

    static void encodeSignatureParameterEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.SIGNATURE_PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParameterEnd(
            ASTBufferRead astBuffer,
            int signatureParameterStartContext,
            Context endContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodSignatureParameterEnd(signatureParameterStartContext, endContext);
    }

    static void encodeScopedTypeReferenceStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.RESOLVE_LATER_SCOPED_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceStart(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceStart(scopedTypeReferenceStartContext, ReferenceType.REFERENCE);
    }

    private static final int SCOPED_TYPE_REFERENCE_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeScopedTypeReferencePart(StringASTBuffer astBuffer, long part) {
        
        astBuffer.writeLeafElement(ParseTreeElement.RESOLVE_LATER_SCOPED_TYPE_REFERENCE_PART);
        
        astBuffer.writeStringRef(part);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferencePart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferencePart(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeScopedTypeReferenceEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.RESOLVE_LATER_SCOPED_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceEnd(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceEndContext,
            Context endContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceEnd(scopedTypeReferenceEndContext, endContext);
    }
    
    static void encodeVariableDeclarationStatementStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.VARIABLE_DECLARATION_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeVariableDeclarationStatementStart(
            ASTBufferRead astBuffer,
            int variableDeclarationStatementStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onVariableDeclarationStatementStart(variableDeclarationStatementStartContext);
    }

    static void encodeVariableDeclarationStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.VARIABLE_DECLARATION_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeVariableDeclarationStatementEnd(
            ASTBufferRead astBuffer,
            int variableDeclarationStatementStartContext,
            Context context,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onVariableDeclarationStatementEnd(variableDeclarationStatementStartContext, context);
    }
    
    private static final int IF_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeIfElseIfElseStatementStart(StringASTBuffer astBuffer, long ifKeyword, int ifKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.IF_ELSE_IF_ELSE_STATEMENT);
        
        astBuffer.writeStringRef(ifKeyword);
        astBuffer.writeContextRef(ifKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeIfElseIfElseStatementStart(
            ASTBufferRead astBuffer,
            int ifStartContext,
            ContextGetter contextGetter,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        final int ifKeywordContext;
        
        if (contextGetter != null) {
            ifKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            ifKeywordContext = ContextRef.NONE;
        }
        
        listener.onIfStatementStart(ifStartContext, astBuffer.getStringRef(index), ifKeywordContext);
    }

    static void encodeIfElseIfElseStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.IF_ELSE_IF_ELSE_STATEMENT);
    }

    public static <COMPILATION_UNIT >void decodeIfElseIfElseStatementEnd(
            ASTBufferRead astBuffer,
            int ifStatementStartContext,
            Context endContext,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        listener.onEndIfStatement(ifStatementStartContext, endContext);
    }
    
    static void encodeIfConditionBlockStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.IF_CONDITION_BLOCK);
    }

    public static <COMPILATION_UNIT> void decodeIfConditionBlockStart(
            ASTBufferRead astBuffer,
            int conditionBlockStartContext,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        listener.onIfStatementInitialBlockStart(conditionBlockStartContext);
    }

    static void encodeIfConditionBlockEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.IF_CONDITION_BLOCK);
    }

    public static <COMPILATION_UNIT> void decodeIfConditionBlockEnd(
            ASTBufferRead astBuffer,
            int conditionBlockStartContext,
            Context endContext,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        listener.onIfStatementInitialBlockEnd(conditionBlockStartContext, endContext);
    }

    private static final int ELSE_IF_CONDITION_BLOCK_SIZE
        =    STRING_REF_SIZE + CONTEXT_REF_SIZE
           + STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeElseIfConditionBlockStart(
            StringASTBuffer astBuffer,
            long elseKeyword, int elseKeywordContext,
            long ifKeyword, int ifKeywordContext) {
        
        astBuffer.writeElementStart(ParseTreeElement.ELSE_IF_CONDITION_BLOCK);
        
        astBuffer.writeStringRef(elseKeyword);
        astBuffer.writeContextRef(elseKeywordContext);
        
        astBuffer.writeStringRef(ifKeyword);
        astBuffer.writeContextRef(ifKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeElseIfConditionBlockStart(
            ASTBufferRead astBuffer,
            int elseIfConditionBlockStartContext,
            ContextGetter contextGetter,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        
        final int elseKeywordContext;
        final int ifKeywordContext;

        if (contextGetter != null) {
            elseKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
            ifKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE + STRING_REF_SIZE);
        }
        else {
            elseKeywordContext = ContextRef.NONE;
            ifKeywordContext = ContextRef.NONE;
        }

        listener.onElseIfStatementStart(
                elseIfConditionBlockStartContext,
                astBuffer.getStringRef(index), elseKeywordContext,
                astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE), ifKeywordContext);
    }

    static void encodeElseIfConditionBlockEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ELSE_IF_CONDITION_BLOCK);
    }

    public static <COMPILATION_UNIT> void decodeElseIfConditionBlockEnd(
            ASTBufferRead astBuffer,
            int elseIfConditionBlockStartContext,
            Context endContext,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        listener.onElseIfStatementEnd(elseIfConditionBlockStartContext, endContext);
    }

    private static final int ELSE_BLOCK_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeElseBlockStart(StringASTBuffer astBuffer, long elseKeyword, int elseKeywordContext) {
        
        astBuffer.writeElementStart(ParseTreeElement.ELSE_BLOCK);
        astBuffer.writeStringRef(elseKeyword);
        astBuffer.writeContextRef(elseKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeElseBlockStart(
            ASTBufferRead astBuffer,
            int elseStartContext,
            ContextGetter contextGetter,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        
        final int elseKeywordContext;

        if (contextGetter != null) {
            elseKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            elseKeywordContext = ContextRef.NONE;
        }

        listener.onElseStatementStart(elseStartContext, astBuffer.getStringRef(index), elseKeywordContext);
    }

    static void encodeElseBlockEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ELSE_BLOCK);
    }

    public static <COMPILATION_UNIT> void decodeElseBlockEnd(
            ASTBufferRead astBuffer,
            int elseStartContext,
            Context endContext,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        listener.onElseStatementEnd(elseStartContext, endContext);
    }

    private static final int VARIABLE_REFERENCE_SIZE = STRING_REF_SIZE;
    
    static void encodeVariableReference(StringASTBuffer astBuffer, long name) {

        astBuffer.writeLeafElement(ParseTreeElement.SIMPLE_VARIABLE_REFERENCE);
        
        astBuffer.writeStringRef(name);
    }

    public static <COMPILATION_UNIT> void decodeVariableReference(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        listener.onVariableReference(leafContext, astBuffer.getStringRef(index));
    }

    private static final int EXPRESSION_BINARY_OPERATOR_SIZE = 1 + 1;
    
    static void encodeExpressionBinaryOperator(StringASTBuffer astBuffer, Operator operator) {
        
        astBuffer.writeLeafElement(ParseTreeElement.EXPRESSION_BINARY_OPERATOR);
        
        astBuffer.writeEnumByte(operator.getOperatorType());
        astBuffer.writeEnumByte(operator.getEnumValue());
    }

    public static <COMPILATION_UNIT> void decodeExpressionBinaryOperator(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        final OperatorType operatorType = astBuffer.getEnumByte(index, OperatorType.class);
        
        final Operator operator;
        
        switch (operatorType) {
        case ARITHMETIC:
            operator = astBuffer.getEnumByte(index + 1, Arithmetic.class);
            break;
            
        case BITWISE:
            operator = astBuffer.getEnumByte(index + 1, Bitwise.class);
            break;
            
        case RELATIONAL:
            operator = astBuffer.getEnumByte(index + 1, Relational.class);
            break;
            
        case LOGICAL:
            operator = astBuffer.getEnumByte(index + 1, Logical.class);
            break;
        
        default:
            throw new IllegalStateException();
        }
        
        listener.onExpressionBinaryOperator(leafContext, operator);
    }

    static void encodeIntegerLiteral(StringASTBuffer astBuffer, long value, Base base, boolean signed, int bits) {

        astBuffer.writeLeafElement(ParseTreeElement.INTEGER_LITERAL);
        
       if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
           astBuffer.writeByte((byte)1);
           astBuffer.writeByte((byte)value);
       }
       else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
           astBuffer.writeByte((byte)2);
           astBuffer.writeShort((short)value);
       }
       else if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
           astBuffer.writeByte((byte)4);
           astBuffer.writeInt((int)value);
       }
       else {
           astBuffer.writeByte((byte)8);
           astBuffer.writeLong(value);
       }
       
       astBuffer.writeEnumByte(base);
       astBuffer.writeBoolean(signed);

       if (bits > Byte.MAX_VALUE) {
           throw new IllegalArgumentException();
       }

       astBuffer.writeByte((byte)bits);
    }

    public static <COMPILATION_UNIT> void decodeIntegerLiteral(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        final long value;
        // length
        final int valueSize = astBuffer.getByte(index);
        
        final int valueIndex = index + 1;
        
        switch (valueSize) {
        case 1:
            value = astBuffer.getByte(valueIndex);
            break;
            
        case 2:
            value = astBuffer.getShort(valueIndex);
            break;
            
        case 4:
            value = astBuffer.getInt(valueIndex);
            break;
            
        case 8:
            value = astBuffer.getLong(valueIndex);
            break;
            
        default:
            throw new IllegalStateException();
        }

        final int afterValueIndex = index + 1 + valueSize;
        
        final Base base = astBuffer.getEnumByte(afterValueIndex, Base.class);
        final boolean signed = astBuffer.getBoolean(afterValueIndex + 1);
        final int bits = astBuffer.getByte(afterValueIndex + 2); 

        listener.onIntegerLiteral(
                leafContext,
                value,
                base,
                signed,
                bits);
    }

    private static final int WHILE_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeWhileStatementStart(StringASTBuffer astBuffer, long whileKeyword, int whileKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.WHILE_STATEMENT);
        
        astBuffer.writeStringRef(whileKeyword);
        astBuffer.writeContextRef(whileKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeWhileStatementStart(
            ASTBufferRead astBuffer,
            int whileStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int whileKeywordContext;
        
        if (contextGetter != null) {
            
            whileKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            whileKeywordContext = ContextRef.NONE;
        }

        listener.onWhileStatementStart(
                whileStatementStartContext,
                astBuffer.getStringRef(index),
                whileKeywordContext);
    }

    static void encodeWhileStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.WHILE_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeWhileStatementEnd(
            ASTBufferRead astBuffer,
            int whileStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onWhileStatementEnd(whileStatementStartContext, endContext);
    }
}
