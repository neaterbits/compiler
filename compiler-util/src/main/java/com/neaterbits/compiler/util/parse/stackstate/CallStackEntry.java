package com.neaterbits.compiler.util.parse.stackstate;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.StackEntry;
import com.neaterbits.compiler.util.parse.stackstate.setters.ParametersSetter;

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
		
		Objects.requireNonNull(name);
		
		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}

		this.name = name;
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
