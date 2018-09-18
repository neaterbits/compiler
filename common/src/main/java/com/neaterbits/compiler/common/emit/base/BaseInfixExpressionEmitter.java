package com.neaterbits.compiler.common.emit.base;


import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ExpressionList;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.operator.Operator;
import com.neaterbits.compiler.common.emit.EmitterState;

public abstract class BaseInfixExpressionEmitter<T extends EmitterState> extends BaseExpressionEmitter<T> {

	protected abstract String getOperatorString(Operator operator);
	
	@Override
	public Void onExpressionList(ExpressionList expressionList, EmitterState param) {
		
		final ASTList<Expression> expressions = expressionList.getExpressions();
		
		expressions.foreachWithIndex((expression, i) -> {
			if (i < expressions.size() - 1) {
				final Operator operator = expressionList.getOperators().get(i);
				
				param.append(' ');
				
				param.append(getOperatorString(operator));
				
				param.append(' ');
			}
		});
		
		return null;
	}
}
