package dev.nimbler.compiler.parser.listener.stackbased;

import org.jutils.io.strings.StringSource;
import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.listener.common.ContextAccess;
import dev.nimbler.compiler.parser.listener.common.InfixParseTreeListener;
import dev.nimbler.compiler.parser.listener.stackbased.state.StackExpressionList;
import dev.nimbler.compiler.parser.listener.stackbased.state.StackIncrementDecrementExpression;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import dev.nimbler.compiler.types.operator.IncrementDecrement;
import dev.nimbler.compiler.types.operator.Operator;
import dev.nimbler.compiler.types.operator.UnaryOperator;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.parse.ParseLogger;

public abstract class BaseInfixParserListener<

		KEYWORD,
		NAME,
		NAME_LIST,
		TYPE_REFERENCE,
		INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
		VARIABLE_MODIFIER_HOLDER,
		COMPILATION_UNIT,
		IMPORT,
		COMPILATION_CODE,
		NAMESPACE,
        TYPE_DEFINITION,
		COMPLEX_MEMBER_DEFINITION,
		STATIC_INITIALIZER extends COMPLEX_MEMBER_DEFINITION,
	    ANNOTATION,
	    ANNOTATION_ELEMENT,
		CLASS_MODIFIER_HOLDER,
		GENERIC_TYPE_ARGUMENT,
		REFERENCE_GENERIC_TYPE_ARGUMENT extends GENERIC_TYPE_ARGUMENT,
        WILDCARD_GENERIC_TYPE_ARGUMENT extends GENERIC_TYPE_ARGUMENT,
        TYPE_BOUND,
        NAMED_GENERIC_TYPE_PARAMETER,
		CLASS_DEFINITION extends TYPE_DEFINITION,
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
		INTERFACE_DEFINITION extends TYPE_DEFINITION,
		
		INTERFACE_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		INTERFACE_METHOD_MODIFIER_HOLDER,
		
		ENUM_DEFINITION extends TYPE_DEFINITION,
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
		
		UNARY_EXPRESSION extends EXPRESSION,

		LAMBDA_EXPRESSION extends EXPRESSION,
		SINGLE_LAMBDA_EXPRESSION extends LAMBDA_EXPRESSION,
		BLOCK_LAMBDA_EXPRESSION extends LAMBDA_EXPRESSION,
		LAMBDA_EXPRESSION_PARAMETERS,
		
		PRIMARY_LIST extends PRIMARY,
		
		NAME_PRIMARY extends PRIMARY,
		
		NESTED_EXPRESSION extends PRIMARY,
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
		
		PARAMETER_MODIFIER_HOLDER,
		
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
		NAME,
		NAME_LIST,
		TYPE_REFERENCE,
		INITIALIZER_VARIABLE_DECLARATION_ELEMENT,
		VARIABLE_MODIFIER_HOLDER,
		COMPILATION_UNIT,
		IMPORT,
		COMPILATION_CODE,
		NAMESPACE,
        TYPE_DEFINITION,
		COMPLEX_MEMBER_DEFINITION,
		STATIC_INITIALIZER,
	    ANNOTATION,
	    ANNOTATION_ELEMENT,
		CLASS_MODIFIER_HOLDER,
		GENERIC_TYPE_ARGUMENT,
		REFERENCE_GENERIC_TYPE_ARGUMENT,
        WILDCARD_GENERIC_TYPE_ARGUMENT,
        TYPE_BOUND,
        NAMED_GENERIC_TYPE_PARAMETER,
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
		
		UNARY_EXPRESSION,
	
		LAMBDA_EXPRESSION,
		SINGLE_LAMBDA_EXPRESSION,
		BLOCK_LAMBDA_EXPRESSION,
		LAMBDA_EXPRESSION_PARAMETERS,
	
		PRIMARY_LIST,
		
		NAME_PRIMARY,
		
		NESTED_EXPRESSION,
		FIELD_ACCESS,
		THIS_PRIMARY,
		CLASS_INSTANCE_CREATION_EXPRESSION,
        UNRESOLVED_METHOD_INVOCATION_EXPRESSION,
		METHOD_INVOCATION_EXPRESSION,
		ARRAY_CREATION_EXPRESSION,
		ARRAY_ACCESS_EXPRESSION,
		CLASS_EXPRESSION,
		
		NAME_REFERENCE,
		
		INTEGER_LITERAL,
		FLOATING_POINT_LITERAL,
		BOOLEAN_LITERAL,
		CHARACTER_LITERAL,
		STRING_LITERAL,
		NULL_LITERAL,
		
		PARAMETER_MODIFIER_HOLDER,
		
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

	implements InfixParseTreeListener<COMPILATION_UNIT> {

	protected BaseInfixParserListener(
	        StringSource stringSource,
	        ContextAccess contextAccess,
            FullContextProvider fullContextProvider,
	        ParseLogger logger,
	        @SuppressWarnings("rawtypes") ParseTreeFactory parseTreeFactory) {

		super(stringSource, contextAccess, fullContextProvider, logger, parseTreeFactory);
	}

	
	@Override
    public void onUnaryExpressionStart(int leafContext, Operator operator) {
	    
        final Context context = getStartContext(leafContext);

        logEnter(context);
        
        final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> expressionList
            = new StackExpressionList<>(getLogger());
        
        push(expressionList);
        
        expressionList.addOperator(operator);
        
        logExit(context);
    }


    @Override
    public void onUnaryExpressionEnd(int startContext, Context endContext) {

        final Context context = getStartContext(startContext);

        logEnter(context);

        final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> expressionList = pop();

        if (expressionList.getList().size() != 1) {
            throw new IllegalStateException();
        }
        
        final UNARY_EXPRESSION expression = parseTreeFactory.createUnaryExpression(
                context,
                (UnaryOperator)expressionList.getOperators().get(0),
                expressionList.getList().get(0));
        
        final ExpressionSetter<EXPRESSION> expressionSetter = get();
        
        expressionSetter.addExpression(expression);
        
        logExit(context);
    }

    @Override
	public final void onExpressionBinaryOperator(int leafContext, Operator operator) {
		
	    final Context context = getLeafContext(leafContext);

		logEnter(context);
		
		final StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE> expressionList = get();
		
		expressionList.addOperator(operator);
		
		logExit(context);
	}
	
	@Override
	public final void onIncrementDecrementExpressionStart(int startContext, IncrementDecrement operator) {

	    final Context context = getStartContext(startContext);

		logEnter(context);
		
		push(new StackIncrementDecrementExpression<>(getLogger(), operator));
		
		logExit(context);
	}

	@Override
	public final void onIncrementDecrementExpressionEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);

		final StackIncrementDecrementExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> stackIncrementDecrementExpression = pop();
		
		final UNARY_EXPRESSION expression;
		
		expression = parseTreeFactory.createUnaryExpression(
		        context,
		        stackIncrementDecrementExpression.getOperator(),
		        stackIncrementDecrementExpression.getExpression());
		
		final ExpressionSetter<EXPRESSION> expressionSetter = get();
		
		expressionSetter.addExpression(expression);
	}
}
