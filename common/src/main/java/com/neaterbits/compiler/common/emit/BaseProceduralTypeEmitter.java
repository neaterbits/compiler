package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.type.complex.ClassType;

public abstract class BaseProceduralTypeEmitter<T extends EmitterState> implements TypeEmitter<T> {

	@Override
	public final Void onClass(ClassType type, T param) {
		throw new UnsupportedOperationException();
	}
}
