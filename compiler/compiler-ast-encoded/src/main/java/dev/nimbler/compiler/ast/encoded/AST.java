package dev.nimbler.compiler.ast.encoded;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.encoded.ASTBufferRead.ParseTreeElementRef;
import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.parser.listener.common.ParseTreeListener;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.ReferenceType;
import dev.nimbler.compiler.types.operator.Arithmetic;
import dev.nimbler.compiler.types.operator.Assignment;
import dev.nimbler.compiler.types.operator.Bitwise;
import dev.nimbler.compiler.types.operator.IncrementDecrement;
import dev.nimbler.compiler.types.operator.Logical;
import dev.nimbler.compiler.types.operator.Operator;
import dev.nimbler.compiler.types.operator.OperatorType;
import dev.nimbler.compiler.types.operator.Relational;
import dev.nimbler.compiler.types.statement.ASTMutability;
import dev.nimbler.compiler.types.typedefinition.ClassMethodModifier;
import dev.nimbler.compiler.types.typedefinition.ClassMethodOverride;
import dev.nimbler.compiler.types.typedefinition.ClassMethodVisibility;
import dev.nimbler.compiler.types.typedefinition.ClassModifier;
import dev.nimbler.compiler.types.typedefinition.ClassVisibility;
import dev.nimbler.compiler.types.typedefinition.FieldModifier;
import dev.nimbler.compiler.types.typedefinition.FieldVisibility;
import dev.nimbler.compiler.types.typedefinition.Subclassing;
import dev.nimbler.compiler.types.typedefinition.TypeBoundType;
import dev.nimbler.compiler.types.typedefinition.VariableModifier;
import dev.nimbler.compiler.util.Base;
import dev.nimbler.compiler.util.ContextRef;
import dev.nimbler.compiler.util.name.Names;
import dev.nimbler.compiler.util.parse.FieldAccessType;
import dev.nimbler.compiler.util.parse.NamePart;
import dev.nimbler.language.common.types.Mutability;
import dev.nimbler.language.common.types.Visibility;

public class AST {

    private static final int STRING_REF_SIZE = 4;
    private static final int CONTEXT_REF_SIZE = 4;

    static final int NO_STRINGREF = -1;
    static final int NO_CONTEXTREF = -1;

    private static final int CONTEXT_SIZE = 8;

    public static int writeContext(ASTBuffer contextBuffer, Context context) {

        if (context.getStartOffset() > 100000) {
            throw new IllegalArgumentException();
        }

        Objects.requireNonNull(context);

        final int writePos = contextBuffer.getWritePos();

        contextBuffer.writeInt(context.getStartOffset());
        contextBuffer.writeInt(context.getEndOffset());

        return writePos;
    }

    public static int writeContext(ASTBuffer contextBuffer, int otherContext) {

        final int writePos = contextBuffer.getWritePos();

        contextBuffer.appendFrom(otherContext, CONTEXT_SIZE);

        return writePos;
    }
    
    public static int index(int parseTreeRef) {
        return parseTreeRef + 1;
    }

    public static int sizeStart(ParseTreeElementRef ref, ASTBufferRead astBuffer) {
        
        return sizeStart(ref.element, astBuffer, ref.index);
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

        case INTERFACE_DEFINITION:
            size = INTERFACE_START_SIZE;
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

        case SWITCH_CASE_STATEMENT:
            size = SWITCH_STATEMENT_SIZE;
            break;
            
        case CONSTANT_SWITCH_CASE_LABEL:
            size = CONSTANT_SWITCH_CASE_LABEL_SIZE;
            break;

        case ENUM_SWITCH_CASE_LABEL:
            size = ENUM_SWITCH_CASE_LABEL_SIZE;
            break;
            
        case DEFAULT_SWITCH_CASE_LABEL:
            size = DEFAULT_SWITCH_CASE_LABEL_SIZE;
            break;
            
        case BREAK_STATEMENT:
            size = BREAK_STATEMENT_SIZE;
            break;

        case NAME_REFERENCE:
            size = NAME_REFERENCE_SIZE;
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

        case FOR_STATEMENT:
            size = FOR_STATEMENT_SIZE;
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

        case UNRESOLVED_METHOD_INVOCATION_EXPRESSION:
            size = UNRESOLVED_METHOD_INVOCATION_SIZE;
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

        case UNRESOLVED_NAME_PRIMARY:
            size = UNRESOLVED_NAME_PRIMARY_SIZE;
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

        case RESOLVED_TYPE_REFERENCE:
            size = RESOLVED_TYPE_REFERENCE_SIZE;
            break;

        case REPLACE:
            size = ASTBufferImpl.REPLACE_SIZE;
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

    public static int sizeLeaf(ParseTreeElement element, ASTBufferRead astBuffer, int index) {

        return sizeStart(element, astBuffer, index);
    }

    private static final int IMPORT_START_SIZE = 2 * (STRING_REF_SIZE + CONTEXT_REF_SIZE);
    private static final int IMPORT_END_SIZE = 1;
    private static final int IMPORT_PART_SIZE = STRING_REF_SIZE;

    public static void encodeImportStart(
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
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeImportNamePart(StringASTBuffer astBuffer, long identifier) {

        astBuffer.writeLeafElement(ParseTreeElement.IMPORT_NAME_PART);

        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeImportNamePart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onImportName(leafContext, astBuffer.getStringRef(index));
    }

    public static void encodeImportEnd(StringASTBuffer astBuffer, boolean onDemand) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPORT);

        astBuffer.writeBoolean(onDemand);
    }

    public static <COMPILATION_UNIT> void decodeImportEnd(
            ASTBufferRead astBuffer,
            int importStartContext,
            Context endContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onImportEnd(importStartContext, endContext, astBuffer.getBoolean(index));
    }

    public static void encodeNamespaceStart(StringASTBuffer astBuffer, long namespaceKeyword, int namespaceKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.NAMESPACE);

        astBuffer.writeStringRef(namespaceKeyword);
        astBuffer.writeContextRef(namespaceKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeNamespaceStart(
            ASTBufferRead astBuffer,
            int namespaceStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeNamespacePart(StringASTBuffer astBuffer, long part) {

        astBuffer.writeLeafElement(ParseTreeElement.NAMESPACE_PART);

        astBuffer.writeStringRef(part);
    }

    public static int decodeNamespacePart(ASTBufferRead astBuffer, int index) {

        return astBuffer.getStringRef(index);
    }

    public static <COMPILATION_UNIT> void decodeNamespacePart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onNamespacePart(leafContext, decodeNamespacePart(astBuffer, index));
    }

    public static void encodeNamespaceEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.NAMESPACE);
    }

    public static <COMPILATION_UNIT> void decodeNamespaceEnd(
            int namespaceStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onNameSpaceEnd(namespaceStartContext, endContext);
    }

    public static void encodeTypeDefinitionStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.TYPE_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeTypeDefinitionStart(
            ASTBufferRead astBuffer,
            int typeDefinitionStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onTypeDefinitionStart(typeDefinitionStartContext);
    }

    public static void encodeTypeDefinitionEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.TYPE_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeTypeDefinitionEnd(
            int typeDefinitionStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onTypeDefinitionEnd(typeDefinitionStartContext, endContext);
    }

    private static final int CLASS_START_SIZE = 2 * (STRING_REF_SIZE + CONTEXT_REF_SIZE);

    public static void encodeClassStart(StringASTBuffer astBuffer, long classKeyword, int classKeywordContext, long name, int nameContext) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_DEFINITION);

        astBuffer.writeStringRef(classKeyword);
        astBuffer.writeContextRef(classKeywordContext);

        astBuffer.writeStringRef(name);
        astBuffer.writeContextRef(nameContext);
    }
    
    public static int decodeClassName(ASTBufferRead astBuffer, int index) {
        return astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE);
    }

    public static <COMPILATION_UNIT> void decodeClassStart(
            ASTBufferRead astBuffer,
            int classStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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
                decodeClassName(astBuffer, index),
                nameContext);
    }

    public static void encodeClassEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeClassEnd(
            ASTBufferRead astBuffer,
            int classStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onClassEnd(classStartContext, endContext);
    }

    private static final int CLASS_MODIFIER_SIZE = 1 + 1;

    public static void encodeVisibilityClassModifier(StringASTBuffer astBuffer, ClassVisibility classVisibility) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassModifier.Type.VISIBILITY);
        astBuffer.writeEnumByte(classVisibility);
    }

    public static void encodeSubclassingModifier(StringASTBuffer astBuffer, Subclassing subclassing) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassModifier.Type.SUBCLASSING);
        astBuffer.writeEnumByte(subclassing);
    }

    public static <COMPILATION_UNIT> void decodeClassModifierHolder(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeClassExtendsStart(StringASTBuffer astBuffer, long extendsKeyword, int extendsKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_EXTENDS);
        astBuffer.writeStringRef(extendsKeyword);
        astBuffer.writeContextRef(extendsKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeClassExtendsStart(
            ASTBufferRead astBuffer,
            int classExtendsStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeClassExtendsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_EXTENDS);
    }

    public static <COMPILATION_UNIT> void decodeClassExtendsEnd(
            ASTBufferRead astBuffer,
            int classExtendsStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onClassExtendsEnd(classExtendsStartContext, endContext);
    }

    private static final int CLASS_IMPLEMENTS_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeImplementsStart(StringASTBuffer astBuffer, long implementsKeyword, int implementsKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.IMPLEMENTS);
        astBuffer.writeStringRef(implementsKeyword);
        astBuffer.writeContextRef(implementsKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeImplementsStart(
            ASTBufferRead astBuffer,
            int classImplementsStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeImplementsTypeStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.IMPLEMENTS_TYPE);

    }

    public static void encodeImplementsTypeEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPLEMENTS_TYPE);

    }

    private static final int IMPLEMENTS_NAME_PART_SIZE = STRING_REF_SIZE;

    public static void encodeImplementsNamePart(StringASTBuffer astBuffer, long identifier) {

        astBuffer.writeLeafElement(ParseTreeElement.IMPLEMENTS_NAME_PART);

        astBuffer.writeStringRef(identifier);
    }

    public static <COMPILATION_UNIT> void decodeImplementsNamePart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onImplementsNamePart(leafContext, astBuffer.getStringRef(index));
    }

    public static void encodeImplementsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.IMPLEMENTS);
    }

    public static <COMPILATION_UNIT> void decodeImplementsEnd(
            ASTBufferRead astBuffer,
            int classImplementsStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onImplementsEnd(classImplementsStartContext, endContext);
    }

    public static <COMPILATION_UNIT> void decodeImplementsTypeStart(
            ASTBufferRead astBuffer,
            int classImplementsTypeStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onImplementsTypeStart(classImplementsTypeStartContext);
    }

    public static <COMPILATION_UNIT> void decodeImplementsTypeEnd(
            ASTBufferRead astBuffer,
            int classImplementsTypeStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onImplementsTypeEnd(classImplementsTypeStartContext, endContext);
    }

    public static void encodeFieldDeclarationStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_DATA_FIELD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeFieldDeclarationStart(
            ASTBufferRead astBuffer,
            int fieldDeclarationStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onFieldDeclarationStart(fieldDeclarationStartContext);
    }

    public static void encodeFieldDeclarationEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_DATA_FIELD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeFieldDeclarationEnd(
            ASTBufferRead astBuffer,
            int fieldDeclarationStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onFieldDeclarationEnd(fieldDeclarationStartContext, endContext);
    }

    private static final int SCALAR_TYPE_REFERENCE_SIZE = STRING_REF_SIZE;

    public static void encodeScalarTypeReference(StringASTBuffer astBuffer, long typeName) {

        astBuffer.writeElementStart(ParseTreeElement.SCALAR_TYPE_REFERENCE);

        astBuffer.writeStringRef(typeName);
    }

    public static <COMPILATION_UNIT> void decodeScalarTypeReference(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onLeafTypeReference(leafContext, astBuffer.getStringRef(index), ReferenceType.SCALAR);
    }

    private static final int IDENTIFIER_TYPE_REFERENCE_SIZE = STRING_REF_SIZE;

    public static void encodeIdentifierTypeReferenceStart(StringASTBuffer astBuffer, long typeName) {

        astBuffer.writeElementStart(ParseTreeElement.UNRESOLVED_IDENTIFIER_TYPE_REFERENCE);

        astBuffer.writeStringRef(typeName);
    }

    public static int decodeIdentifierTypeReferenceName(ASTBufferRead astBuffer, int index) {
        return astBuffer.getStringRef(index);
    }

    public static <COMPILATION_UNIT> void decodeIdentifierTypeReferenceStart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onNonScopedTypeReferenceStart(
                leafContext,
                decodeIdentifierTypeReferenceName(astBuffer, index),
                ReferenceType.REFERENCE);
    }

    public static void encodeIdentifierTypeReferenceEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.UNRESOLVED_IDENTIFIER_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeIdentifierTypeReferenceEnd(
            int leafContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onNonScopedTypeReferenceEnd(leafContext, endContext);
    }

    public static void encodeVariableDeclaratorStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.VARIABLE_DECLARATOR);
    }

    public static <COMPILATION_UNIT> void decodeVariableDeclaratorStart(
            ASTBufferRead astBuffer,
            int variableDeclaratorStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onVariableDeclaratorStart(variableDeclaratorStartContext);
    }

    public static void encodeVariableDeclaratorEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.VARIABLE_DECLARATOR);
    }

    public static <COMPILATION_UNIT> void decodeVariableDeclaratorEnd(
            ASTBufferRead astBuffer,
            int variableDeclaratorStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onVariableDeclaratorEnd(variableDeclaratorStartContext, endContext);
    }

    private static final int VARIABLE_NAME_SIZE = STRING_REF_SIZE + 4;

    public static void encodeVariableName(StringASTBuffer astBuffer, long name, int numDims) {

        astBuffer.writeLeafElement(ParseTreeElement.VAR_NAME_DECLARATION);

        astBuffer.writeStringRef(name);
        astBuffer.writeInt(numDims);
    }
    
    public static int decodeVariable(ASTBufferRead astBuffer, int index) {
        
        return astBuffer.getStringRef(index);
    }

    public static <COMPILATION_UNIT> void decodeVariableName(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onVariableName(
                leafContext,
                decodeVariable(astBuffer, index),
                astBuffer.getInt(index + 4));
    }

    public static void encodeConstructorStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CONSTRUCTOR_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeConstructorStart(
            ASTBufferRead astBuffer,
            int constructorStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onConstructorStart(constructorStartContext);
    }

    private static final int CONSTRUCTOR_NAME_SIZE = STRING_REF_SIZE;

    public static void encodeConstructorName(StringASTBuffer astBuffer, long constructorName) {

        astBuffer.writeLeafElement(ParseTreeElement.METHOD_NAME);

        astBuffer.writeStringRef(constructorName);
    }

    public static <COMPILATION_UNIT> void decodeConstructorName(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onConstructorName(leafContext, astBuffer.getStringRef(index));
    }

    public static void encodeConstructorEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CONSTRUCTOR_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeConstructorEnd(
            int constructorStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onConstructorEnd(constructorStartContext, endContext);
    }

    public static void encodeClassMethodStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_METHOD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeClassMethodStart(
            ASTBufferRead astBuffer,
            int classMethodStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeVisibilityClassMethodModifier(StringASTBuffer astBuffer, ClassMethodVisibility classMethodVisibility) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_METHOD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassMethodModifier.Type.VISIBILITY);
        astBuffer.writeEnumByte(classMethodVisibility);
    }

    public static void encodeStaticClassMethodModifier(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_METHOD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassMethodModifier.Type.STATIC);
    }

    public static void encodeOverrideClassMethodModifier(StringASTBuffer astBuffer, ClassMethodOverride classMethodOverride) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_METHOD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(ClassMethodModifier.Type.OVERRIDE);
        astBuffer.writeEnumByte(classMethodOverride);
    }
    
    public static ClassMethodModifier.Type decodeClassMethodModifierType(ASTBufferRead astBuffer, int index) {

        return astBuffer.getEnumByte(index, ClassMethodModifier.Type.class);
    }

    public static ClassMethodVisibility decodeClassMethodVisibility(ASTBufferRead astBuffer, int index) {
        
        return astBuffer.getEnumByte(index + 1, ClassMethodVisibility.class);
    }
    
    public static ClassMethodOverride decodeClassMethodOverride(ASTBufferRead astBuffer, int index) {
        
        return astBuffer.getEnumByte(index + 1, ClassMethodOverride.class);
    }
    
    public static <COMPILATION_UNIT> void decodeClassMethodModifierHolder(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        final ClassMethodModifier.Type type = decodeClassMethodModifierType(astBuffer, index);

        switch (type) {
        case VISIBILITY:
            listener.onVisibilityClassMethodModifier(leafContext, decodeClassMethodVisibility(astBuffer, index));
            break;

        case STATIC:
            listener.onStaticClassMethodModifier(leafContext);
            break;

        case OVERRIDE:
            listener.onOverrideClassMethodModifier(leafContext, decodeClassMethodOverride(astBuffer, index));
            break;

         default:
             throw new UnsupportedOperationException();
        }
    }


    public static void encodeMethodReturnTypeStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.METHOD_RETURN_TYPE);

    }

    public static <COMPILATION_UNIT> void decodeMethodReturnTypeStart(
            ASTBufferRead astBuffer,
            int methodReturnTypeContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodReturnTypeStart(methodReturnTypeContext);
    }

    public static void encodeMethodReturnTypeEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.METHOD_RETURN_TYPE);
    }

    public static <COMPILATION_UNIT> void decodeMethodReturnTypeEnd(
            ASTBufferRead astBuffer,
            int methodReturnTypeStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodReturnTypeEnd(methodReturnTypeStartContext, endContext);
    }

    private static final int METHOD_NAME_SIZE = STRING_REF_SIZE;

    public static void encodeMethodName(StringASTBuffer astBuffer, long methodName) {

        astBuffer.writeLeafElement(ParseTreeElement.METHOD_NAME);

        astBuffer.writeStringRef(methodName);
    }

    public static <COMPILATION_UNIT> void decodeMethodName(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodName(leafContext, astBuffer.getStringRef(index));
    }

    public static void encodeClassMethodEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_METHOD_MEMBER);
    }

    public static <COMPILATION_UNIT> void decodeClassMethodEnd(
            ASTBufferRead astBuffer,
            int classMethodStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onClassMethodEnd(classMethodStartContext, endContext);
    }

    public static void encodeSignatureParametersStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.SIGNATURE_PARAMETERS);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParametersStart(
            ASTBufferRead astBuffer,
            int signatureParametersStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodSignatureParametersStart(signatureParametersStartContext);
    }

    public static void encodeSignatureParametersEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.SIGNATURE_PARAMETERS);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParametersEnd(
            ASTBufferRead astBuffer,
            int signatureParametersStartContext,
            Context endContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodSignatureParametersEnd(signatureParametersStartContext, endContext);
    }

    private static final int SIGNATURE_PARAMETER_SIZE = 1;

    public static void encodeSignatureParameterStart(StringASTBuffer astBuffer, boolean varArgs) {

        astBuffer.writeElementStart(ParseTreeElement.SIGNATURE_PARAMETER);

        astBuffer.writeBoolean(varArgs);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParameterStart(
            ASTBufferRead astBuffer,
            int signatureParameterStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodSignatureParameterStart(signatureParameterStartContext, astBuffer.getBoolean(index));
    }

    public static void encodeSignatureParameterVarargs(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.SIGNATURE_PARAMETER_VARARGS);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParameterVarargs(
            ASTBufferRead astBuffer,
            int signatureParameterVarargsContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodSignatureParameterVarargs(signatureParameterVarargsContext);
    }

    public static void encodeSignatureParameterEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.SIGNATURE_PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeSignatureParameterEnd(
            ASTBufferRead astBuffer,
            int signatureParameterStartContext,
            Context endContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodSignatureParameterEnd(signatureParameterStartContext, endContext);
    }

    public static void encodeThrowsStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.THROWS);
    }

    public static <COMPILATION_UNIT> void decodeThrowsStart(
            int throwsStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onThrowsStart(throwsStartContext);
    }

    public static void encodeThrowsEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.THROWS);
    }

    public static <COMPILATION_UNIT> void decodeThrowsEnd(
            int throwsStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onThrowsEnd(throwsStartContext, endContext);
    }

    private static final int ENUM_DEFINITION_SIZE = 2 * (STRING_REF_SIZE + CONTEXT_REF_SIZE);

    public static void encodeEnumStart(StringASTBuffer astBuffer, long enumKeyword, int enumKeywordContext, long name, int nameContext) {

        astBuffer.writeElementStart(ParseTreeElement.ENUM_DEFINITION);

        astBuffer.writeStringRef(enumKeyword);
        astBuffer.writeContextRef(enumKeywordContext);

        astBuffer.writeStringRef(name);
        astBuffer.writeContextRef(nameContext);
    }

    public static int decodeEnumName(ASTBufferRead astBuffer, int index) {
        return astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE);
    }

    public static <COMPILATION_UNIT> void decodeEnumStart(
            ASTBufferRead astBuffer,
            int enumStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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
                decodeEnumName(astBuffer, index),
                nameContext);
    }

    private static final int ENUM_CONSTANT_DEFINITION_SIZE = STRING_REF_SIZE;

    public static void encodeEnumConstantStart(StringASTBuffer astBuffer, long name) {

        astBuffer.writeElementStart(ParseTreeElement.ENUM_CONSTANT_DEFINITION);

        astBuffer.writeStringRef(name);
    }

    public static <COMPILATION_UNIT> void decodeEnumConstantStart(
            ASTBufferRead astBuffer,
            int enumConstantStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onEnumConstantStart(enumConstantStartContext, astBuffer.getStringRef(index));
    }

    public static void encodeEnumConstantEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ENUM_CONSTANT_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeEnumConstantEnd(
            ASTBufferRead astBuffer,
            int enumConstantStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onEnumConstantEnd(enumConstantStartContext, endContext);
    }


    public static void encodeEnumEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ENUM_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeEnumEnd(
            ASTBufferRead astBuffer,
            int enumStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onEnumEnd(enumStartContext, endContext);
    }


    private static final int INTERFACE_START_SIZE = 2 * (STRING_REF_SIZE + CONTEXT_REF_SIZE);

    public static void encodeInterfaceStart(
            StringASTBuffer astBuffer,
            long interfaceKeyword, int interfaceKeywordContext,
            long name, int nameContext) {

        astBuffer.writeElementStart(ParseTreeElement.INTERFACE_DEFINITION);

        astBuffer.writeStringRef(interfaceKeyword);
        astBuffer.writeContextRef(interfaceKeywordContext);

        astBuffer.writeStringRef(name);
        astBuffer.writeContextRef(nameContext);
    }

    public static int decodeInterfaceName(ASTBufferRead astBuffer, int index) {
        return astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE);
    }

    public static <COMPILATION_UNIT> void decodeInterfaceStart(
            ASTBufferRead astBuffer,
            int interfaceStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        final int interfaceKeywordContext;
        final int nameContext;

        if (contextGetter != null) {

            interfaceKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
            nameContext = astBuffer.getContextRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE + STRING_REF_SIZE);
        }
        else {
            interfaceKeywordContext = ContextRef.NONE;
            nameContext = ContextRef.NONE;
        }

        listener.onInterfaceStart(
                interfaceStartContext,
                astBuffer.getStringRef(index),
                interfaceKeywordContext,
                decodeInterfaceName(astBuffer, index),
                nameContext);
    }

    public static void encodeInterfaceEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.INTERFACE_DEFINITION);
    }

    public static <COMPILATION_UNIT> void decodeInterfaceEnd(
            ASTBufferRead astBuffer,
            int interfaceStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onInterfaceEnd(interfaceStartContext, endContext);
    }

    public static void encodeScopedTypeReferenceStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceStart(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onScopedTypeReferenceStart(scopedTypeReferenceStartContext, ReferenceType.REFERENCE);
    }

    public static void encodeScopedTypeReferenceNameStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceNameStart(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceNameStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onScopedTypeReferenceNameStart(scopedTypeReferenceNameStartContext);
    }

    private static final int SCOPED_TYPE_REFERENCE_PART_SIZE = STRING_REF_SIZE;


    public static void encodeScopedTypeReferencePart(StringASTBuffer astBuffer, long part) {

        astBuffer.writeLeafElement(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME_PART);

        astBuffer.writeStringRef(part);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferencePart(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onScopedTypeReferenceNamePart(leafContext, astBuffer.getStringRef(index));
    }

    public static void encodeScopedTypeReferenceNameEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceNameEnd(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceNameEndContext,
            Context endContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onScopedTypeReferenceNameEnd(scopedTypeReferenceNameEndContext, endContext);
    }

    public static void encodeScopedTypeReferenceEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE);
    }

    public static <COMPILATION_UNIT> void decodeScopedTypeReferenceEnd(
            ASTBufferRead astBuffer,
            int scopedTypeReferenceEndContext,
            Context endContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onScopedTypeReferenceEnd(scopedTypeReferenceEndContext, endContext);
    }

    private static final int RESOLVED_TYPE_REFERENCE_SIZE = 4;

    public static void encodeResolvedTypeReference(ASTBuffer astBuffer, int typeNo) {

        astBuffer.writeLeafElement(ParseTreeElement.RESOLVED_TYPE_REFERENCE);

        astBuffer.writeInt(typeNo);
    }

    public static int decodeResolvedTypeReferenceTypeNo(ASTBufferRead astBuffer, int index) {

        return astBuffer.getInt(index);
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

    public static void encodeMutabilityVariableModifier(StringASTBuffer astBuffer, ASTMutability mutability) {

        astBuffer.writeLeafElement(ParseTreeElement.VARIABLE_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(VariableModifier.Type.MUTABILITY);
        astBuffer.writeEnumByte(mutability.getMutability());
    }

    public static <COMPILATION_UNIT> void decodeVariableModifierHolder(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeVariableDeclarationStatementStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.VARIABLE_DECLARATION_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeVariableDeclarationStatementStart(
            ASTBufferRead astBuffer,
            int variableDeclarationStatementStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onVariableDeclarationStatementStart(variableDeclarationStatementStartContext);
    }

    public static void encodeVariableDeclarationStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.VARIABLE_DECLARATION_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeVariableDeclarationStatementEnd(
            ASTBufferRead astBuffer,
            int variableDeclarationStatementStartContext,
            Context context,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onVariableDeclarationStatementEnd(variableDeclarationStatementStartContext, context);
    }

    private static final int IF_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeIfElseIfElseStatementStart(StringASTBuffer astBuffer, long ifKeyword, int ifKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.IF_ELSE_IF_ELSE_STATEMENT);

        astBuffer.writeStringRef(ifKeyword);
        astBuffer.writeContextRef(ifKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeIfElseIfElseStatementStart(
            ASTBufferRead astBuffer,
            int ifStartContext,
            ContextGetter contextGetter,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        final int ifKeywordContext;

        if (contextGetter != null) {
            ifKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            ifKeywordContext = ContextRef.NONE;
        }

        listener.onIfStatementStart(ifStartContext, astBuffer.getStringRef(index), ifKeywordContext);
    }

    public static void encodeIfElseIfElseStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.IF_ELSE_IF_ELSE_STATEMENT);
    }

    public static <COMPILATION_UNIT >void decodeIfElseIfElseStatementEnd(
            ASTBufferRead astBuffer,
            int ifStatementStartContext,
            Context endContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onEndIfStatement(ifStatementStartContext, endContext);
    }

    public static void encodeIfConditionBlockStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.IF_CONDITION_BLOCK);
    }

    public static <COMPILATION_UNIT> void decodeIfConditionBlockStart(
            ASTBufferRead astBuffer,
            int conditionBlockStartContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onIfStatementInitialBlockStart(conditionBlockStartContext);
    }

    public static void encodeIfConditionBlockEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.IF_CONDITION_BLOCK);
    }

    public static <COMPILATION_UNIT> void decodeIfConditionBlockEnd(
            ASTBufferRead astBuffer,
            int conditionBlockStartContext,
            Context endContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onIfStatementInitialBlockEnd(conditionBlockStartContext, endContext);
    }

    private static final int ELSE_IF_CONDITION_BLOCK_SIZE
        =    STRING_REF_SIZE + CONTEXT_REF_SIZE
           + STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeElseIfConditionBlockStart(
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
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {


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

    public static void encodeElseIfConditionBlockEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ELSE_IF_CONDITION_BLOCK);
    }

    public static <COMPILATION_UNIT> void decodeElseIfConditionBlockEnd(
            ASTBufferRead astBuffer,
            int elseIfConditionBlockStartContext,
            Context endContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onElseIfStatementEnd(elseIfConditionBlockStartContext, endContext);
    }

    private static final int ELSE_BLOCK_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeElseBlockStart(StringASTBuffer astBuffer, long elseKeyword, int elseKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.ELSE_BLOCK);
        astBuffer.writeStringRef(elseKeyword);
        astBuffer.writeContextRef(elseKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeElseBlockStart(
            ASTBufferRead astBuffer,
            int elseStartContext,
            ContextGetter contextGetter,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {


        final int elseKeywordContext;

        if (contextGetter != null) {
            elseKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            elseKeywordContext = ContextRef.NONE;
        }

        listener.onElseStatementStart(elseStartContext, astBuffer.getStringRef(index), elseKeywordContext);
    }

    public static void encodeElseBlockEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ELSE_BLOCK);
    }

    public static <COMPILATION_UNIT> void decodeElseBlockEnd(
            ASTBufferRead astBuffer,
            int elseStartContext,
            Context endContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onElseStatementEnd(elseStartContext, endContext);
    }

    private static final int SWITCH_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeSwitchStatementStart(StringASTBuffer astBuffer, long switchKeyword, int switchKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.SWITCH_CASE_STATEMENT);

        astBuffer.writeStringRef(switchKeyword);
        astBuffer.writeContextRef(switchKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeSwitchStatementStart(
            ASTBufferRead astBuffer,
            int switchStartContext,
            ContextGetter contextGetter,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        final int switchKeywordContext;

        if (contextGetter != null) {
            switchKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            switchKeywordContext = ContextRef.NONE;
        }

        listener.onSwitchStatementStart(switchStartContext, astBuffer.getStringRef(index), switchKeywordContext);
    }

    public static void encodeSwitchStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.SWITCH_CASE_STATEMENT);

    }

    public static <COMPILATION_UNIT> void decodeSwitchStatementEnd(
            ASTBufferRead astBuffer,
            int switchStartContext,
            Context endContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onSwitchStatementEnd(switchStartContext, endContext);
    }
    
    public static void encodeSwitchCaseGroupStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.SWITCH_CASE_GROUP);
    }

    public static <COMPILATION_UNIT> void decodeSwitchCaseGroupStart(
            ASTBufferRead astBuffer,
            int switchCaseGroupStartContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onJavaSwitchBlockStatementGroupStart(switchCaseGroupStartContext);
    }
    
    public static void encodeSwitchCaseGroupEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.SWITCH_CASE_GROUP);
    }

    public static <COMPILATION_UNIT> void decodeSwitchCaseGroupEnd(
            ASTBufferRead astBuffer,
            int switchCaseGroupStartContext,
            Context endContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onJavaSwitchBlockStatementGroupEnd(switchCaseGroupStartContext, endContext);
    }
    
    private static final int CONSTANT_SWITCH_CASE_LABEL_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    public static void encodeConstantSwitchLabelStart(StringASTBuffer astBuffer, long keyword, int keywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.CONSTANT_SWITCH_CASE_LABEL);
        
        astBuffer.writeStringRef(keyword);
        astBuffer.writeContextRef(keywordContext);
    }

    public static <COMPILATION_UNIT> void decodeConstantSwitchLabelStart(
            ASTBufferRead astBuffer,
            int constantSwitchLabelStartContext,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onConstantSwitchLabelStart(
                constantSwitchLabelStartContext,
                astBuffer.getStringRef(index),
                astBuffer.getContextRef(index + STRING_REF_SIZE));
    }

    public static void encodeConstantSwitchLabelEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CONSTANT_SWITCH_CASE_LABEL);
    }
    
    public static <COMPILATION_UNIT> void decodeConstantSwitchLabelEnd(
            ASTBufferRead astBuffer,
            int constantSwitchLabelStartContext,
            Context endContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onConstantSwitchLabelEnd(constantSwitchLabelStartContext, endContext);
    }
    
    private static final int ENUM_SWITCH_CASE_LABEL_SIZE = 
              STRING_REF_SIZE + CONTEXT_REF_SIZE
            + STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    public static void encodeEnumSwitchLabel(
            StringASTBuffer astBuffer,
            long keyword, int keywordContext,
            long constantName, int constantNameContext) {

        astBuffer.writeLeafElement(ParseTreeElement.ENUM_SWITCH_CASE_LABEL);
        
        astBuffer.writeStringRef(keyword);
        astBuffer.writeContextRef(keywordContext);

        astBuffer.writeStringRef(constantName);
        astBuffer.writeContextRef(constantNameContext);
    }

    public static <COMPILATION_UNIT> void decodeEnumSwitchLabel(
            ASTBufferRead astBuffer,
            int enumSwitchLabelContext,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onEnumSwitchLabel(
                enumSwitchLabelContext,
                
                astBuffer.getStringRef(index),
                astBuffer.getContextRef(index + STRING_REF_SIZE),

                astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE),
                astBuffer.getContextRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE + STRING_REF_SIZE));
    }

    private static final int DEFAULT_SWITCH_CASE_LABEL_SIZE = STRING_REF_SIZE;
  
    public static void encodeDefaultSwitchLabel(StringASTBuffer astBuffer, long keyword) {

        astBuffer.writeLeafElement(ParseTreeElement.DEFAULT_SWITCH_CASE_LABEL);
  
        astBuffer.writeStringRef(keyword);
    }

    public static <COMPILATION_UNIT> void decodeDefaultSwitchLabel(
          ASTBufferRead astBuffer,
          int defaultSwitchLabelContext,
          int index,
          IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onDefaultSwitchLabel(defaultSwitchLabelContext, astBuffer.getStringRef(index));
    }

    private static final int BREAK_STATEMENT_SIZE = 
            STRING_REF_SIZE + CONTEXT_REF_SIZE
          + STRING_REF_SIZE + CONTEXT_REF_SIZE;
    
    public static void encodeBreakStatement(StringASTBuffer astBuffer,
            long keyword, int keywordContext,
            long label, int labelContext) {

        astBuffer.writeElementStart(ParseTreeElement.BREAK_STATEMENT);
  
        astBuffer.writeStringRef(keyword);
        astBuffer.writeContextRef(keywordContext);

        if (label != StringRef.STRING_NONE) {
            astBuffer.writeStringRef(label);
            astBuffer.writeContextRef(labelContext);
        }
        else {
            astBuffer.writeNoStringRef();
            astBuffer.writeNoContextRef();
        }
    }

    public static <COMPILATION_UNIT> void decodeBreakStatement(
          ASTBufferRead astBuffer,
          int breakStatementContext,
          Context endContext,
          int index,
          IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onBreakStatement(
                breakStatementContext,
                
                astBuffer.getStringRef(index),
                astBuffer.getContextRef(index + STRING_REF_SIZE),

                astBuffer.getStringRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE),
                astBuffer.getContextRef(index + STRING_REF_SIZE + CONTEXT_REF_SIZE + STRING_REF_SIZE),
                endContext);
    }
    
    private static final int UNARY_EXPRESSION_SIZE = 1 + 1;

    public static void encodeUnaryExpressionStart(StringASTBuffer astBuffer, Operator operator) {

        astBuffer.writeElementStart(ParseTreeElement.UNARY_EXPRESSION);

        astBuffer.writeEnumByte(operator.getOperatorType());
        astBuffer.writeEnumByte(operator.getEnumValue());
    }

    public static <COMPILATION_UNIT> void decodeUnaryExpressionStart(
            ASTBufferRead astBuffer,
            int startContext,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeUnaryExpressionEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.UNARY_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeUnaryExpressionEnd(
            ASTBufferRead astBuffer,
            int startContext,
            Context endContext,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onUnaryExpressionEnd(startContext, endContext);
    }

    private static final int EXPRESSION_BINARY_OPERATOR_SIZE = 1 + 1;

    public static void encodeExpressionBinaryOperator(StringASTBuffer astBuffer, Operator operator) {

        astBuffer.writeLeafElement(ParseTreeElement.EXPRESSION_BINARY_OPERATOR);

        astBuffer.writeEnumByte(operator.getOperatorType());
        astBuffer.writeEnumByte(operator.getEnumValue());
    }

    public static <COMPILATION_UNIT> void decodeExpressionBinaryOperator(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeIntegerLiteral(StringASTBuffer astBuffer, long value, Base base, boolean signed, int bits) {

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

    public static void encodeExpressionListStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.EXPRESSION_LIST);
    }

    public static <COMPILATION_UNIT> void decodeExpressionListStart(
            int expressionListStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onExpressionListStart(expressionListStartContext);
    }

    public static void encodeExpressionListEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.EXPRESSION_LIST);
    }

    public static <COMPILATION_UNIT> void decodeExpressionListEnd(
            int expressionListStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onExpressionListEnd(expressionListStartContext, endContext);
    }

    public static <COMPILATION_UNIT> void decodeIntegerLiteral(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeStringLiteral(StringASTBuffer astBuffer, long value) {

        astBuffer.writeLeafElement(ParseTreeElement.STRING_LITERAL);

        astBuffer.writeStringRef(value);
    }

    public static <COMPILATION_UNIT> void decodeStringLiteral(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onStringLiteral(leafContext, astBuffer.getStringRef(index));
    }

    private static final int CHARACTER_LITERAL_SIZE = 4;

    public static void encodeCharacterLiteral(StringASTBuffer astBuffer, char value) {

        astBuffer.writeLeafElement(ParseTreeElement.CHARACTER_LITERAL);

        astBuffer.writeInt(value);
    }

    public static <COMPILATION_UNIT> void decodeCharacterLiteral(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        final char c = (char)astBuffer.getInt(index);

        listener.onCharacterLiteral(leafContext, c);
    }

    private static final int BOOLEAN_LITERAL_SIZE = 1;

    public static void encodeBooleanLiteral(StringASTBuffer astBuffer, boolean value) {

        astBuffer.writeLeafElement(ParseTreeElement.BOOLEAN_LITERAL);

        astBuffer.writeByte((byte)(value ? 1 : 0));
    }

    public static <COMPILATION_UNIT> void decodeBooleanLiteral(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        final byte b = astBuffer.getByte(index);

        listener.onBooleanLiteral(leafContext, b != 0);
    }

    private static final int WHILE_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeWhileStatementStart(StringASTBuffer astBuffer, long whileKeyword, int whileKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.WHILE_STATEMENT);

        astBuffer.writeStringRef(whileKeyword);
        astBuffer.writeContextRef(whileKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeWhileStatementStart(
            ASTBufferRead astBuffer,
            int whileStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeWhileStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.WHILE_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeWhileStatementEnd(
            ASTBufferRead astBuffer,
            int whileStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onWhileStatementEnd(whileStatementStartContext, endContext);
    }

    private static final int FOR_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeForStatementStart(StringASTBuffer astBuffer, long forKeyword, int forKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.FOR_STATEMENT);

        astBuffer.writeStringRef(forKeyword);
        astBuffer.writeContextRef(forKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeForStatementStart(
            ASTBufferRead astBuffer,
            int iteratorForStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        final int iteratorForKeywordContext;

        if (contextGetter != null) {

            iteratorForKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            iteratorForKeywordContext = ContextRef.NONE;
        }

        listener.onForStatementStart(
                iteratorForStatementStartContext,
                astBuffer.getStringRef(index),
                iteratorForKeywordContext);
    }

    public static void encodeForInitStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.FOR_INIT);
    }

    public static <COMPILATION_UNIT> void decodeForInitStart(int forInitStartContext, ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onForInitStart(forInitStartContext);
    }

    public static void encodeForInitEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.FOR_INIT);
    }

    public static <COMPILATION_UNIT> void decodeForInitEnd(
            int forInitStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onForInitEnd(forInitStartContext, endContext);
    }

    public static void encodeForExpressionStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.FOR_EXPRESSION_LIST);
    }

    public static <COMPILATION_UNIT> void decodeForExpressionStart(int forInitStartContext, ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onForExpressionStart(forInitStartContext);
    }

    public static void encodeForExpressionEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.FOR_EXPRESSION_LIST);
    }

    public static <COMPILATION_UNIT> void decodeForExpressionEnd(
            int forExpressionStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onForExpressionEnd(forExpressionStartContext, endContext);
    }

    public static void encodeForUpdateStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.FOR_UPDATE);
    }

    public static <COMPILATION_UNIT> void decodeForUpdateStart(int forUpdateStartContext, ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onForUpdateStart(forUpdateStartContext);
    }

    public static void encodeForUpdateEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.FOR_UPDATE);
    }

    public static <COMPILATION_UNIT> void decodeForUpdateEnd(
            int forUpdateStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onForUpdateEnd(forUpdateStartContext, endContext);
    }

    public static void encodeForStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.FOR_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeForStatementEnd(
            int forStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onForStatementEnd(forStatementStartContext, endContext);
    }

    private static final int ITERATOR_FOR_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeIteratorForStatementStart(StringASTBuffer astBuffer, long forKeyword, int forKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.ITERATOR_FOR_STATEMENT);

        astBuffer.writeStringRef(forKeyword);
        astBuffer.writeContextRef(forKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeIteratorForStatementStart(
            ASTBufferRead astBuffer,
            int iteratorForStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeIteratorForTestStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.ITERATOR_FOR_TEST);
    }

    public static <COMPILATION_UNIT> void decodeIteratorForTestStart(
            ASTBufferRead astBuffer,
            int iteratorForTestStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onIteratorForTestStart(iteratorForTestStartContext);
    }

    public static void encodeIteratorForTestEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ITERATOR_FOR_TEST);
    }

    public static <COMPILATION_UNIT> void decodeIteratorForTestEnd(
            ASTBufferRead astBuffer,
            int iteratorForTestStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onIteratorForTestEnd(iteratorForTestStartContext, endContext);
    }

    public static void encodeIteratorForStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ITERATOR_FOR_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeIteratorForStatementEnd(
            ASTBufferRead astBuffer,
            int iteratorForStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onIteratorForStatementEnd(iteratorForStatementStartContext, endContext);
    }

    private static final int TRY_CATCH_FINALLY_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeTryCatchFinallyStatementStart(StringASTBuffer astBuffer, long tryKeyword, int tryKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.TRY_CATCH_FINALLY_STATEMENT);

        astBuffer.writeStringRef(tryKeyword);
        astBuffer.writeContextRef(tryKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeTryCatchFinallyStatementStart(
            ASTBufferRead astBuffer,
            int tryCatchFinallyStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeTryBlockEnd(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.TRY_BLOCK_END);
    }

    public static <COMPILATION_UNIT> void decodeTryBlockEnd(
            int tryBlockContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onTryBlockEnd(tryBlockContext, endContext);
    }

    private static final int CATCH_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeCatchStart(StringASTBuffer astBuffer, long catchKeyword, int catchKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.CATCH);

        astBuffer.writeStringRef(catchKeyword);
        astBuffer.writeContextRef(catchKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeCatchStart(
            ASTBufferRead astBuffer,
            int catchStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        final int catchKeywordContext;

        if (contextGetter != null) {

            catchKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            catchKeywordContext = ContextRef.NONE;
        }

        listener.onCatchStart(catchStartContext, astBuffer.getStringRef(index), catchKeywordContext);
    }

    public static void encodeCatchEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CATCH);
    }

    public static <COMPILATION_UNIT> void decodeCatchEnd(
            ASTBufferRead astBuffer,
            int catchStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onCatchEnd(catchStartContext, endContext);
    }

    private static final int FINALLY_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeFinallyStart(StringASTBuffer astBuffer, long finallyKeyword, int finallyKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.FINALLY);

        astBuffer.writeStringRef(finallyKeyword);
        astBuffer.writeContextRef(finallyKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeFinallyStart(
            ASTBufferRead astBuffer,
            int finallyStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        final int finallyKeywordContext;

        if (contextGetter != null) {

            finallyKeywordContext = astBuffer.getContextRef(index + STRING_REF_SIZE);
        }
        else {
            finallyKeywordContext = ContextRef.NONE;
        }

        listener.onFinallyStart(finallyStartContext, astBuffer.getStringRef(index), finallyKeywordContext);
    }

    public static void encodeFinallyEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.FINALLY);
    }

    public static <COMPILATION_UNIT> void decodeFinallyEnd(
            ASTBufferRead astBuffer,
            int finallyStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onFinallyEnd(finallyStartContext, endContext);
    }

    public static void encodeTryCatchFinallyStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.TRY_CATCH_FINALLY_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeTryCatchFinallyStatementEnd(
            ASTBufferRead astBuffer,
            int tryCatchFinallyStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onTryStatementEnd(tryCatchFinallyStatementStartContext, endContext);
    }

    private static final int RETURN_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeReturnStatementStart(StringASTBuffer astBuffer, long returnKeyword, int returnKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.RETURN_STATEMENT);

        astBuffer.writeStringRef(returnKeyword);
        astBuffer.writeContextRef(returnKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeReturnStatementStart(
            ASTBufferRead astBuffer,
            int returnStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeTryWithResourcesStatementStart(StringASTBuffer astBuffer, long tryKeyword, int tryKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.TRY_WITH_RESOURCES_STATEMENT);

        astBuffer.writeStringRef(tryKeyword);
        astBuffer.writeContextRef(tryKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeTryWithResourcesStatementStart(
            ASTBufferRead astBuffer,
            int tryWithResourcesStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeTryWithResourcesSpecificationStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.RESOURCES_LIST);
    }

    public static <COMPILATION_UNIT> void decodeTryWithResourcesSpecificationStart(
            int resourcesSpecificationStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onTryWithResourcesSpecificationStart(resourcesSpecificationStartContext);
    }

    public static void encodeTryWithResourcesSpecificationEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.RESOURCES_LIST);
    }

    public static <COMPILATION_UNIT> void decodeTryWithResourcesSpecificationEnd(
            int resourcesSpecificationStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onTryWithResourcesSpecificationEnd(resourcesSpecificationStartContext, endContext);
    }

    public static void encodeTryResourceStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.RESOURCE);
    }

    public static <COMPILATION_UNIT> void decodeTryResourceStart(
            int resourceStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onResourceStart(resourceStartContext);
    }

    public static void encodeTryResourceEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.RESOURCE);
    }

    public static <COMPILATION_UNIT> void decodeTryResourceEnd(
            int resourceStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onResourceEnd(resourceStartContext, endContext);
    }

    public static void encodeReturnStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.RETURN_STATEMENT);
    }

    public static void encodeTryWithResourcesStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.TRY_WITH_RESOURCES_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeTryWithResourcesStatementEnd(
            ASTBufferRead astBuffer,
            int tryWithResourcesStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onTryWithResourcesStatementEnd(tryWithResourcesStatementStartContext, endContext);
    }

    public static <COMPILATION_UNIT> void decodeReturnStatementEnd(
            ASTBufferRead astBuffer,
            int returnStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onReturnStatementEnd(returnStatementStartContext, endContext);
    }

    private static final int THROW_STATEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeThrowStatementStart(StringASTBuffer astBuffer, long throwKeyword, int throwKeywordContext) {

        astBuffer.writeElementStart(ParseTreeElement.THROW_STATEMENT);

        astBuffer.writeStringRef(throwKeyword);
        astBuffer.writeContextRef(throwKeywordContext);
    }

    public static <COMPILATION_UNIT> void decodeThrowStatementStart(
            ASTBufferRead astBuffer,
            int throwStatementStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeThrowStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.THROW_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeThrowStatementEnd(
            ASTBufferRead astBuffer,
            int throwStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onThrowStatementEnd(throwStatementStartContext, endContext);
    }

    public static void encodeAssignmentStatementStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.ASSIGNMENT_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentStatementStart(
            ASTBufferRead astBuffer,
            int assignmentStatementStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onAssignmentStatementStart(assignmentStatementStartContext);
    }

    public static void encodeAssignmentStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ASSIGNMENT_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentStatementEnd(
            ASTBufferRead astBuffer,
            int assignmentStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onAssignmentStatementEnd(assignmentStatementStartContext, endContext);
    }

    public static void encodeAssignmentExpressionStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.ASSIGNMENT_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentExpressionStart(
            ASTBufferRead astBuffer,
            int assignmentExpressionStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onEnterAssignmentExpression(assignmentExpressionStartContext);
    }

    public static void encodeAssignmentExpressionEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ASSIGNMENT_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentExpressionEnd(
            ASTBufferRead astBuffer,
            int assignmentStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onExitAssignmentExpression(assignmentStatementStartContext, endContext);
    }

    public static void encodeAssignmentLHSStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.ASSIGNMENT_EXPRESSION_LHS);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentLHSStart(
            ASTBufferRead astBuffer,
            int assignmentLHSStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onEnterAssignmentLHS(assignmentLHSStartContext);
    }

    public static void encodeAssignmentLHSEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ASSIGNMENT_EXPRESSION_LHS);
    }

    public static <COMPILATION_UNIT> void decodeAssignmentLHSEnd(
            ASTBufferRead astBuffer,
            int assignmentLHSStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onExitAssignmentLHS(assignmentLHSStartContext, endContext);
    }

    public static void encodeExpressionStatementStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.EXPRESSION_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeExpressionStatementStart(
            ASTBufferRead astBuffer,
            int expressionStatementStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {


        listener.onExpressionStatementStart(expressionStatementStartContext);
    }

    public static void encodeExpressionStatementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.EXPRESSION_STATEMENT);
    }

    public static <COMPILATION_UNIT> void decodeExpressionStatementEnd(
            ASTBufferRead astBuffer,
            int expressionStatementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    private static final int UNRESOLVED_METHOD_INVOCATION_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeUnresolvedMethodInvocationStart(
            StringASTBuffer astBuffer,
            long methodName,
            int methodNameContext) {

        astBuffer.writeElementStart(ParseTreeElement.UNRESOLVED_METHOD_INVOCATION_EXPRESSION);
        astBuffer.writeStringRef(methodName);
        astBuffer.writeContextRef(methodNameContext);
    }

    public static <COMPILATION_UNIT> void decodeUnresolvedMethodInvocationStart(
            ASTBufferRead astBuffer,
            int methodInvocationStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        final long methodName = astBuffer.getStringRef(index);
        final int methodNameContext = astBuffer.getContextRef(index + STRING_REF_SIZE);

        listener.onMethodInvocationStart(
                methodInvocationStartContext,
                methodName,
                methodNameContext);
    }

    public static void encodeUnresolvedMethodInvocationEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.UNRESOLVED_METHOD_INVOCATION_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeUnresolvedMethodInvocationEnd(
            ASTBufferRead astBuffer,
            int methodInvocationStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onMethodInvocationEnd(methodInvocationStartContext, endContext);
    }

    public static void encodeParametersStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.PARAMETER_LIST);
    }

    public static <COMPILATION_UNIT> void decodeParametersStart(
            ASTBufferRead astBuffer,
            int parametersStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {


        listener.onParametersStart(parametersStartContext);
    }

    public static void encodeParametersEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.PARAMETER_LIST);
    }

    public static <COMPILATION_UNIT> void decodeParametersEnd(
            int parameterStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onParametersEnd(parameterStartContext, endContext);
    }

    public static void encodeParameterStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeParameterStart(
            ASTBufferRead astBuffer,
            int parameterStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {


        listener.onParameterStart(parameterStartContext);
    }

    public static void encodeParameterEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeParameterEnd(
            int parameterStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onParameterEnd(parameterStartContext, endContext);
    }

    public static void encodeNestedExpressionStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.NESTED_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeNestedExpressionStart(
            ASTBufferRead astBuffer,
            int nestedExpressionStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {


        listener.onNestedExpressionStart(nestedExpressionStartContext);
    }

    public static void encodeNestedExpressionEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.NESTED_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeNestedExpressionEnd(
            int nestedExpressionStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onNestedExpressionEnd(nestedExpressionStartContext, endContext);
    }

    public static void encodePrimariesStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.PRIMARY_LIST);
    }

    public static <COMPILATION_UNIT> void decodePrimariesStart(
            ASTBufferRead astBuffer,
            int primariesStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onPrimaryStart(primariesStartContext);
    }

    public static void encodePrimariesEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.PRIMARY_LIST);
    }

    public static <COMPILATION_UNIT> void decodePrimariesEnd(
            ASTBufferRead astBuffer,
            int primariesStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onPrimaryEnd(primariesStartContext, endContext);
    }

    private static final int FIELD_ACCESS_SIZE = STRING_REF_SIZE;

    public static void encodeFieldAccess(StringASTBuffer astBuffer, long fieldName) {

        astBuffer.writeElementStart(ParseTreeElement.FIELD_ACCESS);
        astBuffer.writeStringRef(fieldName);
    }

    public static <COMPILATION_UNIT> void decodeFieldAccess(
            ASTBufferRead astBuffer,
            int fieldAccessStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onFieldAccess(
                fieldAccessStartContext,
                FieldAccessType.FIELD,
                null,
                null,
                astBuffer.getStringRef(index),
                fieldAccessStartContext);
    }

    public static void encodeAnnotationStart(StringASTBuffer astBuffer, Names names) {

        astBuffer.writeElementStart(ParseTreeElement.ANNOTATION);

        encodeNames(astBuffer, names);
    }

    public static <COMPILATION_UNIT> void decodeAnnotationStart(
            ASTBufferRead astBuffer,
            int annotationStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        final Names names = decodeNames(astBuffer, index);

        listener.onAnnotationStart(annotationStartContext, names);
    }

    public static void encodeAnnotationEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ANNOTATION);
    }

    public static <COMPILATION_UNIT> void decodeAnnotationEnd(
            int annotationStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onAnnotationEnd(annotationStartContext, endContext);
    }

    private static final int ANNOTATION_ELEMENT_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeAnnotationElementStart(StringASTBuffer astBuffer, long name, int nameContext) {

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
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onAnnotationElementStart(
                annotationElementStartContext,
                astBuffer.hasContextRef(index) ? astBuffer.getStringRef(index) : StringRef.STRING_NONE,
                astBuffer.getContextRef(index + STRING_REF_SIZE));
    }

    public static void encodeAnnotationElementEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.ANNOTATION_ELEMENT);
    }

    public static <COMPILATION_UNIT> void decodeAnnotationElementEnd(
            int annotationElementStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onAnnotationElementEnd(annotationElementStartContext, endContext);
    }

    private static final int UNRESOLVED_NAME_PRIMARY_SIZE = STRING_REF_SIZE;

    public static void encodeUnresolvedNamePrimary(StringASTBuffer astBuffer, long name, int nameContext) {

        astBuffer.writeLeafElement(ParseTreeElement.UNRESOLVED_NAME_PRIMARY);

        if (name == StringRef.STRING_NONE) {
            throw new IllegalArgumentException();
        }

        astBuffer.writeStringRef(name);
    }
    
    public static int decodeNamePrimaryName(ASTBufferRead astBuffer, int index) {
        
        return astBuffer.getStringRef(index);
    }

    public static <COMPILATION_UNIT> void decodeUnresolvedNamePrimary(
            ASTBufferRead astBuffer,
            int unresolvedNamePrimaryStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onUnresolvedNamePrimary(
                unresolvedNamePrimaryStartContext,
                AST.decodeNamePrimaryName(astBuffer, index));
    }

    private static final int NAME_REFERENCE_SIZE = STRING_REF_SIZE;

    public static void encodeNameReference(StringASTBuffer astBuffer, long name) {

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
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onNameReference(
                nameReferenceStartContext,
                astBuffer.getStringRef(index));
    }

    public static void encodeTypeArgumentListStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.TYPE_ARGUMENT_LIST);
    }

    public static <COMPILATION_UNIT> void decodeTypeArgumentListStart(
            ASTBufferRead astBuffer,
            int typeArgumentListStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onGenericTypeArgumentsStart(typeArgumentListStartContext);
    }

    public static void encodeTypeArgumentListEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.TYPE_ARGUMENT_LIST);
    }

    public static <COMPILATION_UNIT> void decodeTypeArgumentListEnd(
            int typeArgumentListStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onGenericTypeArgumentsEnd(typeArgumentListStartContext, endContext);
    }

    public static void encodeReferenceTypeArgumentStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.REFERENCE_GENERIC_TYPE_ARGUMENT);
    }

    public static <COMPILATION_UNIT> void decodeReferenceTypeArgumentStart(
            ASTBufferRead astBuffer,
            int referenceTypeArgumentStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {


        listener.onGenericReferenceTypeArgumentStart(referenceTypeArgumentStartContext);
    }

    public static void encodeReferenceTypeArgumentEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.REFERENCE_GENERIC_TYPE_ARGUMENT);
    }

    public static <COMPILATION_UNIT> void decodeReferenceTypeArgumentEnd(
            int referenceTypeArgumentStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onGenericReferenceTypeArgumentEnd(referenceTypeArgumentStartContext, endContext);
    }

    public static void encodeWildcardTypeArgumentStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.WILDCARD_GENERIC_TYPE_ARGUMENT);
    }

    public static <COMPILATION_UNIT> void decodeWildcardTypeArgumentStart(
            ASTBufferRead astBuffer,
            int wildcardTypeArgumentStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {


        listener.onGenericWildcardTypeArgumentStart(wildcardTypeArgumentStartContext);
    }

    public static void encodeWildcardTypeArgumentEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.WILDCARD_GENERIC_TYPE_ARGUMENT);
    }

    public static <COMPILATION_UNIT> void decodeWildcardTypeArgumentEnd(
            int wildcardTypeArgumentStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onGenericWildcardTypeArgumentEnd(wildcardTypeArgumentStartContext, endContext);
    }

    private static final int TYPE_BOUND_SIZE = 1;

    public static void encodeTypeBoundStart(StringASTBuffer astBuffer, TypeBoundType type) {

        astBuffer.writeElementStart(ParseTreeElement.TYPE_BOUND);
        astBuffer.writeEnumByte(type);
    }

    public static <COMPILATION_UNIT> void decodeTypeBoundStart(
            ASTBufferRead astBuffer,
            int typeBoundStartContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        final TypeBoundType type = astBuffer.getEnumByte(index, TypeBoundType.class);

        listener.onTypeBoundStart(typeBoundStartContext, type);
    }

    public static void encodeTypeBoundEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.TYPE_BOUND);
    }

    public static <COMPILATION_UNIT> void decodeTypeBoundEnd(
            int typeBoundStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onTypeBoundEnd(typeBoundStartContext, endContext);
    }

    public static void encodeGenericTypeParametersStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.GENERIC_TYPE_PARAMETERS);
    }

    public static <COMPILATION_UNIT> void decodeGenericTypeParametersStart(
            ASTBufferRead astBuffer,
            int parameterStartContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {


        listener.onGenericTypeParametersStart(parameterStartContext);
    }

    private static final int NAMED_GENERIC_TYPE_PARAMETER_SIZE = STRING_REF_SIZE + CONTEXT_REF_SIZE;

    public static void encodeNamedGenericTypeParameterStart(StringASTBuffer astBuffer, long name, int nameContext) {

        astBuffer.writeElementStart(ParseTreeElement.NAMED_GENERIC_TYPE_PARAMETER);
        astBuffer.writeStringRef(name);
        astBuffer.writeContextRef(nameContext);
    }

    public static <COMPILATION_UNIT> void decodeNamedGenericTypeParameterStart(
            ASTBufferRead astBuffer,
            int parameterStartContext,
            ContextGetter contextGetter,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

    public static void encodeNamedGenericTypeParameterEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.NAMED_GENERIC_TYPE_PARAMETER);
    }

    public static <COMPILATION_UNIT> void decodeNamedGenericTypeParameterEnd(
            int parameterStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onNamedGenericTypeParameterEnd(parameterStartContext, endContext);
    }

    public static void encodeGenericTypeParametersEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.GENERIC_TYPE_PARAMETERS);
    }

    public static <COMPILATION_UNIT> void decodeGenericTypeParametersEnd(
            int parameterStartContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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
        case VOLATILE:
        case TRANSIENT:
            size = 1;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return size;
    }

    public static void encodeVisibilityFieldModifier(StringASTBuffer astBuffer, FieldVisibility fieldVisibility) {

        astBuffer.writeLeafElement(ParseTreeElement.FIELD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(FieldModifier.Type.VISIBILITY);
        astBuffer.writeEnumByte(fieldVisibility.getVisibility());
    }

    public static void encodeMutabilityModifier(StringASTBuffer astBuffer, ASTMutability mutability) {

        astBuffer.writeLeafElement(ParseTreeElement.FIELD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(FieldModifier.Type.MUTABILITY);
        astBuffer.writeEnumByte(mutability.getMutability());
    }

    public static void encodeStaticFieldModifier(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.FIELD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(FieldModifier.Type.STATIC);
    }

    public static void encodeVolatileFieldModifier(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.FIELD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(FieldModifier.Type.VOLATILE);
    }

    public static void encodeTransientFieldModifier(StringASTBuffer astBuffer) {

        astBuffer.writeLeafElement(ParseTreeElement.FIELD_MODIFIER_HOLDER);
        astBuffer.writeEnumByte(FieldModifier.Type.TRANSIENT);
    }

    public static <COMPILATION_UNIT> void decodeFieldModifierHolder(
            ASTBufferRead astBuffer,
            int leafContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

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

        case VOLATILE:
            listener.onVolatileFieldModifier(leafContext);
            break;

        case TRANSIENT:
            listener.onTransientFieldModifier(leafContext);
            break;

         default:
             throw new UnsupportedOperationException();
        }
    }

    public static void encodeClassInstanceCreationExpressionStart(StringASTBuffer astBuffer) {

        astBuffer.writeElementStart(ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeClassInstanceCreationExpressionStart(
            int startContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onClassInstanceCreationExpressionStart(startContext);
    }


    public static void encodeClassInstanceCreationTypeAndConstructorName(StringASTBuffer astBuffer, long name, int nameContext) {

        astBuffer.writeLeafElement(ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION_NAME);

        encodeNames(astBuffer, name, nameContext);
    }

    public static <COMPILATION_UNIT> void decodeClassInstanceCreationTypeAndConstructorName(
            ASTBufferRead astBuffer,
            int startContext,
            int index,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onClassInstanceCreationTypeAndConstructorName(startContext, decodeNames(astBuffer, index));
    }

    public static void encodeClassInstanceCreationExpressionEnd(StringASTBuffer astBuffer) {

        astBuffer.writeElementEnd(ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION);
    }

    public static <COMPILATION_UNIT> void decodeClassInstanceCreationExpressionEnd(
            int startContext,
            Context endContext,
            ParseTreeListener<COMPILATION_UNIT> listener) {

        listener.onClassInstanceCreationExpressionEnd(startContext, endContext);
    }
}
