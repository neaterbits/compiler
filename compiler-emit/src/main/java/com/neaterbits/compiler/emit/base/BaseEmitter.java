package com.neaterbits.compiler.emit.base;


import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.emit.EmitterState;

public abstract class BaseEmitter<T extends EmitterState> extends EmitterUtil<T> {

	protected abstract void emitStatement(Statement statement, T state);
	
	protected final void emitBlock(Block block, T state) {
		
		for (Statement statement : block.getStatements()) {
			emitStatement(statement, state);
			
			state.newline();
		}
	}

	protected final void emitIndentedBlock(Block block, T state) {
		state.addIndent();
		
		emitBlock(block, state);
		
		state.subIndent();
	}

	protected final NamedType getType(TypeReference typeReference) {
		
		throw new UnsupportedOperationException();
		// return ((ResolvedNamedTypeReference)typeReference).getNamedType();
	}
}
