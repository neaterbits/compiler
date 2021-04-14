package com.neaterbits.language.codemap.compiler;

public interface CrossReferenceUpdater {

	int addToken(int sourceFile, int parseTreeRef);

	void addTokenVariableReference(int fromToken, int toDeclarationToken);
}
