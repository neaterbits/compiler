package dev.nimbler.compiler.emit.base;


import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.type.NamedType;
import dev.nimbler.compiler.ast.objects.typereference.ResolvedNamedTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.language.common.types.TypeName;

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
