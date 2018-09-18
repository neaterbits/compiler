package com.neaterbits.compiler.java.emit;

import java.util.List;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.BaseTryCatchFinallyStatement;
import com.neaterbits.compiler.common.ast.statement.CatchBlock;
import com.neaterbits.compiler.common.ast.statement.IteratorForStatement;
import com.neaterbits.compiler.common.ast.statement.TryCatchFinallyStatement;
import com.neaterbits.compiler.common.ast.statement.TryWithResourcesStatement;
import com.neaterbits.compiler.common.ast.statement.VariableMutability;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifierVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.c.CLikeStatementEmitter;

public final class JavaStatementEmitter extends CLikeStatementEmitter<EmitterState> {

	private static final JavaConditionEmitter CONDITION_EMITTER = new JavaConditionEmitter();

	private static final JavaExpressionEmitter EXPRESSION_EMITTER = new JavaExpressionEmitter();
	
	private static final JavaTypeEmitter TYPE_EMITTER = new JavaTypeEmitter();

	@Override
	protected void emitCondition(Condition condition, EmitterState param) {
		condition.visit(CONDITION_EMITTER, param);
	}

	@Override
	protected void emitExpression(Expression expression, EmitterState param) {
		expression.visit(EXPRESSION_EMITTER, param);
	}

	@Override
	protected void emitType(TypeReference typeReference, EmitterState param) {
		typeReference.getType().visit(TYPE_EMITTER, param);
	}

	private static final VariableModifierVisitor<Void, String> VARIABLEMODIFIER_TO_STRING = new VariableModifierVisitor<Void, String>() {
		
		@Override
		public String onVariableMutability(VariableMutability mutability, Void param) {
			return "final";
		}
	};

	@Override
	protected void emitVariableModifiers(VariableModifiers modifiers, EmitterState param) {
		emitList(param, modifiers.getModifiers(), " ", modifier -> modifier.visit(VARIABLEMODIFIER_TO_STRING, null));
	}

	
	private void emitCatchBlocks(List<CatchBlock> catchBlocks, EmitterState state) {
		
		for (CatchBlock catchBlock : catchBlocks) {
			state.append("} catch(");
			
			emitListTo(state, catchBlock.getExceptionTypes(), "|", type -> emitType(type, state));
			
			state.append(' ');
			
			state.append(catchBlock.getExceptionVarName().getName());
			
			state.append(") {").newline();
			
			emitIndentedBlock(catchBlock.getBlock(), state);
		}
	}
	
	private void emitTryCatchAndFinallyBlocks(BaseTryCatchFinallyStatement statement, EmitterState param) {
		emitIndentedBlock(statement.getTryBlock(), param);
		
		emitCatchBlocks(statement.getCatchBlocks(), param);
		
		if (statement.getFinallyBlock() != null) {
			emitIndentedBlock(statement.getFinallyBlock(), param);
		}
	}
	
	@Override
	public Void onIteratorFor(IteratorForStatement statement, EmitterState param) {
		
		param.append("for (");
		
		emitVariableModifiers(statement.getVariableDeclaration().getModifiers(), param);
		
		param.append(' ');
		
		emitType(statement.getVariableDeclaration().getTypeReference(), param);

		param.append(' ');

		param.append(statement.getVariableDeclaration().getName().getName());

		param.append(" : ");

		emitExpression(statement.getCollectionExpression(), param);
		
		param.append(") }").newline();

		emitIndentedBlock(statement.getBlock(), param);
		
		param.append('}').newline();
		
		return null;
	}

	@Override
	public Void onTryCatchFinallyStatement(TryCatchFinallyStatement statement, EmitterState param) {

		param.append("try {").newline();
			
		emitTryCatchAndFinallyBlocks(statement, param);

		param.append('}').newline();
		
		return null;
	}

	@Override
	public Void onTryWithResourcesStatement(TryWithResourcesStatement statement, EmitterState param) {

		param.append("try (");

		emitListTo(param, statement.getResources().getList(), "; ", r -> {
			emitVariableModifiers(r.getModifiers(), param);

			param.append(' ');

			emitVariableDeclaration(r.getTypeReference(), r.getName(), param);
			
		});
		
		emitVariableDeclarationElements(statement.getResources().getList(), param);
		
		param.append(") {").newline();
		
		emitTryCatchAndFinallyBlocks(statement, param);
		
		return null;
	}
}
