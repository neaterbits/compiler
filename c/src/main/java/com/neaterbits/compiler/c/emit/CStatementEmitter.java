package com.neaterbits.compiler.c.emit;


import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.StatementEmitter;
import com.neaterbits.compiler.common.emit.base.c.CLikeStatementEmitter;

final class CStatementEmitter extends CLikeStatementEmitter<EmitterState> implements StatementEmitter<EmitterState> {

	private static final CConditionEmitter CONDITION_EMITTER = new CConditionEmitter();

	private static final CExpressionEmitter EXPRESSION_EMITTER = new CExpressionEmitter();
	
	private static final CTypeEmitter TYPE_EMITTER = new CTypeEmitter();
	
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

	@Override
	protected void emitVariableModifiers(VariableModifiers modifiers, EmitterState param) {
		throw new UnsupportedOperationException("");
	}
}
