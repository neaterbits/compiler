package com.neaterbits.compiler.parser.listener.stackbased.state.setters;

public interface StatementSetter<STATEMENT> {

	void addStatement(STATEMENT statement);
	
}
