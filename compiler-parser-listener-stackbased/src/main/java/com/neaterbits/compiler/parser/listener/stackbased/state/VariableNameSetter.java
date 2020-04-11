package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.Context;

public interface VariableNameSetter {

	void init(String name, Context nameContext, int numDims);
	
}
