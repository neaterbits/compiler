package dev.nimbler.compiler.emit;

import dev.nimbler.compiler.ast.objects.statement.StatementVisitor;

public interface StatementEmitter<T extends EmitterState> extends StatementVisitor<T, Void> {

}
