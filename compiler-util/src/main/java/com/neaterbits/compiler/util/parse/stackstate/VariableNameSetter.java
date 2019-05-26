package com.neaterbits.compiler.util.parse.stackstate;

import com.neaterbits.compiler.util.Context;

public interface VariableNameSetter {

	void init(String name, Context nameContext, int numDims);
	
}
