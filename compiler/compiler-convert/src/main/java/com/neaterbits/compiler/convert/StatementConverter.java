package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.objects.statement.Statement;
import com.neaterbits.compiler.ast.objects.statement.StatementVisitor;

public interface StatementConverter<T extends ConverterState<T>> extends StatementVisitor<T, Statement> {
	
	
}
