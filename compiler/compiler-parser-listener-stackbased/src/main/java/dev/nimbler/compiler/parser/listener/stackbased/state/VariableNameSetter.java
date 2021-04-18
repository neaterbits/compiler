package dev.nimbler.compiler.parser.listener.stackbased.state;

import org.jutils.parse.context.Context;

public interface VariableNameSetter {

	void init(String name, Context nameContext, int numDims);
	
}
