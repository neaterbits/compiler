package com.neaterbits.compiler.common.ast.expression;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
