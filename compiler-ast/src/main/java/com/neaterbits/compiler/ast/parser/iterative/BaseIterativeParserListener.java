package com.neaterbits.compiler.ast.parser.iterative;

import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.parser.BaseInfixParserListener;
import com.neaterbits.compiler.ast.parser.StatementSetter;
import com.neaterbits.compiler.ast.parser.stackstate.StackBlock;
import com.neaterbits.compiler.ast.parser.stackstate.StackConditionBlock;
import com.neaterbits.compiler.ast.parser.stackstate.StackElseBlock;
import com.neaterbits.compiler.ast.parser.stackstate.StackExpression;
import com.neaterbits.compiler.ast.parser.stackstate.StackIfElseIfElse;
import com.neaterbits.compiler.ast.parser.stackstate.StackSwitchCase;
import com.neaterbits.compiler.ast.parser.stackstate.StackSwitchCaseGroup;
import com.neaterbits.compiler.ast.statement.BreakStatement;
import com.neaterbits.compiler.ast.statement.ConditionBlock;
import com.neaterbits.compiler.ast.statement.ConstantSwitchCaseLabel;
import com.neaterbits.compiler.ast.statement.DefaultSwitchCaseLabel;
import com.neaterbits.compiler.ast.statement.EnumSwitchCaseLabel;
import com.neaterbits.compiler.ast.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.ast.statement.SwitchCaseGroup;
import com.neaterbits.compiler.ast.statement.SwitchCaseStatement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class BaseIterativeParserListener
	extends BaseInfixParserListener {
	
	protected BaseIterativeParserListener(ParseLogger logger) {
		super(logger);
	}

	public final void onIfStatementStart(Context context, String ifKeyword, Context ifKeywordContext) {
		
		
		push(new StackIfElseIfElse(getLogger()));
		push(new StackConditionBlock(getLogger(), context, null, null, ifKeyword, ifKeywordContext));
		
		pushVariableScope();
	}
	
	private void popAndAddConditionBlock(Context context) {
		
		final StackConditionBlock stackConditionBlock = pop();
		
		final StackIfElseIfElse ifElseIfElse = get();
		
		final ConditionBlock conditionBlock = new ConditionBlock(
				stackConditionBlock.getUpdatedContext(),
				stackConditionBlock.getElseKeyword() != null
					? new Keyword(stackConditionBlock.getElseKeywordContext(), stackConditionBlock.getElseKeyword())
					: null,
				stackConditionBlock.getIfKeyword() != null
					? new Keyword(stackConditionBlock.getIfKeywordContext(), stackConditionBlock.getIfKeyword())
					: null,
				stackConditionBlock.makeExpression(context),
				new Block(context, stackConditionBlock.getStatements()));

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
		
		push(new StackConditionBlock(getLogger(), context, elseKeyword, elseKeywordContext, ifKeyword, ifKeywordContext));
		
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
		
		push(new StackElseBlock(getLogger(), keyword, keywordContext));
		
		pushVariableScope();
		
		logExit(context);
	}
	
	public final void onElseStatementEnd(Context context) {

		logEnter(context);
		
		final StackBlock stackBlock = pop();

		popVariableScope();

		final StackIfElseIfElse ifElseIfElse = get();
		
		ifElseIfElse.setElseBlock(new Block(context, stackBlock.getList()));
		
		logExit(context);
	}

	// Called after last part of statement (ie end if in if-else if-else-end if)
	public final void onEndIfStatement(Context context) {
		
		logEnter(context);
		
		final StackIfElseIfElse ifElseIfElse = pop();

		final StatementSetter statementSetter = get();
		
		final IfElseIfElseStatement statement = new IfElseIfElseStatement(
				context,
				ifElseIfElse.getList(),
				ifElseIfElse.getElseBlock());
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}

	public final void onSwitchStatementStart(Context context) {

		logEnter(context);
		
		push(new StackSwitchCase(getLogger()));

		logExit(context);
	}

	public final void onJavaSwitchBlockStart(Context context) {
		
		logEnter(context);
		
		logExit(context);
	}
	
	public final void onJavaSwitchBlockStatementGroupStart(Context context) {
		
		logEnter(context);
		
		push(new StackSwitchCaseGroup(getLogger()));
		
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
		
		final StackSwitchCaseGroup stackSwitchCaseGroup = pop();
		
		final SwitchCaseGroup switchCaseGroup = new SwitchCaseGroup(
				context,
				stackSwitchCaseGroup.getLabels(),
				new Block(context, stackSwitchCaseGroup.getStatements()));

		final StackSwitchCase stackSwitchCase = get();
		
		stackSwitchCase.addGroup(switchCaseGroup);
		
		logExit(context);
	}

	public final void onConstantSwitchLabelStart(Context context) {
		
		logEnter(context);
		
		push(new StackExpression(getLogger()));
		
		logExit(context);
	}
	
	public final void onConstantSwitchLabelEnd(Context context) {
		
		logEnter(context);
		
		final StackExpression stackExpression = pop();
		
		final ConstantSwitchCaseLabel switchCaseLabel = new ConstantSwitchCaseLabel(
				context,
				stackExpression.getExpression());
		
		final StackSwitchCaseGroup stackSwitchCaseGroup = get();
		
		stackSwitchCaseGroup.addLabel(switchCaseLabel);
		
		logExit(context);
	}
	
	public final void onEnumSwitchLabel(Context context, String constantName) {
		
		logEnter(context);
		
		final EnumSwitchCaseLabel switchCaseLabel = new EnumSwitchCaseLabel(context, constantName);
		
		final StackSwitchCaseGroup stackSwitchCaseGroup = get();
		
		stackSwitchCaseGroup.addLabel(switchCaseLabel);
		
		logExit(context);
	}
	
	
	public final void onDefaultSwitchLabel(Context context) {
		
		logEnter(context);

		final StackSwitchCaseGroup stackSwitchCaseGroup = get();
		
		stackSwitchCaseGroup.addLabel(new DefaultSwitchCaseLabel(context));
		
		logExit(context);
	}
	
	public final void onJavaSwitchBlockEnd(Context context) {
		
		logEnter(context);
		
		logExit(context);
	}
	
	public final void onSwitchStatementEnd(Context context) {
		
		logEnter(context);
		
		final StackSwitchCase stackSwitchCase = pop();
		
		final SwitchCaseStatement statement = new SwitchCaseStatement(
				context,
				stackSwitchCase.makeExpression(context),
				stackSwitchCase.getGroups());
		
		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(statement);
		
		logExit(context);
	}
	
	public final void onBreakStatement(Context context, String label) {
		
		logEnter(context);

		final StatementSetter statementSetter = get();
		
		statementSetter.addStatement(new BreakStatement(context, label));
		
		logExit(context);
	}
}
