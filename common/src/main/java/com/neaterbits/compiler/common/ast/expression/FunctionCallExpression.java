package com.neaterbits.compiler.common.ast.expression;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Function;

public final class FunctionCallExpression extends Expression {

	private final Function function;
	private final List<Expression> parameters;
	
	public FunctionCallExpression(Context context, Function function, List<Expression> parameters) {
		super(context);

		this.function = function;
		this.parameters = Collections.unmodifiableList(parameters);
	}

	public Function getFunction() {
		return function;
	}
	
	public List<Expression> getParameters() {
		return parameters;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onFunctionCall(this, param);
	}
}
