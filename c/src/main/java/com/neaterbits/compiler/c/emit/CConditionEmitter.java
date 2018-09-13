package com.neaterbits.compiler.c.emit;

import java.util.List;

import com.neaterbits.compiler.common.ast.condition.NestedCondition;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.operator.Relational;
import com.neaterbits.compiler.common.emit.ConditionEmitter;
import com.neaterbits.compiler.common.emit.EmitterState;

public class CConditionEmitter implements ConditionEmitter<EmitterState> {

	private static String getCRelationalOperator(Relational relational) {
		final String operator;
		
		switch (relational) {
		case EQUALS: 		operator = "="; 	break;
		case NOT_EQUALS: 	operator = "!="; 	break;
		case LESS_THAN:		operator = "<";		break;
		case LESS_THAN_OR_EQUALS: operator = "<="; break;
		case GREATER_THAN:	operator = ">";		break;
		case GREATER_THAN_OR_EQUALS: operator = ">="; break;
		default:
			throw new UnsupportedOperationException("Unknown relational operator " + relational);
		}

		return operator;
	}
	

	@Override
	public Void onNestedCondition(NestedCondition condition, EmitterState param) {
		
		final List<Expression> expressions = condition.getExpressions();
		
		for (int i = 0; i < expressions.size(); ++ i) {
			if (i < expressions.size() - 1) {
				final Relational operator = condition.getOperators().get(i);
				
				param.append(' ');

				param.append(getCRelationalOperator(operator));

				param.append(' ');
			}
		}

		return null;
	}
}
