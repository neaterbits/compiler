package com.neaterbits.compiler.common.parser.stackstate;

import java.util.List;

import com.neaterbits.compiler.common.ast.block.CallableName;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ParametersSetter;
import com.neaterbits.compiler.common.parser.StackEntry;

public abstract class CallStackEntry<N extends CallableName> extends StackEntry implements ParametersSetter {

	private N name;
	private List<Expression> parameters;

	public CallStackEntry(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public final N getName() {
		return name;
	}

	public final void setName(N name) {
		
		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}

		this.name = name;
	}

	public final List<Expression> getParameters() {
		return parameters;
	}

	@Override
	public final void setParameters(List<Expression> parameters) {
		
		if (this.parameters != null) {
			throw new IllegalStateException("Parameters already set");
		}

		this.parameters = parameters;
	}
}
