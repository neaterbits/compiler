package com.neaterbits.compiler.util.parse.listener.stackbased.state.setters;

import java.util.List;

public interface ParametersSetter<EXPRESSION> {

	void setParameters(List<EXPRESSION> parameters);
	
}
