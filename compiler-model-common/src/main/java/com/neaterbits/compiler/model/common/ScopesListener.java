package com.neaterbits.compiler.model.common;

// For building a model of a files cross references
// Refs are references to the names
public interface ScopesListener extends NamespaceVisitor {

	void onClassStart(int classParseTreeRef);
	
	void onMethodScopeStart(int methodParseTreeRef);

	void onBlockScopeStart(int blockParseTreeRef);

	void onScopeVariableDeclarationStatementStart(
	        int variableDeclarationStatementParseTreeRef,
	        int typeNo,
	        int typeReferenceParseTreeRef);

	void onScopeVariableDeclarator(int variableDeclaratorParseTreeRef, String name);

	void onScopeVariableDeclarationStatementEnd(int variableDeclarationStatementParseTreeRef);

	<T> T onPrimaryListStart(T primaryList, int primaryListParseTreeRef, int size);
	
	<T> void onPrimaryListEnd(T primaryList, int primaryListParseTreeRef);
	
	void onBlockScopeEnd(int blockParseTreeRef);

	void onMethodScopeEnd(int methodParseTreeRef);

	void onClassEnd(int classParseTreeRef);
}

