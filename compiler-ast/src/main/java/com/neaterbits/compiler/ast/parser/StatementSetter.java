package com.neaterbits.compiler.ast.parser;

import com.neaterbits.compiler.ast.statement.Statement;

public interface StatementSetter {

	void addStatement(Statement statement);
	
}
