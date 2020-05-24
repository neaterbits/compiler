package com.neaterbits.compiler.util.model;

public enum ParseTreeElement {

	PROGRAM,
	MODULE,
	FILE,
	COMPILATION_UNIT,
	COMPILATION_CODE_LINES,
	NAMESPACE,
    NAMESPACE_PART,
	NAMESPACE_DECLARATION,
	
	IMPORT,
	IMPORT_NAME_PART,
	
	CLASS_DEFINITION,
	CLASS_MODIFIERS(true),
	CLASS_MODIFIER_HOLDER,
	CLASS_DECLARATION_NAME,
	
	CLASS_EXTENDS,
	CLASS_EXTENDS_NAME_PART,
	
	CONSTRUCTOR_MEMBER,
	CONSTRUCTOR,
	CONSTRUCTOR_NAME,
	CONSTRUCTOR_MODIFIERS(true),
	CONSTRUCTOR_MODIFIER_HOLDER,

	CLASS_METHOD_MEMBER,
	CLASS_METHOD_MODIFIERS(true),
	CLASS_METHOD_MODIFIER_HOLDER,
	CLASS_METHOD,
	METHOD_NAME,
	
	PARAMETER_LIST(true),
	PARAMETER,
	PARAMETER_NAME,
	
	CLASS_DATA_FIELD_MEMBER,
	FIELD_MODIFIERS(true),
	FIELD_MODIFIER_HOLDER,
	FIELD_NAME_DECLARATION,
	
	INNER_CLASS_MEMBER,
	
	STATIC_INITIALIZER,
	
	FUNCTION,

	INTERFACE_DEFINITION,
	INTERFACE_MODIFIERS(true),
	INTERFACE_MODIFIER_HOLDER,
	INTERFACE_DECLARATION_NAME,
	
	INTERFACE_METHOD_MEMBER,
	INTERFACE_METHOD,
	INTERFACE_METHOD_MODIFIERS(true),
	INTERFACE_METHOD_MODIFIER_HOLDER,
	INTERFACE_METHOD_NAME,
	
	ENUM_DEFINITION,
	ENUM_CONSTANT_DEFINITION,
	
	STRUCT_DEFINITION,
	STRUCT_DECLARATION_NAME,
	STRUCT_DATA_FIELD_MEMBER,
	
	COMPLEX_TYPE_REFERENCE,
	ENCODED_TYPE_REFERENCE,
	FUNCTION_POINTER_TYPE_REFERENCE,
	LIBRARY_TYPE_REFERENCE,
	POINTER_TYPE_REFERENCE,
	RESOLVE_LATER_TYPE_REFERENCE,
	SCALAR_TYPE_REFERENCE,
	TYPEDEF_TYPE_REFERENCE,
	UNNAMED_VOID_TYPE_REFERENCE,
	
	FUNCTION_NAME,
	
	BLOCK,
	CONDITION_BLOCK,
	CATCH_BLOCK,
	
	CHARACTER_LITERAL,
	INTEGER_LITERAL,
	FLOATING_POINT_LITERAL,
	STRING_LITERAL,
	BOOLEAN_LITERAL,
	NULL_LITERAL,
	
	ENUM_CONSTANT,
	
	ARRAY_ACCESS_REFERENCE,
	ARRAY_CREATION_EXPRESSION,
	ASSIGNMENT_EXPRESSION,
	BLOCK_LAMBDA_EXPRESSION,
	LAMBDA_EXPRESSION_PARAMETERS,
	CAST_EXPRESSION,
	CLASS_EXPRESSION,
	CLASS_INSTANCE_CREATION_EXPRESSION,
	CONDITIONAL_EXPRESSION,
	EXPRESSION_LIST,
	FIELD_ACCESS_REFERENCE,
	FUNCTION_CALL_EXPRESSION,
	FUNCTION_POINTER_INVOCATION_EXPRESSION,
	METHOD_INVOCATION_EXPRESSION,
	NAME_REFERENCE,
	NESTED_EXPRESSION,
	POST_DECREMENT_EXPRESSION,
	POST_INCREMENT_EXPRESSION,
	PRE_DECREMENT_EXPRESSION,
	PRE_INCREMENT_EXPRESSION,
	PRIMARY_LIST,
	PRIMARY_LIST_VARIABLE_REFERENCE,
	SIMPLE_VARIABLE_REFERENCE,
	SINGLE_LAMBDA_EXPRESSION,
	STATIC_MEMBER_REFERENCE,
	THIS_PRIMARY,
	VARIABLE_EXPRESSION,
	
	CONSTANT_SWITCH_CASE_LABEL,
	DEFAULT_SWITCH_CASE_LABEL,
	ENUM_SWITCH_CASE_LABEL,
	
	ARITHMETIC_BINARY_EXPRESSION,
	ARRAY_ACCESS_EXPRESSION,
	ASSIGNMENT_STATEMENT,
	BREAK_STATEMENT,
	CONSTRUCTOR_INVOCATION_STATEMENT,
	
	DO_WHILE_STATEMENT,

	EXPRESSION_STATEMENT,

	FIELD_ACCESS,
	
	FOR_STATEMENT,
	FOR_INIT,
	FOR_EXPRESSION_LIST,

	IF_ELSE_IF_ELSE_STATEMENT,
	ITERATOR_FOR_STATEMENT,
	
	RETURN_STATEMENT,
	
	SWITCH_CASE_STATEMENT,
	SWITCH_CASE_GROUP,
	
	THROW_STATEMENT,
	
	TRY_CATCH_FINALLY_STATEMENT,
	
	TRY_WITH_RESOURCES_STATEMENT,
	RESOURCES_LIST,
	
	WHILE_STATEMENT,
	
	MODIFIERS_VARIABLE_DECLARATION_ELEMENT,
	VARIABLE_DECLARATION_STATEMENT,
	VARIABLE_MODIFIERS(true),
	VARIABLE_MODIFIER_HOLDER,
	VAR_NAME_DECLARATION,
	INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
	
	KEYWORD,
	IDENTIFIER;

    
    private final boolean placeholder;

    private ParseTreeElement() {
        this(false);
    }

    private ParseTreeElement(boolean placeholder) {
        this.placeholder = placeholder;
    }



    public boolean isPlaceholder() {
        return placeholder;
    }
}
