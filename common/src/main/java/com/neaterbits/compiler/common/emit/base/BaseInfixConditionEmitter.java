package com.neaterbits.compiler.common.emit.base;

import com.neaterbits.compiler.common.ast.condition.ConditionList;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.operator.Relational;
import com.neaterbits.compiler.common.emit.ConditionEmitter;
import com.neaterbits.compiler.common.emit.EmitterState;

public abstract class BaseInfixConditionEmitter<T extends EmitterState> implements ConditionEmitter<T> {

	protected abstract String getRelationalOperator(Relational relational);
	
	@Override
	public final Void onNestedCondition(ConditionList conditionList, EmitterState param) {
		
		final ASTList<Expression> expressions = conditionList.getExpressions();
		
		expressions.foreachWithIndex((expression, i) -> {
			if (i < expressions.size() - 1) {
				final Relational operator = conditionList.getOperators().get(i);
				
				param.append(' ');

				param.append(getRelationalOperator(operator));

				param.append(' ');
			}
		});

		return null;
	}
}
