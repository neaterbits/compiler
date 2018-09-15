package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.parser.NamedListStackEntry;

public abstract class CallableStackEntry extends NamedListStackEntry<Statement> {
	private final List<Parameter> parameters;

	public CallableStackEntry() {
		this.parameters = new ArrayList<>();
	}

	public CallableStackEntry(String name) {
		super(name);

		this.parameters = new ArrayList<>();
	}

	public final void addParameter(Parameter parameter) {
		Objects.requireNonNull(parameter);
		
		parameters.add(parameter);
	}

	public List<Parameter> getParameters() {
		return parameters;
	}
}
