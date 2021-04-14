package dev.nimbler.compiler.model.common;

public interface SourceTokenModel<COMPILATION_UNIT> {

	void iterate(COMPILATION_UNIT sourceFile, SourceTokenVisitor iterator, ResolvedTypes resolvedTypes, boolean visitPlaceholderElements);
	
	ISourceToken getTokenAtOffset(COMPILATION_UNIT sourceFile, long offset, ResolvedTypes resolvedTypes);

	ISourceToken getTokenAtParseTreeRef(COMPILATION_UNIT sourceFile, int parseTreeRef, ResolvedTypes resolvedTypes);

	int getTokenOffset(COMPILATION_UNIT sourceFile, int parseTreeTokenRef);
	
	int getTokenLength(COMPILATION_UNIT sourceFile, int parseTreeTokenRef);

	String getTokenString(COMPILATION_UNIT sourceFile, int parseTreeTokenRef);

}
