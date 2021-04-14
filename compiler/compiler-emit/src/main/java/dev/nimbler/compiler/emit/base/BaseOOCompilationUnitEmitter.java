package dev.nimbler.compiler.emit.base;

import dev.nimbler.compiler.ast.objects.block.Function;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDefinition;
import dev.nimbler.compiler.emit.EmitterState;

public abstract class BaseOOCompilationUnitEmitter<T extends EmitterState>
	extends BaseCompilationUnitEmitter<T> {

	@Override
	public final Void onFunction(Function function, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onStructDefinition(StructDefinition structDefinition, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onStructDataFieldMember(StructDataFieldMember field, T param) {
		throw new UnsupportedOperationException();
	}
}
