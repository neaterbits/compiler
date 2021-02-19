package com.neaterbits.compiler.common.parser.iterative;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.statement.ConditionBlock;
import com.neaterbits.compiler.common.ast.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.BaseInfixParserListener;
import com.neaterbits.compiler.common.parser.StatementSetter;
import com.neaterbits.compiler.common.parser.stackstate.StackBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackConditionBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackIfElseIfElse;

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
}
