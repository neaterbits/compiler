package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.block.CallableName;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class Call<N extends CallableName>
		extends Primary {

	private final ASTSingle<N> callable;
	private final ASTSingle<ParameterList> parameters;

	public Call(Context context, N callable, ParameterList parameters) {
		super(context);

		Objects.requireNonNull(callable);
		Objects.requireNonNull(parameters);

		this.callable = makeSingle(callable);
		this.parameters = makeSingle(parameters);
	}

	public final N getCallable() {
		return callable.get();
	}

	public final ParameterList getParameters() {
		return parameters.get();
	}
	
	@Override
	public final TypeReference getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(callable, recurseMode, iterator);
		
		doIterate(parameters, recurseMode, iterator);
		
	}
}
