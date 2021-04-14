package com.neaterbits.language.codemap.compiler;

public interface CrossReferenceGetters {

	int getTokenForParseTreeRef(int sourceFile, int parseTreeRef);

	int getParseTreeRefForToken(int token);

	int getVariableDeclarationTokenReferencedFrom(int fromReferenceToken);

	int getMethodDeclarationTokenReferencedFrom(int fromReferenceToken);
}
