package com.neaterbits.compiler.c.emit;

import java.util.List;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.AssignmentStatement;
import com.neaterbits.compiler.common.ast.statement.CForStatement;
import com.neaterbits.compiler.common.ast.statement.ConditionBlock;
import com.neaterbits.compiler.common.ast.statement.DoWhileStatement;
import com.neaterbits.compiler.common.ast.statement.IfStatement;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.statement.WhileStatement;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.emit.BaseEmitter;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.StatementEmitter;

public class CStatementEmitter extends BaseEmitter<EmitterState> implements StatementEmitter<EmitterState> {

	private static final CConditionEmitter CONDITION_EMITTER = new CConditionEmitter();

	private static final CExpressionEmitter EXPRESSION_EMITTER = new CExpressionEmitter();
	
	private static final CTypeEmitter TYPE_EMITTER = new CTypeEmitter();
	
	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		statement.visit(this, state);
	}

	private void emitCondition(Condition condition, EmitterState param) {
		condition.visit(CONDITION_EMITTER, param);
	}
	
	private void emitExpression(Expression expression, EmitterState param) {
		expression.visit(EXPRESSION_EMITTER, param);
	}

	private void emitType(TypeReference typeReference, EmitterState param) {
		typeReference.getType().visit(TYPE_EMITTER, param);
	}
	
	@Override
	public Void onIf(IfStatement statement, EmitterState param) {
		
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
	public Void onWhile(WhileStatement statement, EmitterState param) {
		
		param.append("while (");
		emitCondition(statement.getCondition(), param);
		param.append(") {").newline();
		
		emitIndentedBlock(statement.getBlock(), param);
		
		param.append('}').newline();
		
		return null;
	}
	
	@Override
	public Void onDoWhile(DoWhileStatement statement, EmitterState param) {
		
		param.append("do {");
		
		emitIndentedBlock(statement.getBlock(), param);
		
		param.append("} while(");
		emitCondition(statement.getCondition(), param);
		param.append(");").newline();
		
		return null;
	}

	@Override
	public Void onCFor(CForStatement statement, EmitterState param) {

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
	public Void onAssignment(AssignmentStatement statement, EmitterState param) {
		
		final List<VariableReference> vars = statement.getVariables();
		
		BaseType type = null;
		
		for (int i = 0; i < vars.size(); ++ i) {
			final VariableReference var = vars.get(i);
			
			if (i == 0) {
				type = var.getDeclaration().getType();
			}
			else {
				if (!type.equals(var.getDeclaration().getType())) {
					throw new IllegalStateException("Type mismatch");
				}
			}
			
			if (var.isDeclaredHere()) {
				if (i > 0) {
					throw new IllegalStateException("Var declaration for non index 0 var");
				}

				emitType(var.getDeclaration().getTypeReference(), param);

				param.append(' ');
			}

			param.append(var.getName().getName());

			param.append(" = ");
		}
		
		emitExpression(statement.getExpression(), param);
		
		param.newline();

		return null;
	}
}
