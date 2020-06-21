package com.neaterbits.compiler.parser.listener.stackbased;

import com.neaterbits.compiler.parser.listener.common.ContextAccess;
import com.neaterbits.compiler.parser.listener.common.InfixParserListener;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackExpressionList;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackIncrementDecrementExpression;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Notation;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

public abstract class BaseInfixParserListener<

		KEYWORD,
		IDENTIFIER,
		TYPE_REFERENCE,
		INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
		VARIABLE_MODIFIER_HOLDER,
		VARIABLE_DECLARATION,
		COMPILATION_UNIT,
		IMPORT,
		COMPILATION_CODE,
		NAMESPACE,
		COMPLEX_MEMBER_DEFINITION,
		STATIC_INITIALIZER extends COMPLEX_MEMBER_DEFINITION,
		CLASS_MODIFIER_HOLDER,
		CLASS_DEFINITION,
		CONSTRUCTOR_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		CONSTRUCTOR_MODIFIER_HOLDER,
		CONSTRUCTOR_NAME,
		CONSTRUCTOR,
		CONSTRUCTOR_INVOCATION_STATEMENT extends STATEMENT,
		
		CLASS_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		CLASS_METHOD_MODIFIER_HOLDER,
		
		CLASS_FIELD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		FIELD_MODIFIER_HOLDER,
		
		INTERFACE_MODIFIER_HOLDER,
		INTERFACE_DEFINITION,
		
		INTERFACE_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		INTERFACE_METHOD_MODIFIER_HOLDER,
		
		ENUM_DEFINITION,
		ENUM_CONSTANT_DEFINITION,
		
		BLOCK,
		
		STATEMENT,
		
		EXPRESSION,
		PRIMARY extends EXPRESSION,
		VARIABLE_REFERENCE extends PRIMARY,
		LITERAL extends PRIMARY,
		
		EXPRESSION_LIST extends EXPRESSION,
		
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
		
		NESTED_EXPRESSION extends PRIMARY,
		FIELD_ACCESS extends PRIMARY,
		THIS_PRIMARY extends PRIMARY,
		CLASS_INSTANCE_CREATION_EXPRESSION extends PRIMARY,
		METHOD_INVOCATION_EXPRESSION extends PRIMARY,
		ARRAY_CREATION_EXPRESSION extends PRIMARY,
		ARRAY_ACCESS_EXPRESSION extends PRIMARY,
		CLASS_EXPRESSION extends PRIMARY,
		
		NAME_REFERENCE extends VARIABLE_REFERENCE,
		SIMPLE_VARIABLE_REFERENCE extends VARIABLE_REFERENCE,
		
		INTEGER_LITERAL extends LITERAL,
		FLOATING_POINT_LITERAL extends LITERAL,
		BOOLEAN_LITERAL extends LITERAL,
		CHARACTER_LITERAL extends LITERAL,
		STRING_LITERAL extends LITERAL,
		NULL_LITERAL extends LITERAL,
		
		PARAMETER,
		
		VARIABLE_DECLARATION_STATEMENT extends STATEMENT,
		EXPRESSION_STATEMENT extends STATEMENT,
        ASSIGNMENT_STATEMENT extends STATEMENT,
		
		FOR_INIT,
		FOR_EXPRESSION_LIST,
		FOR_STATEMENT extends STATEMENT,
		ITERATOR_FOR_STATEMENT extends STATEMENT,
		
		WHILE_STATEMENT extends STATEMENT,
		
		DO_WHILE_STATEMENT extends STATEMENT,
		
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
		>

	extends BaseParserListener<

		KEYWORD,
		IDENTIFIER,
		TYPE_REFERENCE,
		INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
		VARIABLE_MODIFIER_HOLDER,
		VARIABLE_DECLARATION,
		COMPILATION_UNIT,
		IMPORT,
		COMPILATION_CODE,
		NAMESPACE,
		COMPLEX_MEMBER_DEFINITION,
		STATIC_INITIALIZER,
		CLASS_MODIFIER_HOLDER,
		CLASS_DEFINITION,
		CONSTRUCTOR_MEMBER,
		CONSTRUCTOR_MODIFIER_HOLDER,
		CONSTRUCTOR_NAME,
		CONSTRUCTOR,
		CONSTRUCTOR_INVOCATION_STATEMENT,
	
		CLASS_METHOD_MEMBER,
		CLASS_METHOD_MODIFIER_HOLDER,
		
		CLASS_FIELD_MEMBER,
		FIELD_MODIFIER_HOLDER,
		
		INTERFACE_MODIFIER_HOLDER,
		INTERFACE_DEFINITION,
		
		INTERFACE_METHOD_MEMBER,
		INTERFACE_METHOD_MODIFIER_HOLDER,
		
		ENUM_DEFINITION,
		ENUM_CONSTANT_DEFINITION,
		
		BLOCK,
		
		STATEMENT,
	
		EXPRESSION,
		PRIMARY,
		VARIABLE_REFERENCE,
		LITERAL,
		
		EXPRESSION_LIST,
		
		ASSIGNMENT_EXPRESSION,
		
		CAST_EXPRESSION,
		
		CONDITIONAL_EXPRESSION,
	
		PRE_INCREMENT_EXPRESSION,
		POST_INCREMENT_EXPRESSION,
		
		PRE_DECREMENT_EXPRESSION,
		POST_DECREMENT_EXPRESSION,

		LAMBDA_EXPRESSION,
		SINGLE_LAMBDA_EXPRESSION,
		BLOCK_LAMBDA_EXPRESSION,
		LAMBDA_EXPRESSION_PARAMETERS,
	
		PRIMARY_LIST,
		
		NESTED_EXPRESSION,
		FIELD_ACCESS,
		THIS_PRIMARY,
		CLASS_INSTANCE_CREATION_EXPRESSION,
		METHOD_INVOCATION_EXPRESSION,
		ARRAY_CREATION_EXPRESSION,
		ARRAY_ACCESS_EXPRESSION,
		CLASS_EXPRESSION,
		
		NAME_REFERENCE,
		SIMPLE_VARIABLE_REFERENCE,
		
		INTEGER_LITERAL,
		FLOATING_POINT_LITERAL,
		BOOLEAN_LITERAL,
		CHARACTER_LITERAL,
		STRING_LITERAL,
		NULL_LITERAL,
		
		PARAMETER,
		
		VARIABLE_DECLARATION_STATEMENT,
		EXPRESSION_STATEMENT,
		ASSIGNMENT_STATEMENT,
		
		FOR_INIT,
		FOR_EXPRESSION_LIST,
		FOR_STATEMENT,
		ITERATOR_FOR_STATEMENT,
		
		WHILE_STATEMENT,
		
		DO_WHILE_STATEMENT,
		
		RESOURCE,
		CATCH_BLOCK,
		TRY_CATCH_FINALLY,
		TRY_WITH_RESOURCES,
		
		RETURN_STATEMENT,
		
		THROW_STATEMENT,
		
		IF_ELSE_IF_ELSE_STATEMENT,
        CONDITION_BLOCK,
        IF_CONDITION_BLOCK,
        ELSE_IF_CONDITION_BLOCK,
        ELSE_BLOCK,
		
		SWITCH_CASE_LABEL,
		CONSTANT_SWITCH_CASE_LABEL,
		ENUM_CONSTANT,
		ENUM_SWITCH_CASE_LABEL,
		DEFAULT_SWITCH_CASE_LABEL,
		SWITCH_CASE_GROUP,
		SWITCH_CASE_STATEMENT,
		
		BREAK_STATEMENT
		>

	implements InfixParserListener<COMPILATION_UNIT> {

	protected BaseInfixParserListener(
	        StringSource stringSource,
	        ContextAccess contextAccess,
	        ParseLogger logger,
	        @SuppressWarnings("rawtypes") ParseTreeFactory parseTreeFactory) {

		super(stringSource, contextAccess, logger, parseTreeFactory);
	}

	@Override
	public final void onExpressionBinaryOperator(int leafContext, Operator operator) {
		
	    final Context context = getLeafContext(leafContext);

		logEnter(context);
		
		final StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> expressionList = get();
		
		expressionList.addOperator(operator);
		
		logExit(context);
	}
	
	@Override
	public final void onIncrementDecrementExpressionStart(int startContext, Arithmetic operator, Notation notation) {

	    final Context context = getStartContext(startContext);

		logEnter(context);
		
		push(new StackIncrementDecrementExpression<>(getLogger(), operator, notation));
		
		logExit(context);
	}

	@Override
	public final void onIncrementDecrementExpressionEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);

		final StackIncrementDecrementExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackIncrementDecrementExpression = pop();
		
		final EXPRESSION expression;
		
		switch (stackIncrementDecrementExpression.getOperator()) {
		case INCREMENT:
			switch (stackIncrementDecrementExpression.getNotation()) {
			case POSTFIX:
				expression = parseTreeFactory.createPostIncrementExpression(context, stackIncrementDecrementExpression.getExpression());
				break;
				
			case PREFIX:
				expression = parseTreeFactory.createPreIncrementExpression(context, stackIncrementDecrementExpression.getExpression());
				break;
				
			default:
				throw new UnsupportedOperationException("Unknown notation " + stackIncrementDecrementExpression.getNotation());
			}
			break;
			
		case DECREMENT:
			switch (stackIncrementDecrementExpression.getNotation()) {
			case POSTFIX:
				expression = parseTreeFactory.createPostDecrementExpression(context, stackIncrementDecrementExpression.getExpression());
				break;
				
			case PREFIX:
				expression = parseTreeFactory.createPreDecrementExpression(context, stackIncrementDecrementExpression.getExpression());
				break;
				
			default:
				throw new UnsupportedOperationException("Unknown notation " + stackIncrementDecrementExpression.getNotation());
			}
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown operator " + stackIncrementDecrementExpression.getOperator());
		}
		
		final ExpressionSetter<EXPRESSION> expressionSetter = get();
		
		expressionSetter.addExpression(expression);
	}
}
