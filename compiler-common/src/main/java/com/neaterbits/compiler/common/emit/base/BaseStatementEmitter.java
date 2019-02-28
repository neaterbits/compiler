package com.neaterbits.compiler.common.emit.base;

import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.StatementEmitter;

public abstract class BaseStatementEmitter<T extends EmitterState>
	extends BaseEmitter<T>
	implements StatementEmitter<T> {

	@Override
	protected final void emitStatement(Statement statement, T state) {
		statement.visit(this, state);
	}
}
