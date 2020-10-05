package com.neaterbits.compiler.util.parse;

import com.neaterbits.build.types.TypeName;

// For building a model of a files cross references
// Refs are references to the names
public interface ScopesListener {

	void onClassStart(int classParseTreeRef);
	
	void onMethodScopeStart(int methodParseTreeRef);

	void onBlockScopeStart(int blockParseTreeRef);

	void onScopeVariableDeclaration(int variableDeclarationParseTreeRef, String name, TypeName type);

	<T> void onNonPrimaryListNameReference(int nameParseTreeRef, String name);

	<T> T onPrimaryListStart(T primaryList, int primaryListParseTreeRef, int size);
	
	<T> void onPrimaryListEnd(T primaryList, int primaryListParseTreeRef);

	<T> void onPrimaryListNameReference(T primaryList, int nameParseTreeRef, String name);

	<T> void onSimpleVariableReference(T primaryList, int variableParseTreeRef);
	
	<T> T onArrayAccessReference(T primaryList, int arrayAccessParseTreeRef);
	
	<T> T onFieldAccessReference(T primaryList, int fieldAccessParseTreeRef);

	<T> T onStaticMemberReference(T primaryList, int staticMemberParseTreeRef);
	
	void onBlockScopeEnd(int blockParseTreeRef);

	void onMethodScopeEnd(int methodParseTreeRef);

	void onClassEnd(int classParseTreeRef);
}

