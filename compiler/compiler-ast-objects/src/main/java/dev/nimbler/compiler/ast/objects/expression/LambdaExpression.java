package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;

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
