package com.neaterbits.compiler.util.parse.stackstate;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.stackstate.setters.ParametersSetter;

public final class StackEnumConstant<COMPLEX_MEMBER_DEFINITION, EXPRESSION>
		extends ListStackEntry<COMPLEX_MEMBER_DEFINITION>
		implements ParametersSetter<EXPRESSION> {

	private final String name;
	private List<EXPRESSION> parameters;

	public StackEnumConstant(ParseLogger parseLogger, String name) {
		super(parseLogger);

		Objects.requireNonNull(name);
		
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<EXPRESSION> getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(List<EXPRESSION> parameters) {

		Objects.requireNonNull(parameters);

		if (this.parameters != null) {
			throw new IllegalArgumentException("Parameters already set");
		}

		this.parameters = parameters;
	}
}
