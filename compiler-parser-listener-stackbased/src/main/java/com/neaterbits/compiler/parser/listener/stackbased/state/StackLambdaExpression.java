package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackLambdaExpression<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY, STATEMENT>
		extends StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
		implements StatementSetter<STATEMENT> {

	private String singleParameter;
	private Context singleParameterContext;
	
	private List<String> inferredParameters;
	private Context inferredParametersContext;
	
	private final List<STATEMENT> statements;
	
	public StackLambdaExpression(ParseLogger parseLogger) {
		super(parseLogger);

		this.statements = new ArrayList<>();
	}

	public String getSingleParameter() {
		return singleParameter;
	}

	public Context getSingleParameterContext() {
		return singleParameterContext;
	}

	public void setSingleParameter(String singleParameter, Context singleParameterContext) {
		
		Objects.requireNonNull(singleParameter);
		Objects.requireNonNull(singleParameterContext);
		
		this.singleParameter = singleParameter;
		this.singleParameterContext = singleParameterContext;
	}
	
	public List<String> getInferredParameterList() {
		return inferredParameters;
	}
	
	public Context getInferredParametersContext() {
		return inferredParametersContext;
	}

	public void setInferredParameterList(List<String> inferredParameters, Context inferredParametersContexxt) {
		
		Objects.requireNonNull(inferredParameters);
		Objects.requireNonNull(inferredParametersContexxt);
		
		this.inferredParameters = inferredParameters;
		this.inferredParametersContext = inferredParametersContexxt;
	}

	public List<STATEMENT> getStatements() {
		return statements;
	}

	@Override
	public void addStatement(STATEMENT statement) {
		Objects.requireNonNull(statement);
		
		statements.add(statement);
	}
}
