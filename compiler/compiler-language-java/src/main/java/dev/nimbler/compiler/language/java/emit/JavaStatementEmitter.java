package dev.nimbler.compiler.language.java.emit;

import dev.nimbler.compiler.ast.objects.block.ConstructorInvocationStatement;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.statement.BaseTryCatchFinallyStatement;
import dev.nimbler.compiler.ast.objects.statement.CatchBlock;
import dev.nimbler.compiler.ast.objects.statement.IteratorForStatement;
import dev.nimbler.compiler.ast.objects.statement.SwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.ThrowStatement;
import dev.nimbler.compiler.ast.objects.statement.TryCatchFinallyStatement;
import dev.nimbler.compiler.ast.objects.statement.TryWithResourcesStatement;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifiers;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.base.c.CLikeStatementEmitter;
import dev.nimbler.compiler.types.statement.ASTMutability;
import dev.nimbler.compiler.types.typedefinition.VariableModifierVisitor;

public final class JavaStatementEmitter extends CLikeStatementEmitter<EmitterState> {

	private static final JavaExpressionEmitter EXPRESSION_EMITTER = new JavaExpressionEmitter();

	private static final JavaSwitchCaseLabelEmitter SWITCHCASELABEL_EMITTER = new JavaSwitchCaseLabelEmitter();
	
	private static final JavaTypeEmitter TYPE_EMITTER = new JavaTypeEmitter();

	@Override
	protected void emitExpression(Expression expression, EmitterState param) {
		expression.visit(EXPRESSION_EMITTER, param);
	}

	@Override
	protected void emitSwitchCaseLabel(SwitchCaseLabel label, EmitterState param) {
		label.visit(SWITCHCASELABEL_EMITTER, param);
	}

	@Override
	protected void emitType(TypeReference typeReference, EmitterState param) {
		getType(typeReference).visit(TYPE_EMITTER, param);
	}

	private static final VariableModifierVisitor<Void, String> VARIABLEMODIFIER_TO_STRING = new VariableModifierVisitor<Void, String>() {
		
		@Override
		public String onVariableMutability(ASTMutability mutability, Void param) {
			return "final";
		}
	};

	@Override
	protected void emitVariableModifiers(VariableModifiers modifiers, EmitterState param) {
		emitList(param, modifiers.getModifiers(), " ", modifier -> modifier.visit(VARIABLEMODIFIER_TO_STRING, null));
	}

	
	private void emitCatchBlocks(ASTList<CatchBlock> catchBlocks, EmitterState state) {
		
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
		
		emitType(statement.getTypeReference(), param);

		param.append(' ');

		param.append(statement.getVariableDeclaration().getVarName().getName());

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

			emitVariableDeclaration(r.getVarName(), param);
		});
		
		emitVariableDeclarationElements(statement.getResources().getList(), param);
		
		param.append(") {").newline();
		
		emitTryCatchAndFinallyBlocks(statement, param);
		
		return null;
	}

	@Override
	public Void onThrowStatement(ThrowStatement statement, EmitterState param) {
		
		param.append("throw ");
		
		emitExpression(statement.getExpression(), param);
		
		param.append(';');
		
		return null;
	}

	@Override
	public Void onConstructorInvocation(ConstructorInvocationStatement statement, EmitterState param) {

		
		return null;
	}
}
