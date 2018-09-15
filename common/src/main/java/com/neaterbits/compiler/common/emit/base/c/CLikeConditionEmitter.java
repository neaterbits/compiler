package com.neaterbits.compiler.common.emit.base.c;

import com.neaterbits.compiler.common.ast.operator.Relational;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.BaseInfixConditionEmitter;

public abstract class CLikeConditionEmitter<T extends EmitterState> extends BaseInfixConditionEmitter<T> {
	
	
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
	protected String getRelationalOperator(Relational relational) {
		return getCRelationalOperator(relational);
	}
}
