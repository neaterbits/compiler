package dev.nimbler.compiler.util.model;

public interface CrossReferenceGetters {

	int getTokenForParseTreeRef(int sourceFile, int parseTreeRef);

	int getParseTreeRefForToken(int token);

	int getVariableDeclarationTokenReferencedFrom(int fromReferenceToken);

	int getMethodDeclarationTokenReferencedFrom(int fromReferenceToken);
}
