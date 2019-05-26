package com.neaterbits.compiler.util.parse.stackstate.setters;

import java.util.List;

public interface ParametersSetter<EXPRESSION> {

	void setParameters(List<EXPRESSION> parameters);
	
}
