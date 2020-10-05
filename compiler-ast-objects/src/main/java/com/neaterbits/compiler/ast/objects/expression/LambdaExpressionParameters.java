package com.neaterbits.compiler.ast.objects.expression;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.LAMBDA_EXPRESSION_PARAMETERS;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
