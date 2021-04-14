package com.neaterbits.compiler.emit.base;

import com.neaterbits.compiler.ast.objects.block.Function;
import com.neaterbits.compiler.ast.objects.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.StructDefinition;
import com.neaterbits.compiler.emit.EmitterState;

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
