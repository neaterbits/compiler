package com.neaterbits.compiler.ast.parser.stackstate;

import com.neaterbits.compiler.util.Context;

public interface VariableNameSetter {

	void init(String name, Context nameContext, int numDims);
	
}
