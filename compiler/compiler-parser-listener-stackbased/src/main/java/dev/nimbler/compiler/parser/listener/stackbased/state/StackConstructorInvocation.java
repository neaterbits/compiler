package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ParametersSetter;
import dev.nimbler.compiler.types.block.ConstructorInvocation;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackConstructorInvocation<EXPRESSION, NESTED_EXPRESSION extends EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY>

	extends StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
	implements ParametersSetter<EXPRESSION> {

	private final ConstructorInvocation type;
	private List<EXPRESSION> parameters;
	
	public StackConstructorInvocation(ParseLogger parseLogger, ConstructorInvocation type) {
		super(parseLogger);
		
		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public ConstructorInvocation getType() {
		return type;
	}

	@Override
	public void setParameters(List<EXPRESSION> parameters) {

		Objects.requireNonNull(parameters);
		
		if (this.parameters != null) {
			throw new IllegalStateException("Parameters already set");
		}
		
		this.parameters = parameters;
	}

	public List<EXPRESSION> getParameters() {
		return parameters;
	}
}
