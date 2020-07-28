package com.neaterbits.compiler.parser.listener.encoded;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.model.Visibility;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Assignment;
import com.neaterbits.compiler.util.operator.Bitwise;
import com.neaterbits.compiler.util.operator.IncrementDecrement;
import com.neaterbits.compiler.util.operator.Logical;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.OperatorType;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.parse.NamePart;
import com.neaterbits.compiler.util.statement.ASTMutability;
import com.neaterbits.compiler.util.typedefinition.ClassMethodModifier;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassModifier;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldModifier;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;
import com.neaterbits.compiler.util.typedefinition.VariableModifier;
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

        case IMPLEMENTS:
            size = CLASS_IMPLEMENTS_SIZE;
            break;

        case IMPLEMENTS_NAME_PART:
            size = IMPLEMENTS_NAME_PART_SIZE;
            break;
            
        case ENUM_DEFINITION:
            size = ENUM_DEFINITION_SIZE;
            break;

        case ENUM_CONSTANT_DEFINITION:
            size = ENUM_CONSTANT_DEFINITION_SIZE;
            break;

        case SCALAR_TYPE_REFERENCE:
            size = SCALAR_TYPE_REFERENCE_SIZE;
            break;
        
        case VAR_NAME_DECLARATION:
            size = VARIABLE_NAME_SIZE;
            break;
            
        case UNRESOLVED_IDENTIFIER_TYPE_REFERENCE:
            size = IDENTIFIER_TYPE_REFERENCE_SIZE;
            break;

        case CONSTRUCTOR_NAME:
            size = CONSTRUCTOR_NAME_SIZE;
            break;
            
        case CLASS_METHOD_MODIFIER_HOLDER:
            size = 1 + classMethodModifierDataSize(astBuffer.getEnumByte(index, ClassMethodModifier.Type.class));
            break;
            
        case METHOD_NAME:
            size = METHOD_NAME_SIZE;
            break;
            
        case SIGNATURE_PARAMETER:
            size = SIGNATURE_PARAMETER_SIZE;
            break;
            
        case UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME_PART:
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

        case STRING_LITERAL:
            size = STRING_LITERAL_SIZE;
            break;

        case CHARACTER_LITERAL:
            size = CHARACTER_LITERAL_SIZE;
            break;

        case BOOLEAN_LITERAL:
            size = BOOLEAN_LITERAL_SIZE;
            break;

        case UNARY_EXPRESSION:
            size = UNARY_EXPRESSION_SIZE;
            break;

        case EXPRESSION_BINARY_OPERATOR:
            size = EXPRESSION_BINARY_OPERATOR_SIZE;
            break;
            
        case WHILE_STATEMENT:
            size = WHILE_STATEMENT_SIZE;
            break;

        case ITERATOR_FOR_STATEMENT:
            size = ITERATOR_FOR_STATEMENT_SIZE;
            break;
        
        case TRY_CATCH_FINALLY_STATEMENT:
            size = TRY_CATCH_FINALLY_STATEMENT_SIZE;
            break;

        case CATCH:
            size = CATCH_SIZE;
            break;

        case FINALLY:
            size = FINALLY_SIZE;
            break;

        case TRY_WITH_RESOURCES_STATEMENT:
            size = TRY_WITH_RESOURCES_STATEMENT_SIZE;
            break;

        case RETURN_STATEMENT:
            size = RETURN_STATEMENT_SIZE;
            break;
            
        case THROW_STATEMENT:
            size = THROW_STATEMENT_SIZE;
            break;

        case METHOD_INVOCATION_EXPRESSION:
            size = METHOD_INVOCATION_SIZE;
            break;
            
        case FIELD_ACCESS:
            size = FIELD_ACCESS_SIZE;
            break;
            
        case ANNOTATION:
            size = namesSize(astBuffer.getByte(index));
            break;

        case ANNOTATION_ELEMENT:
            size = ANNOTATION_ELEMENT_SIZE;
            break;
            
        case NAME_REFERENCE:
            size = NAME_REFERENCE_SIZE;
            break;

        case NAME_PRIMARY:
            size = NAME_PRIMARY_SIZE;
            break;

        case TYPE_BOUND:
            size = TYPE_BOUND_SIZE;
            break;

        case FIELD_MODIFIER_HOLDER:
            size = fieldModifierSize(astBuffer, index);
            break;
            
        case VARIABLE_MODIFIER_HOLDER:
            size = variableModifierSize(astBuffer, index);
            break;

        case CLASS_INSTANCE_CREATION_EXPRESSION_NAME:
            size = namesSize(astBuffer.getByte(index));
            break;
            
        case NAMED_GENERIC_TYPE_PARAMETER:
            size = NAMED_GENERIC_TYPE_PARAMETER_SIZE;
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
        
        listener.onImportName(leafContext, astBuffer.getStringRef(index));
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

    static void encodeTypeDefinitionStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.TYPE_DEFINITION);
    }
    
    public static <COMPILATION_UNIT> void decodeTypeDefinitionStart(
            ASTBufferRead astBuffer,
            int typeDefinitionStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onTypeDefinitionStart(typeDefinitionStartContext);
    }

    static void encodeTypeDefinitionEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.TYPE_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeTypeDefinitionEnd(
            int typeDefinitionStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onTypeDefinitionEnd(typeDefinitionStartContext, endContext);
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

    static void encodeImplementsStart(StringASTBuffer astBuffer, long implementsKeyword, int implementsKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.IMPLEMENTS);
        astBuffer.writeStringRef(implementsKeyword);
        astBuffer.writeContextRef(implementsKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeImplementsStart(
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

        listener.onImplementsStart(
                classImplementsStartContext,
                astBuffer.getStringRef(index),
                implementsKeywordContext);
    }

    static void encodeImplementsTypeStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.IMPLEMENTS_TYPE);
    
    }

    static void encodeImplementsTypeEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPLEMENTS_TYPE);
    
    }

    private static final int IMPLEMENTS_NAME_PART_SIZE = STRING_REF_SIZE;
    
    static void encodeImplementsNamePart(StringASTBuffer astBuffer, long identifier) {
        
        astBuffer.writeLeafElement(ParseTreeElement.IMPLEMENTS_NAME_PART);
        
        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeImplementsNamePart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onImplementsNamePart(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeImplementsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPLEMENTS);
    }
    
    public static <COMPILATION_UNIT> void decodeImplementsEnd(
            ASTBufferRead astBuffer,
            int classImplementsStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onImplementsEnd(classImplementsStartContext, endContext);
    }

    public static <COMPILATION_UNIT> void decodeImplementsTypeStart(
            ASTBufferRead astBuffer,
            int classImplementsTypeStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onImplementsTypeStart(classImplementsTypeStartContext);
    }

    public static <COMPILATION_UNIT> void decodeImplementsTypeEnd(
            ASTBufferRead astBuffer,
            int classImplementsTypeStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onImplementsTypeEnd(classImplementsTypeStartContext, endContext);
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

        listener.onLeafTypeReference(leafContext, astBuffer.getStringRef(index), ReferenceType.SCALAR);
    }

    private static final int IDENTIFIER_TYPE_REFERENCE_SIZE = STRING_REF_SIZE;
    
    static void encodeIdentifierTypeReferenceStart(StringASTBuffer astBuffer, long typeName) {
        
        astBuffer.writeElementStart(ParseTreeElement.UNRESOLVED_IDENTIFIER_TYPE_REFERENCE);

        astBuffer.writeStringRef(typeName);
    }

    public static <COMPILATION_UNIT> void decodeIdentifierTypeReferenceStart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNonScopedTypeReferenceStart(leafContext, astBuffer.getStringRef(index), ReferenceType.REFERENCE);
    }

    static void encodeIdentifierTypeReferenceEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.UNRESOLVED_IDENTIFIER_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeIdentifierTypeReferenceEnd(
            int leafContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNonScopedTypeReferenceEnd(leafContext, endContext);
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

    static void encodeConstructorStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CONSTRUCTOR_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeConstructorStart(
            ASTBufferRead astBuffer,
            int constructorStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onConstructorStart(constructorStartContext);
    }

    private static final int CONSTRUCTOR_NAME_SIZE = STRING_REF_SIZE;

    static void encodeConstructorName(StringASTBuffer astBuffer, long constructorName) {

        astBuffer.writeLeafElement(ParseTreeElement.METHOD_NAME);
        
        astBuffer.writeStringRef(constructorName);
    }

    public static <COMPILATION_UNIT> void decodeConstructorName(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onConstructorName(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeConstructorEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CONSTRUCTOR_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeConstructorEnd(
            int constructorStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onConstructorEnd(constructorStartContext, endContext);
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

    
    private static int classMethodModifierDataSize(ClassMethodModifier.Type type) {
        
        final int result;
        
        switch (type) {
        case VISIBILITY:
        case OVERRIDE:
            result = 1;
            break;
        
        case STATIC:
            result = 0;
            break;
            
        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }
    
    static void encodeVisibilityClassMethodModifier(StringASTBuffer astBuffer, ClassMethodVisibility classMethodVisibility) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_METHOD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassMethodModifier.Type.VISIBILITY);
        astBuffer.writeEnumByte(classMethodVisibility);
    }

    static void encodeStaticClassMethodModifier(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_METHOD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassMethodModifier.Type.STATIC);
    }

    static void encodeOverrideClassMethodModifier(StringASTBuffer astBuffer, ClassMethodOverride classMethodOverride) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_METHOD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassMethodModifier.Type.OVERRIDE);
        astBuffer.writeEnumByte(classMethodOverride);
    }

    public static <COMPILATION_UNIT> void decodeClassMethodModifierHolder(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final ClassMethodModifier.Type type = astBuffer.getEnumByte(index, ClassMethodModifier.Type.class);
        
        switch (type) {
        case VISIBILITY:
            listener.onVisibilityClassMethodModifier(leafContext, astBuffer.getEnumByte(index + 1, ClassMethodVisibility.class));
            break;
            
        case STATIC:
            listener.onStaticClassMethodModifier(leafContext);
            break;
            
        case OVERRIDE:
            listener.onOverrideClassMethodModifier(leafContext, astBuffer.getEnumByte(index + 1, ClassMethodOverride.class));
            break;
            
         default:
             throw new UnsupportedOperationException();
        }
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

    static void encodeSignatureParameterVarargs(StringASTBuffer astBuffer) {
        
        astBuffer.writeLeafElement(ParseTreeElement.SIGNATURE_PARAMETER_VARARGS);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParameterVarargs(
            ASTBufferRead astBuffer,
            int signatureParameterVarargsContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onMethodSignatureParameterVarargs(signatureParameterVarargsContext);
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

    static void encodeThrowsStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.THROWS);
    }

    public static <COMPILATION_UNIT> void decodeThrowsStart(
            int throwsStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onThrowsStart(throwsStartContext);
    }

    static void encodeThrowsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.THROWS);
    }

    public static <COMPILATION_UNIT> void decodeThrowsEnd(
            int throwsStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onThrowsEnd(throwsStartContext, endContext);
    }

    private static final int ENUM_DEFINITION_SIZE = 2 * (STRING_REF_SIZE + CONTEXT_REF_SIZE);

    static void encodeEnumStart(StringASTBuffer astBuffer, long enumKeyword, int enumKeywordContext, long name, int nameContext) {

        astBuffer.writeElementStart(ParseTreeElement.ENUM_DEFINITION);
        
        astBuffer.writeStringRef(enumKeyword);
        astBuffer.writeContextRef(enumKeywordContext);
        
        astBuffer.writeStringRef(name);
        astBuffer.writeContextRef(nameContext);
    }

    public static <COMPILATION_UNIT> void decodeEnumStart(
            ASTBufferRead astBuffer,
            int enumStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int enumKeywordContext;
        final int nameContext;
        
        if (contextGetter != null) {
            
            enumKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
            nameContext = astBuffer.getContextRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE + STRING_REF_SIZE);
        }
        else {
            enumKeywordContext = ContextRef.NONE;
            nameContext = ContextRef.NONE;
        }

        listener.onEnumStart(
                enumStartContext,
                astBuffer.getStringRef(index),
                enumKeywordContext,
                astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE),
                nameContext);
    }
    
    private static final int ENUM_CONSTANT_DEFINITION_SIZE = STRING_REF_SIZE;

    static void encodeEnumConstantStart(StringASTBuffer astBuffer, long name) {

        astBuffer.writeElementStart(ParseTreeElement.ENUM_CONSTANT_DEFINITION);
        
        astBuffer.writeStringRef(name);
    }

    public static <COMPILATION_UNIT> void decodeEnumConstantStart(
            ASTBufferRead astBuffer,
            int enumConstantStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onEnumConstantStart(enumConstantStartContext, astBuffer.getStringRef(index));
    }

    static void encodeEnumConstantEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ENUM_CONSTANT_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeEnumConstantEnd(
            ASTBufferRead astBuffer,
            int enumConstantStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onEnumConstantEnd(enumConstantStartContext, endContext);
    }


    static void encodeEnumEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ENUM_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeEnumEnd(
            ASTBufferRead astBuffer,
            int enumStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onEnumEnd(enumStartContext, endContext);
    }

    static void encodeScopedTypeReferenceStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceStart(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceStart(scopedTypeReferenceStartContext, ReferenceType.REFERENCE);
    }

    static void encodeScopedTypeReferenceNameStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceNameStart(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceNameStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceNameStart(scopedTypeReferenceNameStartContext);
    }

    private static final int SCOPED_TYPE_REFERENCE_PART_SIZE = STRING_REF_SIZE;

    
    static void encodeScopedTypeReferencePart(StringASTBuffer astBuffer, long part) {
        
        astBuffer.writeLeafElement(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME_PART);
        
        astBuffer.writeStringRef(part);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferencePart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceNamePart(leafContext, astBuffer.getStringRef(index));
    }

    static void encodeScopedTypeReferenceNameEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceNameEnd(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceNameEndContext,
            Context endContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceNameEnd(scopedTypeReferenceNameEndContext, endContext);
    }

    static void encodeScopedTypeReferenceEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceEnd(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceEndContext,
            Context endContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onScopedTypeReferenceEnd(scopedTypeReferenceEndContext, endContext);
    }
    
    private static int variableModifierSize(ASTBufferRead astBuffer, int index) {

        final VariableModifier.Type type = astBuffer.getEnumByte(index, VariableModifier.Type.class);

        final int size;
        
        switch (type) {
        case MUTABILITY:
            size = 1 + 1;
            break;
            
        default:
            throw new UnsupportedOperationException();
        }
        
        return size;
    }

    static void encodeMutabilityVariableModifier(StringASTBuffer astBuffer, ASTMutability mutability) {
        
        astBuffer.writeLeafElement(ParseTreeElement.VARIABLE_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(VariableModifier.Type.MUTABILITY);
        astBuffer.writeEnumByte(mutability.getMutability());
    }
    
    public static <COMPILATION_UNIT> void decodeVariableModifierHolder(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final VariableModifier.Type type = astBuffer.getEnumByte(index, VariableModifier.Type.class);
        
        switch (type) {
        case MUTABILITY:
            listener.onMutabilityVariableModifier(
                    leafContext,
                    new ASTMutability(astBuffer.getEnumByte(index + 1, Mutability.class)));
            break;

         default:
             throw new UnsupportedOperationException();
        }
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

    private static final int UNARY_EXPRESSION_SIZE = 1 + 1;
    
    static void encodeUnaryExpressionStart(StringASTBuffer astBuffer, Operator operator) {
        
        astBuffer.writeElementStart(ParseTreeElement.UNARY_EXPRESSION);
        
        astBuffer.writeEnumByte(operator.getOperatorType());
        astBuffer.writeEnumByte(operator.getEnumValue());
    }

    public static <COMPILATION_UNIT> void decodeUnaryExpressionStart(
            ASTBufferRead astBuffer,
            int startContext,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        final OperatorType operatorType = astBuffer.getEnumByte(index, OperatorType.class);
        
        final Operator operator;
        
        switch (operatorType) {
            
        case LOGICAL:
            operator = astBuffer.getEnumByte(index + 1, Logical.class);
            break;
            
        case INCREMENT_DECREMENT:
            operator = astBuffer.getEnumByte(index + 1, IncrementDecrement.class);
            break;
            
        default:
            throw new IllegalStateException();
        }
        
        listener.onUnaryExpressionStart(startContext, operator);
    }

    static void encodeUnaryExpressionEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.UNARY_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeUnaryExpressionEnd(
            ASTBufferRead astBuffer,
            int startContext,
            Context endContext,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        listener.onUnaryExpressionEnd(startContext, endContext);
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
            
        case ASSIGNMENT:
            operator = astBuffer.getEnumByte(index + 1, Assignment.class);
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

    private static final int STRING_LITERAL_SIZE = STRING_REF_SIZE;
    
    static void encodeStringLiteral(StringASTBuffer astBuffer, long value) {
        
        astBuffer.writeLeafElement(ParseTreeElement.STRING_LITERAL);
        
        astBuffer.writeStringRef(value);
    }

    public static <COMPILATION_UNIT> void decodeStringLiteral(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        listener.onStringLiteral(leafContext, astBuffer.getStringRef(index));
    }

    private static final int CHARACTER_LITERAL_SIZE = 4;
    
    static void encodeCharacterLiteral(StringASTBuffer astBuffer, char value) {
        
        astBuffer.writeLeafElement(ParseTreeElement.CHARACTER_LITERAL);
        
        astBuffer.writeInt(value);
    }

    public static <COMPILATION_UNIT> void decodeCharacterLiteral(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        final char c = (char)astBuffer.getInt(index);
        
        listener.onCharacterLiteral(leafContext, c);
    }

    private static final int BOOLEAN_LITERAL_SIZE = 1;
    
    static void encodeBooleanLiteral(StringASTBuffer astBuffer, boolean value) {
        
        astBuffer.writeLeafElement(ParseTreeElement.BOOLEAN_LITERAL);
        
        astBuffer.writeByte((byte)(value ? 1 : 0));
    }

    public static <COMPILATION_UNIT> void decodeBooleanLiteral(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        final byte b = astBuffer.getByte(index);
        
        listener.onBooleanLiteral(leafContext, b != 0);
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

    private static final int ITERATOR_FOR_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeIteratorForStatementStart(StringASTBuffer astBuffer, long forKeyword, int forKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.ITERATOR_FOR_STATEMENT);
        
        astBuffer.writeStringRef(forKeyword);
        astBuffer.writeContextRef(forKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeIteratorForStatementStart(
            ASTBufferRead astBuffer,
            int iteratorForStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int iteratorForKeywordContext;
        
        if (contextGetter != null) {
            
            iteratorForKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            iteratorForKeywordContext = ContextRef.NONE;
        }

        listener.onIteratorForStatementStart(
                iteratorForStatementStartContext,
                astBuffer.getStringRef(index),
                iteratorForKeywordContext);
    }

    static void encodeIteratorForTestStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.ITERATOR_FOR_TEST);
    }
    
    public static <COMPILATION_UNIT> void decodeIteratorForTestStart(
            ASTBufferRead astBuffer,
            int iteratorForTestStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onIteratorForTestStart(iteratorForTestStartContext);
    }

    static void encodeIteratorForTestEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ITERATOR_FOR_TEST);
    }

    public static <COMPILATION_UNIT> void decodeIteratorForTestEnd(
            ASTBufferRead astBuffer,
            int iteratorForTestStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onIteratorForTestEnd(iteratorForTestStartContext, endContext);
    }

    static void encodeIteratorForStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ITERATOR_FOR_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeIteratorForStatementEnd(
            ASTBufferRead astBuffer,
            int iteratorForStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onIteratorForStatementEnd(iteratorForStatementStartContext, endContext);
    }

    private static final int TRY_CATCH_FINALLY_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeTryCatchFinallyStatementStart(StringASTBuffer astBuffer, long tryKeyword, int tryKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.TRY_CATCH_FINALLY_STATEMENT);
        
        astBuffer.writeStringRef(tryKeyword);
        astBuffer.writeContextRef(tryKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeTryCatchFinallyStatementStart(
            ASTBufferRead astBuffer,
            int tryCatchFinallyStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int tryKeywordContext;
        
        if (contextGetter != null) {
            
            tryKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            tryKeywordContext = ContextRef.NONE;
        }

        listener.onTryStatementStart(
                tryCatchFinallyStatementStartContext,
                astBuffer.getStringRef(index),
                tryKeywordContext);
    }

    static void encodeTryBlockEnd(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.TRY_BLOCK_END);
    }
    
    public static <COMPILATION_UNIT> void decodeTryBlockEnd(
            int tryBlockContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onTryBlockEnd(tryBlockContext, endContext);
    }

    private static final int CATCH_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeCatchStart(StringASTBuffer astBuffer, long catchKeyword, int catchKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.CATCH);
        
        astBuffer.writeStringRef(catchKeyword);
        astBuffer.writeContextRef(catchKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeCatchStart(
            ASTBufferRead astBuffer,
            int catchStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int catchKeywordContext;
        
        if (contextGetter != null) {
            
            catchKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            catchKeywordContext = ContextRef.NONE;
        }

        listener.onCatchStart(catchStartContext, astBuffer.getStringRef(index), catchKeywordContext);
    }

    static void encodeCatchEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.CATCH);
    }

    public static <COMPILATION_UNIT> void decodeCatchEnd(
            ASTBufferRead astBuffer,
            int catchStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onCatchEnd(catchStartContext, endContext);
    }

    private static final int FINALLY_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeFinallyStart(StringASTBuffer astBuffer, long finallyKeyword, int finallyKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.FINALLY);
        
        astBuffer.writeStringRef(finallyKeyword);
        astBuffer.writeContextRef(finallyKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeFinallyStart(
            ASTBufferRead astBuffer,
            int finallyStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int finallyKeywordContext;
        
        if (contextGetter != null) {
            
            finallyKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            finallyKeywordContext = ContextRef.NONE;
        }

        listener.onFinallyStart(finallyStartContext, astBuffer.getStringRef(index), finallyKeywordContext);
    }

    static void encodeFinallyEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.FINALLY);
    }

    public static <COMPILATION_UNIT> void decodeFinallyEnd(
            ASTBufferRead astBuffer,
            int finallyStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onFinallyEnd(finallyStartContext, endContext);
    }

    static void encodeTryCatchFinallyStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.TRY_CATCH_FINALLY_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeTryCatchFinallyStatementEnd(
            ASTBufferRead astBuffer,
            int tryCatchFinallyStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onTryStatementEnd(tryCatchFinallyStatementStartContext, endContext);
    }

    private static final int RETURN_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeReturnStatementStart(StringASTBuffer astBuffer, long returnKeyword, int returnKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.RETURN_STATEMENT);
        
        astBuffer.writeStringRef(returnKeyword);
        astBuffer.writeContextRef(returnKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeReturnStatementStart(
            ASTBufferRead astBuffer,
            int returnStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int returnKeywordContext;
        
        if (contextGetter != null) {
            
            returnKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            returnKeywordContext = ContextRef.NONE;
        }

        listener.onReturnStatementStart(
                returnStatementStartContext,
                astBuffer.getStringRef(index),
                returnKeywordContext);
    }

    private static final int TRY_WITH_RESOURCES_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeTryWithResourcesStatementStart(StringASTBuffer astBuffer, long tryKeyword, int tryKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.TRY_WITH_RESOURCES_STATEMENT);
        
        astBuffer.writeStringRef(tryKeyword);
        astBuffer.writeContextRef(tryKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeTryWithResourcesStatementStart(
            ASTBufferRead astBuffer,
            int tryWithResourcesStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int tryKeywordContext;
        
        if (contextGetter != null) {
            
            tryKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            tryKeywordContext = ContextRef.NONE;
        }

        listener.onTryWithResourcesStatementStart(
                tryWithResourcesStatementStartContext,
                astBuffer.getStringRef(index),
                tryKeywordContext);
    }

    static void encodeTryWithResourcesSpecificationStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.RESOURCES_LIST);
    }
    
    public static <COMPILATION_UNIT> void decodeTryWithResourcesSpecificationStart(
            int resourcesSpecificationStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onTryWithResourcesSpecificationStart(resourcesSpecificationStartContext);
    }

    static void encodeTryWithResourcesSpecificationEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.RESOURCES_LIST);
    }
    
    public static <COMPILATION_UNIT> void decodeTryWithResourcesSpecificationEnd(
            int resourcesSpecificationStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onTryWithResourcesSpecificationEnd(resourcesSpecificationStartContext, endContext);
    }

    static void encodeTryResourceStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.RESOURCE);
    }
    
    public static <COMPILATION_UNIT> void decodeTryResourceStart(
            int resourceStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onResourceStart(resourceStartContext);
    }

    static void encodeTryResourceEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.RESOURCE);
    }
    
    public static <COMPILATION_UNIT> void decodeTryResourceEnd(
            int resourceStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onResourceEnd(resourceStartContext, endContext);
    }

    static void encodeReturnStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.RETURN_STATEMENT);
    }

    static void encodeTryWithResourcesStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.TRY_WITH_RESOURCES_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeTryWithResourcesStatementEnd(
            ASTBufferRead astBuffer,
            int tryWithResourcesStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onTryWithResourcesStatementEnd(tryWithResourcesStatementStartContext, endContext);
    }

    public static <COMPILATION_UNIT> void decodeReturnStatementEnd(
            ASTBufferRead astBuffer,
            int returnStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onReturnStatementEnd(returnStatementStartContext, endContext);
    }

    private static final int THROW_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeThrowStatementStart(StringASTBuffer astBuffer, long throwKeyword, int throwKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.THROW_STATEMENT);
        
        astBuffer.writeStringRef(throwKeyword);
        astBuffer.writeContextRef(throwKeywordContext);
    }
    
    public static <COMPILATION_UNIT> void decodeThrowStatementStart(
            ASTBufferRead astBuffer,
            int throwStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final int throwKeywordContext;
        
        if (contextGetter != null) {
            
            throwKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            throwKeywordContext = ContextRef.NONE;
        }

        listener.onThrowStatementStart(
                throwStatementStartContext,
                astBuffer.getStringRef(index),
                throwKeywordContext);
    }

    static void encodeThrowStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.THROW_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeThrowStatementEnd(
            ASTBufferRead astBuffer,
            int throwStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onThrowStatementEnd(throwStatementStartContext, endContext);
    }

    static void encodeAssignmentStatementStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.ASSIGNMENT_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentStatementStart(
            ASTBufferRead astBuffer,
            int assignmentStatementStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onAssignmentStatementStart(assignmentStatementStartContext);
    }

    static void encodeAssignmentStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ASSIGNMENT_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentStatementEnd(
            ASTBufferRead astBuffer,
            int assignmentStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onAssignmentStatementEnd(assignmentStatementStartContext, endContext);
    }

    static void encodeAssignmentExpressionStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.ASSIGNMENT_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentExpressionStart(
            ASTBufferRead astBuffer,
            int assignmentExpressionStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onEnterAssignmentExpression(assignmentExpressionStartContext);
    }

    static void encodeAssignmentExpressionEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ASSIGNMENT_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentExpressionEnd(
            ASTBufferRead astBuffer,
            int assignmentStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onExitAssignmentExpression(assignmentStatementStartContext, endContext);
    }

    static void encodeAssignmentLHSStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.ASSIGNMENT_EXPRESSION_LHS);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentLHSStart(
            ASTBufferRead astBuffer,
            int assignmentLHSStartContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onEnterAssignmentLHS(assignmentLHSStartContext);
    }

    static void encodeAssignmentLHSEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ASSIGNMENT_EXPRESSION_LHS);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentLHSEnd(
            ASTBufferRead astBuffer,
            int assignmentLHSStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {
        
        listener.onExitAssignmentLHS(assignmentLHSStartContext, endContext);
    }

    static void encodeExpressionStatementStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.EXPRESSION_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeExpressionStatementStart(
            ASTBufferRead astBuffer,
            int expressionStatementStartContext,
            ParserListener<COMPILATION_UNIT> listener) {


        listener.onExpressionStatementStart(expressionStatementStartContext);
    }

    static void encodeExpressionStatementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.EXPRESSION_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeExpressionStatementEnd(
            ASTBufferRead astBuffer,
            int expressionStatementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onExpressionStatementEnd(expressionStatementStartContext, endContext);
    }

    private static void encodeNames(StringASTBuffer astBuffer, long name, int nameContext) {
        
        astBuffer.writeByte((byte)1);
        
        astBuffer.writeStringRef(name);
        astBuffer.writeContextRef(nameContext);
    }

    private static void encodeNames(StringASTBuffer astBuffer, Names names) {
    
        encodeNames(astBuffer, names, names.count());
    }
    
    private static void encodeNames(StringASTBuffer astBuffer, Names names, int namesCount) {
        
        if (namesCount > Byte.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        
        astBuffer.writeByte((byte)namesCount);
        
        for (int i = 0; i < namesCount; ++ i) {
            astBuffer.writeStringRef(names.getStringAt(i));
            astBuffer.writeContextRef(names.getContextAt(i));
        }
    }

    public static <COMPILATION_UNIT> void decodeUnresolvedMethodInvocationStart(
            ASTBufferRead astBuffer,
            int methodInvocationStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final MethodInvocationType type = astBuffer.getEnumByte(index, MethodInvocationType.class);
        
        final Names names = decodeNames(astBuffer, index + 1);

        final int idx = index + 1 + namesSize(names.count());
        
        final long methodName = astBuffer.getStringRef(idx);
        final int methodNameContext = astBuffer.getContextRef(idx + STRING_REF_SIZE);

        listener.onMethodInvocationStart(
                methodInvocationStartContext,
                type,
                methodName,
                methodNameContext);
    }
    
    private static int namesSize(int count) {
        return 1 + (count * (STRING_REF_SIZE + CONTEXT_REF_SIZE));
    }
    
    private static Names decodeNames(ASTBufferRead astBuffer, int index) {
        
        int idx = index;

        final int numNames = astBuffer.getByte(idx ++);
        
        final List<NamePart> list = new ArrayList<>(numNames);

        final Names names = new Names() {
            
            @Override
            public long getStringAt(int index) {
                return list.get(index).getPart();
            }
            
            @Override
            public int getContextAt(int index) {
                return list.get(index).getContext();
            }
            
            @Override
            public int count() {
                return list.size();
            }
        };
        
        for (int i = 0; i < numNames; ++ i) {
            
            final NamePart namePart = new NamePart(
                    astBuffer.getContextRef(idx + STRING_REF_SIZE),
                    astBuffer.getStringRef(idx));
            
            list.add(namePart);
            
            idx += STRING_REF_SIZE + CONTEXT_REF_SIZE;
        }
    
        return names;
    }

    private static final int METHOD_INVOCATION_SIZE = 1 + STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeMethodInvocationStart(
            StringASTBuffer astBuffer,
            MethodInvocationType type,
            long methodName,
            int methodNameContext) {
        
        astBuffer.writeElementStart(ParseTreeElement.METHOD_INVOCATION_EXPRESSION);
        astBuffer.writeEnumByte(type);
        astBuffer.writeStringRef(methodName);
        astBuffer.writeContextRef(methodNameContext);
    }

    public static <COMPILATION_UNIT> void decodeMethodInvocationStart(
            ASTBufferRead astBuffer,
            int methodInvocationStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final MethodInvocationType type = astBuffer.getEnumByte(index, MethodInvocationType.class);
        final long methodName = astBuffer.getStringRef(index + 1);
        final int methodNameContext = astBuffer.getContextRef(index + 1 + STRING_REF_SIZE);

        listener.onMethodInvocationStart(
                methodInvocationStartContext,
                type,
                methodName,
                methodNameContext);
    }

    static void encodeMethodInvocationEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.METHOD_INVOCATION_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeMethodInvocationEnd(
            ASTBufferRead astBuffer,
            int methodInvocationStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onMethodInvocationEnd(methodInvocationStartContext, endContext);
    }

    static void encodeParametersStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.PARAMETER_LIST);
    }

    public static <COMPILATION_UNIT> void decodeParametersStart(
            ASTBufferRead astBuffer,
            int parametersStartContext,
            ParserListener<COMPILATION_UNIT> listener) {


        listener.onParametersStart(parametersStartContext);
    }

    static void encodeParametersEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.PARAMETER_LIST);
    }

    public static <COMPILATION_UNIT> void decodeParametersEnd(
            int parameterStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onParametersEnd(parameterStartContext, endContext);
    }

    static void encodeParameterStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeParameterStart(
            ASTBufferRead astBuffer,
            int parameterStartContext,
            ParserListener<COMPILATION_UNIT> listener) {


        listener.onParameterStart(parameterStartContext);
    }

    static void encodeParameterEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeParameterEnd(
            int parameterStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onParameterEnd(parameterStartContext, endContext);
    }
    
    static void encodeNestedExpressionStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.NESTED_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeNestedExpressionStart(
            ASTBufferRead astBuffer,
            int nestedExpressionStartContext,
            ParserListener<COMPILATION_UNIT> listener) {


        listener.onNestedExpressionStart(nestedExpressionStartContext);
    }

    static void encodeNestedExpressionEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.NESTED_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeNestedExpressionEnd(
            int nestedExpressionStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNestedExpressionEnd(nestedExpressionStartContext, endContext);
    }

    static void encodePrimariesStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.PRIMARY_LIST);
    }

    public static <COMPILATION_UNIT> void decodePrimariesStart(
            ASTBufferRead astBuffer,
            int primariesStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onPrimaryStart(primariesStartContext);
    }

    static void encodePrimariesEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.PRIMARY_LIST);
    }

    public static <COMPILATION_UNIT> void decodePrimariesEnd(
            ASTBufferRead astBuffer,
            int primariesStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onPrimaryEnd(primariesStartContext, endContext);
    }
    
    private static final int FIELD_ACCESS_SIZE = STRING_REF_SIZE;

    static void encodeFieldAccess(StringASTBuffer astBuffer, long fieldName) {
        
        astBuffer.writeElementStart(ParseTreeElement.FIELD_ACCESS);
        astBuffer.writeStringRef(fieldName);
    }

    public static <COMPILATION_UNIT> void decodeFieldAccess(
            ASTBufferRead astBuffer,
            int fieldAccessStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onFieldAccess(
                fieldAccessStartContext,
                FieldAccessType.FIELD,
                null,
                null,
                astBuffer.getStringRef(index),
                fieldAccessStartContext);
    }

    static void encodeAnnotationStart(StringASTBuffer astBuffer, Names names) {
        
        astBuffer.writeElementStart(ParseTreeElement.ANNOTATION);
        
        encodeNames(astBuffer, names);
    }

    public static <COMPILATION_UNIT> void decodeAnnotationStart(
            ASTBufferRead astBuffer,
            int annotationStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final Names names = decodeNames(astBuffer, index);

        listener.onAnnotationStart(annotationStartContext, names);
    }

    static void encodeAnnotationEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ANNOTATION);
    }

    public static <COMPILATION_UNIT> void decodeAnnotationEnd(
            int annotationStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onAnnotationEnd(annotationStartContext, endContext);
    }

    private static final int ANNOTATION_ELEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    static void encodeAnnotationElementStart(StringASTBuffer astBuffer, long name, int nameContext) {
        
        astBuffer.writeElementStart(ParseTreeElement.ANNOTATION_ELEMENT);
        
        if (name != StringRef.STRING_NONE) {
            astBuffer.writeStringRef(name);
            astBuffer.writeContextRef(nameContext);
        }
        else {
            astBuffer.writeNoStringRef();
            astBuffer.writeNoContextRef();
        }
    }

    public static <COMPILATION_UNIT> void decodeAnnotationElementStart(
            ASTBufferRead astBuffer,
            int annotationElementStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onAnnotationElementStart(
                annotationElementStartContext,
                astBuffer.hasContextRef(index) ? astBuffer.getStringRef(index) : StringRef.STRING_NONE,
                astBuffer.getContextRef(index + STRING_REF_SIZE));
    }

    static void encodeAnnotationElementEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.ANNOTATION_ELEMENT);
    }

    public static <COMPILATION_UNIT> void decodeAnnotationElementEnd(
            int annotationElementStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onAnnotationElementEnd(annotationElementStartContext, endContext);
    }

    private static final int NAME_PRIMARY_SIZE = STRING_REF_SIZE;
    
    static void encodeNamePrimary(StringASTBuffer astBuffer, long name, int nameContext) {
        
        astBuffer.writeLeafElement(ParseTreeElement.NAME_PRIMARY);
        
        if (name == StringRef.STRING_NONE) {
            throw new IllegalArgumentException();
        }
            
        astBuffer.writeStringRef(name);
    }

    public static <COMPILATION_UNIT> void decodeNamePrimary(
            ASTBufferRead astBuffer,
            int namePrimaryStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNamePrimary(
                namePrimaryStartContext,
                astBuffer.getStringRef(index));
    }

    private static final int NAME_REFERENCE_SIZE = STRING_REF_SIZE;
    
    static void encodeNameReference(StringASTBuffer astBuffer, long name, int nameContext) {
        
        astBuffer.writeLeafElement(ParseTreeElement.NAME_REFERENCE);
        
        if (name == StringRef.STRING_NONE) {
            throw new IllegalArgumentException();
        }
            
        astBuffer.writeStringRef(name);
    }

    public static <COMPILATION_UNIT> void decodeNameReference(
            ASTBufferRead astBuffer,
            int nameReferenceStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNameReference(
                nameReferenceStartContext,
                astBuffer.getStringRef(index));
    }

    static void encodeTypeArgumentListStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.TYPE_ARGUMENT_LIST);
    }

    public static <COMPILATION_UNIT> void decodeTypeArgumentListStart(
            ASTBufferRead astBuffer,
            int typeArgumentListStartContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onGenericTypeArgumentsStart(typeArgumentListStartContext);
    }

    static void encodeTypeArgumentListEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.TYPE_ARGUMENT_LIST);
    }

    public static <COMPILATION_UNIT> void decodeTypeArgumentListEnd(
            int typeArgumentListStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onGenericTypeArgumentsEnd(typeArgumentListStartContext, endContext);
    }

    static void encodeReferenceTypeArgumentStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.REFERENCE_GENERIC_TYPE_ARGUMENT);
    }

    public static <COMPILATION_UNIT> void decodeReferenceTypeArgumentStart(
            ASTBufferRead astBuffer,
            int referenceTypeArgumentStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {


        listener.onGenericReferenceTypeArgumentStart(referenceTypeArgumentStartContext);
    }

    static void encodeReferenceTypeArgumentEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.REFERENCE_GENERIC_TYPE_ARGUMENT);
    }

    public static <COMPILATION_UNIT> void decodeReferenceTypeArgumentEnd(
            int referenceTypeArgumentStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onGenericReferenceTypeArgumentEnd(referenceTypeArgumentStartContext, endContext);
    }

    static void encodeWildcardTypeArgumentStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.WILDCARD_GENERIC_TYPE_ARGUMENT);
    }

    public static <COMPILATION_UNIT> void decodeWildcardTypeArgumentStart(
            ASTBufferRead astBuffer,
            int wildcardTypeArgumentStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {


        listener.onGenericWildcardTypeArgumentStart(wildcardTypeArgumentStartContext);
    }

    static void encodeWildcardTypeArgumentEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.WILDCARD_GENERIC_TYPE_ARGUMENT);
    }

    public static <COMPILATION_UNIT> void decodeWildcardTypeArgumentEnd(
            int wildcardTypeArgumentStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onGenericWildcardTypeArgumentEnd(wildcardTypeArgumentStartContext, endContext);
    }

    private static final int TYPE_BOUND_SIZE = 1;

    static void encodeTypeBoundStart(StringASTBuffer astBuffer, TypeBoundType type) {
        
        astBuffer.writeElementStart(ParseTreeElement.TYPE_BOUND);
        astBuffer.writeEnumByte(type);
    }

    public static <COMPILATION_UNIT> void decodeTypeBoundStart(
            ASTBufferRead astBuffer,
            int typeBoundStartContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        final TypeBoundType type = astBuffer.getEnumByte(index, TypeBoundType.class);

        listener.onTypeBoundStart(typeBoundStartContext, type);
    }

    static void encodeTypeBoundEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.TYPE_BOUND);
    }

    public static <COMPILATION_UNIT> void decodeTypeBoundEnd(
            int typeBoundStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onTypeBoundEnd(typeBoundStartContext, endContext);
    }

    static void encodeGenericTypeParametersStart(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementStart(ParseTreeElement.GENERIC_TYPE_PARAMETERS);
    }

    public static <COMPILATION_UNIT> void decodeGenericTypeParametersStart(
            ASTBufferRead astBuffer,
            int parameterStartContext,
            ParserListener<COMPILATION_UNIT> listener) {


        listener.onGenericTypeParametersStart(parameterStartContext);
    }

    private static final int NAMED_GENERIC_TYPE_PARAMETER_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    static void encodeNamedGenericTypeParameterStart(StringASTBuffer astBuffer, long name, int nameContext) {
        
        astBuffer.writeElementStart(ParseTreeElement.NAMED_GENERIC_TYPE_PARAMETER);
        astBuffer.writeStringRef(name);
        astBuffer.writeContextRef(nameContext);
    }

    public static <COMPILATION_UNIT> void decodeNamedGenericTypeParameterStart(
            ASTBufferRead astBuffer,
            int parameterStartContext,
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


        listener.onNamedGenericTypeParameterStart(
                parameterStartContext,
                astBuffer.getStringRef(index),
                extendsKeywordContext);
    }

    static void encodeNamedGenericTypeParameterEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.NAMED_GENERIC_TYPE_PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeNamedGenericTypeParameterEnd(
            int parameterStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onNamedGenericTypeParameterEnd(parameterStartContext, endContext);
    }

    static void encodeGenericTypeParametersEnd(StringASTBuffer astBuffer) {
        
        astBuffer.writeElementEnd(ParseTreeElement.GENERIC_TYPE_PARAMETERS);
    }

    public static <COMPILATION_UNIT> void decodeGenericTypeParametersEnd(
            int parameterStartContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onGenericTypeParametersEnd(parameterStartContext, endContext);
    }
    
    private static int fieldModifierSize(ASTBufferRead astBuffer, int index) {

        final FieldModifier.Type type = astBuffer.getEnumByte(index, FieldModifier.Type.class);

        final int size;
        
        switch (type) {
        case MUTABILITY:
        case VISIBILITY:
            size = 1 + 1;
            break;
            
        case STATIC:
            size = 1;
            break;
            
        default:
            throw new UnsupportedOperationException();
        }
        
        return size;
    }

    static void encodeVisibilityFieldModifier(StringASTBuffer astBuffer, FieldVisibility fieldVisibility) {

        astBuffer.writeLeafElement(ParseTreeElement.FIELD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(FieldModifier.Type.VISIBILITY);
        astBuffer.writeEnumByte(fieldVisibility.getVisibility());
    }

    static void encodeMutabilityModifier(StringASTBuffer astBuffer, ASTMutability mutability) {

        astBuffer.writeLeafElement(ParseTreeElement.FIELD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(FieldModifier.Type.MUTABILITY);
        astBuffer.writeEnumByte(mutability.getMutability());
    }

    static void encodeStaticFieldModifier(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.FIELD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(FieldModifier.Type.STATIC);
    }

    public static <COMPILATION_UNIT> void decodeFieldModifierHolder(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {
        
        final FieldModifier.Type type = astBuffer.getEnumByte(index, FieldModifier.Type.class);
        
        switch (type) {
        case VISIBILITY:
            listener.onVisibilityFieldModifier(
                    leafContext,
                    new FieldVisibility(astBuffer.getEnumByte(index + 1, Visibility.class)));
            break;
            
        case MUTABILITY:
            listener.onMutabilityFieldModifier(
                    leafContext,
                    new ASTMutability(astBuffer.getEnumByte(index + 1, Mutability.class)));
            break;

        case STATIC:
            listener.onStaticFieldModifier(leafContext);
            break;
            
         default:
             throw new UnsupportedOperationException();
        }
    }
    
    static void encodeClassInstanceCreationExpressionStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeClassInstanceCreationExpressionStart(
            int startContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassInstanceCreationExpressionStart(startContext);
    }

    
    static void encodeClassInstanceCreationTypeAndConstructorName(StringASTBuffer astBuffer, long name, int nameContext) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION_NAME);
        
        encodeNames(astBuffer, name, nameContext);
    }

    public static <COMPILATION_UNIT> void decodeClassInstanceCreationTypeAndConstructorName(
            ASTBufferRead astBuffer,
            int startContext,
            int index,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassInstanceCreationTypeAndConstructorName(startContext, decodeNames(astBuffer, index));
    }

    static void encodeClassInstanceCreationExpressionEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeClassInstanceCreationExpressionEnd(
            int startContext,
            Context endContext,
            ParserListener<COMPILATION_UNIT> listener) {

        listener.onClassInstanceCreationExpressionEnd(startContext, endContext);
    }
}
