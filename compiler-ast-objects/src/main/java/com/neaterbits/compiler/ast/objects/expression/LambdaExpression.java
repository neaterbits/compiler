package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

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
