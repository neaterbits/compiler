package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ParametersSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public abstract class CallStackEntry<EXPRESSION> extends StackEntry implements ParametersSetter<EXPRESSION> {

	private String name;
	private Context nameContext;
	
	private List<EXPRESSION> parameters;

	public CallStackEntry(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public final String getName() {
		return name;
	}
	
	public final Context getNameContext() {
		return nameContext;
	}

	public final void setName(String name, Context nameContext) {
		
	    Objects.requireNonNull(nameContext);
		Objects.requireNonNull(name);
		
		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}

		this.name = name;
		this.nameContext = nameContext;
	}

	public final List<EXPRESSION> getParameters() {
		return parameters;
	}

	@Override
	public final void setParameters(List<EXPRESSION> parameters) {
		
		if (this.parameters != null) {
			throw new IllegalStateException("Parameters already set");
		}

		this.parameters = parameters;
	}
}
