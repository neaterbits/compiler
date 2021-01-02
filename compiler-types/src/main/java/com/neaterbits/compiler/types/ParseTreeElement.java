package com.neaterbits.compiler.types;

public enum ParseTreeElement {

	COMPILATION_UNIT(false),
	COMPILATION_CODE_LINES(false),
	NAMESPACE(false),
    NAMESPACE_PART(true),
	NAMESPACE_DECLARATION(false),

	KEYWORD(true),
    NAME(true),
    NAME_LIST(false),

	IMPORT(false),
	IMPORT_NAME_PART(true),

	TYPE_DEFINITION(false),

	ANNOTATION(false),
	ANNOTATION_ELEMENT(false),

	TYPE_ARGUMENT_LIST(false),

	REFERENCE_GENERIC_TYPE_ARGUMENT(false),
    WILDCARD_GENERIC_TYPE_ARGUMENT(false),

	TYPE_BOUND(false),

	GENERIC_TYPE_PARAMETERS(false),

	NAMED_GENERIC_TYPE_PARAMETER(false),

	CLASS_DEFINITION(false, false),
	CLASS_MODIFIER_HOLDER(true),
	CLASS_DECLARATION_NAME(true),

	CLASS_EXTENDS(false),

	IMPLEMENTS(false),
	IMPLEMENTS_TYPE(false),
    IMPLEMENTS_NAME_PART(true),

	CONSTRUCTOR_MEMBER(false),
	CONSTRUCTOR(false),
	CONSTRUCTOR_NAME(true),
	CONSTRUCTOR_MODIFIER_HOLDER(true),

	CLASS_METHOD_MEMBER(false),
	CLASS_METHOD_MODIFIER_HOLDER(true),
	CLASS_METHOD(false),
    METHOD_RETURN_TYPE(false),
	METHOD_NAME(true),

	SIGNATURE_PARAMETERS(false),

	SIGNATURE_PARAMETER(false),

	SIGNATURE_PARAMETER_VARARGS(true),

	THROWS(false),

    PARAMETER_MODIFIER_HOLDER(true),

	PARAMETER_LIST(false, true),
	PARAMETER(false),
	PARAMETER_NAME(true),

	CLASS_DATA_FIELD_MEMBER(false),
	FIELD_MODIFIER_HOLDER(true),
    FIELD_DECLARATION(false),
	FIELD_NAME_DECLARATION(true),

	INNER_CLASS_MEMBER(false),

	STATIC_INITIALIZER(false),

	FUNCTION(false),

	INTERFACE_DEFINITION(false, false),
	INTERFACE_MODIFIER_HOLDER(true),
	INTERFACE_DECLARATION_NAME(true),

	INTERFACE_METHOD_MEMBER(false),
	INTERFACE_METHOD(false),
	INTERFACE_METHOD_MODIFIER_HOLDER(true),
	INTERFACE_METHOD_NAME(true),

	ENUM_DEFINITION(false),
	ENUM_CONSTANT_DEFINITION(false),

	STRUCT_DEFINITION(false),
	STRUCT_DECLARATION_NAME(true),
	STRUCT_DATA_FIELD_MEMBER(false),

    UNRESOLVED_IDENTIFIER_TYPE_REFERENCE(false),
	UNRESOLVED_SCOPED_TYPE_REFERENCE(false),
	UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME(false),
    UNRESOLVED_SCOPED_TYPE_REFERENCE_NAME_PART(true),

    SCALAR_TYPE_REFERENCE(false),
    UNNAMED_VOID_TYPE_REFERENCE(false),

    RESOLVED_TYPE_REFERENCE(true),

	FUNCTION_NAME(true),

	BLOCK(false),
	CATCH_BLOCK(false),

	CHARACTER_LITERAL(true),
	INTEGER_LITERAL(true),
	FLOATING_POINT_LITERAL(true),
	STRING_LITERAL(true),
	BOOLEAN_LITERAL(true),
	NULL_LITERAL(true),

	ENUM_CONSTANT(true),

	ASSIGNMENT_EXPRESSION(false),
	ASSIGNMENT_EXPRESSION_LHS(false),
	BLOCK_LAMBDA_EXPRESSION(false),
	LAMBDA_EXPRESSION_PARAMETERS(false),
	CAST_EXPRESSION(false),
	UNRESOLVED_CLASS_EXPRESSION(false),
	CLASS_INSTANCE_CREATION_EXPRESSION(false),
    CLASS_INSTANCE_CREATION_EXPRESSION_NAME(true),
    ARRAY_CREATION_EXPRESSION(false),
	CONDITIONAL_EXPRESSION(false),
	EXPRESSION_LIST(false),
	FUNCTION_CALL_EXPRESSION(false),
	FUNCTION_POINTER_INVOCATION_EXPRESSION(false),
	UNRESOLVED_METHOD_INVOCATION_EXPRESSION(false),
	RESOLVED_METHOD_INVOCATION_EXPRESSION(false),
	
	NESTED_EXPRESSION(false),
	UNARY_EXPRESSION(false),

	NAME_PRIMARY(true), // unresolved name

	PRIMARY_LIST(false),
	SINGLE_LAMBDA_EXPRESSION(false),
	STATIC_MEMBER_REFERENCE(true),
	THIS_PRIMARY(true),
	
	NAME_REFERENCE(true),
	FIELD_ACCESS_REFERENCE(false),

	ARRAY_ACCESS_REFERENCE(false),

	EXPRESSION_BINARY_OPERATOR(true),

	CONSTANT_SWITCH_CASE_LABEL(true),
	DEFAULT_SWITCH_CASE_LABEL(true),
	ENUM_SWITCH_CASE_LABEL(true),

	ARITHMETIC_BINARY_EXPRESSION(false),
	ARRAY_ACCESS_EXPRESSION(false),
	ASSIGNMENT_STATEMENT(false),
	BREAK_STATEMENT(false),
	CONSTRUCTOR_INVOCATION_STATEMENT(false),

	DO_WHILE_STATEMENT(false),

	EXPRESSION_STATEMENT(false),

	FIELD_ACCESS(false),

	FOR_STATEMENT(false),
	FOR_INIT(false),
	FOR_EXPRESSION_LIST(false),
	FOR_UPDATE(false),

	IF_ELSE_IF_ELSE_STATEMENT(false),
	IF_CONDITION_BLOCK(false),
	ELSE_IF_CONDITION_BLOCK(false),
	ELSE_BLOCK(false),

	ITERATOR_FOR_STATEMENT(false),
    ITERATOR_FOR_TEST(false),

	RETURN_STATEMENT(false),

	SWITCH_CASE_STATEMENT(false),
	SWITCH_CASE_GROUP(false),

	THROW_STATEMENT(false),

	TRY_CATCH_FINALLY_STATEMENT(false),

	TRY_BLOCK_END(true),

	CATCH(false),

	FINALLY(false),

	TRY_WITH_RESOURCES_STATEMENT(false),
	RESOURCES_LIST(false),
	RESOURCE(false),

	WHILE_STATEMENT(false),

	MODIFIERS_VARIABLE_DECLARATION_ELEMENT(false),
	VARIABLE_DECLARATION_STATEMENT(false),
	VARIABLE_MODIFIER_HOLDER(true),
    VARIABLE_DECLARATOR(false),
	VAR_NAME_DECLARATION(true),

	REPLACE(true),
	REPLACE_END(true),
	
	LAST_ENCODED(true),
	
	CLASS_MODIFIERS(false, true),
    FIELD_MODIFIERS(false, true),
    CONSTRUCTOR_MODIFIERS(false, true),
    CLASS_METHOD_MODIFIERS(false, true),
    PARAMETER_MODIFIERS(false, true),
    INTERFACE_MODIFIERS(false, true),
    INTERFACE_METHOD_MODIFIERS(false, true),
    VARIABLE_MODIFIERS(false, true),

    BYTECODE_ENCODED_TYPE_REFERENCE(false, true),
    
    PRIMARY_LIST_VARIABLE_REFERENCE(false, true),

    INITIALIZER_VARIABLE_DECLARATION_ELEMENT(false, true);

    private final boolean leaf;
    private final boolean placeholder;

    private ParseTreeElement(boolean leaf) {
        this(leaf, false);
    }

    private ParseTreeElement(boolean leaf, boolean placeholder) {
        this.leaf = leaf;
        this.placeholder = placeholder;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }
}
