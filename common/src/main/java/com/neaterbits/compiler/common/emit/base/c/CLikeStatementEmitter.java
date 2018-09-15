package com.neaterbits.compiler.common.emit.base.c;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.AssignmentStatement;
import com.neaterbits.compiler.common.ast.statement.CForStatement;
import com.neaterbits.compiler.common.ast.statement.ConditionBlock;
import com.neaterbits.compiler.common.ast.statement.DoWhileStatement;
import com.neaterbits.compiler.common.ast.statement.IfStatement;
import com.neaterbits.compiler.common.ast.statement.WhileStatement;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.StatementEmitter;
import com.neaterbits.compiler.common.emit.base.BaseStatementEmitter;

public abstract class CLikeStatementEmitter<T extends EmitterState>
	extends BaseStatementEmitter<T> implements StatementEmitter<T> {

	protected abstract void emitCondition(Condition condition, T param);
	protected abstract void emitExpression(Expression expression, T param);
	protected abstract void emitType(TypeReference typeReference, T param);
	
	@Override
	public final Void onIf(IfStatement statement, T param) {
		
		for (int i = 0; i < statement.getConditions().size(); ++ i) {
			param.append(i == 0 ? "if" : "else if");
			
			param.append(" (");
			
			final ConditionBlock conditionBlock = statement.getConditions().get(i);
			
			emitCondition(conditionBlock.getCondition(), param);
			
			param.append(") {").newline();
			
			emitIndentedBlock(conditionBlock.getBlock(), param);
		}

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
}
