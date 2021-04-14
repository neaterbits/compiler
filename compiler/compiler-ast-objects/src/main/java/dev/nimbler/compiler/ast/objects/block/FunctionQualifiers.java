package dev.nimbler.compiler.ast.objects.block;

public final class FunctionQualifiers {

	private final boolean fileLocal;
	
	public FunctionQualifiers(boolean fileLocal) {
		this.fileLocal = fileLocal;
	}

	public boolean isFileLocal() {
		return fileLocal;
	}
}
