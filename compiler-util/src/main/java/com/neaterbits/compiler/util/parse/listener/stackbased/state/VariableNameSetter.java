package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import com.neaterbits.compiler.util.Context;

public interface VariableNameSetter {

	void init(String name, Context nameContext, int numDims);
	
}
