package com.neaterbits.compiler.common.emit.base;

import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;
import com.neaterbits.compiler.common.emit.EmitterState;

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
