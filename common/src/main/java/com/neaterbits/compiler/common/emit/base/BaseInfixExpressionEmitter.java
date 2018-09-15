package com.neaterbits.compiler.common.emit.base;

import java.util.List;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ExpressionList;
import com.neaterbits.compiler.common.ast.operator.Operator;
import com.neaterbits.compiler.common.emit.EmitterState;

public abstract class BaseInfixExpressionEmitter<T extends EmitterState> extends BaseExpressionEmitter<T> {

	protected abstract String getOperatorString(Operator operator);
	
	@Override
	public Void onExpressionList(ExpressionList expression, EmitterState param) {
		
		final List<Expression> expressions = expression.getExpressions();
		
		for (int i = 0; i < expressions.size(); ++ i) {
			if (i < expressions.size() - 1) {
				final Operator operator = expression.getOperators().get(i);
				
				param.append(' ');
				
				param.append(getOperatorString(operator));
				
				param.append(' ');
			}
		}
		
		return null;
	}
}
