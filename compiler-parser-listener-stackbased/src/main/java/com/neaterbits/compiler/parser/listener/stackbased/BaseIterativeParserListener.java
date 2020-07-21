package com.neaterbits.compiler.parser.listener.stackbased;

import com.neaterbits.compiler.parser.listener.common.ContextAccess;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackConstantSwitchLabel;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackElseBlock;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackElseIfConditionBlock;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackIfConditionBlock;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackIfElseIfElse;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackSwitchCase;
import com.neaterbits.compiler.parser.listener.stackbased.state.StackSwitchCaseGroup;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

public abstract class BaseIterativeParserListener<

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
        GENERIC_TYPE,
        NAMED_GENERIC_TYPE extends GENERIC_TYPE,
        WILDCARD_GENERIC_TYPE extends GENERIC_TYPE,
        TYPE_BOUND,
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
		
		PRE_INCREMENT_EXPRESSION extends EXPRESSION,
		POST_INCREMENT_EXPRESSION extends EXPRESSION,
		
		PRE_DECREMENT_EXPRESSION extends EXPRESSION,
		POST_DECREMENT_EXPRESSION extends EXPRESSION,
		
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

	extends BaseInfixParserListener<

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
		GENERIC_TYPE,
        NAMED_GENERIC_TYPE,
        WILDCARD_GENERIC_TYPE,
        TYPE_BOUND,
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
	implements IterativeParserListener<COMPILATION_UNIT> {
	
	protected BaseIterativeParserListener(
	        StringSource stringSource,
            ContextAccess contextAccess,
	        ParseLogger logger,
	        @SuppressWarnings("rawtypes") ParseTreeFactory parseTreeFactory) {
		super(stringSource, contextAccess, logger, parseTreeFactory);
	}

	@Override
	public final void onIfStatementStart(int startContext, long ifKeyword, int ifKeywordContext) {
		
	    final Context context = getStartContext(startContext);
	    
	    logEnter(context);
	    
		push(new StackIfElseIfElse<>(getLogger()));
		push(new StackIfConditionBlock<>(
		        getLogger(),
		        context,
		        stringSource.asString(ifKeyword),
		        getOtherContext(ifKeywordContext)));
		
		logExit(context);
	}
	
	private void popAndAddIfConditionBlock(Context context) {
		
		final StackIfConditionBlock<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackConditionBlock = pop();
		
		final StackIfElseIfElse<KEYWORD, CONDITION_BLOCK, STATEMENT> ifElseIfElse = get();
		
		final IF_CONDITION_BLOCK conditionBlock = parseTreeFactory.createIfConditionBlock(
				stackConditionBlock.getUpdatedContext(),
				parseTreeFactory.createKeyword(
				        stackConditionBlock.getIfKeywordContext(),
				        stackConditionBlock.getIfKeyword()),
				makeExpression(context, stackConditionBlock),
				parseTreeFactory.createBlock(context, stackConditionBlock.getStatements()));

		ifElseIfElse.add(conditionBlock);
	}

	   private void popAndAddElseIfConditionBlock(Context context) {
	        
	        final StackElseIfConditionBlock<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackConditionBlock = pop();
	        
	        final StackIfElseIfElse<KEYWORD, CONDITION_BLOCK, STATEMENT> ifElseIfElse = get();
	        
	        final ELSE_IF_CONDITION_BLOCK conditionBlock = parseTreeFactory.createElseIfConditionBlock(
	                stackConditionBlock.getUpdatedContext(),
	                parseTreeFactory.createKeyword(
	                        stackConditionBlock.getElseKeywordContext(),
	                        stackConditionBlock.getElseKeyword()),
	                makeExpression(context, stackConditionBlock),
	                parseTreeFactory.createBlock(context, stackConditionBlock.getStatements()));

	        ifElseIfElse.add(conditionBlock);
	    }

	@Override
    public void onIfStatementInitialBlockStart(int ifStatementInitialBlockStartContext) {

        final Context context = getStartContext(ifStatementInitialBlockStartContext);

        logEnter(context);

        logExit(context);
    }

    // End of initial if-statement and block
	@Override
	public final void onIfStatementInitialBlockEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);
		
		popAndAddIfConditionBlock(context);

		logExit(context);
	}

	@Override
	public final void onElseIfStatementStart(
	        int startContext,
	        long elseKeyword, int elseKeywordContext,
	        long ifKeyword, int ifKeywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);
		
		push(new StackElseIfConditionBlock<>(
		        getLogger(),
		        context,
		        stringSource.asString(elseKeyword),
		        getOtherContext(elseKeywordContext),
		        stringSource.asString(ifKeyword),
                getOtherContext(ifKeywordContext)));
		
		logExit(context);
	}
	
	@Override
	public final void onElseIfStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);

		popAndAddElseIfConditionBlock(context);
		
		logExit(context);
	}

	
	/*
	public final void onIfElseIfExpressionEnd(Context context) {
		popAndAddConditionBlock(context);
	}
	*/

	@Override
	public final void onElseStatementStart(int startContext, long keyword, int keywordContext) {
		
	    final Context context = getStartContext(startContext);
	    
		logEnter(context);
		
		push(new StackElseBlock<>(getLogger(), stringSource.asString(keyword), getOtherContext(keywordContext)));
		
		logExit(context);
	}
	
	@Override
	public final void onElseStatementEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);

		logEnter(context);
		
		final StackElseBlock<STATEMENT> stackBlock = pop();

		final StackIfElseIfElse<KEYWORD, CONDITION_BLOCK, BLOCK> ifElseIfElse = get();
		
		ifElseIfElse.setElseBlock(
				stackBlock.getKeyword() != null
					? parseTreeFactory.createKeyword(stackBlock.getKeywordContext(), stackBlock.getKeyword())
					: null,
				parseTreeFactory.createBlock(context, stackBlock.getList()));
		
		logExit(context);
	}

	// Called after last part of statement (ie end if in if-else if-else-end if)
	@Override
	public final void onEndIfStatement(int startContext, Context endContext) {
		
	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);
		
		final StackIfElseIfElse<KEYWORD, CONDITION_BLOCK, BLOCK> ifElseIfElse = pop();

		final StatementSetter<STATEMENT> statementSetter = get();
		
		final IF_ELSE_IF_ELSE_STATEMENT statement = parseTreeFactory.createIfElseIfElseStatement(
				context,
				ifElseIfElse.getList(),
				ifElseIfElse.getElseKeyword(),
				ifElseIfElse.getElseBlock());
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}

	@Override
	public final void onSwitchStatementStart(int startContext, long keyword, int keywordContext) {
	    
	    final Context context = getStartContext(startContext);

		logEnter(context);
		
		push(new StackSwitchCase<>(getLogger(), stringSource.asString(keyword), getOtherContext(keywordContext)));

		logExit(context);
	}

	@Override
	public final void onJavaSwitchBlockStart(int startContext) {
		
	    final Context context = getStartContext(startContext);
	    
		logEnter(context);
		
		logExit(context);
	}
	
	@Override
	public final void onJavaSwitchBlockStatementGroupStart(int startContext) {
		
	    final Context context = getStartContext(startContext);
	    
		logEnter(context);
		
		push(new StackSwitchCaseGroup<>(getLogger()));
		
		logExit(context);
	}

	@Override
	public final void onSwitchLabelsStart(int startContext) {
	    
	    final Context context = getStartContext(startContext);
		
		logEnter(context);

		logExit(context);
	}
	
	@Override
	public final void onSwitchLabelsEnd(int startContext, Context endContext) {

	    final Context context = getEndContext(startContext, endContext);
		
		logEnter(context);

		logExit(context);
	}

	@Override
	public final void onJavaSwitchBlockStatementGroupEnd(int startContext, Context endContext) {
	    
	    final Context context = getEndContext(startContext, endContext);
		
		logEnter(context);
		
		final StackSwitchCaseGroup<SWITCH_CASE_LABEL, STATEMENT> stackSwitchCaseGroup = pop();
		
		final SWITCH_CASE_GROUP switchCaseGroup = parseTreeFactory.createSwitchCaseGroup(
				context,
				stackSwitchCaseGroup.getLabels(),
				parseTreeFactory.createBlock(context, stackSwitchCaseGroup.getStatements()));

		final StackSwitchCase<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, SWITCH_CASE_GROUP> stackSwitchCase = get();
		
		stackSwitchCase.addGroup(switchCaseGroup);
		
		logExit(context);
	}

	@Override
	public final void onConstantSwitchLabelStart(int startContext, long keyword, int keywordContext) {
		
	    final Context context = getStartContext(startContext);
	    
		logEnter(context);
		
		push(new StackConstantSwitchLabel<>(getLogger(), stringSource.asString(keyword), getOtherContext(keywordContext)));
		
		logExit(context);
	}
	
	@Override
	public final void onConstantSwitchLabelEnd(int startContext, Context endContext) {
		
	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);
		
		final StackConstantSwitchLabel<EXPRESSION, VARIABLE_REFERENCE, PRIMARY> stackLabel = pop();
		
		final CONSTANT_SWITCH_CASE_LABEL switchCaseLabel = parseTreeFactory.createConstantSwitchCaseLabel(
				context,
				parseTreeFactory.createKeyword(stackLabel.getKeywordContext(), stackLabel.getKeyword()),
				stackLabel.getExpression());
		
		final StackSwitchCaseGroup<SWITCH_CASE_LABEL, STATEMENT> stackSwitchCaseGroup = get();
		
		stackSwitchCaseGroup.addLabel(switchCaseLabel);
		
		logExit(context);
	}
	
	@Override
	public final void onEnumSwitchLabel(
			int startContext,
			long keyword, int keywordContext,
			long constantName, int constantNameContext) {
		
	    final Context context = getStartContext(startContext);
	    
		logEnter(context);
		
		final KEYWORD k = parseTreeFactory.createKeyword(getOtherContext(keywordContext), stringSource.asString(keyword));
		
		final ENUM_SWITCH_CASE_LABEL switchCaseLabel = parseTreeFactory.createEnumSwitchCaseLabel(
				context,
				k,
				parseTreeFactory.createEnumConstant(getOtherContext(constantNameContext), stringSource.asString(constantName)));
		
		final StackSwitchCaseGroup<SWITCH_CASE_LABEL, STATEMENT> stackSwitchCaseGroup = get();
		
		stackSwitchCaseGroup.addLabel(switchCaseLabel);
		
		logExit(context);
	}
	
	@Override
	public final void onDefaultSwitchLabel(int leafContext, long keyword) {
		
	    final Context context = getLeafContext(leafContext);
	    
		logEnter(context);

		final StackSwitchCaseGroup<SWITCH_CASE_LABEL, STATEMENT> stackSwitchCaseGroup = get();
		
		final KEYWORD k = parseTreeFactory.createKeyword(context, stringSource.asString(keyword));
		
		stackSwitchCaseGroup.addLabel(parseTreeFactory.createDefaultSwitchCaseLabel(context, k));
		
		logExit(context);
	}
	
	@Override
	public final void onJavaSwitchBlockEnd(int startContext, Context endContext) {
		
	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);
		
		logExit(context);
	}
	
	@Override
	public final void onSwitchStatementEnd(int startContext, Context endContext) {
		
	    final Context context = getEndContext(startContext, endContext);
	    
		logEnter(context);
		
		final StackSwitchCase<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, SWITCH_CASE_GROUP> stackSwitchCase = pop();
		
		final KEYWORD k = parseTreeFactory.createKeyword(stackSwitchCase.getKeywordContext(), stackSwitchCase.getKeyword());
		
		final SWITCH_CASE_STATEMENT statement = parseTreeFactory.createSwitchCaseStatement(
				context,
				k,
				makeExpression(context, stackSwitchCase),
				stackSwitchCase.getGroups());
		
		final StatementSetter<STATEMENT> statementSetter = get();
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}
	
	@Override
	public final void onBreakStatement(int startContext, long keyword, int keywordContext, long label, int labelContext, Context endContext) {
		
	    final Context context = getStartContext(startContext);
	    
		logEnter(context);

		final StatementSetter<STATEMENT> statementSetter = get();
		
		final KEYWORD keywordObject = parseTreeFactory.createKeyword(getOtherContext(keywordContext), stringSource.asString(keyword));
		
		final STATEMENT breakStatement
		    = parseTreeFactory.createBreakStatement(context, keywordObject, stringSource.asString(label));
		
		statementSetter.addStatement(breakStatement);
		
		logExit(context);
	}
}
