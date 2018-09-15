package com.neaterbits.compiler.common.emit.base;


import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.emit.EmitterState;

public abstract class BaseEmitter<T extends EmitterState> extends EmitterUtil<T> {

	protected abstract void emitStatement(Statement statement, T state);
	
	protected final void emitBlock(Block block, T state) {
		
		for (Statement statement : block.getStatements()) {
			emitStatement(statement, state);
		}
	}

	protected final void emitIndentedBlock(Block block, T state) {
		state.addIndent();
		
		emitBlock(block, state);
		
		state.subIndent();
	}
}
