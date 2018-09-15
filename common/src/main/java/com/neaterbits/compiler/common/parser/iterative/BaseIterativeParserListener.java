package com.neaterbits.compiler.common.parser.iterative;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.statement.ConditionBlock;
import com.neaterbits.compiler.common.ast.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.common.parser.BaseParserListener;
import com.neaterbits.compiler.common.parser.StatementSetter;
import com.neaterbits.compiler.common.parser.stackstate.StackBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackConditionBlock;
import com.neaterbits.compiler.common.parser.stackstate.StackIfElseIfElse;

public abstract class BaseIterativeParserListener
	extends BaseParserListener {
	
	public final void onIfStatementStart(Context context) {
		
		push(new StackIfElseIfElse());
		push(new StackConditionBlock());
		
	}
	
	private void addConditionBlock(Context context) {
		
		final StackConditionBlock stackConditionBlock = pop();
		
		final StackIfElseIfElse ifElseIfElse = get();
		
		final ConditionBlock conditionBlock = new ConditionBlock(
				context,
				stackConditionBlock.getExpression(),
				new Block(context, stackConditionBlock.getStatements()));
		
		ifElseIfElse.add(conditionBlock);

	}
	
	// End of initial if-statement
	public final void onIfStatementInitialBlockEnd(Context context) {
		
		addConditionBlock(context);
		
	}

	public final void onElseIfStatementStart(Context context) {
		
		push(new StackConditionBlock());
	}
	
	public final void onElseIfStatementEnd(Context context) {
		
		addConditionBlock(context);
		
	}

	public final void onElseStatementStart(Context context) {
		push(new StackBlock());
	}
	
	public final void onElseStatementEnd(Context context) {

		final StackBlock stackBlock = pop();
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
