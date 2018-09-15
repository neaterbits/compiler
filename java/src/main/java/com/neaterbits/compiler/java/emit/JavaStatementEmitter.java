package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.Expression;
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
}
