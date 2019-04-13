package com.neaterbits.compiler.emit.base;


import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.ExpressionList;
import com.neaterbits.compiler.ast.expression.PrimaryList;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.operator.Operator;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.emit.EmitterState;

public abstract class BaseInfixExpressionEmitter<T extends EmitterState> extends BaseExpressionEmitter<T> {

	protected abstract String getOperatorString(Operator operator);

	protected abstract void emitVariableReference(VariableReference variableReference, T param);

	protected abstract void emitTypeReference(TypeReference typeReference, T param);

	@Override
	public final Void onExpressionList(ExpressionList expressionList, T param) {
		
		final ASTList<Expression> expressions = expressionList.getExpressions();
		
		expressions.foreachWithIndex((expression, i) -> {

			emitExpression(expression, param);

			if (i < expressions.size() - 1) {
				final Operator operator = expressionList.getOperators().get(i);
				
				param.append(' ');
				
				param.append(getOperatorString(operator));
				
				param.append(' ');
			}
		});
		
		return null;
	}
	
	
	@Override
	public final Void onPrimaryList(PrimaryList expression, T param) {

		expression.getPrimaries().forEach(primary -> emitExpression(primary, param));
		
		return null;
	}

	@Override
	public final Void onVariableReference(VariableReference expression, T param) {
		
		emitVariableReference(expression, param);
		
		return null;
	}
}
