package com.neaterbits.compiler.util.model;

public enum ParseTreeElement {

	PROGRAM(false),
	MODULE(false),
	FILE(false),
	COMPILATION_UNIT(false),
	COMPILATION_CODE_LINES(false),
	NAMESPACE(false),
    NAMESPACE_PART(true),
	NAMESPACE_DECLARATION(false),
	
	IMPORT(false),
	IMPORT_NAME_PART(true),
	
	CLASS_DEFINITION(false, false),
	CLASS_MODIFIERS(true, true),
	CLASS_MODIFIER_HOLDER(true),
	CLASS_DECLARATION_NAME(true),
	
	CLASS_EXTENDS(false),
	CLASS_EXTENDS_NAME_PART(true),

	CLASS_IMPLEMENTS(false),
	CLASS_IMPLEMENTS_TYPE(false),
    CLASS_IMPLEMENTS_NAME_PART(true),
	
	CONSTRUCTOR_MEMBER(false),
	CONSTRUCTOR(false),
	CONSTRUCTOR_NAME(true),
	CONSTRUCTOR_MODIFIERS(false, true),
	CONSTRUCTOR_MODIFIER_HOLDER(true),

	CLASS_METHOD_MEMBER(false),
	CLASS_METHOD_MODIFIERS(false, true),
	CLASS_METHOD_MODIFIER_HOLDER(true),
	CLASS_METHOD(false),
    METHOD_RETURN_TYPE(false),
	METHOD_NAME(true),
	
	SIGNATURE_PARAMETERS(false),
	
	SIGNATURE_PARAMETER(false),
	
	PARAMETER_LIST(false, true),
	PARAMETER(false),
	PARAMETER_NAME(true),
	
	CLASS_DATA_FIELD_MEMBER(false),
	FIELD_MODIFIERS(false, true),
	FIELD_MODIFIER_HOLDER(true),
    FIELD_DECLARATION(false),
	FIELD_NAME_DECLARATION(true),
	
	INNER_CLASS_MEMBER(false),
	
	STATIC_INITIALIZER(false),
	
	FUNCTION(false),

	INTERFACE_DEFINITION(false, false),
	INTERFACE_MODIFIERS(false, true),
	INTERFACE_MODIFIER_HOLDER(true),
	INTERFACE_DECLARATION_NAME(true),
	
	INTERFACE_METHOD_MEMBER(false),
	INTERFACE_METHOD(false),
	INTERFACE_METHOD_MODIFIERS(false, true),
	INTERFACE_METHOD_MODIFIER_HOLDER(true),
	INTERFACE_METHOD_NAME(true),
	
	ENUM_DEFINITION(false),
	ENUM_CONSTANT_DEFINITION(false),
	
	STRUCT_DEFINITION(false),
	STRUCT_DECLARATION_NAME(true),
	STRUCT_DATA_FIELD_MEMBER(false),
	
	COMPLEX_TYPE_REFERENCE(false),
	ENCODED_TYPE_REFERENCE(false),
	FUNCTION_POINTER_TYPE_REFERENCE(false),
	LIBRARY_TYPE_REFERENCE(false),
	POINTER_TYPE_REFERENCE(false),
    RESOLVE_LATER_IDENTIFIER_TYPE_REFERENCE(false),
	RESOLVE_LATER_SCOPED_TYPE_REFERENCE(false),
    RESOLVE_LATER_SCOPED_TYPE_REFERENCE_PART(true),
	SCALAR_TYPE_REFERENCE(false),
	TYPEDEF_TYPE_REFERENCE(false),
	UNNAMED_VOID_TYPE_REFERENCE(false),
	
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
	
	ARRAY_ACCESS_REFERENCE(false),
	ARRAY_CREATION_EXPRESSION(false),
	ASSIGNMENT_EXPRESSION(false),
	ASSIGNMENT_EXPRESSION_LHS(false),
	BLOCK_LAMBDA_EXPRESSION(false),
	LAMBDA_EXPRESSION_PARAMETERS(false),
	CAST_EXPRESSION(false),
	CLASS_EXPRESSION(false),
	CLASS_INSTANCE_CREATION_EXPRESSION(false),
	CONDITIONAL_EXPRESSION(false),
	EXPRESSION_LIST(false),
	FIELD_ACCESS_REFERENCE(false),
	FUNCTION_CALL_EXPRESSION(false),
	FUNCTION_POINTER_INVOCATION_EXPRESSION(false),
	METHOD_INVOCATION_EXPRESSION(false),
	NAME_REFERENCE(true),
	NESTED_EXPRESSION(false),
	POST_DECREMENT_EXPRESSION(false),
	POST_INCREMENT_EXPRESSION(false),
	PRE_DECREMENT_EXPRESSION(false),
	PRE_INCREMENT_EXPRESSION(false),
	PRIMARY_LIST(false),
	PRIMARY_LIST_VARIABLE_REFERENCE(false),
	SIMPLE_VARIABLE_REFERENCE(true),
	SINGLE_LAMBDA_EXPRESSION(false),
	STATIC_MEMBER_REFERENCE(true),
	THIS_PRIMARY(true),
	VARIABLE_EXPRESSION(false),
	
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

	IF_ELSE_IF_ELSE_STATEMENT(false),
	IF_CONDITION_BLOCK(false),
	ELSE_IF_CONDITION_BLOCK(false),
	ELSE_BLOCK(false),

	ITERATOR_FOR_STATEMENT(false),
	
	RETURN_STATEMENT(false),
	
	SWITCH_CASE_STATEMENT(false),
	SWITCH_CASE_GROUP(false),
	
	THROW_STATEMENT(false),
	
	TRY_CATCH_FINALLY_STATEMENT(false),
	
	TRY_WITH_RESOURCES_STATEMENT(false),
	RESOURCES_LIST(false),
	
	WHILE_STATEMENT(false),
	
	MODIFIERS_VARIABLE_DECLARATION_ELEMENT(false),
	VARIABLE_DECLARATION_STATEMENT(false),
	VARIABLE_MODIFIERS(false, true),
	VARIABLE_MODIFIER_HOLDER(true),
    VARIABLE_DECLARATION_ELEMENT(false),
	VAR_NAME_DECLARATION(true),
	INITIALIZER_VARIABLE_DECLARATION_ELEMENT(false),
	
	KEYWORD(true),
	NAME(true);
    
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
