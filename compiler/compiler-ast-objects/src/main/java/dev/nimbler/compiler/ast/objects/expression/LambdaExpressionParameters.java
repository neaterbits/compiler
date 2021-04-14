package dev.nimbler.compiler.ast.objects.expression;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.types.ParseTreeElement;

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
