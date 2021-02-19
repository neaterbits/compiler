package com.neaterbits.compiler.common.emit.base.c;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.statement.AssignmentStatement;
import com.neaterbits.compiler.common.ast.statement.CForStatement;
import com.neaterbits.compiler.common.ast.statement.DoWhileStatement;
import com.neaterbits.compiler.common.ast.statement.ExpressionStatement;
import com.neaterbits.compiler.common.ast.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.common.ast.statement.ReturnStatement;
import com.neaterbits.compiler.common.ast.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.common.ast.statement.WhileStatement;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.ast.variables.VarName;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.StatementEmitter;
import com.neaterbits.compiler.common.emit.base.BaseStatementEmitter;

public abstract class CLikeStatementEmitter<T extends EmitterState>
	extends BaseStatementEmitter<T> implements StatementEmitter<T> {

	protected abstract void emitCondition(Condition condition, T param);
	protected abstract void emitExpression(Expression expression, T param);
	protected abstract void emitType(TypeReference typeReference, T param);
	
	protected abstract void emitVariableModifiers(VariableModifiers modifiers, T param);
	

	protected final void emitVariableDeclaration(TypeReference typeReference, VarName varName, T param) {
		emitType(typeReference, param);
		
		param.append(' ');
		
		param.append(varName.getName());
	}

	
	protected final void emitVariableDeclarationElement(InitializerVariableDeclarationElement element, T param) {
		
		emitVariableDeclaration(element.getTypeReference(), element.getName(), param);

	}

	protected final <E extends InitializerVariableDeclarationElement>

	void emitVariableDeclarationElements(ASTList<E> elements, T param) {

		emitListTo(param, elements, ", ", e -> {
			emitVariableDeclarationElement(e, param);
		});
	}
	
	@Override
	public final Void onVariableDeclaration(VariableDeclarationStatement statement, T param) {

		emitVariableModifiers(statement.getModifiers(), param);
		emitVariableDeclarationElements(statement.getDeclarations(), param);
		
		return null;
	}
	@Override
	public final Void onIf(IfElseIfElseStatement statement, T param) {
		
		statement.getConditions().foreachWithIndex((conditionBlock, i) -> {
			param.append(i == 0 ? "if" : "else if");
			
			param.append(" (");
			
			emitExpression(conditionBlock.getCondition(), param);
			
			param.append(") {").newline();
			
			emitIndentedBlock(conditionBlock.getBlock(), param);
			
			param.append('}').newline();
		});

		if (statement.getElseBlock() != null) {
			param.append("else {").newline();
			
			emitIndentedBlock(statement.getElseBlock(), param);
			
			param.append('}').newline();
		}

		return null;
	}

	@Override
	public final Void onWhile(WhileStatement statement, T param) {
		
		param.append("while (");
		emitCondition(statement.getCondition(), param);
		param.append(") {").newline();
		
		emitIndentedBlock(statement.getBlock(), param);
		
		param.append('}').newline();
		
		return null;
	}
	
	@Override
	public final Void onDoWhile(DoWhileStatement statement, T param) {
		
		param.append("do {");
		
		emitIndentedBlock(statement.getBlock(), param);
		
		param.append("} while(");
		emitCondition(statement.getCondition(), param);
		param.append(");").newline();
		
		return null;
	}

	@Override
	public final Void onCFor(CForStatement statement, T param) {

		param.append("for (");
		
		if (statement.getInitialStatement() != null) {
			param.append(' ');
			emitStatement(statement.getInitialStatement(), param);
		}
		
		param.append(';');
		
		if (statement.getCondition() != null) {
			param.append(' ');
			emitCondition(statement.getCondition(), param);
		}
		
		param.append(';');
		
		if (statement.getEachStatement() != null) {
			param.append(' ');
			emitStatement(statement.getEachStatement(), param);
		}
		
		param.append(") {").newline();
		
		emitIndentedBlock(statement.getBlock(), param);
		
		param.append('}').newline();
		
		return null;
	}

	@Override
	public final Void onAssignment(AssignmentStatement statement, T param) {

		emitExpression(statement.getExpression(), param);
		
		param.append(';');

		param.newline();

		return null;
	}

	@Override
	public Void onExpressionStatement(ExpressionStatement statement, T param) {
		emitExpression(statement.getExpression(), param);

		return null;
	}
	@Override
	public Void onReturnStatement(ReturnStatement statement, T param) {
		param.append("return ");
		
		emitExpression(statement.getExpression(), param);

		param.append(';').newline();

		return null;
	}
}
