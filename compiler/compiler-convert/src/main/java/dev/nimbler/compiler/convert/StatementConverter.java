package dev.nimbler.compiler.convert;

import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.statement.StatementVisitor;

public interface StatementConverter<T extends ConverterState<T>> extends StatementVisitor<T, Statement> {
	
	
}
