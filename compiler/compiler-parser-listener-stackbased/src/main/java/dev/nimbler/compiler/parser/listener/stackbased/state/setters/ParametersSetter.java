package dev.nimbler.compiler.parser.listener.stackbased.state.setters;

import java.util.List;

public interface ParametersSetter<EXPRESSION> {

	void setParameters(List<EXPRESSION> parameters);
	
}
