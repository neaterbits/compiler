package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.parser.StatementSetter;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackLambdaExpression extends StackExpressionList implements StatementSetter {

	private String singleParameter;
	private Context singleParameterContext;
	
	private List<String> inferredParameters;
	private Context inferredParametersContext;
	
	private final List<Statement> statements;
	
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

	public List<Statement> getStatements() {
		return statements;
	}

	@Override
	public void addStatement(Statement statement) {
		Objects.requireNonNull(statement);
		
		statements.add(statement);
	}
}
