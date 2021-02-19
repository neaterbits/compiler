package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.StatementSetter;

public final class StackLambdaExpression extends StackExpressionList implements StatementSetter {

	private String singleParameter;
	private List<String> inferredParameters;
	
	private final List<Statement> statements;
	
	public StackLambdaExpression(ParseLogger parseLogger) {
		super(parseLogger);

		this.statements = new ArrayList<>();
	}

	public String getSingleParameter() {
		return singleParameter;
	}

	public void setSingleParameter(String singleParameter) {
		
		Objects.requireNonNull(singleParameter);
		
		this.singleParameter = singleParameter;
	}

	public List<String> getInferredParameterList() {
		return inferredParameters;
	}

	public void setInferredParameterList(List<String> inferredParameters) {
		
		Objects.requireNonNull(inferredParameters);
		
		this.inferredParameters = inferredParameters;
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
