package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ParametersSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
