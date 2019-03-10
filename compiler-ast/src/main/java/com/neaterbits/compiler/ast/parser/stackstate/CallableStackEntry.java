package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.parser.NamedListStackEntry;
import com.neaterbits.compiler.ast.parser.StatementSetter;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class CallableStackEntry extends NamedListStackEntry<Statement> implements StatementSetter {
	private final List<Parameter> parameters;

	private TypeReference returnType;
	
	public CallableStackEntry(ParseLogger parseLogger) {
		super(parseLogger);

		this.parameters = new ArrayList<>();
	}

	public CallableStackEntry(ParseLogger parseLogger, String name) {
		super(parseLogger, name);

		this.parameters = new ArrayList<>();
	}

	public final TypeReference getReturnType() {
		return returnType;
	}

	public final void setReturnType(TypeReference returnType) {
		this.returnType = returnType;
	}

	public final void addParameter(Parameter parameter) {
		Objects.requireNonNull(parameter);
		
		parameters.add(parameter);
	}

	public final List<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public final void addStatement(Statement statement) {
		super.add(statement);
	}
}
