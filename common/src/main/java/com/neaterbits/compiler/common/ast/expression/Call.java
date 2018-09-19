package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.block.CallableName;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public abstract class Call<N extends CallableName>
		extends Primary {

	private final N callable;
	private final ASTSingle<ParameterList> parameters;

	public Call(Context context, N callable, ParameterList parameters) {
		super(context);

		Objects.requireNonNull(callable);
		Objects.requireNonNull(parameters);

		this.callable = callable;
		this.parameters = makeSingle(parameters);
	}

	public final N getCallable() {
		return callable;
	}

	public final ParameterList getParameters() {
		return parameters.get();
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(parameters, recurseMode, visitor);
		
	}
}
