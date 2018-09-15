package com.neaterbits.compiler.common.emit.base;

import java.util.List;

import com.neaterbits.compiler.common.ast.condition.ConditionList;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.operator.Relational;
import com.neaterbits.compiler.common.emit.ConditionEmitter;
import com.neaterbits.compiler.common.emit.EmitterState;

public abstract class BaseInfixConditionEmitter<T extends EmitterState> implements ConditionEmitter<T> {

	protected abstract String getRelationalOperator(Relational relational);
	
	@Override
	public final Void onNestedCondition(ConditionList condition, EmitterState param) {
		
		final List<Expression> expressions = condition.getExpressions();
		
		for (int i = 0; i < expressions.size(); ++ i) {
			if (i < expressions.size() - 1) {
				final Relational operator = condition.getOperators().get(i);
				
				param.append(' ');

				param.append(getRelationalOperator(operator));

				param.append(' ');
			}
		}

		return null;
	}
}
