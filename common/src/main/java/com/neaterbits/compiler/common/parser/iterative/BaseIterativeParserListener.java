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

		popAndAddConditionBlock(context);

		popVariableScope();
		
	}

	public final void onElseIfStatementStart(Context context) {
		
		push(new StackConditionBlock(getLogger()));
		
		pushVariableScope();
	}
	
	public final void onElseIfStatementEnd(Context context) {
		
		popVariableScope();
	}

	
	/*
	public final void onIfElseIfExpressionEnd(Context context) {
		popAndAddConditionBlock(context);
	}
	*/

	public final void onElseStatementStart(Context context) {
		push(new StackBlock(getLogger()));
		
		pushVariableScope();
	}
	
	public final void onElseStatementEnd(Context context) {

		final StackBlock stackBlock = pop();

		popVariableScope();

		final StackIfElseIfElse ifElseIfElse = get();
		
		ifElseIfElse.setElseBlock(new Block(context, stackBlock.getList()));
	}

	// Called after last part of statement (ie end if in if-else if-else-end if)
	public final void onEndIfStatement(Context context) {
		
		final StackIfElseIfElse ifElseIfElse = pop();

		final StatementSetter statementSetter = get();
		
		final IfElseIfElseStatement statement = new IfElseIfElseStatement(
				context,
				ifElseIfElse.getList(),
				ifElseIfElse.getElseBlock());
		
		statementSetter.addStatement(statement);
	}
}
