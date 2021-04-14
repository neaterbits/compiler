package dev.nimbler.compiler.parser.listener.stackbased.state;

import com.neaterbits.util.parse.context.Context;

public interface VariableNameSetter {

	void init(String name, Context nameContext, int numDims);
	
}
