package com.neaterbits.compiler.common.parser.iterative;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.statement.BreakStatement;
import com.neaterbits.compiler.common.ast.statement.ConditionBlock;
import com.neaterbits.compiler.common.ast.statement.ConstantSwitchCaseLabel;
import com.neaterbits.compiler.common.ast.statement.DefaultSwitchCaseLabel;
import com.neaterbits.compiler.common.ast.statement.EnumSwitchCaseLabel;
import com.neaterbits.compiler.common.ast.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.common.ast.statement.SwitchCaseGroup;
import com.neaterbits.compiler.common.ast.statement.SwitchCaseStatement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.BaseInfixParserListener;
import com.neaterbits.compiler.common.parser.StatementSetter;
import com.neaterbits.compiler.common.parser.stackstate.StackBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackConditionBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackExpression;
import com.neaterbits.compiler.common.parser.stackstate.StackIfElseIfElse;
import com.neaterbits.compiler.common.parser.stackstate.StackSwitchCase;
import com.neaterbits.compiler.common.parser.stackstate.StackSwitchCaseGroup;

public abstract class BaseIterativeParserListener
	extends BaseInfixParserListener {
	
	protected BaseIterativeParserListener(ParseLogger logger) {
		super(logger);
	}

	public final void onIfStatementStart(Context context) {
		
		
		push(new StackIfElseIfElse(getLogger()));
		push(new StackConditionBlock(getLogger()));
		
		pushVariableScope();
	}
	
	private void popAndAddConditionBlock(Context context) {
		
		final StackConditionBlock stackConditionBlock = pop();
		
		final StackIfElseIfElse ifElseIfElse = get();
		
		final ConditionBlock conditionBlock = new ConditionBlock(
				context,
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

	public final void onElseIfStatementStart(Context context) {

		logEnter(context);
		
		push(new StackConditionBlock(getLogger()));
		
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

	public final void onElseStatementStart(Context context) {
		
		logEnter(context);
		
		push(new StackBlock(getLogger()));
		
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
