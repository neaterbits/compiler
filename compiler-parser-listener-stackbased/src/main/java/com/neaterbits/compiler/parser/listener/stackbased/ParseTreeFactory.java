package com.neaterbits.compiler.parser.listener.stackbased;

import java.math.BigDecimal;
import java.util.List;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.block.ConstructorInvocation;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.typedefinition.ClassMethodModifier;
import com.neaterbits.compiler.util.typedefinition.ClassModifier;
import com.neaterbits.compiler.util.typedefinition.ConstructorModifier;
import com.neaterbits.compiler.util.typedefinition.FieldModifier;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodModifier;
import com.neaterbits.compiler.util.typedefinition.InterfaceModifier;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;
import com.neaterbits.compiler.util.typedefinition.VariableModifier;

public interface ParseTreeFactory<
	KEYWORD,
	NAME,
	NAME_LIST,
	TYPE_REFERENCE,
	COMPILATION_UNIT,
	IMPORT,
	COMPILATION_CODE,
	NAMESPACE,
	COMPLEX_MEMBER_DEFINITION,
	STATIC_INITIALIZER,
	ANNOTATION,
	ANNOTATION_ELEMENT,
	CLASS_MODIFIER_HOLDER,
	GENERIC_TYPE,
    NAMED_GENERIC_TYPE extends GENERIC_TYPE,
    WILDCARD_GENERIC_TYPE extends GENERIC_TYPE,
    TYPE_BOUND,
	CLASS_DEFINITION,
	CONSTRUCTOR_MEMBER,
	CONSTRUCTOR_MODIFIER_HOLDER,
	CONSTRUCTOR_NAME,
	CONSTRUCTOR,
	CONSTRUCTOR_INVOCATION_STATEMENT,
	
	CLASS_METHOD_MEMBER,
	CLASS_METHOD_MODIFIER_HOLDER,

	FIELD_MEMBER,
	FIELD_MODIFIER_HOLDER,

	INTERFACE_MODIFIER_HOLDER,
	INTERFACE_DEFINITION,

	INTERFACE_METHOD_MEMBER,
	INTERFACE_METHOD_MODIFIER_HOLDER,

	ENUM_DEFINITION,
	ENUM_CONSTANT_DEFINITION,

	VARIABLE_MODIFIER_HOLDER,
	
	BLOCK,
	
	STATEMENT,
	
	EXPRESSION,
	PRIMARY extends EXPRESSION,
	VARIABLE_REFERENCE extends PRIMARY,
	LITERAL extends PRIMARY,
	
	EXPRESSION_LIST,
	
	ASSIGNMENT_EXPRESSION extends EXPRESSION,

	CAST_EXPRESSION extends EXPRESSION,

	CONDITIONAL_EXPRESSION extends EXPRESSION,
	
	PRE_INCREMENT_EXPRESSION extends EXPRESSION,
	POST_INCREMENT_EXPRESSION extends EXPRESSION,
	
	PRE_DECREMENT_EXPRESSION extends EXPRESSION,
	POST_DECREMENT_EXPRESSION extends EXPRESSION,
	
	LAMBDA_EXPRESSION extends EXPRESSION,
	SINGLE_LAMBDA_EXPRESSION extends LAMBDA_EXPRESSION,
	BLOCK_LAMBDA_EXPRESSION extends LAMBDA_EXPRESSION,
	LAMBDA_EXPRESSION_PARAMETERS,
	
	PRIMARY_LIST extends PRIMARY,

	NESTED_EXPRESSION extends EXPRESSION,
	FIELD_ACCESS extends PRIMARY,
	THIS_PRIMARY extends PRIMARY,
	CLASS_INSTANCE_CREATION_EXPRESSION extends PRIMARY,
	UNRESOLVED_METHOD_INVOCATION_EXPRESSION extends PRIMARY,
	METHOD_INVOCATION_EXPRESSION extends PRIMARY,
	ARRAY_CREATION_EXPRESSION extends PRIMARY,
	ARRAY_ACCESS_EXPRESSION extends PRIMARY,
	CLASS_EXPRESSION extends PRIMARY,
	
	NAME_REFERENCE extends VARIABLE_REFERENCE,
	
	INTEGER_LITERAL extends LITERAL,
	FLOATING_POINT_LITERAL extends LITERAL,
	BOOLEAN_LITERAL extends LITERAL,
	CHARACTER_LITERAL extends LITERAL,
	STRING_LITERAL extends LITERAL,
	NULL_LITERAL extends LITERAL,
	
	PARAMETER,
	
	INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
	VARIABLE_DECLARATION_STATEMENT extends STATEMENT,

	EXPRESSION_STATEMENT extends STATEMENT,
	
	ASSIGNMENT_STATEMENT extends STATEMENT,
	
	FOR_INIT,
	FOR_STATEMENT extends STATEMENT,
	
	ITERATOR_FOR_STATEMENT extends STATEMENT,
	
	WHILE_STATEMENT,
	
	DO_WHILE_STATEMENT,
	
	RESOURCE,
	CATCH_BLOCK,
	TRY_CATCH_FINALLY extends STATEMENT,
	TRY_WITH_RESOURCES extends STATEMENT,
	
	RETURN_STATEMENT extends STATEMENT,
	
	THROW_STATEMENT extends STATEMENT,
	
	IF_ELSE_IF_ELSE_STATEMENT extends STATEMENT,
	CONDITION_BLOCK,
    IF_CONDITION_BLOCK extends CONDITION_BLOCK,
    ELSE_IF_CONDITION_BLOCK extends CONDITION_BLOCK,
    ELSE_BLOCK,
	
	SWITCH_CASE_LABEL,
	CONSTANT_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
	ENUM_CONSTANT,
	ENUM_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
	DEFAULT_SWITCH_CASE_LABEL extends SWITCH_CASE_LABEL,
	SWITCH_CASE_GROUP,
	SWITCH_CASE_STATEMENT extends STATEMENT,
	
	BREAK_STATEMENT extends STATEMENT

	> {

	KEYWORD createKeyword(Context context, String name);

	NAME createName(Context context, String name);
	
	NAME_LIST createNameList(Context context, List<NAME> names);

    TYPE_REFERENCE createScalarTypeReference(Context context, String name);

	TYPE_REFERENCE createUnresolvedTypeReference(Context context, ScopedName name, ReferenceType type);
		
	COMPILATION_UNIT createCompilationUnit(Context context, List<IMPORT> imports, List<COMPILATION_CODE> code);
	
	IMPORT createImport(Context context, KEYWORD importKeyword, KEYWORD staticKeyword, List<NAME> identifiers, boolean ondemand);
	
	CLASS_MODIFIER_HOLDER createClassModifierHolder(Context context, ClassModifier classModifier);

	NAMESPACE createNamespace(
			Context context,
			KEYWORD namespaceKeyword,
			String [] parts,
			Context nameContext,
			List<COMPILATION_CODE> codeLines);
	
	NAMED_GENERIC_TYPE createNamedTypeArgument(Context context, NAME name, List<TYPE_BOUND> bounds);

	WILDCARD_GENERIC_TYPE createWildcardTypeArgument(Context context, List<TYPE_BOUND> bounds);
	
	TYPE_BOUND createTypeBound(Context context, TypeBoundType type, ScopedName name);
	
	CLASS_DEFINITION createClassDefinition(
			Context context,
			List<ANNOTATION> annotations,
			List<CLASS_MODIFIER_HOLDER> modifiers,
			KEYWORD classKeyword,
			String name,
			Context nameContext,
			List<GENERIC_TYPE> genericTypes,
			KEYWORD extendsKeyword,
			List<TYPE_REFERENCE> extendsClasses,
			List<TYPE_REFERENCE> implementsInterfaces,
			List<COMPLEX_MEMBER_DEFINITION> members);
	
	STATIC_INITIALIZER createStaticInitializer(Context context, List<STATEMENT> statements);

    ANNOTATION_ELEMENT createAnnotationElementFromExpression(Context context, NAME name, EXPRESSION value);

    ANNOTATION_ELEMENT createAnnotationElementFromAnnotation(Context context, NAME name, ANNOTATION annotation);
    
    ANNOTATION_ELEMENT createAnnotationElementFromElements(Context context, NAME name, List<ANNOTATION_ELEMENT> elements);
    
    ANNOTATION createAnnotation(Context context, ScopedName typeName, List<ANNOTATION_ELEMENT> elements);

    CONSTRUCTOR_MEMBER createConstructorMember(
			Context context,
			List<CONSTRUCTOR_MODIFIER_HOLDER> modifiers,
			String name,
			Context nameContext,
			List<PARAMETER> parameters,
			List<STATEMENT> statements);
	
	CONSTRUCTOR_MODIFIER_HOLDER createConstructorModifierHolder(Context context, ConstructorModifier modifier);

	CONSTRUCTOR_NAME createConstructorName(Context context, String name);
	
	CONSTRUCTOR_INVOCATION_STATEMENT createConstructorInvocationStatement(
			Context context,
			ConstructorInvocation type,
			EXPRESSION expression,
			List<EXPRESSION> parameters);

	CLASS_METHOD_MEMBER createClassMethodMember(
			Context context,
			List<CLASS_METHOD_MODIFIER_HOLDER> modifiers,
			TYPE_REFERENCE returnType,
			String name,
			Context nameContext,
			List<PARAMETER> parameters,
			List<STATEMENT> block);
	
	CLASS_METHOD_MODIFIER_HOLDER createClassMethodModifierHolder(Context context, ClassMethodModifier modifier);

	FIELD_MEMBER createClassFieldMember(
			Context context,
			List<FIELD_MODIFIER_HOLDER> modifiers,
			TYPE_REFERENCE type,
			List<INITIALIZER_VARIABLE_DECLARATION_ELEMENT> initializers);
	
	FIELD_MODIFIER_HOLDER createFieldModifierHolder(Context context, FieldModifier modifier);
	
	INTERFACE_MODIFIER_HOLDER createInterfaceModifierHolder(Context context, InterfaceModifier modifier);
	
	INTERFACE_DEFINITION createInterfaceDefinition(
			Context context,
			List<INTERFACE_MODIFIER_HOLDER> modifiers,
			KEYWORD interfaceKeyword,
			String name,
			Context nameContext,
			KEYWORD extendsKeyword,
			List<TYPE_REFERENCE> extendsInterfaces,
			List<COMPLEX_MEMBER_DEFINITION> members);

	INTERFACE_METHOD_MEMBER createInterfaceMethodMember(
			Context context,
			List<INTERFACE_METHOD_MODIFIER_HOLDER> modifiers,
			TYPE_REFERENCE returnType,
			String name,
			Context nameContext,
			List<PARAMETER> parameters,
			List<STATEMENT> block);

	INTERFACE_METHOD_MODIFIER_HOLDER createInterfaceMethodModifierHolder(
			Context context,
			InterfaceMethodModifier modifier);
	
	ENUM_DEFINITION createEnumDefinition(
			Context context,
			List<ANNOTATION> annotations,
			List<CLASS_MODIFIER_HOLDER> modifiers,
			KEYWORD enumKeyword,
			String name,
			Context nameContext,
			KEYWORD implementsKeyword,
			List<TYPE_REFERENCE> implementsInterfaces,
			List<ENUM_CONSTANT_DEFINITION> enumConstants,
			List<COMPLEX_MEMBER_DEFINITION> members);

	ENUM_CONSTANT_DEFINITION createEnumConstantDefinition(Context context, String name, List<EXPRESSION> parameters, List<COMPLEX_MEMBER_DEFINITION> members);
	
	Context getExpressionContext(EXPRESSION expression);

	BLOCK createBlock(Context context, List<STATEMENT> statements);
	
	EXPRESSION_LIST createExpressionList(Context context, List<Operator> operators, List<EXPRESSION> expressions);
	
	ASSIGNMENT_EXPRESSION createAssignmentExpression(Context context, VARIABLE_REFERENCE lhs, EXPRESSION rhs);
	
	NESTED_EXPRESSION createNestedExpression(Context context, EXPRESSION nestedExpression);

	CAST_EXPRESSION createCastExpression(Context context, TYPE_REFERENCE castType, EXPRESSION expression);
	
	CONDITIONAL_EXPRESSION createConditionalExpression(Context context, EXPRESSION part1, EXPRESSION part2, EXPRESSION part3);

	PRE_INCREMENT_EXPRESSION createPreIncrementExpression(Context context, EXPRESSION expression);
	POST_INCREMENT_EXPRESSION createPostIncrementExpression(Context context, EXPRESSION expression);
	PRE_DECREMENT_EXPRESSION createPreDecrementExpression(Context context, EXPRESSION expression);
	POST_DECREMENT_EXPRESSION createPostDecrementExpression(Context context, EXPRESSION expression);

	LAMBDA_EXPRESSION_PARAMETERS createLambdaExpressionParameters(Context context, String singleParameter);
	LAMBDA_EXPRESSION_PARAMETERS createLambdaExpressionParameters(Context context, List<String> inferredParameters);
	
	SINGLE_LAMBDA_EXPRESSION createSingleLambdaExpression(Context context, LAMBDA_EXPRESSION_PARAMETERS parameters, EXPRESSION expression);
	BLOCK_LAMBDA_EXPRESSION createBlockLambdaExpression(Context context, LAMBDA_EXPRESSION_PARAMETERS parameters, List<STATEMENT> statements);
	
	NAME_REFERENCE createNameReference(Context context, String name);
	
	PRIMARY_LIST createPrimaryList(Context context, List<PRIMARY> list);

	FIELD_ACCESS createFieldAccess(Context context, FieldAccessType type, TYPE_REFERENCE classType, String fieldName);
	
	THIS_PRIMARY createThisPrimary(Context context, TYPE_REFERENCE classType);
	
	CLASS_INSTANCE_CREATION_EXPRESSION createClassInstanceCreationExpression(
			Context context,
			TYPE_REFERENCE type,
			CONSTRUCTOR_NAME name,
			List<EXPRESSION> parameters,
			List<CLASS_METHOD_MEMBER> anonymousClassMethods);

   UNRESOLVED_METHOD_INVOCATION_EXPRESSION createUnresolvedMethodInvocationExpression(
            Context context,
            MethodInvocationType type,
            NAME_LIST nameList,
            String methodName,
            Context methodNameContext,
            List<EXPRESSION> parameters);

	METHOD_INVOCATION_EXPRESSION createMethodInvocationExpression(
			Context context,
			MethodInvocationType type,
			TYPE_REFERENCE classType,
			EXPRESSION object,
			String methodName,
			Context methodNameContext,
			List<EXPRESSION> parameters);
	
	ARRAY_CREATION_EXPRESSION createArrayCreationExpression(Context context, TYPE_REFERENCE type, List<EXPRESSION> dimExpressions, int numDims);
	
	ARRAY_ACCESS_EXPRESSION createArrayAccessExpression(Context context, PRIMARY primary, EXPRESSION expression);
	
	CLASS_EXPRESSION createClassExpression(Context context, String className, int numArrayDims);
	
	INTEGER_LITERAL createIntegerLiteral(Context context, long value, Base base, boolean signed, int bits);

	FLOATING_POINT_LITERAL createFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits);

	BOOLEAN_LITERAL createBooleanLiteral(Context context, boolean value);

	CHARACTER_LITERAL createCharacterLiteral(Context context, char value);

	STRING_LITERAL createStringLiteral(Context context, String value);

	NULL_LITERAL createNullLiteral(Context context);
	
	PARAMETER createParameter(Context context, TYPE_REFERENCE type, String name, Context nameContext, boolean varArgs);

	VARIABLE_MODIFIER_HOLDER createVariableModifierHolder(Context context, VariableModifier modifier);

	INITIALIZER_VARIABLE_DECLARATION_ELEMENT createInitializerVariableDeclarationElement(
			Context context,
			String varName,
			Context varNameContext,
			int numDims,
			EXPRESSION initializer);
	
	VARIABLE_DECLARATION_STATEMENT createVariableDeclarationStatement(
			Context context,
			List<VARIABLE_MODIFIER_HOLDER> modifiers,
			TYPE_REFERENCE typeReference,
			List<INITIALIZER_VARIABLE_DECLARATION_ELEMENT> elements);
	
	EXPRESSION_STATEMENT createExpressionStatement(Context context, EXPRESSION expression);
	
	ASSIGNMENT_STATEMENT createAssignmentStatement(Context context, ASSIGNMENT_EXPRESSION assignmentExpression);
	
	FOR_INIT createForInit(Context context, INITIALIZER_VARIABLE_DECLARATION_ELEMENT initializer);
	FOR_INIT createForInit(Context context, List<EXPRESSION> expressionList);
	FOR_STATEMENT createForStatement(
			Context context,
			KEYWORD keyword,
			FOR_INIT forInit,
			EXPRESSION condition,
			List<EXPRESSION> expressionList,
			List<STATEMENT> statements);

	ITERATOR_FOR_STATEMENT createIteratorForStatement(
			Context context,
			List<VARIABLE_MODIFIER_HOLDER> modifiers,
			TYPE_REFERENCE type,
			String varName,
			Context varNameContext,
			int numDims,
			EXPRESSION expression,
			List<STATEMENT> statements);

	WHILE_STATEMENT createWhileStatement(Context context, EXPRESSION condition, List<STATEMENT> statements);

	DO_WHILE_STATEMENT createDoWhileStatement(Context context, EXPRESSION expression, List<STATEMENT> statements);

	RESOURCE createResource(
			Context context,
			List<VARIABLE_MODIFIER_HOLDER> modifiers,
			TYPE_REFERENCE type,
			String varName,
			Context varNameContext,
			int numDims,
			EXPRESSION initializer);

	CATCH_BLOCK createCatchBlock(Context context, List<TYPE_REFERENCE> exceptionTypes, String exceptionVarName, List<STATEMENT> statements);
	
	TRY_CATCH_FINALLY createTryCatchFinallyStatement(Context context, List<STATEMENT> tryBlock, List<CATCH_BLOCK> catchBlocks, List<STATEMENT> finallyBlock);

	TRY_WITH_RESOURCES createTryWithResourcesStatement(Context context, List<RESOURCE> resources, List<STATEMENT> tryBlock, List<CATCH_BLOCK> catchBlocks, List<STATEMENT> finallyBlock);

	RETURN_STATEMENT createReturnStatement(Context context, EXPRESSION expression);

	THROW_STATEMENT createThrowStatement(Context context, EXPRESSION expression);

	IF_ELSE_IF_ELSE_STATEMENT createIfElseIfElseStatement(Context context, List<CONDITION_BLOCK> conditions, KEYWORD elseKeyword, BLOCK elseBlock);

	IF_CONDITION_BLOCK createIfConditionBlock(Context context, KEYWORD ifKeyword, EXPRESSION condition, BLOCK block);

    ELSE_IF_CONDITION_BLOCK createElseIfConditionBlock(Context context, KEYWORD elseIfKeyword, EXPRESSION condition, BLOCK block);

	CONSTANT_SWITCH_CASE_LABEL createConstantSwitchCaseLabel(Context context, KEYWORD keyword, EXPRESSION constant);

	ENUM_CONSTANT createEnumConstant(Context context, String enumConstant);
	
	ENUM_SWITCH_CASE_LABEL createEnumSwitchCaseLabel(Context context, KEYWORD keyword, ENUM_CONSTANT enumConstant);
	
	DEFAULT_SWITCH_CASE_LABEL createDefaultSwitchCaseLabel(Context context, KEYWORD keyword);
	
	SWITCH_CASE_GROUP createSwitchCaseGroup(Context context, List<SWITCH_CASE_LABEL> labels, BLOCK block);
	
	SWITCH_CASE_STATEMENT createSwitchCaseStatement(
			Context context,
			KEYWORD keyword,
			EXPRESSION expression,
			List<SWITCH_CASE_GROUP> groups);
	
	BREAK_STATEMENT createBreakStatement(Context context, KEYWORD keyword, String label);
	
	VARIABLE_REFERENCE makeVariableReference(Context context, List<PRIMARY> primaries);
}
