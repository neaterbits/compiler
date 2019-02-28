package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public abstract class LambdaExpression extends Expression {

	private final ASTSingle<LambdaExpressionParameters> parameters;

	public LambdaExpression(Context context, LambdaExpressionParameters parameters) {
		super(context);

		Objects.requireNonNull(parameters);
		
		this.parameters = makeSingle(parameters);
	}

	public LambdaExpressionParameters getParameters() {
		return parameters.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(parameters, recurseMode, iterator);
		
	}
}
