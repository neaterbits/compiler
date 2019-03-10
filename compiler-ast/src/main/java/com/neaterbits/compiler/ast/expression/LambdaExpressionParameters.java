package com.neaterbits.compiler.ast.expression;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.util.Context;

public final class LambdaExpressionParameters extends BaseASTElement {

	private final String singleParameter;
	private final List<String> inferredParameters;
	
	public LambdaExpressionParameters(Context context, String singleParameter) {
		
		super(context);
		
		Objects.requireNonNull(singleParameter);
		
		this.singleParameter = singleParameter;
		this.inferredParameters = null;
	}

	public LambdaExpressionParameters(Context context, List<String> inferredParameters) {
		super(context);

		Objects.requireNonNull(inferredParameters);
		
		this.singleParameter = null;
		this.inferredParameters = inferredParameters;
	}
	
	public String getSingleParameter() {
		return singleParameter;
	}

	public List<String> getInferredParameters() {
		return inferredParameters;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
