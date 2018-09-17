package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.CallableName;

public abstract class CallExpression<N extends CallableName>
		extends Expression {

	private final N callable;
	private final ParameterList parameters;

	public CallExpression(Context context, N callable, ParameterList parameters) {
		super(context);

		Objects.requireNonNull(callable);
		Objects.requireNonNull(parameters);

		this.callable = callable;
		this.parameters = parameters;
	}

	public final N getCallable() {
		return callable;
	}

	public final ParameterList getParameters() {
		return parameters;
	}
}
