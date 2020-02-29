package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.NamedListStackEntry;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.StatementSetter;

public abstract class CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE>
	extends NamedListStackEntry<STATEMENT>
	implements StatementSetter<STATEMENT> {
		
	private final List<PARAMETER> parameters;

	private TYPE_REFERENCE returnType;
	
	public CallableStackEntry(ParseLogger parseLogger) {
		super(parseLogger);

		this.parameters = new ArrayList<>();
	}

	public CallableStackEntry(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger, name, nameContext);

		this.parameters = new ArrayList<>();
	}

	public final TYPE_REFERENCE getReturnType() {
		return returnType;
	}

	public final void setReturnType(TYPE_REFERENCE returnType) {
		this.returnType = returnType;
	}

	public final void addParameter(PARAMETER parameter) {
		Objects.requireNonNull(parameter);
		
		parameters.add(parameter);
	}

	public final List<PARAMETER> getParameters() {
		return parameters;
	}

	@Override
	public final void addStatement(STATEMENT statement) {
		super.add(statement);
	}
}
