package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.NamedListStackEntry;
import com.neaterbits.compiler.common.parser.StatementSetter;

public abstract class CallableStackEntry extends NamedListStackEntry<Statement> implements StatementSetter {
	private final List<Parameter> parameters;

	public CallableStackEntry(ParseLogger parseLogger) {
		super(parseLogger);

		this.parameters = new ArrayList<>();
	}

	public CallableStackEntry(ParseLogger parseLogger, String name) {
		super(parseLogger, name);

		this.parameters = new ArrayList<>();
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
