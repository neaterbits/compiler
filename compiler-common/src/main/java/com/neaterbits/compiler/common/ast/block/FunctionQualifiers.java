package com.neaterbits.compiler.common.ast.block;

public final class FunctionQualifiers {

	private final boolean fileLocal;
	
	public FunctionQualifiers(boolean fileLocal) {
		this.fileLocal = fileLocal;
	}

	public boolean isFileLocal() {
		return fileLocal;
	}
}
