package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.statement.StatementVisitor;

public interface StatementConverter<T extends ConverterState<T>> extends StatementVisitor<T, Statement> {
	
	
}
