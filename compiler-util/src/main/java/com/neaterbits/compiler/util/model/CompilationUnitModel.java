package com.neaterbits.compiler.util.model;

import java.io.PrintStream;

import com.neaterbits.compiler.util.parse.ScopesListener;

public interface CompilationUnitModel<COMPILATION_UNIT> extends ImportsModel<COMPILATION_UNIT> {

	void iterate(COMPILATION_UNIT sourceFile, SourceTokenVisitor iterator, ResolvedTypes resolvedTypes, boolean visitPlaceholderElements);
	
	ISourceToken getTokenAtOffset(COMPILATION_UNIT sourceFile, long offset, ResolvedTypes resolvedTypes);

	ISourceToken getTokenAtParseTreeRef(COMPILATION_UNIT sourceFile, int parseTreeRef, ResolvedTypes resolvedTypes);

	void iterateScopesAndVariables(COMPILATION_UNIT sourceFile, ScopesListener scopesListener);

	String getMethodName(COMPILATION_UNIT sourceFile, int parseTreeMethodDeclarationRef);

	String getVariableName(COMPILATION_UNIT sourceFile, int parseTreeVariableDeclarationRef);

	String getClassDataFieldMemberName(COMPILATION_UNIT sourceFile, int parseTreeDataMemberDeclarationRef);

	String getClassName(COMPILATION_UNIT sourceFile, int parseTreeTypeDeclarationRef);
	
	int getTokenOffset(COMPILATION_UNIT sourceFile, int parseTreeTokenRef);
	
	int getTokenLength(COMPILATION_UNIT sourceFile, int parseTreeTokenRef);

	String getTokenString(COMPILATION_UNIT sourceFile, int parseTreeTokenRef);

	void print(COMPILATION_UNIT sourceFile, PrintStream out);
}
