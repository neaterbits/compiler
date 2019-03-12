package com.neaterbits.compiler.util.model;

public interface TypeImportVisitor {

	void onKnownNamespace(String [] namespace, String typeName);
	
	void onNamespaceOrTypeName(String [] namespaceOrTypeName);
	
	void onStaticMethodWildcard(String [] namespace, String typeName);
	
	void onKnownStaticMethod(String [] namespace, String typeName, String methodName);
}
