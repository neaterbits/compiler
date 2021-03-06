package dev.nimbler.compiler.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.expression.AssignmentExpression;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.statement.AssignmentStatement;
import dev.nimbler.compiler.ast.objects.statement.BreakStatement;
import dev.nimbler.compiler.ast.objects.statement.ConditionBlock;
import dev.nimbler.compiler.ast.objects.statement.DoWhileStatement;
import dev.nimbler.compiler.ast.objects.statement.ExpressionStatement;
import dev.nimbler.compiler.ast.objects.statement.ForStatement;
import dev.nimbler.compiler.ast.objects.statement.IfConditionBlock;
import dev.nimbler.compiler.ast.objects.statement.IfElseIfElseStatement;
import dev.nimbler.compiler.ast.objects.statement.ReturnStatement;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.statement.SwitchCaseGroup;
import dev.nimbler.compiler.ast.objects.statement.SwitchCaseStatement;
import dev.nimbler.compiler.ast.objects.statement.VariableDeclarationStatement;
import dev.nimbler.compiler.ast.objects.statement.WhileStatement;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.convert.ConverterState;
import dev.nimbler.compiler.convert.StatementConverter;

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

			final ConditionBlock convertedConditionBlock = new IfConditionBlock(
					conditionBlock.getContext(),
					null,
					convertExpression(conditionBlock.getCondition(), param),
					convertBlock(conditionBlock.getBlock(), param));

			convertedConditionBlocks.add(convertedConditionBlock);
		}
		
		final Block convertedElseBlock = statement.getElseBlock() != null
				? convertBlock(statement.getElseBlock(), param)
				: null;
		
		return new IfElseIfElseStatement(statement.getContext(), convertedConditionBlocks, null, convertedElseBlock);
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
		
		return new SwitchCaseStatement(statement.getContext(), null, convertedExpression, convertedGroups);
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
					declaration.getNameDeclaration(),
					declaration.getNumDims(),
					convertExpression(declaration.getInitializer(), param));
			
			convertedDeclarations.add(convertedDeclaration);
		}
		
		return new VariableDeclarationStatement(
				statement.getContext(),
				convertModifiers(statement.getModifiers()),
                convertType(statement.getTypeReference(), param),
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
