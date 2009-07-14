package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.expression.AssignmentExpression;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.AssignmentStatement;
import com.neaterbits.compiler.common.ast.statement.BreakStatement;
import com.neaterbits.compiler.common.ast.statement.ConditionBlock;
import com.neaterbits.compiler.common.ast.statement.DoWhileStatement;
import com.neaterbits.compiler.common.ast.statement.ExpressionStatement;
import com.neaterbits.compiler.common.ast.statement.ForStatement;
import com.neaterbits.compiler.common.ast.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.common.ast.statement.ReturnStatement;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.statement.SwitchCaseGroup;
import com.neaterbits.compiler.common.ast.statement.SwitchCaseStatement;
import com.neaterbits.compiler.common.ast.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.common.ast.statement.WhileStatement;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.common.convert.ConverterState;
import com.neaterbits.compiler.common.convert.StatementConverter;

public abstract class BaseStatementConverter<T extends ConverterState<T>>
			extends IterativeConverter<T>
			implements StatementConverter<T> {

	@Override
	protected final Statement convertStatement(Statement statement, T state) {
		return statement.visit(this, state);
	}

	@Override
	public Statement onIf(IfElseIfElseStatement statement, T param) {

		final List<ConditionBlock> convertedConditionBlocks = new ArrayList<>(statement.getConditions().size());
		
		for (ConditionBlock conditionBlock : statement.getConditions()) {

			final ConditionBlock convertedConditionBlock = new ConditionBlock(
					conditionBlock.getContext(),
					convertExpression(conditionBlock.getCondition(), param),
					convertBlock(conditionBlock.getBlock(), param));

			convertedConditionBlocks.add(convertedConditionBlock);
		}
		
		final Block convertedElseBlock = statement.getElseBlock() != null
				? convertBlock(statement.getElseBlock(), param)
				: null;
		
		return new IfElseIfElseStatement(statement.getContext(), convertedConditionBlocks, convertedElseBlock);
	}

	@Override
	public Statement onSwitchCase(SwitchCaseStatement statement, T param) {
		
		final List<SwitchCaseGroup> convertedGroups = new ArrayList<>(statement.getGroups().size());
		
		final Expression convertedExpression = convertExpression(statement.getExpression(), param);

		if (Boolean.TRUE) {
			throw new UnsupportedOperationException();
		}
		else {
			/*
			for (SwitchCaseGroup switchCaseGroup : statement.getGroups()) {
			}
			*/
		}
		

		return new SwitchCaseStatement(statement.getContext(), convertedExpression, convertedGroups);
	}

	@Override
	public Statement onWhile(WhileStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onDoWhile(DoWhileStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onCFor(ForStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onAssignment(AssignmentStatement statement, T param) {

		System.out.println("## onAssignmentStatement");
		
		return new AssignmentStatement(
				statement.getContext(),
				(AssignmentExpression)convertExpression(statement.getExpression(), param));
	}

	@Override
	public Statement onVariableDeclaration(VariableDeclarationStatement statement, T param) {
		
		final List<InitializerVariableDeclarationElement> convertedDeclarations = new ArrayList<>(statement.getDeclarations().size());
		
		for (InitializerVariableDeclarationElement declaration : statement.getDeclarations()) {
			final InitializerVariableDeclarationElement convertedDeclaration = new InitializerVariableDeclarationElement(
					declaration.getContext(),
					convertType(declaration.getTypeReference(), param),
					declaration.getName(),
					declaration.getNumDims(),
					convertExpression(declaration.getInitializer(), param));
			
			convertedDeclarations.add(convertedDeclaration);
		}
		
		return new VariableDeclarationStatement(
				statement.getContext(),
				statement.getModifiers(),
				convertedDeclarations);
	}

	@Override
	public Statement onExpressionStatement(ExpressionStatement statement, T param) {
		
		return new ExpressionStatement(
				statement.getContext(),
				convertExpression(statement.getExpression(), param));
	}

	@Override
	public Statement onReturnStatement(ReturnStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onBreakStatement(BreakStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}
}
