package com.neaterbits.compiler.c.emit;


import com.neaterbits.compiler.ast.block.ConstructorInvocationStatement;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.statement.IteratorForStatement;
import com.neaterbits.compiler.ast.statement.SwitchCaseLabel;
import com.neaterbits.compiler.ast.statement.ThrowStatement;
import com.neaterbits.compiler.ast.statement.TryCatchFinallyStatement;
import com.neaterbits.compiler.ast.statement.TryWithResourcesStatement;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.StatementEmitter;
import com.neaterbits.compiler.emit.base.c.CLikeStatementEmitter;

final class CStatementEmitter extends CLikeStatementEmitter<EmitterState> implements StatementEmitter<EmitterState> {

	private static final CExpressionEmitter EXPRESSION_EMITTER = new CExpressionEmitter();

	private static final CSwitchCaseLabelEmitter SWITCHCASELABEL_EMITTER = new CSwitchCaseLabelEmitter();
	
	private static final CTypeEmitter TYPE_EMITTER = new CTypeEmitter();

	
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
		typeReference.getType().visit(TYPE_EMITTER, param);
	}

	@Override
	protected void emitVariableModifiers(VariableModifiers modifiers, EmitterState param) {

	}

	@Override
	public Void onIteratorFor(IteratorForStatement statement, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onTryCatchFinallyStatement(TryCatchFinallyStatement statement, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onTryWithResourcesStatement(TryWithResourcesStatement statement, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onThrowStatement(ThrowStatement statement, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onConstructorInvocation(ConstructorInvocationStatement statement, EmitterState param) {
		throw new UnsupportedOperationException();
	}
}
