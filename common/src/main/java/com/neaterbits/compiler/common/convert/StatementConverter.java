package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.statement.StatementVisitor;

public interface StatementConverter<T extends ConverterState<T>> extends StatementVisitor<T, Statement> {
	
	
}
