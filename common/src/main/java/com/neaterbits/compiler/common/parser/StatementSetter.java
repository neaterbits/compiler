package com.neaterbits.compiler.common.parser;

import com.neaterbits.compiler.common.ast.statement.Statement;

public interface StatementSetter {

	void addStatement(Statement statement);
	
}
