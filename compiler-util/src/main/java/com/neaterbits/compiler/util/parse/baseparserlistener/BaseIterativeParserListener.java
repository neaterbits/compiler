package com.neaterbits.compiler.util.parse.baseparserlistener;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.StackConditionBlock;
import com.neaterbits.compiler.util.parse.stackstate.StackConstantSwitchLabel;
import com.neaterbits.compiler.util.parse.stackstate.StackElseBlock;
import com.neaterbits.compiler.util.parse.stackstate.StackIfElseIfElse;
import com.neaterbits.compiler.util.parse.stackstate.StackSwitchCase;
import com.neaterbits.compiler.util.parse.stackstate.StackSwitchCaseGroup;
import com.neaterbits.compiler.util.parse.stackstate.setters.StatementSetter;

public abstract class BaseIterativeParserListener<

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
		
		CONDITION_BLOCK,
		
		IF_ELSE_IF_ELSE_STATEMENT extends STATEMENT,
		
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
		
		CONDITION_BLOCK,
		IF_ELSE_IF_ELSE_STATEMENT,
		
		SWITCH_CASE_LABEL,
		CONSTANT_SWITCH_CASE_LABEL,
		ENUM_CONSTANT,
		ENUM_SWITCH_CASE_LABEL,
		DEFAULT_SWITCH_CASE_LABEL,
		SWITCH_CASE_GROUP,
		SWITCH_CASE_STATEMENT,
		
		BREAK_STATEMENT
		
		
		> {
	
	protected BaseIterativeParserListener(ParseLogger logger, @SuppressWarnings("rawtypes") ParseTreeFactory parseTreeFactory) {
		super(logger, parseTreeFactory);
	}

	public final void onIfStatementStart(Context context, String ifKeyword, Context ifKeywordContext) {
		
		
		push(new StackIfElseIfElse<>(getLogger()));
		push(new StackConditionBlock<>(getLogger(), context, null, null, ifKeyword, ifKeywordContext));
		
		pushVariableScope();
	}
	
	private void popAndAddConditionBlock(Context context) {
		
		final StackConditionBlock<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackConditionBlock = pop();
		
		final StackIfElseIfElse<KEYWORD, CONDITION_BLOCK, STATEMENT> ifElseIfElse = get();
		
		final CONDITION_BLOCK conditionBlock = parseTreeFactory.createConditionBlock(
				stackConditionBlock.getUpdatedContext(),
				stackConditionBlock.getElseKeyword() != null
					? parseTreeFactory.createKeyword(stackConditionBlock.getElseKeywordContext(), stackConditionBlock.getElseKeyword())
					: null,
				stackConditionBlock.getIfKeyword() != null
					? parseTreeFactory.createKeyword(stackConditionBlock.getIfKeywordContext(), stackConditionBlock.getIfKeyword())
					: null,
				makeExpression(context, stackConditionBlock),
				parseTreeFactory.createBlock(context, stackConditionBlock.getStatements()));

		ifElseIfElse.add(conditionBlock);
	}
	
	// End of initial if-statement and block
	public final void onIfStatementInitialBlockEnd(Context context) {

		logEnter(context);
		
		popAndAddConditionBlock(context);

		popVariableScope();
		
		logExit(context);
	}

	public final void onElseIfStatementStart(Context context, String elseKeyword, Context elseKeywordContext, String ifKeyword, Context ifKeywordContext) {

		logEnter(context);
		
		push(new StackConditionBlock<>(getLogger(), context, elseKeyword, elseKeywordContext, ifKeyword, ifKeywordContext));
		
		pushVariableScope();
		
		logExit(context);
	}
	
	public final void onElseIfStatementEnd(Context context) {

		logEnter(context);

		popAndAddConditionBlock(context);
		
		popVariableScope();
		
		logExit(context);
	}

	
	/*
	public final void onIfElseIfExpressionEnd(Context context) {
		popAndAddConditionBlock(context);
	}
	*/

	public final void onElseStatementStart(Context context, String keyword, Context keywordContext) {
		
		logEnter(context);
		
		push(new StackElseBlock<>(getLogger(), keyword, keywordContext));
		
		pushVariableScope();
		
		logExit(context);
	}
	
	public final void onElseStatementEnd(Context context) {

		logEnter(context);
		
		final StackElseBlock<STATEMENT> stackBlock = pop();

		popVariableScope();

		final StackIfElseIfElse<KEYWORD, CONDITION_BLOCK, BLOCK> ifElseIfElse = get();
		
		ifElseIfElse.setElseBlock(
				stackBlock.getKeyword() != null
					? parseTreeFactory.createKeyword(stackBlock.getKeywordContext(), stackBlock.getKeyword())
					: null,
				parseTreeFactory.createBlock(context, stackBlock.getList()));
		
		logExit(context);
	}

	// Called after last part of statement (ie end if in if-else if-else-end if)
	public final void onEndIfStatement(Context context) {
		
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

	public final void onSwitchStatementStart(Context context, String keyword, Context keywordContext) {

		logEnter(context);
		
		push(new StackSwitchCase<>(getLogger(), keyword, keywordContext));

		logExit(context);
	}

	public final void onJavaSwitchBlockStart(Context context) {
		
		logEnter(context);
		
		logExit(context);
	}
	
	public final void onJavaSwitchBlockStatementGroupStart(Context context) {
		
		logEnter(context);
		
		push(new StackSwitchCaseGroup<>(getLogger()));
		
		logExit(context);
	}

	public final void onSwitchLabelsStart(Context context) {
		
		logEnter(context);

		logExit(context);
	}
	
	public final void onSwitchLabelsEnd(Context context) {
		
		logEnter(context);

		logExit(context);
	}

	public final void onJavaSwitchBlockStatementGroupEnd(Context context) {
		
		logEnter(context);
		
		final StackSwitchCaseGroup<SWITCH_CASE_LABEL, STATEMENT> stackSwitchCaseGroup = pop();
		
		final SWITCH_CASE_GROUP switchCaseGroup = parseTreeFactory.createSwitchCaseGroup(
				context,
				stackSwitchCaseGroup.getLabels(),
				parseTreeFactory.createBlock(context, stackSwitchCaseGroup.getStatements()));

		final StackSwitchCase<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, SWITCH_CASE_GROUP> stackSwitchCase = get();
		
		stackSwitchCase.addGroup(switchCaseGroup);
		
		logExit(context);
	}

	public final void onConstantSwitchLabelStart(Context context, String keyword, Context keywordContext) {
		
		logEnter(context);
		
		push(new StackConstantSwitchLabel<>(getLogger(), keyword, keywordContext));
		
		logExit(context);
	}
	
	public final void onConstantSwitchLabelEnd(Context context) {
		
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
	
	public final void onEnumSwitchLabel(
			Context context,
			String keyword, Context keywordContext,
			String constantName, Context constantNameContext) {
		
		logEnter(context);
		
		final KEYWORD k = parseTreeFactory.createKeyword(keywordContext, keyword);
		
		final ENUM_SWITCH_CASE_LABEL switchCaseLabel = parseTreeFactory.createEnumSwitchCaseLabel(
				context,
				k,
				parseTreeFactory.createEnumConstant(constantNameContext, constantName));
		
		final StackSwitchCaseGroup<SWITCH_CASE_LABEL, STATEMENT> stackSwitchCaseGroup = get();
		
		stackSwitchCaseGroup.addLabel(switchCaseLabel);
		
		logExit(context);
	}
	
	
	public final void onDefaultSwitchLabel(Context context, String keyword, Context keywordContext) {
		
		logEnter(context);

		final StackSwitchCaseGroup<SWITCH_CASE_LABEL, STATEMENT> stackSwitchCaseGroup = get();
		
		final KEYWORD k = parseTreeFactory.createKeyword(keywordContext, keyword);
		
		stackSwitchCaseGroup.addLabel(parseTreeFactory.createDefaultSwitchCaseLabel(context, k));
		
		logExit(context);
	}
	
	public final void onJavaSwitchBlockEnd(Context context) {
		
		logEnter(context);
		
		logExit(context);
	}
	
	public final void onSwitchStatementEnd(Context context) {
		
		logEnter(context);
		
		final StackSwitchCase<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, SWITCH_CASE_GROUP> stackSwitchCase = pop();
		
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
	
	public final void onBreakStatement(Context context, String keyword, Context keywordContext, String label) {
		
		logEnter(context);

		final StatementSetter<STATEMENT> statementSetter = get();
		
		statementSetter.addStatement(parseTreeFactory.createBreakStatement(context, parseTreeFactory.createKeyword(keywordContext, keyword), label));
		
		logExit(context);
	}
}
