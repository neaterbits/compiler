package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.Namespace;

public abstract class ConverterState {

	private Namespace currentNamespace;
	
	public final TypeReference convertTypeReference(TypeReference type) {
		throw new UnsupportedOperationException("TODO");
	}

	public final Namespace getCurrentNamespace() {
		return currentNamespace;
	}

	public final void setCurrentNamespace(Namespace currentNamespace) {
		this.currentNamespace = currentNamespace;
	}
}
