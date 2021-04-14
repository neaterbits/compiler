package dev.nimbler.compiler.emit.base;

import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.StatementEmitter;

public abstract class BaseStatementEmitter<T extends EmitterState>
	extends BaseEmitter<T>
	implements StatementEmitter<T> {

	@Override
	protected final void emitStatement(Statement statement, T state) {
		statement.visit(this, state);
	}
}
