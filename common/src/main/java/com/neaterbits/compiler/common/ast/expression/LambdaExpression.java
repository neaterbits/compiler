package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(parameters, recurseMode, visitor);
		
	}
}
