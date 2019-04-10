package com.neaterbits.compiler.emit.base.c;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.statement.AssignmentStatement;
import com.neaterbits.compiler.ast.statement.BreakStatement;
import com.neaterbits.compiler.ast.statement.DoWhileStatement;
import com.neaterbits.compiler.ast.statement.ExpressionStatement;
import com.neaterbits.compiler.ast.statement.ForExpressionList;
import com.neaterbits.compiler.ast.statement.ForStatement;
import com.neaterbits.compiler.ast.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.ast.statement.ReturnStatement;
import com.neaterbits.compiler.ast.statement.SwitchCaseLabel;
import com.neaterbits.compiler.ast.statement.SwitchCaseStatement;
import com.neaterbits.compiler.ast.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.ast.statement.WhileStatement;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.variables.VarName;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.StatementEmitter;
import com.neaterbits.compiler.emit.base.BaseStatementEmitter;

public abstract class CLikeStatementEmitter<T extends EmitterState>
	extends BaseStatementEmitter<T> implements StatementEmitter<T> {

	protected abstract void emitExpression(Expression expression, T param);
	protected abstract void emitType(TypeReference typeReference, T param);
	
	protected abstract void emitVariableModifiers(VariableModifiers modifiers, T param);

	protected abstract void emitSwitchCaseLabel(SwitchCaseLabel label, T param);

	protected final void emitVariableDeclaration(TypeReference typeReference, VarName varName, T param) {

		emitType(typeReference, param);
		
		param.append(' ');
		
		param.append(varName.getName());
	}

	
	protected final void emitVariableDeclarationElement(InitializerVariableDeclarationElement element, T param) {
		
		emitVariableDeclaration(element.getTypeReference(), element.getVarName(), param);

		if (element.getInitializer() != null) {
			param.append(" = ");
			
			emitExpression(element.getInitializer(), param);
		}
	}

	protected final <E extends InitializerVariableDeclarationElement>

	void emitVariableDeclarationElements(ASTList<E> elements, T param) {

		emitListTo(param, elements, ", ", e -> {
			emitVariableDeclarationElement(e, param);
		});

		param.append(';');
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
	public final Void onSwitchCase(SwitchCaseStatement statement, T param) {

		param.append("switch (");
		
		emitExpression(statement.getExpression(), param);
		
		param.append(") {").newline();
		
		statement.getGroups().forEach(group -> {
			
			group.getLabels().foreachWithIndex((label, index) -> {
				
				emitSwitchCaseLabel(label, param);
				
				if (index < group.getLabels().size() - 1) {
					param.newline();
				}
				else {
					param.append(" {").newline();
				}
			});
			
			emitIndentedBlock(group.getBlock(), param);
			
			param.append('}');
			
		});
		
		param.append('}');

		return null;
	}
	@Override
	public final Void onWhile(WhileStatement statement, T param) {
		
		param.append("while (");
		emitExpression(statement.getCondition(), param);
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
		emitExpression(statement.getCondition(), param);
		param.append(");").newline();
		
		return null;
	}
	
	private void emitForExpressionList(ForExpressionList forExpressionList, T param) {
		
		emitListTo(param, forExpressionList.getExpressions(), ", ", expression -> {
			emitExpression(expression, param);
		});
	}

	@Override
	public final Void onCFor(ForStatement statement, T param) {

		param.append("for (");
		
		if (statement.getForInit() != null) {
			param.append(' ');
			
			if (statement.getForInit().getLocalVariableDeclaration() != null) {
				emitVariableDeclarationElement(statement.getForInit().getLocalVariableDeclaration(), param);
			}
			else if (statement.getForInit().getExpressionList() != null) {
				emitForExpressionList(statement.getForInit().getExpressionList(), param);
			}
		}
		
		param.append(';');
		
		if (statement.getCondition() != null) {
			param.append(' ');
			emitExpression(statement.getCondition(), param);
		}
		
		param.append(';');
		
		if (statement.getForUpdate() != null) {
			param.append(' ');
			emitForExpressionList(statement.getForUpdate(), param);
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
	@Override
	public final Void onBreakStatement(BreakStatement statement, T param) {
		
		param.append("break");
		
		return null;
	}
}
