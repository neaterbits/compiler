package com.neaterbits.compiler.emit.base;

import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.StatementEmitter;

public abstract class BaseStatementEmitter<T extends EmitterState>
	extends BaseEmitter<T>
	implements StatementEmitter<T> {

	@Override
	protected final void emitStatement(Statement statement, T state) {
		statement.visit(this, state);
	}
}
