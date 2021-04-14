package dev.nimbler.compiler.emit.base.c;

import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.statement.AssignmentStatement;
import dev.nimbler.compiler.ast.objects.statement.BreakStatement;
import dev.nimbler.compiler.ast.objects.statement.DoWhileStatement;
import dev.nimbler.compiler.ast.objects.statement.ExpressionStatement;
import dev.nimbler.compiler.ast.objects.statement.ForStatement;
import dev.nimbler.compiler.ast.objects.statement.ForUpdateExpressionList;
import dev.nimbler.compiler.ast.objects.statement.IfElseIfElseStatement;
import dev.nimbler.compiler.ast.objects.statement.ReturnStatement;
import dev.nimbler.compiler.ast.objects.statement.SwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.SwitchCaseStatement;
import dev.nimbler.compiler.ast.objects.statement.VariableDeclarationStatement;
import dev.nimbler.compiler.ast.objects.statement.WhileStatement;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifiers;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.ast.objects.variables.VarName;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.StatementEmitter;
import dev.nimbler.compiler.emit.base.BaseStatementEmitter;

public abstract class CLikeStatementEmitter<T extends EmitterState>
	extends BaseStatementEmitter<T> implements StatementEmitter<T> {

	protected abstract void emitExpression(Expression expression, T param);
	protected abstract void emitType(TypeReference typeReference, T param);
	
	protected abstract void emitVariableModifiers(VariableModifiers modifiers, T param);

	protected abstract void emitSwitchCaseLabel(SwitchCaseLabel label, T param);

	protected final void emitVariableDeclaration(VarName varName, T param) {

		param.append(' ');
		
		param.append(varName.getName());
	}

	protected final void emitVariableDeclarationElement(InitializerVariableDeclarationElement element, T param) {
		
		emitVariableDeclaration(element.getVarName(), param);

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
	
	private void emitVariableDeclarationStatement(VariableDeclarationStatement statement, T state) {

	    emitVariableModifiers(statement.getModifiers(), state);

	    emitVariableDeclarationElements(statement.getDeclarations(), state);
	}
	
	@Override
	public final Void onVariableDeclaration(VariableDeclarationStatement statement, T param) {

	    emitVariableDeclarationStatement(statement, param);
		
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
	
	private void emitForExpressionList(ForUpdateExpressionList forExpressionList, T param) {
		
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
				emitVariableDeclarationStatement(statement.getForInit().getLocalVariableDeclaration(), param);
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
